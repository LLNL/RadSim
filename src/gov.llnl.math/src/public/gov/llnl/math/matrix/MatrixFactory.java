/*
 * Copyright 2016, Lawrence Livermore National Security, LLC.
 * All rights reserved
 *
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.math.matrix;

import gov.llnl.math.MathExceptions.SizeException;
import gov.llnl.math.matrix.special.MatrixTriDiagonal;
import java.util.Collection;

/**
 *
 */
public class MatrixFactory
{

  /**
   * Verify that the second dimension is consistent throughout an array.
   *
   * By default java arrays make be jagged but we require rectangular. It is
   * best to always catch the problem at the start rather than failing later.
   *
   * If a jagged arrays or mismatching sizes is required, create the matrix
   * directly using one of the wrapper classes.
   */
  private static void assertRectangular(double[][] v) throws SizeException
  {
    // Check the major dimension
    int d1 = v.length;
    if (d1 == 0)
      throw new SizeException("zero dimension matrix");

    // Check the minor dimension
    int d2 = v[0].length;
    if (d2 == 0)
      throw new SizeException("zero dimension matrix");

    for (double[] v1 : v)
    {
      if (d2 != v1.length)
        throw new SizeException("dimension mismatch");
    }
  }

  /**
   * Produce a copy of a matrix.
   *
   * The layout of the matrix will depend on the orientation of the original
   * matrix. Row and Column access is preserved. The copy is always a fully
   * writable matrix.
   *
   * @param m
   * @return a new matrix which holds the same data as the input.
   */
  public static Matrix newMatrix(Matrix m)
  {
    // Keep the access policy if it has one.
    if (m instanceof Matrix.ColumnAccess)
      return newColumnMatrix(m);
    if (m instanceof Matrix.RowAccess)
      return newRowMatrix(m);

    // Otherwise select the best representation.
    if (m.rows() > m.columns())
      return newColumnMatrix(m);
    return newRowMatrix(m);
  }

  /**
   * Create a new matrix with a specific size.
   *
   * The matrix type will depend on the number of rows and columns such that the
   * most efficient implementation is used.
   *
   * @param rows
   * @param columns
   * @return
   */
  public static Matrix newMatrix(int rows, int columns)
  {
    if (rows == 1)
      return new MatrixColumnArray(rows, columns);
    if (columns == 1)
      return new MatrixRowArray(rows, columns);
    if (rows >= columns)
      return new MatrixColumnTable(rows, columns);
    return new MatrixRowTable(rows, columns);
  }

  /**
   * Create a new matrix initialized to values stored in an array.
   *
   * Data is interpreted in column major order.
   *
   * @param v
   * @param r
   * @param c
   * @return a new matrix initialized by the array.
   * @throws SizeException
   */
  public static Matrix createFromArray(double[] v, int r, int c) throws SizeException
  {
    return wrapArray(v, r, c).copyOf();
  }

  public interface MatrixNew<T>
  {
    T create(int r, int c);
  }

  public static <T extends Matrix>
          T createFromArray(double[] v, int r, int c, MatrixNew<T> supplier) throws SizeException
  {
    return (T) supplier.create(r, c).assign(wrapArray(v, r, c));
  }

  /**
   * Creates a matrix from a list of arrays. 
   * This is useful when working with
   * MATLAB.
   *
   * @param v
   * @param layout should be true if the data is row major (C convention) or
   * false if data is column major.
   * @return a new matrix initialized from the array.
   * @throws SizeException
   */
  @Deprecated
  public static Matrix createFromArray(double[][] v, boolean layout) throws SizeException
  {
    return wrapFromArray(v, layout).copyOf();
  }

  /**
   * Creates a matrix from a list of arrays.
   *
   * This is useful when working with MATLAB. SUPERSEDED this does not produce
   * an access type, better to use wrapRows or wrapColumns.
   *
   * @param v
   * @param layout should be true if the data is row major (C convention) or
   * false if data is column major.
   * @return a new matrix backed by the array. The matrix is a view and cannot
   * be resized.
   * @throws SizeException
   */
  @Deprecated
  public static Matrix wrapFromArray(double[][] v, boolean layout) throws SizeException
  {
    assertRectangular(v);
    int d1 = v.length;
    int d2 = v[0].length;

    // Determine the orientation
    if (layout == true)
    {
      // Row major  v[r][c]
      return new MatrixRowTable(v, v, d2);
    }
    else
    {
      // Column major v[c][r]
      return new MatrixColumnTable(v, v, d2);
    }
  }

