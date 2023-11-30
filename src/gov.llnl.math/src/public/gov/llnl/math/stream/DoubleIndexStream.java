/* 
 * Copyright 2019, Lawrence Livermore National Security, LLC.
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.math.stream;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.OptionalDouble;
import java.util.Spliterator;
import java.util.function.DoublePredicate;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.StreamSupport;

/**
 * Stream for walking through a double array by index and value.
 * 
 * This is an implementation of streams for double arrays for use
 * in gov.llnl.math.  Normal DoubleStreams do not keep track of the 
 * indices in the underlying array which makes it difficult for many operations
 * that we do such as finding the location of the peak or searching for a crossing point.  
 * This is a partially complete implementation as we don't yet have 
 * support of parallel operations.  The underlying implementation is slower than 
 * the dedicated routines in DoubleArray class, but they are considerably more 
 * flexible.  Eventually the specialized routines will be linked into the stream
 * versions so that we only have one interface exposed.
 * 
 * @author nelson85
 */
public interface DoubleIndexStream
{

  /**
   * Reduces the range of the stream to cover a sub range.
   *
   * FIXME currently asking for a region outside the range produces the sub set
   * that overlaps the existing range and requested range, rather than throwing
   * an exception. It is not clear yet what behavior is best.
   *
   * FIXME this has a name conflict with IntStream.range().   Perhaps it should
   * be sub to match String.subst()
   * 
   * @param begin
   * @param end
   * @return
   */
  DoubleIndexStream range(int begin, int end);

  /**
   * Produce a spliterator for this data type.
   *
   * @return
   */
  DoubleIndexSpliterator spliterator();

  /**
   * Produce a spliterator for the values.
   *
   * @return
   */
  Spliterator.OfDouble spliteratorValues();

  /**
   * Produce a spliterator for the indices.
   *
   * @return
   */
  Spliterator.OfInt spliteratorIndices();

  /**
   * Collect the items into a result.
   *
   * Depending on the operation this may traverse the entire list serially or in
   * parallel. Predicated operations may terminate early.
   *
   * Operations in reverse are permitted if the items are finite and known in
   * advance. Reverse operations on infinite or undefined streams will produce
   * an error.
   *
   * @param <R>
   * @param <A>
   * @param op is a class that implements
   * @return
   */
  <R, A> R collect(Operation<R, A> op);

  /**
   * Select only a subset of items in the stream.
   *
   * This is an intermediate operation.
   * 
   * @param condition
   * @return
   */
  DoubleIndexStream filter(DoublePredicate condition);

  default boolean isParallel()
  {
    return false;
  }
  
  static DoubleIndexStream of(double ... d)
  {
    return new DoubleIndexStreamImpl(d, 0, d.length);
  }
  
  /**
   * Create a new stream from a spliterator.
   *
   * This is an intermediate operation.
   * 
   * @param siter
   * @return
   */
  static DoubleIndexStream from(DoubleIndexSpliterator siter)
  {
    return new DoubleIndexStreamProxy(siter);
  }

  /**
   * Restrict the range by specifying a new beginning.
   *
   * This is an intermediate operation.
   * 
   * @param begin
   * @return
   */
  default DoubleIndexStream range(int begin)
  {
    return this.range(begin, Integer.MAX_VALUE);
  }

  /**
   * Operate on all items in the stream.
   * 
   * This is a terminator.
   *
   * @param consumer
   */
  default void forEach(DoubleIndexConsumer consumer)
  {
    this.collect(DoubleIndexCollectors.forEach(consumer));
  }

  /**
   * Get a stream containing the values.
   * 
   * The stream is sequential by default.
   *
   * This is an intermediate operation.
   * 
   * @return
   */
  default DoubleStream values()
  {
    return StreamSupport.doubleStream(this.spliteratorValues(), isParallel());
  }

  /**
   * Get a stream containing the indices.
   * 
   * The stream is sequenctial by default.
   * 
   * This is an intermediate operation.
   *
   * @return
   */
  default IntStream indices()
  {
    return StreamSupport.intStream(this.spliteratorIndices(), isParallel());
  }

  /**
   * Get the mean of the items in the stream.
   *
   * This is a terminator.
   * 
   * @return
   */
  default OptionalDouble mean()
  {
    return values().average();
  }

  /**
   * Get the standard deviation of the items in the stream.
   * 
   * This is a terminator.
   *
   * @return
   */
  default OptionalDouble std()
  {
    OptionalDouble out = variance();
    if (out.isPresent())
      return OptionalDouble.of(Math.sqrt(out.getAsDouble()));
    return out;
  }

  /**
   * Get the variance of the items in the stream.
   *
   * This is a terminator.
   * 
   * @return the variance if it exists.
   */
  default OptionalDouble variance()
  {
    return this.collect(DoubleIndexCollectors.variance());
  }

  /**
   * Count the items in the stream.
   *
   * This is a terminator.
   * 
   * @return the number of items in the stream.
   */
  default long count()
  {
    return values().count();
  }

  /**
   * Get the maximum of the items in the stream.
   *
   * This does not track multiple maxima.
   *
   * @return the value and index if they exist.
   */
  default DoubleIndex maximum()
  {
    return this.collect(DoubleIndexCollectors.maximum());
  }

  /**
   * Get the minimum of the items in the stream.
   *
   * @return the value and index if they exist.
   */
  default DoubleIndex minimum()
  {
    return this.collect(DoubleIndexCollectors.minimum());
  }

  /** Return the first item that meets the condition.
   * 
   * @param condition
   * @return 
   */
  default DoubleIndex first(DoublePredicate condition)
  {
    return this.collect(new FindOfOperation(condition, false));
  }

  /** Return the last item that meets the condition.
   * 
   * This operation requires reverse operations which may not be allowed.
   * If the stream does not support  reverse operations, then an exception 
   * will be thrown.
   * 
   * @param condition
   * @return 
   */
  default DoubleIndex last(DoublePredicate condition)
  {
    return this.collect(new FindOfOperation(condition, true));
  }

  /** Definition of a collector for DoubleIndexStreams.
   * 
   * @param <R>
   * @param <A> 
   */
  public interface Operation<R, A>
  {
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Parallel
    {
    }
    
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Predicated
    {
    }
    
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Reverse
    {
    }

    default boolean hasCharacteristic(Class cls)
    {
      return (this.getClass().getAnnotation(cls)!=null);
    }

    A accumulator();

    R finish(A a);

    void accept(A a, int index, double value);

    A combine(A a, A b);

    default boolean done()
    {
      return false;
    }
  }

}
