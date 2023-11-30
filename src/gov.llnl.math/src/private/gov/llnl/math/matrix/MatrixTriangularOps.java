/*
 * Copyright 2016, Lawrence Livermore National Security, LLC. 
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.math.matrix;

import gov.llnl.math.DoubleComparator;

/**
 *
 * @author nelson85
 */
public class MatrixTriangularOps
{
  
  // FIXME NOT USED?
  
  static final double TOLERANCE = 1e-15;

  static double getMaximumDiagonal(Matrix m)
  {
    Matrix diag = MatrixViews.diagonal(m);
    return Math.abs(MatrixOps.findExtrema(diag, new DoubleComparator.Absolute()).value);
  }

  static <MatrixType extends Matrix.Triangular & Matrix.ColumnAccess>
          void backSubstituteColumn(MatrixType triangular, MatrixRowOperations mro)
  {
    if (triangular.rows() != triangular.columns())
      throw new UnsupportedOperationException("Only implemented for square matrix.");

    // Compute the epsilon (effective 0) based on the range of values 
    double eps = TOLERANCE * getMaximumDiagonal(triangular);

    int n = triangular.rows();
    if (triangular.isUpper())
    {
      // Start from the lower right and work up
      for (int c = n - 1; c > -1; --c)
      {
        double[] cv = triangular.accessColumn(c);
        int co = triangular.addressColumn(c);

        // TODO handle sigularity here
        if (cv[co + c] < eps && cv[co + c] > -eps)
        {
          mro.multiplyAssignRow(c, 0);
          continue;
        }

        mro.divideAssignRow(c, cv[co + c]);
        for (int i = 0; i < c; i++)
        {
          if (cv[co + i] != 0)
            mro.addScaledRows(c, c, -cv[co + i]);
        }
      }
    }
    else
    {
      // Start for the upper left and work down
      for (int c = 0; c < n; ++c)
      {
        double[] cv = triangular.accessColumn(c);
        int co = triangular.addressColumn(c);

        double diag = cv[co + c];
        if (diag < eps && diag > -eps)
        {
          mro.multiplyAssignRow(c, 0);
          continue;
        }
        mro.divideAssignRow(c, cv[co + c]);
        for (int i = c + 1; i < triangular.rows(); ++i)
        {
          mro.addScaledRows(i, c, -cv[co + i]);
        }
      }
    }
    
    mro.apply();
  }

  static <MatrixType extends Matrix.Triangular & Matrix.RowAccess>
          void backSubstituteRow(MatrixType triangular, MatrixRowOperations mro)
  {
    if (triangular.rows() != triangular.columns())
      throw new UnsupportedOperationException("Only implemented for square matrix.");

    // Compute the epsilon (effective 0) based on the range of values 
    double eps = TOLERANCE * getMaximumDiagonal(triangular);

    int n = triangular.rows();
    int[] skip = new int[triangular.columns()];
    if (triangular.isUpper())
    {
      // Start from the lower right and work up
      for (int r = n - 1; r > -1; --r)
      {
        double[] rv = triangular.accessRow(r);
        int ro = triangular.addressRow(r);
        for (int c = n; c > r; --c)
        {
          if (skip[r] != 0 && rv[ro + c] != 0)
            mro.addScaledRows(c, r, -rv[ro + c]);
        }
        double diag = rv[ro + r];
        if (diag < eps && diag > -eps)
        {
          skip[r] = 1;
          mro.multiplyAssignRow(r, 0);
        }
        else
          mro.divideAssignRow(r, diag);
      }
    }
    else
    {
      // Start for the upper left and work down
      for (int r = 0; r < n; ++r)
      {
        double[] rv = triangular.accessRow(r);
        int ro = triangular.addressRow(r);
        for (int c = 0; c < r - 1; ++c)
        {
          if (skip[c] == 0 && rv[ro + c] != 0)
            mro.addScaledRows(c, r, -rv[ro + c]);
        }
        double diag = rv[ro + r];
        if (diag < eps && diag > -eps)
        {
          mro.multiplyAssignRow(r, 0);
          skip[r] = 1;
        }
        else
          mro.divideAssignRow(r, rv[ro + r]);
      }
    }
    
    mro.apply();
  }
}
