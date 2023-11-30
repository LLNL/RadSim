/*
 * Copyright 2016, Lawrence Livermore National Security, LLC. 
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.math.optimize;

import gov.llnl.math.MathAssert;
import gov.llnl.math.MathExceptions.ConvergenceException;
import gov.llnl.math.MathExceptions.SingularException;
import gov.llnl.math.function.LinearFunction;
import gov.llnl.math.matrix.Matrix;
import gov.llnl.math.matrix.MatrixFactory;
import gov.llnl.math.matrix.MatrixOps;

/**
 * Utility function to compute the linear regression for a set of points.
 *
 */
public class LinearRegression implements Regression<LinearFunction>
{
  // number of observation points
  private int n = 0;

  // statistics needed to compute the regression
  private double x2 = 0;
  private double x1 = 0;
  private double x0 = 0;
  private double xy = 0;
  private double y0 = 0;

  @Override
  public int getNumObservations()
  {
    return n;
  }

  @Override
  public void add(double x, double y)
  {
    this.add(x, y, 1);
  }

  @Override
  public void add(double x, double y, double lambda)
  {
    if (lambda == 0)
      return;
    x2 += lambda * x * x;
    x1 += lambda * x;
    x0 += lambda;
    xy += lambda * x * y;
    y0 += lambda * y;
    n++;
  }

  /**
   * Compute the slope and intercept the represents the linear regression for
   * this point.
   *
   * @return a linear function with a slope and offset.
   * @throws ConvergenceException
   */
  @Override
  public LinearFunction compute()
          throws ConvergenceException 
  {
    if (n < 2)
      throw new ConvergenceException("More than one observation is required.");
    Matrix A = MatrixFactory.wrapArray(new double[]
    {
      x2, x1, x1, x0
    }, 2, 2);
    Matrix Y = MatrixFactory.wrapColumnVector(new double[]
    {
      xy, y0
    });
    try
    {
      Matrix X = MatrixOps.divideLeft(A, Y);
      return new LinearFunction(X.get(1, 0), X.get(0, 0));
    }
    catch (SingularException ex)
    {
      throw new ConvergenceException("Regression is singular.");
    }
  }

  public void add(double[] x, double[] y)
          throws IndexOutOfBoundsException
  {
    MathAssert.assertEqualLength(x, y);
    for (int i = 0; i < x.length; ++i)
      this.add(x[i], y[i]);
  }

  public void clear()
  {
    n = 0;
    x2 = 0;
    x1 = 0;
    x0 = 0;
    xy = 0;
    y0 = 0;
  }

}
