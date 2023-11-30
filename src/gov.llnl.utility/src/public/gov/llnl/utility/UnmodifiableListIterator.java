/*
 * Copyright 2016, Lawrence Livermore National Security, LLC. 
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.utility;

import java.util.ListIterator;

/**
 *
 * @author nelson85
 */
public abstract class UnmodifiableListIterator<T> implements ListIterator<T>
{


  @Override
  public void remove()
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public void set(T e)
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public void add(T e)
  {
    throw new UnsupportedOperationException();
  }
  
}
