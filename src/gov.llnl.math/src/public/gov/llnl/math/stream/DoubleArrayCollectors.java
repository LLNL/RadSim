/*
 * Copyright 2016, Lawrence Livermore National Security, LLC. 
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.math.stream;

import gov.llnl.math.DoubleArray;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

/**
 * Utilities to deal with a stream of double[].
 *
 * Additional methods for this type of stream are in MatrixCollectors.
 *
 * @author nelson85
 */
public class DoubleArrayCollectors
{
  private static final Set<Collector.Characteristics> CHARACTERISTICS
          = Collections.unmodifiableSet(EnumSet.of(
                  Collector.Characteristics.CONCURRENT,
                  Collector.Characteristics.UNORDERED));

  /**
   * Average a series of double[].
   *
   * All arrays must be the same length.
   *
   * @return the element-wise average of a series of double arrays.
   */
  static public Collector<double[], ?, double[]> mean()
  {
    return new Collector<double[], DoubleArrayAccumulator, double[]>()
    {
      @Override
      public Supplier<DoubleArrayAccumulator> supplier()
      {
        return DoubleArrayAccumulator::new;
      }

      @Override
      public BiConsumer<DoubleArrayAccumulator, double[]> accumulator()
      {
        return DoubleArrayAccumulator::accumulate;
      }

      @Override
      public BinaryOperator<DoubleArrayAccumulator> combiner()
      {
        return DoubleArrayAccumulator::combine;
      }

      @Override
      public Function<DoubleArrayAccumulator, double[]> finisher()
      {
        return p ->
        {
          if (p.n == 0)
            return null;
          return DoubleArray.divideAssign(p.contents, p.n);
        };
      }

      @Override
      public Set<Collector.Characteristics> characteristics()
      {
        return CHARACTERISTICS;
      }
    };
  }

  /**
   * Collects a set of double arrays with the same length and sums them.
   *
   * @return
   */
  public static Collector<double[], ?, double[]> sum()
  {
    return new Collector<double[], DoubleArrayAccumulator, double[]>()
    {
      @Override
      public Supplier<DoubleArrayAccumulator> supplier()
      {
        return DoubleArrayAccumulator::new;
      }

      @Override
      public BiConsumer<DoubleArrayAccumulator, double[]> accumulator()
      {
        return DoubleArrayAccumulator::accumulate;
      }

      @Override
      public BinaryOperator<DoubleArrayAccumulator> combiner()
      {
        return DoubleArrayAccumulator::combine;
      }

      @Override
      public Function<DoubleArrayAccumulator, double[]> finisher()
      {
        return p -> p.contents;
      }

      @Override
      public Set characteristics()
      {
        return CHARACTERISTICS;
      }
    };
  }  

//<editor-fold desc="internal">  
  static class DoubleArrayAccumulator
  {
    double[] contents;
    int n;

    void accumulate(double[] v)
    {
      if (contents == null)
        contents = v.clone();
      else
        DoubleArray.addAssign(contents, v);
      n++;
    }

    DoubleArrayAccumulator combine(DoubleArrayAccumulator v)
    {
      this.n += v.n;
      if (v.contents == null)
        return this;
      if (this.contents == null)
        return v;
      else
        DoubleArray.addAssign(contents, v.contents);
      return this;
    }
  }
//</editor-fold>
}
