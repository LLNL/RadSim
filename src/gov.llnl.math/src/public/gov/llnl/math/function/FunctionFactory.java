/*
 * Copyright 2016, Lawrence Livermore National Security, LLC. 
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.math.function;

/**
 * Factory for commonly used functions. Primarily here to make pythons life
 * simpler.
 *
 * @author nelson85
 */
public class FunctionFactory
{

  /**
   * Linear function of the form y=k*x
   *
   * @param k is the slope
   * @return
   */
  static public LinearFunction newLinear(double k)
  {
    return new LinearFunction(0, k);
  }

  /**
   * Quadratic function of the form y = a x^2+b x + c
   *
   * @param offset
   * @param slope
   * @param accel
   * @return
   */
  static public QuadraticFunction newQuadratic(double offset, double slope, double accel)
  {
    return new QuadraticFunction(offset, slope, accel);
  }

  /**
   * Polynomial function of the form y=\Sum_i a_i x^i.
   *
   * Order of coefficients is the same as Matlab with highest first.
   * Such as [a3,a2,a1,a0].
   * 
   * @param a
   * @return
   */
  static public PolynomialFunction newPolynomial(double[] a)
  {
    return new PolynomialFunction(a);
  }

  /**
   * Power function given as y=k x^p.
   *
   * @param k
   * @param p
   * @return
   */
  static public PowerFunction newPower(double k, double p)
  {
    return new PowerFunction(k, p);
  }
}
