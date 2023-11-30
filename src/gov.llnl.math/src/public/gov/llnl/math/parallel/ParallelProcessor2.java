/*
 * Copyright 2016, Lawrence Livermore National Security, LLC. 
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.math.parallel;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * New implementation of parallel processing using Java executor.
 *
 * @author nelson85
 */
public class ParallelProcessor2
{

  static final ParallelProcessor2 INSTANCE = new ParallelProcessor2();
  ExecutorService executor;
  int cores;

  public static ParallelContext newContext()
  {
    return getInstance().new ParallelContext();
  }

  public static ParallelProcessor2 getInstance()
  {
    return INSTANCE;
  }

  private ParallelProcessor2()
  {
    cores = Math.min(8, Runtime.getRuntime().availableProcessors());
    executor = Executors.newFixedThreadPool(cores, (Runnable r) ->
    {
      Thread thread = new Thread(r);
      thread.setDaemon(true);
      return thread;
    });
  }

  /**
   * A context is the interface for one thread to submit jobs to the worker
   * pool. Contexts must not be shared between different
   */
  public class ParallelContext
  {
    LinkedList<Future> delegated = new LinkedList<>();
    int size = 0;

    /**
     * Place an job in the queue.
     *
     * @param callable
     */
    public synchronized int delegate(Callable callable) throws ExecutionException
    {
      if (cores == 0)
      {
        try
        {
          callable.call();
        }
        catch (Exception ex)
        {
          throw new ExecutionException(ex);
        }
        return 0;
      }

      Future future = executor.submit(callable);
      delegated.add(future);

      // Clean up the list starting from the oldest
      while (!delegated.isEmpty() && delegated.getFirst().isDone())
      {
        delegated.removeFirst();
      }

      // Keep the size of the queue under twice the number of cores.
      if (delegated.size() > 2 * cores)
        waitForFree(2 * cores);

      return delegated.size();
    }

    public synchronized int waitForFree(int size) throws ExecutionException
    {
      try
      {

        while (true)
        {
          Iterator<Future> iterator = this.delegated.iterator();
          // Retire old jobs
          while (iterator.hasNext())
          {
            Future next = iterator.next();
            if (next.isDone())
            {
              next.get();
              iterator.remove();
            }
          }

          // Check the number of unretired jobs
          int post = this.delegated.size();
          if (post <= size)
            return post;

          if (delegated.isEmpty())
            return 0;

          // Wait on the oldest 
          try
          {
            delegated.getFirst().get(10, TimeUnit.MICROSECONDS);
          }
          catch (TimeoutException ex)
          {
          }
        }
      }
      catch (InterruptedException ex)
      {
        throw new RuntimeException(ex);
      }
    }

    /**
     * Wait for all delegated jobs to complete before proceeding.
     */
    public synchronized void waitForAll() throws ExecutionException
    {
      while (!delegated.isEmpty())
      {
        try
        {
          Future future = delegated.removeFirst();
          future.get();
          size--;
        }
        catch (InterruptedException ex)
        {
          throw new RuntimeException(ex);
        }
      }
    }

    public int size()
    {
      return cores;
    }
  }

  static public void main(String[] args) throws ExecutionException
  {
    ParallelContext context = ParallelProcessor2.newContext();
    System.out.println(context.size());
    context.delegate((Callable) () ->
    {
      System.out.println("Wait");
      Thread.sleep(10000);
      System.out.println("Done");
      return null;
    });

    context.waitForAll();
    System.out.println("Complete");
  }
}
