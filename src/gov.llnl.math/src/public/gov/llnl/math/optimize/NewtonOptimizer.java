/*
 * Copyright 2016, Lawrence Livermore National Security, LLC.
 * All rights reserved
 *
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.math.optimize;

import java.util.function.DoubleUnaryOperator;

// Newton Search with limits
public class NewtonOptimizer
{
  DoubleUnaryOperator objective;
  double lowerLimit;
  double upperLimit;
  double target;
  double[] x = new double[3];
  double[] y = new double[3];
  int n = 0;

  public NewtonOptimizer(DoubleUnaryOperator objective, double target, double lower, double upper)
  {
    this.objective = objective;
    this.lowerLimit = lower;
    this.upperLimit = upper;
    this.target = target;
  }

  public double optimize()
  {
    x[0] = lowerLimit;
    y[0] = objective.applyAsDouble(x[0]);
    x[2] = upperLimit;
    y[2] = objective.applyAsDouble(x[2]);
    //System.out.println("x=" + x[0] + " y=" + y[0]);
    //System.out.println("x=" + x[2] + " y=" + y[2]);
    n = 2;
    x[1] = initial();
    for (int i = 0; i < 8; ++i)
    {
      if (x[1] < lowerLimit)
        return lowerLimit;
      if (x[1] > upperLimit)
        return upperLimit;
      y[1] = objective.applyAsDouble(x[1]);
      //System.out.println("x=" + x[1] + " y=" + y[1]);
      if (Math.abs(y[1] - target) < 0.001)
        return x[1];
      double xn = next();
      if (xn == x[1])
        return xn;
      if (xn < x[1])
      {
        x[2] = x[1];
        y[2] = y[1];
      }
      else
      {
        x[0] = x[1];
        y[0] = y[1];
      }
      x[1] = xn;
    }
    return x[1];
  }

  double initial()
  {
    if (y[2] == y[0])
      return (x[0] + x[2]) / 2;
    return (x[2] - x[0]) * (target - y[0]) / (y[2] - y[0]) + x[0];
  }

  double next()
  {
    double m1 = (y[1] - y[0]) / (x[1] - x[0]);
    double m2 = (y[2] - y[0]) / (x[2] - x[0]);
    double a = (m2 - m1) / (x[2] - x[0]);
    double b = m1 - a * (x[1] + x[0]);
    double m = 2 * a * x[1] + b;

    // Standard case
    if (Math.abs(y[1] - target) < Math.abs(m * (x[2] - x[0])))
      return x[1] - (y[1] - target) / m;

    // Flat??
    if (b == 0)
      return x[1];

    // In the interval
    if (y[1] > target && (y[0] < target || y[2] < target))
      return x[0] - (y[0] - target) / b;
    if (y[1] < target && (y[0] < target || y[2] < target))
      return x[0] - (y[0] - target) / b;

    if (Math.abs(y[0] - target) <= Math.abs(y[2] - target))
      return (x[1] + x[0]) / 2;
    return (x[2] + x[1]) / 2;
  }
}
