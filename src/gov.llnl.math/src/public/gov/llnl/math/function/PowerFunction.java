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
 * @author seilhan3
 */
public class PowerFunction implements Function.Invertable,
        Function.Parameterized
{
  double k;
  double p;

  public PowerFunction(double k, double p)
  {
    this.k = k;
    this.p = p;
  }

  @Override
  public double inverse(double y) throws MathExceptions.RangeException
  {
    if (y == 0)
      return Double.NaN;
    return Math.pow(y / k, 1 / p);
  }

  @Override
  public double applyAsDouble(double x) throws MathExceptions.DomainException
  {
    return k * Math.pow(x, p);
  }
  
  
  @Override
  public double[] toArray()
  {
    return new double[]
    {
      k, p
    };
  }
  
  @Override
  public boolean equals(Object o)
  {
    if (!(o instanceof PowerFunction))
      return false;
    PowerFunction o2 = (PowerFunction) o;
    return this.k == o2.k && this.p == o2.p;
  }

  @Override
  public int hashCode()
  {
    int hash = 5;
    hash = 47 * hash + (int) (Double.doubleToLongBits(this.k) ^ (Double.doubleToLongBits(this.k) >>> 32));
    hash = 47 * hash + (int) (Double.doubleToLongBits(this.p) ^ (Double.doubleToLongBits(this.p) >>> 32));
    return hash;
  }

}
