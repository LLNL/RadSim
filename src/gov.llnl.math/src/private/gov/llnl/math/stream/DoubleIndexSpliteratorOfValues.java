/* 
 * Copyright 2019, Lawrence Livermore National Security, LLC.
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.math.stream;

import java.util.Spliterator;
import java.util.function.DoubleConsumer;

/**
 *
 * @author nelson85
 */
class DoubleIndexSpliteratorOfValues implements Spliterator.OfDouble
{
  DoubleIndexSpliterator source;

  DoubleIndexSpliteratorOfValues(DoubleIndexSpliterator source)
  {
    this.source = source;
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
  public boolean tryAdvance(DoubleConsumer arg0)
  {
    return source.tryAdvance((i, d) -> arg0.accept(d));
  }

  @Override
  public Spliterator.OfDouble trySplit()
  {
    DoubleIndexSpliterator out = source.trySplit();
    if (out == null)
      return null;
    return new DoubleIndexSpliteratorOfValues(out);
  }
  
}
