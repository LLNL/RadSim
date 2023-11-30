/*
 * Copyright 2016, Lawrence Livermore National Security, LLC. 
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.math.matrix;

import gov.llnl.math.MathExceptions.MathException;
import gov.llnl.math.MathExceptions.ResizeException;
import gov.llnl.math.MathExceptions.SizeException;

/**
 *
 * @author nelson85
 */
public class MatrixAssert
{

  /**
   * Convert the size of a matrix to a string. Used for producing exceptions.
   *
   * @param matrix is the matrix to print.
   * @return a string with the rows and columns in parenthesis.
   */
  public static String getSize(Matrix matrix)
  {
    return getSize(matrix.rows(), matrix.columns());
  }

  /**
   * Convert the size of a matrix to a string. Used for producing exceptions.
   *
   * @param rows is the rows to print.
   * @param columns is the columns to print.
   * @return a string with the rows and columns in parenthesis.
   */
  private static String getSize(int rows, int columns)
  {
    return String.format("(%d,%d)", rows, columns);
  }

  /**
   * Throws a SizeException if the matrix is not square.
   *
   * @param m is the first matrix operand.
   * @throws SizeException when the size condition is not met.
   */
  public static void assertSquare(Matrix m) throws SizeException
  {
    if (m.rows() == m.columns())
      return;
    throw new SizeException("Matrix must be square, " + getSize(m));
  }

  /**
   * Throws a SizeException if the matrix is equal to size.
   *
   * @param m is the first matrix operand.
   * @throws SizeException when the size condition is not met.
   */
  public static void assertSizeEquals(Matrix m, int size) throws SizeException
  {
    if (m.rows() * m.columns() == size)
      return;
    throw new SizeException("Matrix must have size, " + getSize(m) + " required size=" + size);
  }

  /**
   * Throws a SizeException if the dimensions do not match.
   *
   * @param a is the first matrix operand.
   * @param b is the second matrix operand.
   * @throws SizeException when the size condition is not met.
   */
  public static void assertEqualSize(Matrix a, Matrix b) throws SizeException
  {
    if (a.rows() == b.rows() && a.columns() == b.columns())
      return;
    throw new SizeException("Operator size mismatch a=" + getSize(a)
            + " b=" + getSize(b));
  }

  /**
   * Throws a SizeException if the inner dimensions do not match.
   *
   * @param a is the first matrix operand.
   * @param b is the second matrix operand.
   * @throws SizeException when the size condition is not met.
   */
  public static void assertColumnsEqualsRows(Matrix a, Matrix b) throws SizeException
  {
    if (a.columns() == b.rows())
      return;
    throw new SizeException("Operator size mismatch a=" + getSize(a)
            + " b=" + getSize(b));
  }

  /**
   * Throws a SizeException if the rows are not equal.
   *
   * @param a is the first matrix operand.
   * @param b is the second matrix operand.
   * @throws SizeException when the size condition is not met.
   */
  public static void assertRowsEqual(Matrix a, Matrix b) throws SizeException
  {
    if (a.rows() == b.rows())
      return;
    throw new SizeException("Operator size mismatch on rows, a=" + getSize(a)
            + " b=" + getSize(b));
  }

  /**
   * Throws a SizeException if the rows are not equal.
   *
   * @param a is the first matrix operand.
   * @param rows are the requested size.
   * @throws SizeException when the size condition is not met.
   */
  public static void assertRowsEqual(Matrix a, int rows) throws SizeException
  {
    if (a.rows() == rows)
      return;
    throw new SizeException("Operator size mismatch on rows, a=" + getSize(a)
            + " rows=" + rows);
  }

  /**
   * Throws a SizeException if the columns are not equal.
   *
   * @param a is the first matrix operand.
   * @param columns are the requested size.
   * @throws SizeException when the size condition is not met.
   */
  public static void assertColumnsEqual(Matrix a, int columns) throws SizeException
  {
    if (a.columns() == columns)
      return;
    throw new SizeException("Operator size mismatch on columns, a=" + getSize(a)
            + " columns=" + columns);
  }

  /**
   * Throws a SizeException if the columns are not equal.
   *
   * @param a is the first matrix operand.
   * @param b is the first matrix operand.
   * @throws SizeException when the size condition is not met.
   */
  public static void assertColumnsEqual(Matrix a, Matrix b) throws SizeException
  {
    if (a.columns() == b.columns())
      return;
    throw new SizeException("Operator size mismatch on columns, a=" + getSize(a)
            + " b=" + getSize(b));
  }

  /**
   * Throws a ResizeException if matrix is a view. 
   * 
   * This is called on a matrix to
   * verify that we can alter the memory while resizing.
   *
   * @param m is the matrix to test.
   * @throws ResizeException when the size condition is not met.
   */
  public static void assertNotViewResize(Matrix m) throws ResizeException
  {
    if (m != m.sync())
      throw new ResizeException("Views cannot be resized");
  }

