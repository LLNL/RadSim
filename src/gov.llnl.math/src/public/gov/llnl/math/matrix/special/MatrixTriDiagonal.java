/*
 * Copyright 2016, Lawrence Livermore National Security, LLC. 
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.math.matrix.special;

import gov.llnl.math.DoubleArray;
import gov.llnl.math.MathExceptions;
import gov.llnl.math.MathExceptions.ResizeException;
import gov.llnl.math.MathExceptions.SingularException;
import gov.llnl.math.MathExceptions.SizeException;
import gov.llnl.math.MathExceptions.WriteAccessException;
import gov.llnl.math.matrix.Matrix;
import gov.llnl.math.matrix.MatrixAssert;
import gov.llnl.math.matrix.MatrixRowOperations;
import gov.llnl.utility.UUIDUtilities;

/**
 *
 * @author nelson85
 */
public class MatrixTriDiagonal implements Matrix, Matrix.ScalarOperations, Matrix.DivideOperation
{
  private static final long serialVersionUID = UUIDUtilities.createLong("MatrixTriDiagonal");
  final static int[] DIAG_OFFSET = new int[]
  {
    -1, 0, 0
  };
  double[][] data;

  public MatrixTriDiagonal()
  {
  }

  public MatrixTriDiagonal(int n)
  {
    data = new double[3][];
    data[0] = new double[n - 1];
    data[1] = new double[n];
    data[2] = new double[n - 1];
  }

  public static MatrixTriDiagonal wrap(double[] lower, double[] diag, double[] upper)
          throws SizeException
  {
    MatrixTriDiagonal matrix = new MatrixTriDiagonal();
    if (lower.length + 1 != diag.length || upper.length + 1 != diag.length)
      throw new SizeException("Size mismatch, lower" + lower.length
              + " diag=" + diag.length + " upper=" + upper.length);

    matrix.data = new double[3][];
    matrix.data[0] = lower;
    matrix.data[1] = diag;
    matrix.data[2] = upper;
    return matrix;
  }

  @Override
  public int rows()
  {
    return data[1].length;
  }

  @Override
  public int columns()
  {
    return data[1].length;
  }

  @Override
  public void set(int row, int column, double value) throws MathExceptions.WriteAccessException,
          IndexOutOfBoundsException
  {
    if (row < 0 || row > this.rows())
      MatrixAssert.assertInRange(this, row, column);
    if (column < 0 || column > this.columns())
      MatrixAssert.assertInRange(this, row, column);

    int offset;
    int index;
    offset = column - row + 1;
    if (offset < 0 || offset > 2)
      if (value != 0)
        throw new WriteAccessException("Unable to set outside of diagonals");
      else
        return;

    index = row + DIAG_OFFSET[offset];
    try
    {
      this.data[offset][index] = value;
    }
    catch (IndexOutOfBoundsException ex)
    {
      MatrixAssert.assertInRange(this, row, column);
      throw ex;
    }
  }

  @Override
  public double get(int row, int column)
          throws IndexOutOfBoundsException
  {
    if (row < 0 || row > this.rows())
      MatrixAssert.assertInRange(this, row, column);
    if (column < 0 || column > this.columns())
      MatrixAssert.assertInRange(this, row, column);

    int offset;
    int index;
    offset = column - row + 1;
    if (offset < 0 || offset > 2)
      return 0;
    index = row + DIAG_OFFSET[offset];

    try
    {
      return this.data[offset][index];
    }
    catch (IndexOutOfBoundsException ex)
    {
      MatrixAssert.assertInRange(this, row, column);
      throw ex;
    }
  }

  @Override
  final public void mutable() throws WriteAccessException
  {
    throw new WriteAccessException("Tridiagonal matrices cannot be altered directly.");
  }

  @Override
  public Object sync()
  {
    return this;
  }

  @Override
  public boolean resize(int rows, int columns) throws ResizeException
  {
    MatrixAssert.assertResizeSquare(rows, columns);
    if (rows == rows())
      return false;
    data[0] = new double[rows - 1];
    data[1] = new double[rows];
    data[2] = new double[rows - 1];
    return true;
  }

