/*
 * Copyright 2016, Lawrence Livermore National Security, LLC. 
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.math.matrix;

import gov.llnl.utility.annotation.Debug;

/**
 * Implementation of row operations for column matrix.
 * 
 * This will be refactored to use deferred operations rather that
 * direct unless the matrix is very small. Block operations are 
 * required for efficency when processing a matrix in the wrong direction.
 * 
 * @author nelson85
 */
class MatrixRowOpsColumnAccess implements MatrixRowOperations
{
  static final int BLOCK_SIZE = 8;

  Matrix.ColumnAccess matrix;

  public MatrixRowOpsColumnAccess(Matrix.ColumnAccess matrix)
  {
    this.matrix = matrix;
    matrix.mutable();
  }

  @Override
  public void addScaledRows(int r1, int r2, double scalar)
  {
    if (scalar == 0)
      return;
    int c = matrix.columns();
    for (int i = 0; i < c; i += BLOCK_SIZE)
    {
      int l = i + BLOCK_SIZE;
      if (l > c)
        l = c;
      for (int j = i; j < l; ++j)
      {
        double[] v = matrix.accessColumn(j);
        int offset = matrix.addressColumn(j);
        v[offset + r1] += v[offset + r2] * scalar;
      }
    }
  }

  @Override
  public void swapRows(int r1, int r2)
  {
    int c = matrix.columns();
    for (int i = 0; i < c; i += BLOCK_SIZE)
    {
      int l = i + BLOCK_SIZE;
      if (l > c)
        l = c;
      for (int j = i; j < l; ++j)
      {
        double[] v = matrix.accessColumn(j);
        int offset = matrix.addressColumn(j);
        double tmp = v[offset + r1];
        v[offset + r1] = v[offset + r2];
        v[offset + r2] = tmp;
      }
    }
  }

  @Override
  public void divideAssignRow(int row, double scalar)
  {
    if (scalar == 1)
      return;
    int c = matrix.columns();
    for (int i = 0; i < c; i += BLOCK_SIZE)
    {
      int l = i + BLOCK_SIZE;
      if (l > c)
        l = c;
      for (int j = i; j < l; ++j)
      {
        double[] v = matrix.accessColumn(j);
        int offset = matrix.addressColumn(j);
        v[offset + row] /= scalar;
      }
    }
  }

  @Debug
  public Matrix getObj()
  {
    return matrix;
  }

  @Override
  public void multiplyAssignRow(int row, double scalar)
  {
    if (scalar == 1)
      return;
    int c = matrix.columns();
    for (int i = 0; i < c; i += BLOCK_SIZE)
    {
      int l = i + BLOCK_SIZE;
      if (l > c)
        l = c;
      for (int j = i; j < l; ++j)
      {
        double[] v = matrix.accessColumn(j);
        int offset = matrix.addressColumn(j);
        v[offset + row] *= scalar;
      }
    }
  }

  @Override
  public void apply()
  {
    // not used currently.
  }

}
