/*
 * Copyright 2016, Lawrence Livermore National Security, LLC. 
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.math.algebra;

import java.util.Iterator;

/**
 *
 * @author nelson85
 */
class NnlsqRegressorSetIterator implements Iterator<NnlsqRegressor>
{
  NnlsqRegressor current;
  NnlsqRegressor last;
  boolean forward = true;

  NnlsqRegressorSetIterator(NnlsqRegressor head, boolean forward)
  {
    super();
    this.current = head;
    this.forward = forward;
  }

  @Override
  public boolean hasNext()
  {
    return current != null;
  }

  @Override
  public NnlsqRegressor next()
  {
    last = current;
    if (forward)
      current = current.next;
    else
      current = current.prev;
    return last;
  }

  @Override
  public void remove()
  {
    if (last == null)
      throw new IllegalStateException();
    last.getSet().remove(last);
    last = null;
  }
  
}
