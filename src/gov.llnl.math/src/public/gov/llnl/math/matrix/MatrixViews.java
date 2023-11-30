/*
 * Copyright 2016, Lawrence Livermore National Security, LLC. 
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.math.matrix;

import gov.llnl.math.DoubleArray;
import gov.llnl.math.MathExceptions;
import gov.llnl.math.MathExceptions.SizeException;
import gov.llnl.math.matrix.Matrix.ColumnAccess;
import gov.llnl.math.matrix.Matrix.RowAccess;
import gov.llnl.math.matrix.Matrix.SelectAccess;
import java.util.ArrayList;

/**
 * Collection of utility functions to extract views for a matrix. 
 * 
 * Where possible
 * the views will try to maintain the access policy of the matrix. Views will
 * become invalid if the matrix is resized. Some views are implemented by the
 * matrix class if SelectAccess is implemented. The transpose view is
 * implemented by the matrix.
 *
 * @author nelson85
 */
public class MatrixViews
{
//<editor-fold desc="column views" defaultstate="collapsed">
  /**
   * Produces a view of the matrix with access to one column. Any modifications
   * to the proxy object will affect the original.
   *
   * @param matrix is the matrix to create a view for.
   * @param index is an index to view.
   * @return a view of the matrix.
   * @throws IndexOutOfBoundsException
   */
  static public Matrix selectColumn(Matrix matrix, int index) throws IndexOutOfBoundsException
  {
    if (matrix instanceof ColumnAccess)
    {
      ColumnAccess accessor = (ColumnAccess) matrix;
      ArrayList<MatrixListBase.Vector> out = new ArrayList<>();
      out.add(new MatrixListBase.Vector(accessor.accessColumn(index), accessor.addressColumn(index)));
      return new MatrixColumnList(matrix.sync(), out, matrix.rows());
    }

    if (matrix instanceof SelectAccess)
      return ((SelectAccess) matrix).selectColumn(index);

    return new MatrixColumnProxy(matrix, index);
  }

  /**
   * Produces a view of the matrix with access to multiple columns. Any
   * modifications to the proxy object will affect the original.
   *
   * @param matrix is the matrix to create a view for.
   * @param index is an array of indices to view.
   * @return a view of the matrix.
   * @throws IndexOutOfBoundsException
   */
  static public Matrix selectColumns(Matrix matrix, int ... index) throws IndexOutOfBoundsException
  {
    if (matrix instanceof ColumnAccess)
    {
      ColumnAccess accessor = (ColumnAccess) matrix;
      ArrayList<MatrixListBase.Vector> out = new ArrayList<>();
      for (int i : index)
        out.add(new MatrixListBase.Vector(accessor.accessColumn(index[i]), accessor.addressColumn(index[i])));
      return new MatrixColumnList(matrix.sync(), out, matrix.rows());
    }

    if (matrix instanceof SelectAccess)
      return ((SelectAccess) matrix).selectColumns(index);

    return new MatrixColumnProxy(matrix, index);
  }

