/*
 * Copyright 2016, Lawrence Livermore National Security, LLC. 
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.utility;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.function.Function;

/**
 *
 * @author nelson85
 */
public class CollectionUtilities
{

  static public <R, T> Collection<R> adapter(Collection<T> base, Function<T, R> accessor)
  {
    return new AdapterCollection<>(base, accessor);
  }

  static class AdapterCollection<R, T> implements Collection<R>
  {
    Collection<T> base;
    Function<T, R> accessor;

    AdapterCollection(Collection<T> base, Function<T, R> accessor)
    {
      this.base = base;
      this.accessor = accessor;
    }

    @Override
    public int size()
    {
      return base.size();
    }

    @Override
    public boolean isEmpty()
    {
      return base.isEmpty();
    }

    @Override
    public boolean contains(Object o)
    {
      return find(o) != null;
    }

    @Override
    public Iterator<R> iterator()
    {
      return Iterators.<R, T>adapter(base.iterator(), accessor);
    }

    @Override
    public Object[] toArray()
    {
      return CollectionUtilities.toArray(this);
    }

    @Override
    public <T> T[] toArray(T[] ts)
    {
      return (T[]) new ArrayList<>(this).toArray(ts);
    }

    @Override
    public boolean add(R e)
    {
      throw new UnsupportedOperationException("Not supported.");
    }

    final Iterator<T> find(Object o)
    {
      Iterator<T> iter;
      for (iter = base.iterator(); iter.hasNext();)
      {
        T next = iter.next();
        if (next.equals(o))
          return iter;
      }
      return null;
    }

    @Override
    public boolean remove(Object o)
    {
      Iterator<T> iter = find(o);
      if (iter == null)
        return false;
      iter.remove();
      return true;
    }

    @Override
    public void clear()
    {
      base.clear();
    }

//<editor-fold desc="unsupported">
    @Override
    public boolean containsAll(Collection<?> clctn)
    {
      throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public boolean addAll(Collection<? extends R> clctn)
    {
      throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public boolean removeAll(Collection<?> clctn)
    {
      throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public boolean retainAll(Collection<?> clctn)
    {
      throw new UnsupportedOperationException("Not supported.");
    }
//</editor-fold>
  }

  /**
   * Function to get an object
   *
   * @param collection
   * @return
   */
  public static Object[] toArray(Collection collection)
  {
    Object[] out = new Object[collection.size()];
    int i = 0;
    for (Object obj : collection)
    {
      out[i++] = obj;
    }
    return out;
  }

}
