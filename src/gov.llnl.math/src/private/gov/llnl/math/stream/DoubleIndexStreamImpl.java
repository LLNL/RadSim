/* 
 * Copyright 2019, Lawrence Livermore National Security, LLC.
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.math.stream;

import java.util.Spliterator;
import java.util.function.DoubleConsumer;
import java.util.function.DoublePredicate;
import java.util.function.IntConsumer;

/**
 *
 * @author nelson85
 */
class DoubleIndexStreamImpl implements DoubleIndexStream
{
  double[] values;
  int begin;
  int end;

  public DoubleIndexStreamImpl(double[] values, int begin, int end)
  {
    this.values = values;
    this.begin = begin;
    this.end = end;
  }

  @Override
  public <R, A> R collect(Operation<R, A> op)
  {
    A a = op.accumulator();
    // Add support for parallel processing here
    if (!op.hasCharacteristic(Operation.Reverse.class))
    {
      if (op.hasCharacteristic(Operation.Predicated.class))
      {

        // Predicated can't support loop unrolling nor parallel processing
        for (int i = this.begin; i < this.end && !op.done(); ++i)
        {
          op.accept(a, i, values[i]);
        }
      }
      else
      {
        // Parallel processing and loop unrolling can happen here
        for (int i = this.begin; i < this.end; ++i)
        {
          op.accept(a, i, values[i]);
        }
      }
    }
    else
    {
      if (op.hasCharacteristic(Operation.Predicated.class))
      {
        // Predicated can't support loop unrolling
        for (int i = this.end - 1; i >= this.begin && !op.done(); --i)
        {
          op.accept(a, i, values[i]);
        }
      }
      else
      {
        // Parellel and unrolling can happen here.
        for (int i = this.end - 1; i >= this.begin; --i)
        {
          op.accept(a, i, values[i]);
        }
      }
    }
    return op.finish(a);
  }

  @Override
  public DoubleIndexStream filter(DoublePredicate condition
  )
  {
    return DoubleIndexStream.from(
            new DoubleIndexFilterSpliterator(
                    new DoubleIndexSpliteratorImpl(this.values, this.begin, this.end), condition));
  }

  @Override
  public DoubleIndexStream range(int start, int end
  )
  {
    return new DoubleIndexStreamImpl(this.values,
            Math.max(this.begin, start),
            Math.min(this.end, end));
  }

  public DoubleIndexSpliterator spliterator()
  {
    return new DoubleIndexSpliteratorImpl(this.values, this.begin, this.end);
  }

  @Override
  public Spliterator.OfDouble spliteratorValues()
  {
    return new SpliteratorOfValues(this.values, this.begin, this.end);
  }

  @Override
  public Spliterator.OfInt spliteratorIndices()
  {
    return new SpliteratorOfIndices(this.values, this.begin, this.end);
  }

  static class SpliteratorOfIndices implements Spliterator.OfInt
  {
    double[] values;
    int begin;
    int end;

    SpliteratorOfIndices(double[] values, int begin, int end)
    {
      this.values = values;
      this.begin = begin;
      this.end = end;
    }

    @Override
    public int characteristics()
    {
      return Spliterator.ORDERED;

    }

    @Override
    public long estimateSize()
    {
      return end - begin;
    }

    @Override
    public boolean tryAdvance(IntConsumer arg0)
    {
      if (begin >= end)
        return false;
      arg0.accept(begin);
      begin++;
      return true;
    }

    @Override
    public Spliterator.OfInt trySplit()
    {
      if (end - begin < 4)
        return null;
      int split = (end + begin) / 2;
      int b0 = begin;
      this.begin = split;
      return new SpliteratorOfIndices(values, b0, split);
    }
  }

  static class SpliteratorOfValues implements Spliterator.OfDouble
  {

    double[] values;
    int begin;
    int end;

    SpliteratorOfValues(double[] values, int begin, int end)
    {
      this.values = values;
      this.begin = begin;
      this.end = end;
    }

    @Override
    public int characteristics()
    {
      return Spliterator.ORDERED;
    }

    @Override
    public long estimateSize()
    {
      return end - begin;
    }

    @Override
    public boolean tryAdvance(DoubleConsumer arg0)
    {
      if (begin >= end)
        return false;
      arg0.accept(values[begin]);
      begin++;
      return true;
    }

    @Override
    public Spliterator.OfDouble trySplit()
    {
      if (end - begin < 4)
        return null;
      int split = (end + begin) / 2;
      int b0 = begin;
      this.begin = split;
      return new SpliteratorOfValues(values, b0, split);
    }
  }

}
