/*
 * Copyright 2016, Lawrence Livermore National Security, LLC. 
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.utility;

import java.util.Collection;
import java.util.List;

/**
 * Abstract class implementing an unmodifiable list.
 *
 * Java chose not to give the concept of a readonly list view. But making a read
 * only list is necessary for many concepts.
 *
 * @author nelson85
 */
public abstract class UnmodifiableList<T> implements List<T>
{

  @Override
  public boolean add(T e)
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public boolean remove(Object o)
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public boolean containsAll(Collection<?> clctn)
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public boolean addAll(Collection<? extends T> clctn)
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public boolean addAll(int i, Collection<? extends T> clctn)
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public boolean removeAll(Collection<?> clctn)
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public boolean retainAll(Collection<?> clctn)
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public void clear()
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public T set(int i, T e)
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public void add(int i, T e)
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public T remove(int i)
  {
    throw new UnsupportedOperationException();
  }

}
