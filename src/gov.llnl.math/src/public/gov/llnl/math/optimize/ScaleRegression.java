/*
 * Copyright 2016, Lawrence Livermore National Security, LLC. 
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.math.optimize;

import gov.llnl.math.MathExceptions;
import gov.llnl.math.function.PolynomialFunction;

/**
 *
 * @author nelson85
 */
public class ScaleRegression implements Regression<PolynomialFunction>
{
  double u0 = 0;
  double u1 = 0;
  int n = 0;

  @Override
  public int getNumObservations()
  {
    return n;
  }

  @Override
  public void add(double x, double y)
  {
    u0 = u0 + x * y;
    u1 = u1 + x * x;
  }

  @Override
  public void add(double x, double y, double lambda)
  {
    u0 = u0 + lambda * x * y;
    u1 = u1 + lambda * x * x;
  }

  @Override
  public PolynomialFunction compute() throws MathExceptions.ConvergenceException
  {
    return new PolynomialFunction(new double[]
    {
      u0 / u1, 0
    });
  }
}
