/*
 * Copyright 2016, Lawrence Livermore National Security, LLC. 
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.math.matrix;

import gov.llnl.math.MathExceptions.SingularException;
import gov.llnl.math.MathExceptions.SizeException;
import gov.llnl.math.MathExceptions.WriteAccessException;
import gov.llnl.utility.annotation.Internal;

/**
 * Collection of utilities to compute solve division by a matrix. Many of these
 * methods are destructive and will alter the operands.
 */
@Internal
class MatrixOpDivide
{

  /**
   * Solve A*R=B by Gauss elimination.
   *
   * @param C is used to store both R and B
   * @param A is destroyed in the process
   * @throws SizeException
   * @throws WriteAccessException
   * @throws SingularException if the matrix is singular and the target is
   * outside the domain.
   */
  static public void solveDestructive(Matrix C, Matrix.RowAccess A)
          throws SizeException, WriteAccessException, SingularException
  {
    MatrixAssert.assertRowsEqual(A, C);
    C.mutable();
    solveDestructive(MatrixFactory.createRowOperations(C), A);
  }

  /**
   * Solve A*R=B by Gauss elimination.
   *
   * @param C is used to store both R and B
   * @param A is destroyed in the process
   * @throws SizeException
   * @throws WriteAccessException
   * @throws SingularException if the matrix is singular and the target is
   * outside the domain.
   *
   */
  static public void solveDestructive(double[] C, Matrix.RowAccess A)
          throws SizeException, WriteAccessException, SingularException
  {
    MatrixAssert.assertRowsEqual(A, C.length);
    solveDestructive(new MatrixRowOpsVector(C, 0), A);
  }

  /**
   * Solve A*R=B by Gauss elimination. This is the backend code that operates on
   * the matrix using the abstraction of RowOperations
   *
   * @param C is used to store both R and B
   * @param A is destroyed in the process
   * @throws SizeException
   * @throws WriteAccessException
   * @throws SingularException
   */
  static public void solveDestructive(MatrixRowOperations C, Matrix.RowAccess A)
          throws SizeException, WriteAccessException, SingularException
  {
    // Sanity checks
    MatrixAssert.assertSquare(A);
    A.mutable();
    int n = A.rows();

    // Convert to upper tri form
    for (int i1 = 0; i1 < n; i1++)
    {
      // Find the largest absolute value in the column starting from A(i,i)
      double max = Math.abs(A.get(i1, i1));
      int index = i1;
      for (int i2 = i1 + 1; i2 < n; i2++)
      {
        double d = Math.abs(A.get(i2, i1));
        if (max < d)
        {
          max = d;
          index = i2;
        }
      }

      // Watch for singularity
      if (max < 1e-16)
        throw new SingularException("Singular inverse, condition=" + max);

      // Use the largest row 
      if (index != i1)
      {
        swapRows(A, index, i1, i1);
        C.swapRows(index, i1);
      }

      // Normalized row 
      double tmp = normalizeDiag(A, i1, i1);
      C.divideAssignRow(i1, tmp);

      // Reduce rows under it 
      for (int i2 = i1 + 1; i2 < n; i2++)
      {
        double ratio = cancelRow(A, i1, i2, i1);
        if (ratio == 0.0)
          continue;
        C.addScaledRows(i2, i1, -ratio);
      }
    }

    if (n == 1)
      return;

    // Finish the inverse with back substitution
    for (int i1 = n - 2; i1 >= 0; i1--)
    {
      for (int i2 = i1 + 1; i2 < n; i2++)
      {
        C.addScaledRows(i1, i2, -A.get(i1, i2));
      }
    }
  }

  private static void swapRows(Matrix.RowAccess m, int r1, int r2, int c1)
  {
    int c = m.columns();
    double[] v1 = m.accessRow(r1);
    double[] v2 = m.accessRow(r2);
    int a1 = m.addressRow(r1) + c1;
    int a2 = m.addressRow(r2) + c1;
    for (; c1 < c; ++c1)
    {
      double t = v1[a1];
      v1[a1] = v2[a2];
      v2[a2] = t;
      ++a1;
      ++a2;
    }
  }

  private static double normalizeDiag(Matrix.RowAccess m, int r, int c)
  {
    double[] v1 = m.accessRow(r);
    int a = m.addressRow(r) + c;
    double tmp = v1[a];
    v1[a++] = 1;
    int n = m.columns();
    for (++c; c < n; ++c, ++a)
    {
      v1[a] /= tmp;
    }
    return tmp;
  }

  private static double cancelRow(Matrix.RowAccess m, int r1, int r2, int c1)
  {
    double[] v1 = m.accessRow(r1);
    double[] v2 = m.accessRow(r2);
    int a1 = m.addressRow(r1) + c1;
    int a2 = m.addressRow(r2) + c1;
    double ratio = v2[a2];
    if (ratio == 0)
      return 0;
    v2[a2++] = 0;
    ++a1;
    ++c1;
    int n = m.columns();
    for (; c1 < n; ++c1, ++a1, ++a2)
      v2[a2] -= ratio * v1[a1];
    return ratio;
  }

}
