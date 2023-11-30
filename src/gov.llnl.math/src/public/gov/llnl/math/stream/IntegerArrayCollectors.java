/*
 * Copyright 2016, Lawrence Livermore National Security, LLC. 
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.math.stream;

import gov.llnl.math.DoubleArray;
import gov.llnl.math.IntegerArray;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

/**
 * Utilities to deal with a stream of int[].
 *
 * Additional methods for this type of stream are in MatrixCollectors.
 *
 * @author nelson85
 */
public class IntegerArrayCollectors
{
  private static final Set<Collector.Characteristics> CHARACTERISTICS
          = Collections.unmodifiableSet(EnumSet.of(
                  Collector.Characteristics.CONCURRENT,
                  Collector.Characteristics.UNORDERED));

  /**
   * Collects a set of int arrays with the same length and sums them.
   *
   * @return
   */
  public static Collector<int[], ?, int[]> sum()
  {
    return new Collector<int[], IntArrayAccumulator, int[]>()
    {
      @Override
      public Supplier<IntArrayAccumulator> supplier()
      {
        return IntArrayAccumulator::new;
      }

      @Override
      public BiConsumer<IntArrayAccumulator, int[]> accumulator()
      {
        return IntArrayAccumulator::accumulate;
      }

      @Override
      public BinaryOperator<IntArrayAccumulator> combiner()
      {
        return IntArrayAccumulator::combine;
      }

      @Override
      public Function<IntArrayAccumulator, int[]> finisher()
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
  static class IntArrayAccumulator
  {
    int[] contents;
    int n;

    void accumulate(int[] v)
    {
      if (contents == null)
        contents = v.clone();
      else
        IntegerArray.addAssign(contents, v);
      n++;
    }

    IntArrayAccumulator combine(IntArrayAccumulator v)
    {
      this.n += v.n;
      if (v.contents == null)
        return this;
      if (this.contents == null)
        return v;
      else
        IntegerArray.addAssign(contents, v.contents);
      return this;
    }
  }
//</editor-fold>
}
