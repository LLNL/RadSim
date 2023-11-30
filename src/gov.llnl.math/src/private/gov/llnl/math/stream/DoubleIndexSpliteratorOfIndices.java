/* 
 * Copyright 2019, Lawrence Livermore National Security, LLC.
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.math.stream;

import java.util.Spliterator;
import java.util.function.IntConsumer;

/**
 *
 * @author nelson85
 */
class DoubleIndexSpliteratorOfIndices implements Spliterator.OfInt
{
  DoubleIndexSpliterator source;

  DoubleIndexSpliteratorOfIndices(DoubleIndexSpliterator source)
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
  public boolean tryAdvance(IntConsumer arg0)
  {
    return source.tryAdvance((i, d) -> arg0.accept(i));
  }

  @Override
  public Spliterator.OfInt trySplit()
  {
    DoubleIndexSpliterator out = source.trySplit();
    if (out == null)
      return null;
    return new DoubleIndexSpliteratorOfIndices(out);
  }
  
}
