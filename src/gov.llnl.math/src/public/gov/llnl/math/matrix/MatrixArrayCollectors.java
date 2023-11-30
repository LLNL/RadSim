/*
 * Copyright 2016, Lawrence Livermore National Security, LLC.
 * All rights reserved
 *
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.math.matrix;

import gov.llnl.math.stream.DoubleArrayCollectors;
import java.util.Collections;
import java.util.EnumSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;
import java.util.stream.Collector;

/**
 * Utilities to operate on Stream&lt;Matrix&gt;.
 *
 * @author nelson85
 */
public class MatrixArrayCollectors
{

  /**
   * Sums a series of matrix together.
   *
   * All matrix must be the same size.
   *
   * @param <T>
   * @return
   */
  public static <T> Collector<Matrix, ?, Matrix> sum()
  {
    return new Collector<Matrix, MatrixAccumulator, Matrix>()
    {

      @Override
      public Supplier<MatrixAccumulator> supplier()
      {
        UnaryOperator<Matrix> f = (Matrix p) -> MatrixFactory.newMatrix(p);
        return () -> new MatrixAccumulator(f);
      }

      @Override
      public BiConsumer<MatrixAccumulator, Matrix> accumulator()
      {
        return MatrixAccumulator::accumulate;
      }

      @Override
      public Set<Collector.Characteristics> characteristics()
      {
        return Collections.unmodifiableSet(EnumSet.of(Collector.Characteristics.UNORDERED));
      }

      @Override
      public BinaryOperator<MatrixAccumulator> combiner()
      {
        return MatrixAccumulator::combine;
      }

      @Override
      public Function<MatrixAccumulator, Matrix> finisher()
      {
        return (MatrixAccumulator p) -> p.contents;
      }

    };
  }

  public static <T extends Matrix> Collector<Matrix, ?, T> sum(Supplier<T> t)
  {
    DoubleArrayCollectors a;
    return new Collector<Matrix, MatrixAccumulator, T>()
    {
      @Override
      public Supplier<MatrixAccumulator> supplier()
      {
        Function<Matrix, T> f = (Matrix p) ->
        {
          return (T) t.get().assign(p);
        };
        return () -> new MatrixAccumulator(f);
      }

      @Override
      public BiConsumer<MatrixAccumulator, Matrix> accumulator()
      {
        return MatrixAccumulator::accumulate;
      }

      @Override
      public BinaryOperator<MatrixAccumulator> combiner()
      {
        return MatrixAccumulator::combine;
      }

      @Override
      public Function<MatrixAccumulator, T> finisher()
      {
        return (p) -> (T) p.contents;
      }

      @Override
      public Set characteristics()
      {
        return Collections.emptySet();
      }
    };
  }

  /**
   * Collect a set of matrix by concatenating as columns.
   *
   * @param <T>
   * @return
   */
  public static <T> Collector<Matrix, ?, Matrix> hcat()
  {
    return new Collector<Matrix, List<double[]>, Matrix>()
    {
      @Override
      public BiConsumer<List<double[]>, Matrix> accumulator()
      {
        return (List<double[]> t, Matrix u) ->
        {
          for (int i = 0; i < u.columns(); ++i)
          {
            t.add(u.copyColumn(i));
          }
        };
      }

      @Override
      public Set<Collector.Characteristics> characteristics()
      {
        return Collections.emptySet();
      }

      @Override
      public BinaryOperator<List<double[]>> combiner()
      {
        return (List<double[]> t, List<double[]> u) ->
        {
          t.addAll(u);
          return t;
        };
      }

      @Override
      public Function<List<double[]>, Matrix> finisher()
      {
        return (columns) -> MatrixFactory.newColumnMatrix(columns);
      }

      @Override
      public Supplier<List<double[]>> supplier()
      {
        return () -> new LinkedList<>();
      }
    };
  }

//<editor-fold desc="internal">  
  static class MatrixAccumulator<T extends Matrix>
  {
    Function<Matrix, T> supplier;
    T contents;
    int n;

    MatrixAccumulator(Function<Matrix, T> supplier)
    {
      this.supplier = supplier;
    }

    void accumulate(Matrix v)
    {
      if (contents == null)
      {
        contents = supplier.apply(v);
      }
      else
        MatrixOps.addAssign(contents, v);
      n++;
    }

    MatrixAccumulator combine(MatrixAccumulator v)
    {
      this.n += v.n;
      if (v.contents == null)
        return this;
      if (this.contents == null)
        return v;
      MatrixOps.addAssign(contents, v.contents);
      return this;
    }
  }

  /**
   * Mutable representation to use of the accumulator.
   */
  private static class MatrixPtr
  {
    Matrix matrix = null;
  }

//</editor-fold>
}
