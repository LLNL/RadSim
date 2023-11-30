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
class FindOfOperation implements DoubleIndexStream.Operation<DoubleIndex, DoubleIndexImpl>
{
  boolean done_ = false;
  DoublePredicate predicate;
  boolean reverse;

  public FindOfOperation(DoublePredicate predicate, boolean reverse)
  {
    this.predicate = predicate;
    this.reverse = reverse;
  }

  @Override
  public void accept(DoubleIndexImpl a, int index, double value)
  {
    if (predicate.test(value))
    {
      a.index_ = index;
      a.value_ = value;
      done_ = true;
    }
  }

  @Override
  public DoubleIndexImpl combine(DoubleIndexImpl a, DoubleIndexImpl b)
  {
    return null;
  }

  public boolean done()
  {
    return done_;
  }

  @Override
  public DoubleIndexImpl accumulator()
  {
    return new DoubleIndexImpl(-1, 0);
  }

  @Override
  public boolean hasCharacteristic(Class cls)
  {
    return (cls == DoubleIndexStream.Operation.Predicated.class) || 
            ((cls == DoubleIndexStream.Operation.Reverse.class)&&reverse);
  }

  @Override
  public DoubleIndex finish(DoubleIndexImpl a)
  {
    return a;
  }

}
