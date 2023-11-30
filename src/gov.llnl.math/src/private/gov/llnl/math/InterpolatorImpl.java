/*
 * Copyright 2016, Lawrence Livermore National Security, LLC.
 * All rights reserved
 *
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.math;

import gov.llnl.utility.annotation.Internal;
import java.util.Arrays;
import java.util.function.DoubleUnaryOperator;
import java.util.function.Function;

/**
 *
 * @author nelson85
 */
@Internal
class InterpolatorImpl implements Interpolator
{
  private final Method lower;
  private final Method inner;
  private final Method upper;

  public InterpolatorImpl(Method lower, Method inner, Method upper)
  {
    this.lower = lower;
    this.inner = inner;
    this.upper = upper;
  }
  
  @Override
  public double interp(double[] x, double[] y, double xq)
  {
    _check(x, y);
    return _interpOne(x, y, xq);
  }

  @Override
  public double[] interp(double[] x, double[] y, double[] xq)
          throws MathExceptions.MathException
  {
    _check(x, y);
    return _interpArray(x, y, xq);
  }

  @Override
  public DoubleUnaryOperator asOperator(double[] x, double[] y)
  {
    _check(x, y);
    return (double operand) -> _interpOne(x, y, operand);
  }

  @Override
  public Function<double[], double[]> asArray(double[] x, double[] y)
  {
    _check(x, y);
    return (double[] operand) -> _interpArray(x, y, operand);
  }

  /** 
   * Verify that the input coordinates are in order.
   * 
   * @param x
   * @param y 
   */
  private void _check(double[] x, double[] y)
  {
    MathAssert.assertEqualLength(x, y);

    // This seems like a waste of time, if the user is willing
    // to stimpulate that they have ordered the samples.
    for (int i = 0; i < x.length - 1; ++i)
      if (x[i] > x[i + 1])
        throw new MathExceptions.MathException("Inputs out of order");
  }

  double _interpOne(double[] x, double[] y, double xq)
  {
    int m = x.length;

    // Check lower limits
    double q = xq;
    if (q < x[0])
    {
      return this.lower.evaluate(x, y, q, 0);
    }

    // Check upper limits
    if (q >= x[m - 1])
    {
      return this.upper.evaluate(x, y, q, m - 1);
    }

    // Search inner O = n*log(n)
    return this.inner.evaluate(x, y, q, nearest(x, q));
  }

  private double[] _interpArray(double[] x, double[] y, double[] xq)
  {
    int n = xq.length;
    int m = x.length;
    double out[] = new double[n];

    for (int i = 0; i < n; ++i)
    {
      // Check lower limits
      double q = xq[i];
      if (q < x[0])
      {
        out[i] = this.lower.evaluate(x, y, q, 0);
        continue;
      }

      // Check upper limits
      if (q >= x[m - 1])
      {
        out[i] = this.upper.evaluate(x, y, q, m - 1);
        continue;
      }

      // Search inner O = n*log(n)
      out[i] = this.inner.evaluate(x, y, q, nearest(x, q));
      MathAssert.assertNotNaN(out[i]);
    }
    return out;
  }

  public int nearest(double[] x, double q)
  {
    int k = Arrays.binarySearch(x, q);

    // If we hit an exact point then call it directly
    if (k >= 0)
      return k;

    // Otherwise find the nearest point
    k = -k - 1;
    if (k <= 0)
      return 0;
    if (k >= x.length)
      k = x.length - 1;
    if (q - x[k - 1] < x[k] - q)
      return k - 1;
    return k;
  }

}
