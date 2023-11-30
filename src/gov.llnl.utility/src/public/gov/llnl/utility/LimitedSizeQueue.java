/*
 * Copyright 2016, Lawrence Livermore National Security, LLC. 
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.utility;

import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * @author seilhan3
 */
public class LimitedSizeQueue<T> implements Iterable<T>
{
  public Object[] storage = null;
  public int index = -1; // points to the last entry filled
  public int filled = 0;

  public LimitedSizeQueue(int size)
  {
    if (size > 0)
      storage = new Object[size];
  }

  public Iterable<T> enqueueAll(Iterable<T> in)
  {
    ArrayList<T> out = new ArrayList<>();
    for (T obj : in)
    {
      T o = enqueue(obj);
      if (o != null)
        out.add(o);
    }
    return out;
  }

  public T enqueue(T in)
  {
    if (in == null)
      return null;
    if (storage == null)
      return in;
    // advance to next spot
    index++;
    if (index == storage.length)
      index = 0;
    if (filled < storage.length)
      filled++;
    // replace with a value;
    T out = (T) storage[index];
    storage[index] = in;
    return out;
  }

  public void clear()
  {
    if (storage != null)
      for (int i = 0; i < storage.length; ++i)
      {
        storage[i] = null;
      }
    index = -1;
    filled = 0;
  }

  public T get(int i)
  {
    if (i >= filled)
      return null;
    int i2 = index - i;
    if (i2 < 0)
      i2 += storage.length;
    return (T) storage[i2];
  }

  public int size()
  {
    return filled;
  }

  public boolean isFull()
  {
    return filled == storage.length;
  }

  @Override
  public Iterator<T> iterator()
  {
    return new IteratorImpl();
  }

  public class IteratorImpl implements Iterator<T>
  {
    int i;
    int counter;

    IteratorImpl()
    {
      if (LimitedSizeQueue.this.filled < LimitedSizeQueue.this.storage.length)
        i = 0;
      else
        i = LimitedSizeQueue.this.index + 1;
      if (i >= LimitedSizeQueue.this.storage.length)
        i = 0;
      this.counter = filled;

    }

    @Override
    public boolean hasNext()
    {
//      System.out.println(i + " " + HistoryQueueImpl.this.index);
      return counter > 0;
    }

    @Override
    public T next()
    {
      T out = (T) LimitedSizeQueue.this.storage[i++];
      --counter;
      // Roll over for rolling buffer
      if (i >= LimitedSizeQueue.this.storage.length)
        i = 0;

      // Return the current index
      return out;
    }

    @Override
    public void remove()
    {
      throw new UnsupportedOperationException();
    }
  }

}