  /**
   * Select a set of columns that are together in sequence as a view. Attempts
   * to preserve the access capabilities of the matrix.
   *
   * @param matrix is the matrix to create a view for.
   * @param begin is the inclusive beginning of the range of columns.
   * @param end is the exclusive end of the range of columns.
   * @return a view of the matrix.
   * @throws SizeException if the requested range is invalid.
   */
  static public Matrix selectColumnRange(Matrix matrix, int begin, int end) throws SizeException
  {
    if ((begin < 0) || (end > matrix.columns()))
      throw new SizeException("Selected out of bounds column range begin : " + begin + " end : " +end +  " from : " + matrix.columns() );

    if (matrix instanceof ColumnAccess)
    {
      ColumnAccess accessor = (ColumnAccess) matrix;
      ArrayList<MatrixListBase.Vector> out = new ArrayList<>();
      out.ensureCapacity(end - begin);
      for (int i = begin; i < end; ++i)
        out.add(new MatrixListBase.Vector(accessor.accessColumn(i), accessor.addressColumn(i)));
      return new MatrixColumnList(matrix.sync(), out, matrix.rows());
    }

    if (matrix instanceof RowAccess)
    {
      // Handle trivial case
      if (end - begin == 1)
        return new MatrixColumnProxy(matrix, begin);

      RowAccess accessor = (RowAccess) matrix;
      ArrayList<MatrixListBase.Vector> out = new ArrayList<>();
      int rows = matrix.rows();
      out.ensureCapacity(rows);
      for (int i = 0; i < rows; ++i)
      {
        out.add(new MatrixListBase.Vector(accessor.accessRow(i), accessor.addressRow(i) + begin));
      }
      return new MatrixRowList(matrix.sync(), out, end - begin);
    }

    int[] index = new int[end - begin];
    for (int i = 0; i < end - begin; ++i)
      index[i] = begin + i;
    return new MatrixColumnProxy(matrix, index);
  }

//</editor-fold>
//<editor-fold desc="row views" defaultstate="collapsed">
  /**
   * Produces a view of the matrix with access to one row. 
   * 
   * Any modifications to the proxy object will affect the original.
   *
   * @param matrix is the matrix to create a view for.
   * @param index is an index to view.
   * @return a view of the matrix
   * @throws IndexOutOfBoundsException
   */
  static public Matrix selectRow(Matrix matrix, int index) throws IndexOutOfBoundsException
  {
    if (matrix instanceof RowAccess)
    {
      RowAccess accessor = (RowAccess) matrix;
      ArrayList<MatrixListBase.Vector> out = new ArrayList<>();
      out.add(new MatrixListBase.Vector(accessor.accessRow(index), accessor.addressRow(index)));
      return new MatrixRowList(matrix.sync(), out, matrix.columns());
    }

    if (matrix instanceof SelectAccess)
      return ((SelectAccess) matrix).selectRow(index);

    return new MatrixRowProxy(matrix, index);
  }

  /**
   * Produces a view of the matrix with access to multiple rows. Any
   * modifications to the proxy object will affect the original.
   *
   * @param matrix is the matrix to create a view for.
   * @param index is an array of indices to view.
   * @return a view of the matrix
   * @throws IndexOutOfBoundsException
   */
  static public Matrix selectRows(Matrix matrix, int... index) throws IndexOutOfBoundsException
  {
    if (matrix instanceof RowAccess)
    {
      RowAccess accessor = (RowAccess) matrix;
      ArrayList<MatrixListBase.Vector> out = new ArrayList<>();
      for (int i : index)
        out.add(new MatrixListBase.Vector(accessor.accessRow(i), accessor.addressRow(i)));
      return new MatrixRowList(matrix.sync(), out, matrix.columns());
    }

    if (matrix instanceof SelectAccess)
      return ((SelectAccess) matrix).selectRows(index);

    return new MatrixRowProxy(matrix, index);
  }