  @Override
  public Matrix assign(Matrix matrix) throws MathExceptions.ResizeException, MathExceptions.WriteAccessException
  {
    if (matrix == this)
      return this;

    if (matrix instanceof MatrixTriDiagonal)
    {
      data[0] = ((MatrixTriDiagonal) matrix).data[0].clone();
      data[1] = ((MatrixTriDiagonal) matrix).data[1].clone();
      data[2] = ((MatrixTriDiagonal) matrix).data[2].clone();
      return this;
    }

    // Check assertions
    MatrixAssert.assertSquare(matrix);
    MatrixAssert.assertTriDiagonal(matrix);

    int n = matrix.rows();
    this.resize(n, n);
    for (int i = 0; i < n; ++i)
      this.data[1][i] = matrix.get(i, i);
    for (int i = 0; i < n - 1; ++i)
    {
      this.data[0][i] = matrix.get(i + 1, i);
      this.data[2][i] = matrix.get(i, i + 1);
    }
    return this;
  }

//  @Override
//  public void assignRow(double[] in, int index) throws MathExceptions.WriteAccessException, IndexOutOfBoundsException
//  {
//    int n = rows();
//    if (index == 0)
//    {
//      assertZeroRange(in, 2, n);
//      data[1][0] = in[0];
//      data[2][0] = in[1];
//    }
//    else if (index == n - 1)
//    {
//      assertZeroRange(in, 0, n - 2);
//      data[0][n - 2] = in[n - 2];
//      data[1][n - 1] = in[n - 1];
//    }
//    else
//    {
//      assertZeroRange(in, 0, index - 1);
//      assertZeroRange(in, index + 2, n);
//      data[0][index - 1] = in[index - 1];
//      data[1][index] = in[index];
//      data[2][index] = in[index + 1];
//    }
//  }
//
//  @Override
//  public void assignColumn(double[] in, int index) throws MathExceptions.WriteAccessException, IndexOutOfBoundsException
//  {
//    int n = rows();
//    if (index == 0)
//    {
//      assertZeroRange(in, 2, n);
//      data[1][0] = in[0];
//      data[0][0] = in[1];
//    }
//    else if (index == n - 1)
//    {
//      assertZeroRange(in, 0, n - 2);
//      data[2][n - 2] = in[n - 2];
//      data[1][n - 1] = in[n - 1];
//    }
//    else
//    {
//      assertZeroRange(in, 0, index - 1);
//      assertZeroRange(in, index + 2, n);
//      data[2][index - 1] = in[index - 1];
//      data[1][index] = in[index];
//      data[0][index] = in[index + 1];
//    }
//  }
  @Override
  public double[] copyRowTo(double[] out, int offset, int index) throws IndexOutOfBoundsException
  {
    int n = rows();
    DoubleArray.fillRange(out, offset, offset + n, 0);
    if (index == 0)
    {
      out[offset + 0] = data[1][0];
      out[offset + 1] = data[2][0];
    }
    else if (index == n - 1)
    {
      out[offset + n - 2] = data[0][n - 2];
      out[offset + n - 1] = data[1][n - 1];
    }
    else
    {
      out[offset + index - 1] = data[0][index - 1];
      out[offset + index] = data[1][index];
      out[offset + index + 1] = data[2][index];
    }
    return out;
  }

  @Override
  public double[] copyColumnTo(double[] out, int offset, int index) throws IndexOutOfBoundsException
  {
    int n = rows();
    DoubleArray.fillRange(out, offset, offset + n, 0);
    if (index == 0)
    {
      out[offset] = data[1][0];
      out[offset + 1] = data[0][0];
    }
    else if (index == n - 1)
    {
      out[offset + n - 2] = data[2][n - 2];
      out[offset + n - 1] = data[1][n - 1];
    }
    else
    {
      out[offset + index - 1] = data[2][index - 1];
      out[offset + index] = data[1][index];
      out[offset + index + 1] = data[0][index];
    }
    return out;
  }

  @Override
  public Matrix copyOf()
  {
    double[] d1 = DoubleArray.copyOf(data[0]);
    double[] d2 = DoubleArray.copyOf(data[1]);
    double[] d3 = DoubleArray.copyOf(data[2]);
    return MatrixTriDiagonal.wrap(d1, d2, d3);
  }

  private void assertZeroRange(double[] in, int b, int e)
  {
    for (int i = b; i < e; ++i)
      if (in[i] != 0)
        throw new WriteAccessException("Off diagonal elements must be zero");
  }

  @Override
  public void divideLeft(MatrixRowOperations ro)
  {
    solve(ro, data[0], data[1], data[2]);
  }

  /**
   * Finds the solution to TD*X=R. Uses Gaussian elimination and back
   * substitution. This algorithm is O(n)
   *
   * @param R
   * @return a new vector that solves TD*X=R.
   * @throws SizeException if the size of the matrix and the vector differ.
   * @throws SingularException if the tridiagonal matrix is not invertible.
   */
  public double[] divideLeft(double R[])
          throws SizeException, SingularException
  {
    double[] lower = this.data[0];
    double[] diagonal = this.data[1];
    double[] upper = this.data[2];
    return solve(R, lower, diagonal, upper);
  }

