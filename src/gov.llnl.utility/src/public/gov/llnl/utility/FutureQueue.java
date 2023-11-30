/*
 * Copyright 2017, Lawrence Livermore National Security, LLC.
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.utility;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * Utility class for queuing up results from a single thread executor. Methods
 * are not synchronized thus they should only be used from a single submission
 * thread unless external locks are added.
 *
 * @author nelson85
 */
public class FutureQueue<T>
{
  List<Future<T>> queue = new LinkedList<>();

  /**
   * Put a future in the queue to retrieve when complete.
   *
   * @param input
   */
  public void put(Future<T> input)
  {
    queue.add(input);
  }

  /**
   * Determine if the queue is empty.
   *
   * @return true if the queue is empty.
   */
  public boolean isEmpty()
  {
    return queue.isEmpty();
  }

  /**
   * Get the number of outstanding items in the queue.
   *
   * @return the number of pending items.
   */
  public int size()
  {
    return queue.size();
  }

  /**
   * Get the next result in the queue. Does not block. Null results are eaten.
   *
   * @return the next available result or null if no non-null results are
   * available.
   * @throws CancellationException if the computation was cancelled
   * @throws ExecutionException if the computation threw an exception
   * @throws InterruptedException if the current thread was interrupted while
   * waiting
   */
  public T get() throws CancellationException, InterruptedException, ExecutionException
  {
    Iterator<Future<T>> iter = queue.iterator();
    while (iter.hasNext())
    {
      Future<T> next = iter.next();
      if (!next.isDone())
        return null;
      iter.remove();
      T out = next.get();
      if (out != null)
        return out;
    }
    return null;
  }
}
