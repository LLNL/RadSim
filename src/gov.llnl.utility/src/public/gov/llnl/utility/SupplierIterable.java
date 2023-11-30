/*
 * Copyright 2016, Lawrence Livermore National Security, LLC. 
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.utility;

import java.util.Iterator;
import java.util.function.Supplier;

/**
 * Use a supplier as a iterable. This can only traverse the supplier once. It
 * stops on the first null.
 *
 * @author nelson85
 */
public class SupplierIterable<T> implements Iterable<T>
{
  final Supplier<T> supplier;
  T next_;

  public SupplierIterable(Supplier<T> supplier)
  {
    this.supplier = supplier;
  }

  @Override
  public Iterator<T> iterator()
  {

    return new Iterator<T>()
    {
      @Override
      public boolean hasNext()
      {
        next_ = supplier.get();
        return (next_ != null);
      }

      @Override
      public T next()
      {
        return next_;
      }

    };
  }
}
