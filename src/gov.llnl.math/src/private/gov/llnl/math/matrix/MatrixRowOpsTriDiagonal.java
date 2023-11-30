/*
 * Copyright 2016, Lawrence Livermore National Security, LLC.
 * All rights reserved
 *
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.math.matrix;

import gov.llnl.math.MathExceptions.MathException;
import gov.llnl.math.matrix.special.MatrixTriDiagonal;

/**
 *
 * @author nelson85
 */
class MatrixRowOpsTriDiagonal implements MatrixRowOperations
{
  final MatrixTriDiagonal diag;

  public MatrixRowOpsTriDiagonal(MatrixTriDiagonal matrixTriDiagonal)
  {
    this.diag = matrixTriDiagonal;
  }

  @Override
  public void addScaledRows(int r1, int r2, double scalar)
  {
    double t1;
    double s1;
    if (r1 == r2 - 1)
    {
      // verify that the element at the end is zero before attempting.
      if (r2 + 1 != diag.columns() && diag.get(r2, r2 + 1) != 0)
        throw new MathException("Operation would break tridiagonal matrix");
    }
    else if (r1 == r2 + 1)
    {
      // verify that the element at the start is zero before attempting.
      if (r2 != 0 && diag.get(r2, r2 - 1) != 0)
        throw new MathException("Operation would break tridiagonal matrix");
    }
    else
      // otherwise fail
      throw new MathException("Operation would break tridiagonal matrix");

    s1 = diag.get(r2, r1);
    t1 = diag.get(r1, r1);
    diag.set(r1, r1, t1 + scalar * s1);
    s1 = diag.get(r2, r2);
    t1 = diag.get(r1, r2);
    diag.set(r1, r2, t1 + scalar * s1);
  }

  @Override
  public void multiplyAssignRow(int row, double scalar)
  {
    double q;
    q = diag.get(row, row) * scalar;
    diag.set(row, row, q);
    if (row != 0)
    {
      q = diag.get(row, row - 1) * scalar;
      diag.set(row, row - 1, q);
    }
    if (row + 1 != diag.columns())
    {
      q = diag.get(row, row + 1) * scalar;
      diag.set(row, row + 1, q);
    }
  }

  @Override
  public void divideAssignRow(int row, double scalar)
  {
    double q;
    q = diag.get(row, row) / scalar;
    diag.set(row, row, q);
    if (row != 0)
    {
      q = diag.get(row, row - 1) / scalar;
      diag.set(row, row - 1, q);
    }
    if (row + 1 != diag.columns())
    {
      q = diag.get(row, row + 1) / scalar;
      diag.set(row, row + 1, q);
    }
  }

  @Override
  public void swapRows(int r1, int r2)
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public void apply()
  {
  }

  public static void main(String[] args)
  {

    double[] d = new double[]
    {
      1, 2, 3, 4, 5
    };
    double[] l = new double[]
    {
      1, 2, 3, 1
    };
    double[] u = new double[]
    {
      1, 2, 4, 5
    };

    MatrixTriDiagonal m = MatrixTriDiagonal.wrap(l, d, u);
    MatrixRowOperations ro = MatrixFactory.createRowOperations(m);
    MatrixOps.dump(System.out, m);

    ro.multiplyAssignRow(0, 2);
    MatrixOps.dump(System.out, m);
    // d[0] == 2;
    //u[0] == 2;

    ro.multiplyAssignRow(1, 2);
    MatrixOps.dump(System.out, m);
    //l[0]=2;
    //d[1]==4;
    //u[1]==4;

    ro.multiplyAssignRow(4, 2);
    MatrixOps.dump(System.out, m);
    // l[4]==8
    // d[5]==10

    try
    {
      ro.multiplyAssignRow(5, 2);
      //fail
    }
    catch (IndexOutOfBoundsException ex)
    {
      // pass
    }
  }

}