  /**
   * Select a set of rows that are together in sequence as a view. Attempts to
   * preserve the access capabilities of the matrix.
   *
   * @param matrix is the matrix to create a view for.
   * @param begin is the inclusive beginning of the range of rows.
   * @param end is the exclusive end of the range of rows.
   * @return a view of the matrix.
   * @throws SizeException if the requested range is invalid.
   */
  static public Matrix selectRowRange(Matrix matrix, int begin, int end) throws SizeException
  {
    if ((begin < 0) || (end > matrix.rows()))
      throw new SizeException();

    if (matrix instanceof RowAccess)
    {
      RowAccess accessor = (RowAccess) matrix;
      ArrayList<MatrixListBase.Vector> out = new ArrayList<>();
      out.ensureCapacity(end - begin);
      for (int i = begin; i < end; ++i)
      {
        out.add(new MatrixListBase.Vector(accessor.accessRow(i), accessor.addressRow(i)));
      }
      return new MatrixRowList(matrix.sync(), out, matrix.columns());
    }

    if (matrix instanceof ColumnAccess)
    {
      // Handle trivial case
      if (end - begin == 1)
        return new MatrixRowProxy(matrix, begin);

      ColumnAccess accessor = (ColumnAccess) matrix;
      ArrayList<MatrixListBase.Vector> out = new ArrayList<>();
      int columns = matrix.columns();
      for (int i = 0; i < columns; ++i)
      {
        out.add(new MatrixListBase.Vector(accessor.accessColumn(i), accessor.addressColumn(i) + begin));
      }
      return new MatrixColumnList(matrix.sync(), out, end - begin);
    }

    int[] index = new int[end - begin];
    for (int i = 0; i < end - begin; ++i)
      index[i] = begin + i;
    return new MatrixRowProxy(matrix, index);
  }

//</editor-fold>
//<editor-fold desc="block views" defaultstate="collapsed">
  /**
   * Select a set of rows that are together in sequence as a view. Attempts to
   * preserve the access capabilities of the matrix.
   *
   * @param matrix is the matrix to create a view for.
   * @param beginRow is the inclusive beginning of the range of rows.
   * @param endRow is the exclusive end of the range of rows.
   * @param beginColumn is the inclusive beginning of the range of column.
   * @param endColumn is the exclusive end of the range of column.
   * @return a view of the matrix.
   * @throws SizeException if the requested range is invalid.
   */
  static public Matrix select(Matrix matrix,
          int beginRow, int endRow,
          int beginColumn, int endColumn)
          throws SizeException
  {
    if ((beginRow < 0) || (endRow > matrix.rows()))
      throw new SizeException();
    if ((beginColumn < 0) || (endColumn > matrix.columns()))
      throw new SizeException();

    if (matrix instanceof RowAccess)
    {
      RowAccess accessor = (RowAccess) matrix;
      ArrayList<MatrixListBase.Vector> out = new ArrayList<>();
      out.ensureCapacity(endRow - beginRow);
      for (int i = beginRow; i < endRow; ++i)
        out.add(new MatrixListBase.Vector(accessor.accessRow(i), accessor.addressRow(i) + beginColumn));
      return new MatrixRowList(matrix.sync(), out, endColumn - beginColumn);
    }

    if (matrix instanceof ColumnAccess)
    {
      ColumnAccess accessor = (ColumnAccess) matrix;
      ArrayList<MatrixListBase.Vector> out = new ArrayList<>();
      out.ensureCapacity(endColumn - beginColumn);
      for (int i = beginColumn; i < endColumn; ++i)
        out.add(new MatrixListBase.Vector(accessor.accessColumn(i), accessor.addressColumn(i) + beginRow));
      return new MatrixColumnList(matrix.sync(), out, endRow - beginRow);
    }

    int[] index = new int[endRow - beginRow];
    for (int i = 0; i < endRow - beginRow; ++i)
      index[i] = beginRow + i;
    return selectColumnRange(new MatrixRowProxy(matrix, index), beginColumn, endColumn);
  }

//</editor-fold>
//<editor-fold desc="misc" defaultstate="collapsed">
  /**
   * Select the diagonal as a view. Any modifications to the proxy object will
   * affect the original.
   *
   * @param matrix
   * @return a proxy to the object
   */
  static public Matrix diagonal(Matrix matrix)
  {
    return new MatrixDiagProxy(matrix);
  }

//</editor-fold>

  public static Matrix.RowReadAccess duplicateRowVector(Matrix.RowAccess vector, int rows)
  {
    return new Matrix.RowReadAccess()
    {
      @Override
      public double[] accessRow(int index)
      {
        return vector.accessRow(0);
      }

      @Override
      public int addressRow(int r)
      {
        return vector.addressRow(0);
      }

      @Override
      public int rows()
      {
        return rows;
      }

      @Override
      public int columns()
      {
        return vector.columns();
      }

      @Override
      public void set(int row, int column, double value) throws MathExceptions.WriteAccessException, IndexOutOfBoundsException
      {
        mutable();
        vector.set(0, column, value);
      }

      @Override
      public double get(int row, int column) throws IndexOutOfBoundsException
      {
        return vector.get(0, column);
      }

      @Override
      public Object sync()
      {
        return vector.sync();
      }

      @Override
      public Matrix assign(Matrix matrix) throws MathExceptions.ResizeException, MathExceptions.WriteAccessException
      {
        mutable();
        return null;
      }

      @Override
      public Matrix copyOf()
      {
        return new MatrixRowTable(this);
      }

      @Override
      public double[] copyRowTo(double[] destination, int offset, int index) throws IndexOutOfBoundsException
      {
        return vector.copyRowTo(destination, offset, 0);
      }

      @Override
      public double[] copyColumnTo(double[] destination, int offset, int index) throws IndexOutOfBoundsException
      {
        return DoubleArray.fillRange(destination, offset, offset+rows, vector.get(0, index));
      }
      
      @Override
      public boolean equals(Object other)
      {
        if (!(other instanceof Matrix))
          return false;
        return MatrixOps.equals(this, (Matrix) other);
      }

    };
  }
}