  /**
   * Wrap a double array as a matrix.
   *
   * Data is interpreted in column major order.
   *
   * @param v
   * @param r
   * @param c
   * @return a new matrix backed the array. The matrix is a view and cannot be
   * resized.
   * @throws SizeException if the length of v does not match the number of
   * requested rows and columns.
   */
  public static Matrix wrapArray(double[] v, int r, int c) throws SizeException
  {
    if (r * c != v.length)
      throw new SizeException("Size mismatch (" + r + "," + c + ") " + v.length);
    if (r == 1)
      return new MatrixRowArray(v, v, r, c);
    return new MatrixColumnArray(v, v, r, c);
  }

//<editor-fold desc="column" defaultstate="collapsed">
  public static Matrix.ColumnAccess newColumnMatrix(int rows, int columns)
  {
    // If we have only a single column, we can use a column array
    if (columns == 1)
      return new MatrixColumnArray(rows, columns);
    return new MatrixColumnTable(rows, columns);
  }

  /**
   * Treat an array as a column vector.
   *
   * This produces a view so alterations to the matrix are reflected back on the
   * original data.
   *
   * @param column is a double array to be used as a view.
   * @return a matrix view of the supplied columns.
   * @throws gov.llnl.math.MathExceptions.SizeException
   */
  public static Matrix.ColumnAccess newColumnMatrix(double[] column)
  {
    return new MatrixColumnArray(column, column, column.length, 1);
  }

  /**
   * Treat a set of columns as a column matrix.
   *
   * This produces a view so alterations to the matrix are reflected back on the
   * original data.
   *
   * @param columns is a set of columns to be used as a view.
   * @return a matrix view of the supplied columns.
   * @throws gov.llnl.math.MathExceptions.SizeException
   */
  public static Matrix.ColumnAccess newColumnMatrix(double[]   ... columns) throws SizeException
  {
    assertRectangular(columns);
    return new MatrixColumnTable(columns, columns, columns[0].length);
  }

  public static Matrix.ColumnAccess newColumnMatrix(Collection<double[]> columns)
  {
    double[][] data = columns.toArray(new double[0][]);
    return new MatrixColumnTable(data, data, data[0].length);
  }

  /**
   * Copy an existing matrix as column matrix.
   *
   * @param matrix
   * @return
   */
  public static Matrix.ColumnAccess newColumnMatrix(Matrix matrix)
  {
    return new MatrixColumnTable(matrix);
  }

  /**
   * Create a column vector initialized to a set of values.
   *
   * The matrix is a copy and thus will not modify the original.
   *
   * Use newColumnMatrix(values.clone()).
   *
   * @param v
   * @return a column vector initialized to the value specified.
   */
  @Deprecated
  public static MatrixColumnArray createColumnVector(double[] v)
  {
    return wrapColumnVector(v).copyOf();
  }

  /**
   * Wrap a double array as a column vector.
   *
   * @param v
   * @return a new column vector backed by the array. The matrix is a view and
   * cannot be resized.
   */
  @Deprecated
  public static MatrixColumnArray wrapColumnVector(double[] v)
  {
    try
    {
      return new MatrixColumnArray(v, v, v.length, 1);
    }
    catch (SizeException ex)
    {
      throw new RuntimeException(ex);
    }
  }

  /**
   * Ensures that a matrix for read access has column access.
   *
   * If the matrix does not have column access, it will produce a copy. Because
   * the result is not guaranteed to be a copy the result should not be
   * modified.
   *
   * @param matrix
   * @return the input if matrix already has column access, or a copy if it does
   * not.
   */
  public static Matrix.ColumnAccess asColumnMatrix(Matrix matrix)
  {
    if (matrix instanceof Matrix.ColumnAccess)
      return (Matrix.ColumnAccess) matrix;
    return newColumnMatrix(matrix);
  }

//</editor-fold>
//<editor-fold desc="row" defaultstate="collapsed">
  /**
   * Create a new row matrix.
   *
   * The matrix is backed by the data supplied so any changes in the original
   * will be reflected in the matrix.
   *
   * @param rows is a set of rows. Each row must have the same length.
   * @return a new matrix with references supplied rows.
   * @throws gov.llnl.math.MathExceptions.SizeException if the rows have
   * differing lengths.
   */
  public static Matrix.RowAccess newRowMatrix(double[]   ... rows) throws SizeException
  {
    assertRectangular(rows);
    return new MatrixRowTable(rows, rows, rows[0].length);
  }

