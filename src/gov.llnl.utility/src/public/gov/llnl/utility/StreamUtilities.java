/*
 * Copyright 2018, Lawrence Livermore National Security, LLC. 
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.utility;

import java.util.Iterator;
import java.util.function.Supplier;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 *
 * @author nelson85
 */
public class StreamUtilities
{

  /**
   * Constructs a stream from a supplier. The stream ends when the supplier
   * produces a null.
   *
   * @param producer
   * @return
   */
  public static <T> Stream<T> from(Supplier<T> producer)
  {
    Iterator<T> data = new Iterator<T>()
    {
      T value = producer.get();

      @Override
      public boolean hasNext()
      {
        return value != null;
      }

      @Override
      public T next()
      {
        T out = value;
        value = producer.get();
        return out;
      }
    };
    Iterable<T> iterable = () -> data;
    return StreamSupport.stream(iterable.spliterator(), false);
  }
}
