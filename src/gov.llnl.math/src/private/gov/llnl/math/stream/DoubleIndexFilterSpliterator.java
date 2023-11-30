/* 
 * Copyright 2019, Lawrence Livermore National Security, LLC.
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.math.stream;

import java.util.function.DoublePredicate;

/**
 *
 * @author nelson85
 */
class DoubleIndexFilterSpliterator implements DoubleIndexSpliterator
{
  DoubleIndexSpliterator source;
  DoublePredicate filter;

  public DoubleIndexFilterSpliterator(DoubleIndexSpliterator source, DoublePredicate filter)
  {
    this.source = source;
    this.filter = filter;
  }

  @Override
  public int characteristics()
  {
    return source.characteristics();
  }

  @Override
  public long estimateSize()
  {
    return source.estimateSize();
  }

  @Override
  public void range(int begin, int end)
  {
    source.range(begin, end);
  }

  @Override
  public boolean tryAdvance(DoubleIndexConsumer action)
  {
    return source.tryAdvance((i, d) ->
    {
      if (filter.test(d))
        action.accept(i, d);
    });
  }

  @Override
  public boolean tryAdvanceReverse(DoubleIndexConsumer action)
  {
    return source.tryAdvanceReverse((i, d) ->
    {
      if (filter.test(d))
        action.accept(i, d);
    });
  }

  @Override
  public DoubleIndexSpliterator trySplit()
  {
    DoubleIndexSpliterator out = source.trySplit();
    if (out == null)
      return null;
    return new DoubleIndexFilterSpliterator(out, filter);
  }

}
