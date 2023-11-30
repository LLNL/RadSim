/* 
 * Copyright 2019, Lawrence Livermore National Security, LLC.
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.math.stream;

import java.util.Spliterator;
import java.util.function.DoublePredicate;

/**
 *
 * @author nelson85
 */
class DoubleIndexStreamProxy implements DoubleIndexStream
{
  DoubleIndexSpliterator source;

  public DoubleIndexStreamProxy(DoubleIndexSpliterator source)
  {
    this.source = source;
  }

  @Override
  @SuppressWarnings("empty-statement")
  public <R, A> R collect(Operation<R, A> op)
  {
    A a = op.accumulator();
    // Add support for parallel processing here
    if (op.hasCharacteristic(Operation.Reverse.class))
    {
      if (op.hasCharacteristic(Operation.Predicated.class))
        while (!op.done() && source.tryAdvanceReverse((i, d) -> op.accept(a, i, d)));
      else
        source.forEachRemainingReverse((i, d) -> op.accept(a, i, d));
    }
    else
    {
      if (op.hasCharacteristic(Operation.Predicated.class))
        while (!op.done() && source.tryAdvance((i, d) -> op.accept(a, i, d)));
      else
        source.forEachRemaining((i, d) -> op.accept(a, i, d));
    }
    return op.finish(a);
  }

  @Override
  public DoubleIndexStream filter(DoublePredicate condition)
  {
    return DoubleIndexStream.from(new DoubleIndexFilterSpliterator(this.spliterator(), condition));
  }

  @Override
  public DoubleIndexStream range(int begin, int end)
  {
    source.range(begin, end);
    return this;
  }

  @Override
  public DoubleIndexSpliterator spliterator()
  {
    return source;
  }

  @Override
  public Spliterator.OfInt spliteratorIndices()
  {
    return new DoubleIndexSpliteratorOfIndices(this.source);
  }

  @Override
  public Spliterator.OfDouble spliteratorValues()
  {
    return new DoubleIndexSpliteratorOfValues(this.source);
  }

}
