/*
 * Copyright 2016, Lawrence Livermore National Security, LLC. 
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.math.optimize;

import gov.llnl.math.DoubleArray;
import gov.llnl.math.MathExceptions;
import gov.llnl.math.function.Function;
import gov.llnl.utility.annotation.Debug;

/**
 * Simply polynomial search optimizer to find the optimal in some defined
 * region.
 *
 * @author nelson85
 */
public class FunctionOptimizer
{
  double start;
  double end;
  int scalar;
  int steps = 20;
  final Function function;

  public FunctionOptimizer(Function function, double start, double end, boolean maximize)
  {
    this.function = function;
    this.start = start;
    this.end = end;
    this.scalar = maximize ? 1 : -1;
  }

  public double optimize() throws MathExceptions.ConvergenceException
  {
    double[] x = new double[]
    {
      start, (start + end) / 2, end
    };
    double[] y = new double[3];
    for (int i = 0; i < 3; ++i)
      y[i] = scalar * function.applyAsDouble(x[i]);

    for (int j = 0; j < steps; ++j)
    {
      int i = DoubleArray.findIndexOfMaximum(y);

      // Okay we have a high point in the interval, we can use Newton 
      // to find an update.
      if (i == 1)
      {
        double f = 4 * (x[2] - x[1]) * (x[1] - x[0]) / (x[2] - x[0]) / (x[2] - x[0]);
        System.out.println("fcond=" + f);
        double xn;
        if (f < 0.3)
          xn = (x[2] + x[0]) / 2;
        else
          xn = polyEval(x, y);

        if (Math.abs(xn - x[1]) < 1e-5)
          break;
        double yn = scalar * function.applyAsDouble(xn);
        int r1, r2;
        if (xn > x[1])
        {
          r1 = 0;
          r2 = 2;
        }
        else
        {
          r1 = 2;
          r2 = 0;
        }

        // y2 is new best so it should be the middle point
        if (yn > y[1])
        {
          x[r1] = x[1];
          y[r1] = y[1];
          x[1] = xn;
          y[1] = yn;
        }
        // y2 is not the best, so it should be an edge
        else
        {
          x[r2] = xn;
          y[r2] = yn;
        }
        continue;
      }

      // One edge is better
      if (i == 0)
      {
        x[2] = x[1];
        y[2] = y[1];
      }
      else
      {
        x[0] = x[1];
        y[0] = y[1];
      }
      x[1] = (x[2] + x[0]) / 2;
      y[1] = scalar * function.applyAsDouble(x[1]);
    }
    return x[DoubleArray.findIndexOfMaximum(y)];
  }

  public double polyEval(double[] x, double[] y) throws MathExceptions.ConvergenceException
  {
    double v1 = y[0] - y[1];
    double v2 = y[2] - y[1];
    double u11 = x[0] * x[0] - x[1] * x[1];
    double u12 = x[0] - x[1];
    double u21 = x[2] * x[2] - x[1] * x[1];
    double u22 = x[2] - x[1];
    double D = u11 * u22 - u12 * u21;
    if (Math.abs(D) < 1e-12)
      return x[1];

    double a = v1 * u22 - v2 * u12;
    double b = u11 * v2 - u21 * v1;
    if (a > 0)
      throw new MathExceptions.ConvergenceException("inverted poly");
    return -b / 2 / a;
  }

}
