/* 
 * Copyright 2019, Lawrence Livermore National Security, LLC.
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.math.stream;

import java.util.OptionalDouble;

/**
 *
 * @author nelson85
 */
@DoubleIndexStream.Operation.Parallel
class VarianceOperation implements DoubleIndexStream.Operation<OptionalDouble, VarianceOperation.Moments>
{
  @Override
  public void accept(Moments a, int index, double value)
  {
    a.count++;
    a.m0 += value;
    a.m1 += value * value;
  }

  @Override
  public Moments accumulator()
  {
    return new Moments();
  }

  @Override
  public boolean hasCharacteristic(Class cls)
  {
    return cls == DoubleIndexStream.Operation.Parallel.class;
  }

  @Override
  public Moments combine(Moments a, Moments b)
  {
    a.count += b.count;
    a.m0 += b.m0;
    a.m1 += b.m1;
    return a;
  }

  @Override
  public OptionalDouble finish(Moments a)
  {
    if (a.count == 0)
      return OptionalDouble.empty();
    a.m0 /= a.count;
    return OptionalDouble.of((a.m1 - a.count * a.m0 * a.m0) / (a.count - 1));
  }

  static class Moments
  {
    int count = 0;
    double m0 = 0;
    double m1 = 0;
  }

  public VarianceOperation()
  {
  }

}
