/*
 * Copyright 2016, Lawrence Livermore National Security, LLC. 
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.math.optimize;

public class RegressionPointImpl implements RegressionPoint
{
  final double x;
  final double y;
  final double lambda;

  public RegressionPointImpl(double x, double y)
  {
    this.x = x;
    this.y = y;
    this.lambda = 1;
  }

  public RegressionPointImpl(double x, double y, double lambda)
  {
    this.x = x;
    this.y = y;
    this.lambda = lambda;
  }

  @Override
  public double getX()
  {
    return this.x;
  }

  @Override
  public double getY()
  {
    return this.y;
  }

  @Override
  public double getLambda()
  {
    return this.lambda;
  }

}
