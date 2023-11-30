/* 
 * Copyright 2016, Lawrence Livermore National Security, LLC.
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.math.function;

import gov.llnl.math.MathExceptions;
import java.util.Arrays;

/**
 *
 * @author nelson85
 */
public class QuadraticFunction implements
        Function.Differentiable,
        Function.Invertable,
        Function.Parameterized
{
  double k[] = new double[3];

  public QuadraticFunction()
  {
  }

  public QuadraticFunction(double offset, double slope, double accel)
  {
    k[0] = offset;
    k[1] = slope;
    k[2] = accel;
  }

  @Override
  public double applyAsDouble(double x)
  {
    return k[0] + k[1] * x + k[2] * x * x;
  }

  @Override
  public double inverse(double y)
  {
    if (k[2] == 0)
    {
      double denom = k[1];
      if (denom == 0)
        denom = 1e-10;
      return (y - k[0]) / denom;
    }
    else
    {
      // restricted domain best solution around 0.5
      double denom = k[2] * 2;
      if (denom == 0)
        denom = 1e-10;
      double q = k[1] * k[1] - 4 * (k[0] - y) * k[2];
      if (q < 0)
        throw new MathExceptions.RangeException("out of range");
      q = Math.sqrt(q);
      return (-k[1] + q) / denom;
    }
  }

  @Override
  public double derivative(double x)
  {
    return k[1] + 2 * k[2] * x;
  }

  @Override
  public double[] toArray()
  {
    return k.clone();
  }

  @Override
  public QuadraticFunction clone() throws CloneNotSupportedException
  {
    return new QuadraticFunction(this.k[0],this.k[1],this.k[2]);
  }

  @Override
  public boolean equals(Object o)
  {
    if (!(o instanceof QuadraticFunction))
      return false;
    QuadraticFunction o2 = (QuadraticFunction) o;
    return Arrays.equals(this.k, o2.k);
  }

  @Override
  public int hashCode()
  {
    int hash = 5;
    hash = 37 * hash + Arrays.hashCode(this.k);
    return hash;
  }
}
