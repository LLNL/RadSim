/*
 * Copyright 2016, Lawrence Livermore National Security, LLC. 
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.math.function;

import gov.llnl.math.DoubleArray;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;

/**
 * Equivalent of Matlab polyval. For use with 3rd order or above.
 *
 * @author nelson85
 */
public class PolynomialFunction implements Function.Parameterized
{
  // WARNING the two functions here implement the order of the polynomial coefficients 
  // differently,  This could lead to problems in implementation.  
  // Matlab uses A(0)*X^n+ A(1)*X^(n-1) + ... + A(N-1)
  // should try to get all versions of the code to use the same definition

  double[] v;

  @Override
  public String toString()
  {
    StringBuilder sb = new StringBuilder();
    sb.append("polynomial(");
    sb.append(DoubleStream.of(v).mapToObj(Double::toString).collect(Collectors.joining(", ")));
    sb.append(")");
    return sb.toString();
  }

  public PolynomialFunction(double... v)
  {
    if (v == null)
      throw new NullPointerException("Coefficients must not be null");
    this.v = DoubleArray.copyOf(v);
  }

  @Override
  public double applyAsDouble(double x)
  {
    int n = v.length;
    double out = v[n - 1];
    double y = x;
    for (int i = 2; i < n; ++i)
    {
      out += y * v[n - i];
      y = y * x;
    }
    out += y * v[0];
    return out;
  }

  @Override
  public double[] toArray()
  {
    return v;
  }

  /**
   * Implementation of MATLAB polyval function.
   *
   * @param x
   * @param coef
   * @return the evaluation of the polynomial at x.
   */
  public static double polyval(double x, double[] coef)
  {
    double xp = x;
    double out = coef[0];
    for (int i = 1; i < coef.length; ++i)
    {
      out += coef[i] * xp;
      xp *= x;
    }
    return out;
  }

  /**
   * SVM polynomial
   *
   * @param x
   * @param y
   * @param coef
   * @return
   */
  public static double polyval2(double x, double y, double[] coef)
  {
    double xp = x;
    double out = coef[0];
    out += coef[1] * x;
    out += coef[2] * y;

    double x2 = x * x;
    double y2 = y * y;
    out += coef[3] * x2;
    out += coef[4] * x * y;
    out += coef[5] * y2;

    out += coef[6] * x2 * x;
    out += coef[7] * x2 * y;
    out += coef[8] * y2 * x;
    out += coef[9] * y2 * y;
    return out;
  }

  public boolean equals(Object o)
  {
    if (!(o instanceof PolynomialFunction))
      return false;
    PolynomialFunction o2= (PolynomialFunction) o; 
    return Arrays.equals(o2.v, this.v);
  }
}
