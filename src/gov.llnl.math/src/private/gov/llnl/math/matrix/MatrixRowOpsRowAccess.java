/*
 * Copyright 2016, Lawrence Livermore National Security, LLC. 
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.math.matrix;

import gov.llnl.math.DoubleArray;
import gov.llnl.utility.annotation.Debug;
import gov.llnl.utility.annotation.Internal;

/**
 * Implementation of Row Operations for a row matrix.
 * 
 * @author nelson85
 */
@Internal
class MatrixRowOpsRowAccess implements MatrixRowOperations
{
  Matrix.RowAccess matrix;

  @Debug
  public Matrix getObj()
  {
    return matrix;
  }

  public MatrixRowOpsRowAccess(Matrix.RowAccess matrix)
  {
    this.matrix = matrix;
    matrix.mutable();
  }

  @Override
  public void addScaledRows(int rowTarget, int rowSource, double scalar)
  {
    if (scalar == 0)
      return;
    double[] v1 = matrix.accessRow(rowSource);
    double[] v2 = matrix.accessRow(rowTarget);
    int a1 = matrix.addressRow(rowSource);
    int a2 = matrix.addressRow(rowTarget);
    int n = matrix.columns();
    for (int c1 = 0; c1 < n; ++c1, ++a1, ++a2)
      v2[a2] += scalar * v1[a1];
  }

  @Override
  public void swapRows(int r1, int r2)
  {
    int c = matrix.columns();
    double[] v1 = matrix.accessRow(r1);
    double[] v2 = matrix.accessRow(r2);
    int a1 = matrix.addressRow(r1);
    int a2 = matrix.addressRow(r2);
    for (int c1 = 0; c1 < c; ++c1)
    {
      double t = v1[a1];
      v1[a1] = v2[a2];
      v2[a2] = t;
      ++a1;
      ++a2;
    }
  }

  @Override
  public void divideAssignRow(int row, double scalar)
  {
    if (scalar == 1)
      return;
    double[] v1 = matrix.accessRow(row);
    int a = matrix.addressRow(row);
    int n = matrix.columns();
    DoubleArray.divideAssignRange(v1, a, a + n, scalar);
  }

  @Override
  public void multiplyAssignRow(int row, double scalar)
  {
    if (scalar == 1)
      return;
    double[] v1 = matrix.accessRow(row);
    int a = matrix.addressRow(row);
    int n = matrix.columns();
    DoubleArray.multiplyAssignRange(v1, a, a + n, scalar);
  }

  @Override
  public void apply()
  {
    // Not used.
  }

}
