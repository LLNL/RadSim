/* 
 * Copyright 2019, Lawrence Livermore National Security, LLC.
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.math.stream;

import java.util.Spliterator;

/**
 *
 * @author nelson85
 */
class DoubleIndexSpliteratorImpl implements DoubleIndexSpliterator
{
  double[] values;
  int begin;
  int end;

  public DoubleIndexSpliteratorImpl(double[] values, int begin, int end)
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
  public void range(int begin, int end)
  {
    this.begin = Math.max(this.begin, begin);
    this.end = Math.min(this.end, end);
  }

  @Override
  public boolean tryAdvance(DoubleIndexConsumer action)
  {
    if (begin >= end)
      return false;
    action.accept(begin, values[begin]);
    begin++;
    return true;
  }

  @Override
  public boolean tryAdvanceReverse(DoubleIndexConsumer action)
  {
    if (begin >= end)
      return false;
    end--;
    action.accept(end, values[end]);
    return true;
  }

  @Override
  public DoubleIndexSpliterator trySplit()
  {
    if (end - begin < 4)
      return null;
    int split = (end + begin) / 2;
    int b0 = begin;
    this.begin = split;

    return new DoubleIndexSpliteratorImpl(values, b0, split);
  }

}
