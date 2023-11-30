/*
 * Copyright 2016, Lawrence Livermore National Security, LLC. 
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.math.algebra;

import gov.llnl.math.matrix.Matrix;
import gov.llnl.math.matrix.MatrixFactory;
import gov.llnl.math.matrix.MatrixOps;
import gov.llnl.math.matrix.MatrixViews;

/**
 *
 * @author nelson85
 */
public class MatrixExponential
{

  /**
   * This is a matrix exponential.
   *
   * @param m is the a matrix.
   * @param t is the multiplication factor.
   * @return
   */
  public Matrix expm(Matrix m, double t)
  {
    // This is the minimum number of squares that we expect to perform in post correction
    //  The post divisions the smaller the matrix elements the less values are
    //  required in the series.
    int k = 5;

    // This is the order of the Pade approximate.  
    //   It should be between 4-8 for accuracy.
    //   Smaller the 4 caused accuracy issues we lack enough terms in the series.
    //   Larger than 8 tends to cause inaccuracies due to rounding in the inverse.
    int l = 5;

    // Compute the scale
    Matrix m2 = MatrixOps.multiply(m, m);
    double max = Math.sqrt(MatrixOps.maximumOfAll(m2));

    // This is dynamic to the time scale and the decay constants we are computing.  
    //   The larger the time scale the more squares; the larger the decay constant
    //   the more squares we will require.
    int v = (int) (Math.log(t * max) / Math.log(2)) + k;
    if (v > 0)
      t = t / Math.pow(2.0, v);

    // Pade Approximate for exponential
    MatrixOps.multiplyAssign(m, t);
    Matrix u = MatrixFactory.newMatrix(m.rows(), m.columns());
    MatrixOps.fill(MatrixViews.diagonal(u), 1.0);
    Matrix p1 = MatrixFactory.newMatrix(m.rows(), m.columns());
    Matrix p2 = MatrixFactory.newMatrix(m.rows(), m.columns());

    // Dynamically compute the coefficents of the series using 1F1(-m; -m-n; z)
    double kn = 1;
    double kd = 1;
    for (int i = 0; i < l + 1; ++i)
    {
      if (i % 2 == 0)
      {
        MatrixOps.addAssignScaled(p1, u, kn / kd);
      }
      else
      {
        MatrixOps.addAssignScaled(p2, u, kn / kd);
      }
      if (i < l)
      {
        kn *= (l - i);
        kd *= (2 * l - i) * (i + 1);
        u = MatrixOps.multiply(u, m);
      }
    }
    Matrix mn = MatrixOps.add(p1, p2);
    Matrix md = MatrixOps.subtract(p1, p2);
    Matrix r = MatrixOps.divideLeft(md, mn);

    // Post correction
    if (v > 0)
    {
      // Square the product multiple times
      for (int i = 0; i < v; ++i)
      {
        Matrix r2 = r.copyOf();

        // Square the matrix.
        r = MatrixOps.multiply(r2, r);
      }
    }
    return r;
  }
//</editor-fold> 
}