  /**
   * Finds the solution to X*TD=R. Uses Gaussian elimination and back
   * substitution. This algorithm is O(n)
   *
   * @param R
   * @return a new vector that solves X*TD=R.
   * @throws SizeException if the size of the matrix and the vector differ.
   * @throws SingularException if the tridiagonal matrix is not invertible.
   */
  public double[] divideRight(double R[])
          throws SizeException, SingularException
  {
    double[] lower = this.data[0];
    double[] diagonal = this.data[1];
    double[] upper = this.data[2];
    return solve(R, upper, diagonal, lower);
  }

  private static void solve(MatrixRowOperations R, double[] lower, double[] diagonal, double[] upper)
  {
    int n = diagonal.length;
    double[] D2 = new double[diagonal.length];
    D2[0] = diagonal[0];

    for (int i = 0; i < n - 1; ++i)
    {
      if (D2[i] == 0)
        throw new MathExceptions.ConvergenceException("bad condition number");

      double r = lower[i] / D2[i];
      D2[i + 1] = diagonal[i + 1] - r * upper[i];
      R.addScaledRows(i + 1, i, -r);
    }

    R.divideAssignRow(n - 1, D2[n - 1]);
    for (int i = n - 1; i > 0; --i)
    {
      R.addScaledRows(i - 1, i, -upper[i - 1]);
      R.divideAssignRow(i - 1, D2[i - 1]);
    }

    R.apply();
  }

  /**
   * Solve A*X=R. This is not a destructive operation.
   *
   * @param R is a row vector with the same size as matrix A.
   * @param lower is the lower diagonal of matrix A.
   * @param diagonal is the diagonal of the matrix A.
   * @param upper is the upper diagonal of the matrix A.
   * @return a new array that solves A*X=R.
   * @throws SizeException if the length of the A and R differ.
   * @throws SingularException if the matrix encounters a singular condition.
   */
  private double[] solve(double[] R, double[] lower, double[] diagonal, double[] upper)
          throws SizeException, SingularException
  {
    int n = R.length;
    if (diagonal.length != n)
      throw new SizeException("Input size mismatch "
              + " D=" + diagonal.length
              + " R=" + R.length);

    double[] D2 = new double[diagonal.length];
    double[] X = new double[R.length];
    D2[0] = diagonal[0];
    X[0] = R[0];

    for (int i = 0; i < n - 1; ++i)
    {
      if (D2[i] == 0)
        throw new SingularException("bad condition number");

      double r = lower[i] / D2[i];
      D2[i + 1] = diagonal[i + 1] - r * upper[i];
      X[i + 1] = R[i + 1] - r * X[i];
    }

    X[n - 1] /= D2[n - 1];
    for (int i = n - 1; i > 0; --i)
    {
      X[i - 1] = (X[i - 1] - upper[i - 1] * X[i]) / D2[i - 1];
    }
    return X;
  }

  @Override
  public Matrix addAssign(double scalar) throws UnsupportedOperationException
  {
    if (scalar == 0)
      return this;
    throw new UnsupportedOperationException("Tridiagnonal condition violated");
  }

  @Override
  public Matrix multiplyAssign(double scalar)
  {
    if (scalar == 1)
      return this;
    DoubleArray.multiplyAssign(this.data[0], scalar);
    DoubleArray.multiplyAssign(this.data[1], scalar);
    DoubleArray.multiplyAssign(this.data[2], scalar);
    return this;
  }

  @Override
  public Matrix divideAssign(double scalar)
  {
    if (scalar == 1)
      return this;
    DoubleArray.divideAssign(this.data[0], scalar);
    DoubleArray.divideAssign(this.data[1], scalar);
    DoubleArray.divideAssign(this.data[2], scalar);
    return this;
  }

// /* 
//    It implement equivalent we would need to compute a hashcode using all of the 
//    elements of the matrix. Alos the double will have to be exactly equal. 
//  */
//  public boolean equalMatrix(Matrix matrix)
//  {
//    if (this == matrix)
//      return true;
//    if (matrix == null)
//      return false;
//    if (matrix.rows() != rows())
//      return false;
//    if (matrix.columns() != columns())
//      return false;
//    if (matrix instanceof MatrixTriDiagonal)
//    {
//      return DoubleArray.equivalent(data[0], ((MatrixTriDiagonal) matrix).data[0])
//              && DoubleArray.equivalent(data[1], ((MatrixTriDiagonal) matrix).data[1])
//              && DoubleArray.equivalent(data[2], ((MatrixTriDiagonal) matrix).data[2]);
//    }
//    return MatrixOps.equivalent(this, matrix);
//  }
}
