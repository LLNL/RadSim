/*
 * Copyright 2016, Lawrence Livermore National Security, LLC. 
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.math.optimize;

import gov.llnl.math.MathAssert;
import gov.llnl.math.MathExceptions;
import gov.llnl.math.function.SaturationFunction;
import gov.llnl.math.matrix.Matrix;
import gov.llnl.math.matrix.MatrixColumnArray;
import gov.llnl.math.matrix.MatrixFactory;
import gov.llnl.math.matrix.MatrixOps;
import gov.llnl.math.matrix.special.MatrixSymmetric;

/**
 * Utility function to compute the saturation regression for a set of points.
 *
 */
public class SaturationRegression implements Regression<SaturationFunction>
{

  // number of observation points
  private int n = 0;
  private MatrixSymmetric A = new MatrixSymmetric(3);
  private Matrix B = MatrixFactory.newColumnMatrix(3, 1);
  private boolean constrainZero = false;

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
    Matrix v = MatrixFactory.createColumnVector(new double[]
    {
      -x * y, x, 1,
    });
    Matrix u = MatrixFactory.createColumnVector(new double[]
    {
      -lambda * x * y * y, lambda * y * x, lambda * y
    });
    MatrixOps.addAssign(B, u);
    MatrixOps.addAssign(A, MatrixSymmetric.multiplySelfOuter(v).multiplyAssign(lambda));
    n++;
  }

  /**
   * Compute the slope and intercept the represents the linear regression for
   * this point.
   *
   * @return an array holding the slope and offset.
   * @throws MathExceptions.ConvergenceException
   */
  @Override
  public SaturationFunction compute()
          throws MathExceptions.ConvergenceException
  {
    if (!constrainZero)
    {
      // Otherwise, zero is not constrained
      if (n < 3)
        throw new MathExceptions.ConvergenceException("Three observations are required.");
      Matrix X = MatrixOps.divideLeft(A, B);
      double[] v = new MatrixColumnArray(X).toArray();
      return new SaturationFunction(v[2], v[1], v[0]);
    }

    if (n < 2)
      throw new MathExceptions.ConvergenceException("Two observations are required.");

    Matrix A1 = MatrixFactory.wrapArray(new double[]
    {
      A.get(0, 0), A.get(0, 1), A.get(0, 1), A.get(1, 1)
    }, 2, 2);
    Matrix B1 = MatrixFactory.wrapColumnVector(new double[]
    {
      B.get(0, 0), B.get(1, 0)
    });
    Matrix X = MatrixOps.divideLeft(A1, B1);
    double[] v = new MatrixColumnArray(X).toArray();
    return new SaturationFunction(0, v[1], v[0]);
  }

  /**
   * @return the constrainZero
   */
  public boolean isConstrainZero()
  {
    return constrainZero;
  }

  /**
   * @param constrainZero the constrainZero to set
   */
  public void setConstrainZero(boolean constrainZero)
  {
    this.constrainZero = constrainZero;
  }

}