  /**
   * Create a new row matrix.
   *
   * The matrix is backed by the data supplied so any changes in the original
   * will be reflected in the matrix.
   *
   * @param row is a single row.
   * @return a new matrix with references supplied rows.
   * @throws gov.llnl.math.MathExceptions.SizeException if the rows have
   * differing lengths.
   */
  public static Matrix.RowAccess newRowMatrix(double[] row)
  {
    return new MatrixRowArray(row, row, 1, row.length);
  }

  /**
   * Create a new row matrix.
   *
   * The matrix is backed by the data supplied so any changes in the original
   * will be reflected in the matrix.
   *
   * @param rows
   * @return a new matrix with references supplied rows.
   * @throws gov.llnl.math.MathExceptions.SizeException if the rows have
   * differing lengths.
   */
  public static MatrixRowTable newRowMatrix(Collection<double[]> rows) throws SizeException
  {
    double[][] data = rows.toArray(new double[0][]);
    assertRectangular(data);
    return new MatrixRowTable(data, data, data[0].length);
  }

  public static Matrix.RowAccess newRowMatrix(int rows, int columns)
  {
    if (rows == 1)
      return new MatrixRowArray(rows, columns);
    return new MatrixRowTable(rows, columns);
  }

  /**
   * Copy a matrix to a row matrix.
   *
   * This is different from other newRowMatrix in that it produces a resizable
   * copy rather than a view.
   *
   * @param matrix
   * @return
   */
  public static Matrix.RowAccess newRowMatrix(Matrix matrix)
  {
    return new MatrixRowTable(matrix);
  }

  @Deprecated
  public static MatrixRowArray wrapRowVector(double[] v) throws SizeException
  {
    return new MatrixRowArray(v, v, 1, v.length);
  }

  /**
   * Convert a single row into a matrix.
   *
   * Use newRowMatrix(v).copyOf()
   *
   * @param v
   * @return
   */
  @Deprecated
  public static Matrix createRowVector(double[] v)
  {
    return newRowMatrix(v).copyOf();
  }

  /**
   * Replaced with newColumnMatrix(v).
   *
   * @param v
   * @return
   * @throws gov.llnl.math.MathExceptions.SizeException
   */
  @Deprecated
  public static Matrix.ColumnAccess wrapColumns(double[][] v) throws SizeException
  {
    assertRectangular(v);
    return new MatrixColumnTable(v, v, v[0].length);
  }

  @Deprecated
  public static Matrix.RowAccess wrapRows(double[][] v) throws SizeException
  {
    assertRectangular(v);
    return new MatrixRowTable(v, v, v[0].length);
  }

  /**
   * Ensures that a matrix for read access has row access.
   *
   * If the matrix does not have row access, it will produce a copy.
   *
   * @param matrix
   * @return the input if matrix already has row access, or a copy if it does
   * not.
   */
  public static Matrix.RowAccess asRowMatrix(Matrix matrix)
  {
    if (matrix instanceof Matrix.RowAccess)
      return (Matrix.RowAccess) matrix;
    return newRowMatrix(matrix);
  }

  /**
   * Create a row operations view for an existing matrix.
   *
   * The operations are backed by the original matrix and thus requested row
   * operations are reflected on the matrix. Not all operations may be
   * supported. For example, swapping rows of a symmetric matrix breaks
   * symmetry.
   *
   * @param matrix
   * @return
   */
  public static MatrixRowOperations createRowOperations(Matrix matrix)
  {
    if (matrix instanceof MatrixTriDiagonal)
      return new MatrixRowOpsTriDiagonal((MatrixTriDiagonal) matrix);
    if (matrix instanceof Matrix.ColumnAccess
            && matrix instanceof Matrix.WriteAccess)
      return new MatrixRowOpsColumnAccess((Matrix.ColumnAccess) matrix);
    if (matrix instanceof Matrix.RowAccess
            && matrix instanceof Matrix.WriteAccess)
      return new MatrixRowOpsRowAccess((Matrix.RowAccess) matrix);
    throw new UnsupportedOperationException("Row operations not supported for matrix of type "
            + matrix.getClass());
  }

  /**
   * Create a row operations backed by a single column vector.
   *
   * This is used mainly as speed optimization as we don't need to iterate
   * through multiple columns.
   *
   * @param values is the values in the column vector.
   * @return
   */
  public static MatrixRowOperations createRowOperations(double[] values)
  {
    return new MatrixRowOpsVector(values, 0);
  }
//</editor-fold>
}
