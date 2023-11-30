/* 
 * Copyright 2019, Lawrence Livermore National Security, LLC.
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.math.stream;

/**
 *
 * @author nelson85
 */
@DoubleIndexStream.Operation.Parallel
class MaximumOperation implements DoubleIndexStream.Operation<DoubleIndex, DoubleIndexImpl>
{

  @Override
  public void accept(DoubleIndexImpl a, int index, double value)
  {
    if (!a.exists() || a.value_ < value)
    {
      a.value_ = value;
      a.index_ = index;
    }
  }

  @Override
  public DoubleIndexImpl accumulator()
  {
    return new DoubleIndexImpl(-1, 0);
  }

  @Override
  public DoubleIndexImpl combine(DoubleIndexImpl a, DoubleIndexImpl b)
  {
    if (!a.exists())
      return b;
    if (!b.exists() || a.value_ > b.value_)
      return a;
    return b;
  }

  @Override
  public DoubleIndex finish(DoubleIndexImpl a)
  {
    return a;
  }

  @Override
  public boolean hasCharacteristic(Class cls)
  {
    return cls == DoubleIndexStream.Operation.Parallel.class;
  }

}
