/*
 * Copyright 2016, Lawrence Livermore National Security, LLC. 
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.utility;

import gov.llnl.utility.annotation.Internal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * View for iterating in reverse order.
 */
@Internal
class ReverseCollection<T> implements Collection<T>
{
  private final List<T> list;

  public ReverseCollection(List<T> list)
  {
    this.list = list;
  }

  @Override
  public Iterator<T> iterator()
  {
    return new Iterator<T>()
    {
      final ListIterator<T> iter = list.listIterator(list.size());

      @Override
      public boolean hasNext()
      {
        return iter.hasPrevious();
      }

      @Override
      public T next()
      {
        return iter.previous();
      }

      @Override
      public void remove()
      {
        iter.remove();
      }
    };
  }

  @Override
  public int size()
  {
    return this.list.size();
  }

  @Override
  public boolean isEmpty()
  {
    return this.list.isEmpty();
  }

  @Override
  public boolean contains(Object o)
  {
    return this.list.contains(o);
  }

  @Override
  public Object[] toArray()
  {
    Object[] out = new Object[this.size()];
    int i = 0;
    for (Object v : this)
    {
      out[i++] = v;
    }
    return out;
  }

  @Override
  public <T> T[] toArray(T[] a)
  {
    @SuppressWarnings("unchecked")
    List<T> q = new ArrayList(list);
    Collections.reverse(q);
    q.toArray(a);
    return a;
  }

  @Override
  public boolean add(T e)
  {
    list.add(0, e);
    return true;
  }

  @Override
  public boolean remove(Object o)
  {
    return list.remove(o);
  }

  @Override
  public boolean containsAll(Collection<?> c)
  {
    return list.containsAll(c);
  }

  @Override
  public boolean addAll(Collection<? extends T> c)
  {
    for (T element : c)
    {
      list.add(0, element);
    }
    return true;
  }

  @Override
  public boolean removeAll(Collection<?> c)
  {
    return list.removeAll(c);
  }

  @Override
  public boolean retainAll(Collection<?> c)
  {
    return list.retainAll(c);
  }

  @Override
  public void clear()
  {
    list.clear();
  }

}