  /**
   * Throws a ResizeException if matrix is a view of a different size. This is
   * called on a matrix to verify the view can hold the requested data.
   *
   * @param m is the matrix to test.
   * @param rows
   * @param columns
   * @throws ResizeException when the size condition is not met.
   */
  public static void assertViewResize(Matrix m, int rows, int columns)
          throws ResizeException
  {
    if (m.rows() != rows || m.columns() != columns)
      throw new ResizeException("Views cannot be resized, m=" + getSize(m)
              + " requested=" + getSize(rows, columns));
  }

  /**
   * Verifies a matrix is symmetric.
   *
   * @param m is the matrix under test.
   * @throws SizeException if matrix is not square.
   * @throws MathException if assertion fails.
   */
  public static void assertSymmetric(Matrix m) throws MathException
  {
    // Symmetric requires a square matrix
    assertSquare(m);
    double n = m.rows();

    // Verify element by element
    for (int i = 0; i < n; ++i)
    {
      for (int j = i + 1; j < n; ++j)
      {
        // This code could be better optimized for memory usage.
        double u1 = m.get(i, j);
        double u2 = m.get(j, i);
        if (u1 == u2)
          continue;

        double v1 = u1;
        double v2 = u2;
        if (u1 < 0)
          v1 = -v1;
        if (u2 < 0)
          v2 = -v2;

        double diff = (u1 - u2);
        if (diff < 0)
          diff = -diff;

        double cond = diff / (v1 + v2);
        if (cond > 1e-14)
          throw new MathException(
                  "Assertion failed (" + i + "," + j + ") cond=" + cond);
      }
    }
  }

  /**
   * Verifies a matrix is a vector.
   *
   * @param matrix is the item to be checked.
   * @throws SizeException if the matrix is not a vector.
   */
  public static void assertVector(Matrix matrix) throws SizeException
  {
    if (matrix.rows() == 1 || matrix.columns() == 1)
      return;
    throw new SizeException("Operand must be a vector m=" + getSize(matrix));
  }

  public static void assertInRange(Matrix matrix, int row, int column)
          throws IndexOutOfBoundsException
  {
    if (row < 0 || column < 0 || row >= matrix.rows() || column >= matrix.columns())
      throw new IndexOutOfBoundsException("Access outside matrix m=" + getSize(matrix)
              + " access " + row + "," + column);
  }

  public static void assertResizeSquare(int rows, int columns)
          throws ResizeException
  {
    if (rows == columns)
      return;
    throw new ResizeException("Matrix must be square, requested " + getSize(rows, columns));
  }

  /**
   * Verifies a matrix is a triangular.
   *
   * @param matrix is the matrix to be checked.
   * @return true if the matrix is an upper triangular and false if it is lower
   * triangular.
   */
  public static boolean assertTriangular(Matrix matrix)
          throws MathException
  {
    if (matrix instanceof Matrix.Triangular)
      return ((Matrix.Triangular) matrix).isUpper();

    boolean upper;
    if (MatrixOps.determineVectorAccess1(matrix))
      upper = checkTriagularIterator(MatrixIterators.newColumnReadIterator(matrix));
    else
      upper = !checkTriagularIterator(MatrixIterators.newRowReadIterator(matrix));
    return upper;
  }
  
  public static void assertTriDiagonal(Matrix matrix)
          throws MathException
  {
    if (MatrixOps.determineVectorAccess1(matrix))
      checkTriDiagonalIterator(MatrixIterators.newColumnReadIterator(matrix));
    else
      checkTriDiagonalIterator(MatrixIterators.newRowReadIterator(matrix));
  }

//<editor-fold desc="internal">
  private static boolean checkTriagularIterator(MatrixIterators.VectorIterator iter)
  {
    boolean below = false, above = false;
    while (iter.advance())
    {
      double[] v = iter.access();
      int begin = iter.begin();
      int end = iter.end();
      int diag = begin + iter.index();
      if (diag > end)
        diag = end;

      // check if anything above diagonal in non-zero
      for (; begin < diag; ++begin)
        if (v[begin] != 0)
          above = true;

      // Skip diagonal
      ++begin;

      for (; begin < end; ++begin)
        if (v[begin] != 0)
          below = true;

      if (above && below)
        throw new MathException("Matrix is not triangular");
    }
    return above;
  }


  private static void checkTriDiagonalIterator(MatrixIterators.VectorIterator iter)
  {
    int k = iter.length();
    while (iter.advance())
    {
      double[] v = iter.access();
      int n = iter.index();
      int offset = iter.begin();
      for (int i = 0; i < n - 1; ++i)
      {
        if (v[offset + i] != 0)
          throw new MathException("Not tridiagonal");
      }
      for (int i = n + 2; i < k; ++i)
      {
        if (v[offset + i] != 0)
          throw new MathException("Not tridiagonal");
      }
    }
  }
  
//</editor-fold>
}
