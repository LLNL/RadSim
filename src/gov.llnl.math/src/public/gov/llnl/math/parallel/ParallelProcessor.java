/* 
 * Copyright 2016, Lawrence Livermore National Security, LLC.
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.math.parallel;

/**
 *
 * @author nelson85
 */
public class ParallelProcessor
{
  static abstract public class Action
  {
    boolean complete = false;

    /**
     * Performs the requested work. This method is implemented to preform work.
     */
    public abstract void execute();

    final public synchronized void complete()
    {
      complete = true;
      this.notifyAll();
    }

    final public synchronized boolean waitOnComplete()
    {
      while (complete == false)
      {
        try
        {
          this.wait();
        }
        catch (InterruptedException ex)
        {
          return false;
        }
      }
      return true;
    }
  }

  public static class Worker extends Thread
  {
    boolean done = false;
    Action nextAction = null;
    final Object eventAction = new Object();

    public void quit()
    {
      synchronized (eventAction)
      {
        done = true;
        eventAction.notifyAll();
      }
    }

    public void delegate(Action action)
    {
      synchronized (eventAction)
      {
        nextAction = action;
        eventAction.notify();
      }
    }

    @Override
    public void run()
    {
      Action action;
      while (!done)
      {
        // Wait for an action or done
        synchronized (eventAction)
        {
          while (nextAction == null && !done)
          {
            try
            {
              eventAction.wait();
            }
            catch (InterruptedException ex)
            {
              break;
            }
          }
          if (done)
            break;
          action = nextAction;
          nextAction = null;
        }

        // Execute action
        if (action != null)
        {
          action.execute();
          action.complete();
        }
      }
    }
  }

  Worker workers[];
  Action actions[];

  public ParallelProcessor()
  {
    int cores = Math.min(Runtime.getRuntime().availableProcessors(), 4);
    initialize(cores);
  }

  public ParallelProcessor(int workers)
  {
    initialize(workers);
  }

  private void initialize(int workers)
  {
    this.workers = new Worker[workers];
    this.actions = new Action[workers];
    for (int i = 0; i < workers; ++i)
    {
      this.workers[i] = new Worker();
      this.workers[i].start();
    }
  }

  public Action delegate(int worker, Action action)
  {
    actions[worker] = action;
    workers[worker].delegate(action);
    return action;
  }

  // Called when this object is no longer needed
  public void dispose()
  {
    for (int i = 0; i < workers.length; i++)
    {
      workers[i].quit();
      try
      {
        workers[i].join();
      }
      catch (InterruptedException ex)
      {
        // do nothing
      }
    }
  }

  public int size()
  {
    return workers.length;
  }

  public void waitForComplete()
  {
    for (int i = 0; i < workers.length; i++)
    {
      if (actions[i] != null)
      {
        actions[i].waitOnComplete();
        actions[i] = null;
      }
    }
  }
}
