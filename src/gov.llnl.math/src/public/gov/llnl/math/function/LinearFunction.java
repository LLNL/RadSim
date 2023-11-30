/* 
 * Copyright 2016, Lawrence Livermore National Security, LLC.
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.math.function;

import gov.llnl.math.MathExceptions;

/**
 *
 * @author nelson85
 */
public class LinearFunction
        implements 
        Function.Differentiable, 
        Function.Invertable, 
        Function.Parameterized
{
  double offset;
  double slope;

  public LinearFunction()
  {
  }

  public LinearFunction(double offset, double slope)
  {
    this.offset = offset;
    this.slope = slope;
  }

  @Override
  public double applyAsDouble(double x)
  {
    return offset + slope * x;
  }

  @Override
  public double inverse(double y) throws MathExceptions.RangeException
  {
    double denom = slope;
    if (denom == 0)
      throw new MathExceptions.RangeException("Slope is zero");
    return (y - offset) / denom;
  }

  @Override
  public double derivative(double y)
  {
    return slope;
  }

  @Override
  public LinearFunction clone() throws CloneNotSupportedException
  {
    return new LinearFunction(this.offset, this.slope);
  }

  @Override
  public double[] toArray()
  {
    return new double[]
    {
      offset, slope
    };
  }
  
  @Override
  public boolean equals(Object o)
  {
    if (!(o instanceof LinearFunction))
      return false;
    LinearFunction o2=(LinearFunction) o;
    return this.offset == o2.offset && this.slope == o2.slope;
  }
}
