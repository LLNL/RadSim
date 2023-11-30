/* 
 * Copyright 2016, Lawrence Livermore National Security, LLC.
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.math;

import gov.llnl.math.SpecialFunctions;

/**
 * Utility class for computation of the regularized gamma functions which are
 * defined by the incomplete gamma functions over the gamma function. Because
 * the accuracy of each approximation method has a limited range of accuracy,
 * the most appropriate method must be selected for a given range of numbers.
 * The evaluate function has these domains incorporated into it.
 *
 * For each of the evaluation class the results are stored in the class and can
 * be accessed with getP() and getQ().
 *
 * @author nelson85
 */
class RegularizedGammaFunction
{
  public RegularizedGammaFunction()
  {
    p_ = q_ = 0;
    lngamma_ = 0;
    lnk_ = 0;
  }

  /**
   * Evaluates the regularized gamma functions with automatic domain selection.
   *
   * @param a is the gamma factorial
   * @param x is lower limit
   * @return if the computation was successful.
   */
  public boolean evaluate(double a, double x)
  {
    p_ = q_ = 0;

    // Special cases
    if (a < 0 || x < 0)
      return false;

    if (a == 0)
    {
      p_ = 1;
      return true;
    }
    if (x == 0)
    {
      q_ = 1;
      return true;
    }

    // different regions of the parameter space require different policies
    double lambda = x / a;
    if ((lambda < 0.5) || (x < 1))
    {
      return evaluateContinuedFraction1(a, x, 5);
    }
    if ((lambda > 2.5) && (x > 10))
    {
      return evaluateContinuedFraction2(a, x, 5);
    }
    if ((lambda > 1.4) && (x > 4))
    {
      return evaluateContinuedFraction2(a, x, 10);
    }
    if ((x < 2) && (a < 2))
    {
      return evaluatePowerSeries(a, x, 20);
    }
    if (lambda < 0.95)
    {
      return evaluateContinuedFraction1(a, x, 20);
    }
    if ((x > 100000) && (lambda < 0.98))
    {
      return evaluateContinuedFraction1(a, x, 40);
    }
    if (x < 10)
    {
      return evaluateContinuedFraction2(a, x, 20);
    }
    return evaluateUniformAsymptoticExpansions(a, x);
  }

  /**
   * Gives the lower regularized gamma function for a,x
   *
   * @return the parameter P.
   */
  public double getP()
  {
    return p_;
  }

  /**
   * Gives the upper regularized gamma function for a,x
   *
   * @return the parameter Q.
   */
  public double getQ()
  {
    return q_;
  }

  // Different implementations (Note each has differing range of 
  // valid results)
  /**
   * Evaluate the regularized gamma functions by power series. Converges quickly
   * if both x and a are small.
   *
   * @param a
   * @param x
   * @param iterations
   * @return
   */
  public boolean evaluatePowerSeries(double a, double x, int iterations)
  {
    // Kummer's confluent hypergeometric function
    double f = 1;
    double M = f;
    for (int i = 1; i < iterations; ++i)
    {
      f = f * x / (a + i);
      M = M + f;
      if (f < M * 1e-10)
        break;
    }
    lngamma_ = SpecialFunctions.gammaln(a);
    lnk_ = a * Math.log(x) - x - lngamma_;
    p_ = Math.exp(Math.log(M / a) + lnk_);
    q_ = 1.0 - p_;
    return true;
  }

  static final double asymptoticTable[] =
  {
    1.000000000000e+000, -3.333333333333e-001, 8.333333333333e-002,
    -1.481481481481e-002, 1.157407407407e-003, 3.527336860670e-004,
    -1.787551440329e-004, 3.919263178522e-005, -2.185448510680e-006,
    -1.854062210715e-006, 8.296711340953e-007, -1.766595273683e-007,
    6.707853543402e-009, 1.026180978424e-008, -4.382036018453e-009,
    9.147699582237e-010, -2.551419399495e-011, -5.830772132550e-011
  };

  /**
   * Evaluate regularized gamma functions using a uniform asymptotic expansion.
   *
   * @param a
   * @param x
   * @return the evaluation of the function.
   */
  public boolean evaluateUniformAsymptoticExpansions(double a, double x)
  {
    double lambda = x / a;
    double eta = Math.sqrt(2 * (lambda - 1 - Math.log(lambda)));
    if ((lambda - 1) < 0)
      eta = -eta;

    double beta = eta * Math.sqrt(a / 2);
    double efQ = SpecialFunctions.erfc(beta);
    double efP = (2 - efQ);

    final int N = 18;

    double Sa = 0;
    double bm1 = asymptoticTable[N - 1];
    double bm0 = asymptoticTable[N - 2];
    for (int i = N - 3; i > 0; i--)
    {
      double u = asymptoticTable[i] + (i + 1) / a * bm1;
      Sa = eta * Sa + u;
      bm1 = bm0;
      bm0 = u;
    }

    // "tempered" gamma function
    double loga = Math.log(a);
    lngamma_ = SpecialFunctions.gammaln(a);
    double lntgamma = lngamma_ + a - a * loga;
    double Ra = Sa * Math.exp(-a * eta * eta / 2 - lntgamma - loga);

    q_ = 0.5 * efQ + Ra;
    p_ = 0.5 * efP - Ra;
    return true;
  }

  /**
   * Evaluate the continued fraction expansion for the lower function using
   * continued fractions. The upper is computed as one minus the lower.
   *
   * @param a
   * @param x
   * @param iterations
   * @return true always
   */
  public boolean evaluateContinuedFraction1(double a, double x, int iterations)
  {
    double f = 0;
    for (int m = iterations; m > 0; m--)
    {
      f = m * x / (a + 2 * m - (a + m) * x / (a + 2 * m + 1 + f));
    }
    double cf = 1 / (a - a * x / (a + 1 + f));
    lngamma_ = SpecialFunctions.gammaln(a);
    lnk_ = a * Math.log(x) - x - lngamma_;
    p_ = Math.exp(lnk_ + Math.log(cf));
    q_ = 1.0 - p_;
    return true;

  }

  /**
   * Evaluate the continued fraction expansion for the upper function using
   * continued fractions. The lower is computed as one minus the upper.
   *
   * @param a
   * @param x
   * @param iterations
   * @return true always
   */
  public boolean evaluateContinuedFraction2(double a, double x, int iterations)
  {
    // x much greater than a converges rapidly
    double f = 0;
    for (int i = iterations; i > 0; --i)
    {
      f = i * (a - i) / (2 * i + 1 + x - a + f);
    }
    double cf = 1.0 / (1 + x - a + f);
    lngamma_ = SpecialFunctions.gammaln(a);
    lnk_ = a * Math.log(x) - x - lngamma_;
    q_ = Math.exp(Math.log(cf) + lnk_);
    p_ = 1.0 - q_;
    return true;
  }

//  public static void main(String[] args)
//  {
//    System.out.println(SpecialFunctions.gammaP(78.892, 76.6147));
//    System.out.println(SpecialFunctions.gammaP(1.0638e+04, 1.0543e+04));
//  }
  double p_;
  double q_;
  double lngamma_;
  double lnk_;
};
