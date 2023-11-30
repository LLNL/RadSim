/*
 * Copyright 2016, Lawrence Livermore National Security, LLC.
 * All rights reserved
 *
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.utility;

import java.util.Iterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.DoubleUnaryOperator;
import java.util.function.Function;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Collection of utilities for working with iterators.
 */
public class Iterators
{
  public static <T> Stream<T> stream(Iterator<T> iterator)
  {
    return StreamSupport.stream(
                Spliterators.spliteratorUnknownSize(
                        iterator,
                        Spliterator.ORDERED)
                , false);
  }
  
  public static <T, T2> Iterator<T> cast(Iterator<T2> iter, Class<T> kls)
  {
    return new CastableIterator<>(iter);
  }

  public static <T, T2 extends T> Iterator<T> cast(Iterator<T2> iter)
  {
    return new CastableIterator<>(iter);
  }

  public static <T> Iterable<T> iterate(Stream<T> t)
  {
    return (Iterable<T>) t::iterator;
  }
        
  /**
   * Apply an adapter to every element of an Iterator.
   *
   * Adapter is applied on the fly as the elements are traversed.
   *
   * @param <T>
   * @param <T2>
   * @param iter
   * @param adapter
   * @return
   */
  public static <T, T2> Iterator<T> adapt(Iterator<T2> iter, Function<T2, T> adapter)
  {
    return new Iterator<T>()
    {
      @Override
      public boolean hasNext()
      {
        return iter.hasNext();
      }

      @Override
      public T next()
      {
        return adapter.apply(iter.next());
      }
    };
  }

  /**
   * Apply an adapter to every element of an Iterable.
   *
   * Adapter is applied on the fly as the elements are traversed.
   *
   * @param <T>
   * @param <T2>
   * @param iter
   * @param adapter
   * @return
   */
  public static <T, T2> Iterable<T> adapt(Iterable<T2> iter, Function<T2, T> adapter)
  {
    return () -> adapt(iter.iterator(), adapter);
  }

  /**
   * Adaptor for casting the type as it is iterated through.
   *
   * @author nelson85
   * @param <T>
   */
  public static class CastableIterator<T, T2> implements Iterator<T>
  {
    Iterator<T2> iter;

    CastableIterator(Iterator<T2> iter)
    {
      super();
      this.iter = iter;
    }

    @Override
    public boolean hasNext()
    {
      return iter.hasNext();
    }

    @Override
    @SuppressWarnings("unchecked")
    public T next()
    {
      return (T) iter.next();
    }

    @Override
    public void remove()
    {
      iter.remove();
    }
  }

  static public <R, T> Iterator<R> adapter(Iterator<T> iter, Function<T, R> accessor)
  {
    return new AdapterIterator<>(iter, accessor);
  }

  static class AdapterIterator<R, T> implements Iterator<R>
  {
    Iterator<T> iter;
    Function<T, R> accessor;

    AdapterIterator(Iterator<T> iter, Function<T, R> accessor)
    {
      this.iter = iter;
      this.accessor = accessor;
    }

    @Override
    public boolean hasNext()
    {
      return iter.hasNext();
    }

    @Override
    public R next()
    {
      return accessor.apply(iter.next());
    }

    @Override
    public void remove()
    {
      iter.remove();
    }
  }
}
