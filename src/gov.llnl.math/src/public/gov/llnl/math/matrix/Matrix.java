/*
 * Copyright 2016, Lawrence Livermore National Security, LLC.
 * All rights reserved
 *
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.math.matrix;

import gov.llnl.math.MathExceptions.ResizeException;
import gov.llnl.math.MathExceptions.WriteAccessException;
import gov.llnl.utility.xml.bind.ReaderInfo;
import gov.llnl.utility.xml.bind.WriterInfo;
import java.io.Serializable;

/**
 * Interface for a two dimensional array of doubles.
 *
 * @author nelson85
 */
@ReaderInfo(gov.llnl.math.matrix.MatrixReader.class)
@WriterInfo(gov.llnl.math.matrix.MatrixWriter.class)
public interface Matrix extends Serializable
{
//<editor-fold desc="basic" defaultstate="collapsed">
  /**
   * Get the number of rows in this matrix.
   *
   * @return the rows in the matrix.
   */
  int rows();

  /**
   * Get the number of columns in this matrix.
   *
   * @return the columns in the matrix.
   */
  int columns();

  /**
   * Set the contents of a matrix element. The location is not checked and thus
   * may or may not produce an out of bounds exception depending on the
   * implementation.
   *
   * @param row is the row of the element.
   * @param column is the colum of the element.
   * @param value is the value to set.
   * @throws IndexOutOfBoundsException if the matrix element does not exist.
   * (optional)
   * @throws WriteAccessException if this matrix is not writable.
   */
  void set(int row, int column, double value)
          throws WriteAccessException, IndexOutOfBoundsException;

  /**
   * Get an element at a specified location. The location is not checked and
   * thus may or may not produce an out of bounds exception depending on the
   * implementation.
   *
   * @param row is the row.
   * @param column is the column.
   * @return the element at the location.
   * @throws IndexOutOfBoundsException if the index exceeds the array size.
   * (optional)
   */
  double get(int row, int column) throws IndexOutOfBoundsException;

  /**
   * Request access to manipulate data using row or column access.
   *
   * This must be called prior to using row or column access to write data.
   * Certain matrices have read access but must not be written to. Calling
   * mutable insures access policies are enforced.
   *
   * @throws WriteAccessException if the matrix does not support direct write
   * access.
   */
  default void mutable() throws WriteAccessException
  {
    throw new WriteAccessException("Matrix is not mutable");
  }

  /**
   * Get the synchronizer object for this matrix.
   *
   * Must not be null. Use the sync method, to lock a matrix when sharing
   * between threads.
   *
   * @return the object that owns this memory.
   */
  Object sync();

//</editor-fold>
//<editor-fold desc="assign/copy" defaultstate="collapsed">
  /**
   * Change the layout of this matrix.
   *
   * Resize is an optional operation. The memory is reused if the matrix is the
   * same size as needed. Contents of the matrix depend on the operation
   * performed as indicated by the return code. It will fail on all views if the
   * existing size is different than specified. Resizing the array will invalid
   * all existing views of the data if the size is changed.
   *
   * @param rows is the desired row size.
   * @param columns is the desired column size.
   * @return true if the matrix was zeroed, or false if the memory is
   * uninitialized.
   * @throws ResizeException if the matrix is not resizable or is a view of a
   * different size.
   */
  default boolean resize(int rows, int columns) throws ResizeException
  {
    if (rows == rows() && columns == columns())
      return false;
    throw new ResizeException("Resize not supported for this type.");
  }

  /**
   * Copy the contents of the matrix into the current matrix. The target must be
   * resizable if the size of the source matrix is different. Existing views
   * will become invalid if the size is changed.
   *
   * @param matrix is a matrix holding the desired contents.
   * @return this matrix.
   * @throws ResizeException if the matrix cannot be resized or is a view of a
   * different size.
   * @throws WriteAccessException if this matrix is not writable.
   */
  Matrix assign(Matrix matrix) throws ResizeException, WriteAccessException;

  default void assignColumn(double[] d, int c)
          throws WriteAccessException, IndexOutOfBoundsException
  {
    MatrixViews.selectColumn(this, c).assign(MatrixFactory.newColumnMatrix(d));
  }

  default void assignRow(double[] d, int r)
          throws WriteAccessException, IndexOutOfBoundsException
  {
    MatrixViews.selectRow(this, r).assign(MatrixFactory.newRowMatrix(d));
  }

  /**
   * Create a copy of the matrix.
   *
   * Layout of the produced matrix will be the most efficient for operations.
   * The copy must support either RowAccess or ColumnAccess. The access policy
   * of the returned matrix may not match the original.
   *
   * @return a matrix with the same contents as this matrix.
   */
  Matrix copyOf();

//</editor-fold>
//<editor-fold desc="read/write" defaultstate="collapsed">
  /**
   * Copy the values from a row. Values are copied into specified destination.
   * The location of the row is not check and thus an out of bounds exception
   * may or may not be issued.
   *
   * @param destination is an array large enough to hold the row.
   * @param offset
   * @param index is the row number to copy.
   * @return the values copied.
   * @throws IndexOutOfBoundsException if the index exceeds the array size.
   * (optional)
   */
  double[] copyRowTo(double[] destination, int offset, int index)
          throws IndexOutOfBoundsException;

  /**
   * Copy the values from a column. Values are copied into specified
   * destination. The location of the column is not check and thus an out of
   * bounds exception may or may not be issued.
   *
   * @param destination is an array large enough to hold the column.
   * @param offset
   * @param index is the column number to copy.
   * @return the values copied.
   * @throws IndexOutOfBoundsException if the index exceeds the array size.
   * (optional)
   */
  double[] copyColumnTo(double[] destination, int offset, int index)
          throws IndexOutOfBoundsException;

  default double[] copyRow(int i) throws IndexOutOfBoundsException
  {
    return copyRowTo(new double[this.columns()], 0, i);
  }

  default double[] copyColumn(int i) throws IndexOutOfBoundsException
  {
    return copyColumnTo(new double[this.rows()], 0, i);
  }

  public interface WriteAccess extends Matrix
  {
    /**
     * Assign a row to a vector. The length of the the vector is not checked.
     * The location of the row is not check and thus an out of bounds exception
     * may or may not be issued.
     *
     * @param in is an array large enough to fill the rows.
     * @param index is the index of the row to update.
     * @throws WriteAccessException if this matrix is not writable.
     * @throws IndexOutOfBoundsException if the index exceeds the array size.
     * (optional)
     */
    @Override
    void assignRow(double[] in, int index)
            throws WriteAccessException, IndexOutOfBoundsException;

    /**
     * Assign a column to a vector. The length of the the vector is not checked.
     * The location of the column is not check and thus an out of bounds
     * exception may or may not be issued.
     *
     * @param in is an array large enough to fill the column.
     * @param index is the index of the column to update.
     * @throws WriteAccessException if this matrix is not writable.
     * @throws IndexOutOfBoundsException if the index exceeds the array size.
     * (optional)
     */
    @Override
    void assignColumn(double[] in, int index)
            throws WriteAccessException, IndexOutOfBoundsException;
  }

//</editor-fold>
//<editor-fold desc="utility" defaultstate="collapsed">
  /**
   * Create a flattened copy of an array in Column major order. This function is
   * to support Matlab translation. It is primarily an example of how to deal
   * with certain Matlab code that used flattened matrix access.
   * MatrixColumnArray and MatrixRowArray has direct access to flattened arrays.
   * Any other form must be converted first.
   * <p>
   * Matlab Version:
   * <pre>
   * {@code
   *   M=zeros(N,N);
   *   M(1:N+1:end)=2;  % Set the diagonal
   *   M(2:N+1:end)=1;  % Set the lower diagonal
   * }
   * </pre><p>
   * Java version:
   * <pre>
   * {@code
   *   M=new MatrixColumnArray(N,N);
   *   vM=M.toArray();
   *   int sz=N*N;
   *   for (int i=0; i<sz; i+=N+1)
   *     vM[i]=2;
   *   for (int i=1; i<sz; i+=N+1)
   *     vM[i]=1;
   * }
   * </pre>
   *
   * @return a flat array starting with values from the first column.
   */
  default double[] flatten()
  {
    return new MatrixColumnArray(this).toArray();
  }

  /**
   * Produces a transpose view of a matrix. The resulting matrix will be a proxy
   * to the same memory with a transpose layout. Any modifications to the object
   * will affect the contents of the transpose.
   *
   * This method will be overridden by matrices with a special access
   * restrictions.
   *
   * @return a view with a transpose memory scheme.
   */
  @SuppressWarnings("unchecked")
  default Matrix transpose()
  {
    // Otherwise chose the most appropriate
    if (this instanceof Matrix.ColumnAccess)
      return new MatrixTransposeView.TransposeRowAccess((Matrix.ColumnAccess) this);
    if (this instanceof Matrix.RowAccess)
      return new MatrixTransposeView.TransposeColumnAccess((Matrix.RowAccess) this);
    return new MatrixTransposeView(this);
  }

//</editor-fold>
//<editor-fold desc="derived" defaultstate="collapsed">
  /**
   * Interface for Matrix in which elements on the same row are stored together.
   */
  public interface RowReadAccess extends Matrix
  {
    /**
     * Access the data backing a row.
     *
     * The location of the row is not check and thus an out of bounds exception
     * may or may not be issued. If the data is to be written to
     * {@link #mutable} must be called first.
     *
     * @param index is the row to access.
     * @return a continuous memory block that holds this row.
     * @throws IndexOutOfBoundsException if the index exceeds the array size.
     * (optional)
     */
    double[] accessRow(int index);

    /**
     * Get the offset for the first element in the row.
     *
     * @param r is the row to get the addressColumn.
     * @return the offset for this element.
     */
    int addressRow(int r);
  }

  public interface RowAccess extends RowReadAccess, WriteAccess
  {
    /**
     * Gets the data as a series of columns.
     *
     * This may access the underlying data, thus mutating the data should not
     * occur.
     *
     * @return
     */
    default double[][] asRows()
    {
      int r = this.rows();
      int c = this.columns();
      double[][] out = new double[c][];
      for (int i = 0; i < c; ++i)
      {
        // If the row is addressable as an array then we can skip the copy.
        if (this.addressRow(i) == 0 && this.accessRow(i).length == r)
          out[i] = this.accessRow(i);
        else
          out[i] = this.copyRow(i);
      }
      return out;
    }
  }

  /**
   * Interface for Matrix in which elements on the same column are stored
   * together.
   */
  public interface ColumnReadAccess extends Matrix
  {
    /**
     * Access the data backing a column. The location of the column is not check
     * and thus an out of bounds exception may or may not be issued. If the data
     * is to be written to {@link #mutable} must be called first.
     *
     * @param index is the column to access.
     * @return a continuous memory block that holds this column.
     * @throws IndexOutOfBoundsException if the index exceeds the array size.
     * (optional)
     */
    double[] accessColumn(int index) throws IndexOutOfBoundsException;

    /**
     * Get the offset for first element in a column.
     *
     * @param c is the column to get the addressColumn.
     * @return the offset for this element.
     */
    int addressColumn(int c);
  }

  public interface ColumnAccess extends ColumnReadAccess, WriteAccess
  {
    /**
     * Gets the data as a series of columns.
     *
     * This may access the underlying data, thus mutating the data should not
     * occur.
     *
     * @return
     */
    default double[][] asColumns()
    {
      int r = this.rows();
      int c = this.columns();
      double[][] out = new double[c][];
      for (int i = 0; i < c; ++i)
      {
        if (this.addressColumn(i) == 0 && this.accessColumn(i).length == r)
          out[i] = this.accessColumn(i);
        else
          out[i] = this.copyColumn(i);
      }
      return out;
    }
  }

  /**
   * Base type for matrices that have special select methods.
   */
  public interface SelectAccess extends Matrix.WriteAccess
  {
    /**
     * Produce a view of the matrix in which a single column is selected.
     *
     * @param index is the column to produce a view of.
     * @return the view of the matrix.
     * @throws IndexOutOfBoundsException if the index exceeds the array size.
     *
     */
    Matrix selectColumn(int index) throws IndexOutOfBoundsException;

    /**
     * Produce a view of the matrix in which multiple columns are selected.
     *
     * @param index is a list of columns to produce a view of.
     * @return the view of the matrix.
     * @throws IndexOutOfBoundsException if the index exceeds the array size.
     */
    Matrix selectColumns(int[] index) throws IndexOutOfBoundsException;

    /**
     * Produce a view of the matrix in which a single row is selected.
     *
     * @param index is the row to produce a view of.
     * @return the view of the matrix.
     * @throws IndexOutOfBoundsException if the index exceeds the array size.
     */
    Matrix selectRow(int index) throws IndexOutOfBoundsException;

    /**
     * Produce a view of the matrix in which multiple rows are selected.
     *
     * @param index is a list of rows to produce a view of.
     * @return the view of the matrix.
     * @throws IndexOutOfBoundsException if the index exceeds the array size.
     */
    Matrix selectRows(int[] index) throws IndexOutOfBoundsException;
  }

  /**
   * Base type for matrix that implements specialized scalar operations. These
   * operations include adding, multiplying and dividing elements by scalar.
   * Some operations may not be supported.
   */
  public interface ScalarOperations extends Matrix
  {
    /**
     * Add a scalar to every element in this matrix. This operation is not
     * supported on all matrix types. Addition to a sparse matrix may cause a
     * violation of storage constraints.
     *
     * @param scalar is the value to add.
     * @return this matrix.
     * @throws UnsupportedOperationException if the addition of the scalar will
     * result in a matrix that does not comply with storage constraints.
     */
    Matrix addAssign(double scalar) throws UnsupportedOperationException;

    /**
     * Multiply every element in this matrix by a scalar.
     *
     * @param scalar is the value to multiply be.
     * @return this matrix.
     */
    Matrix multiplyAssign(double scalar);

    /**
     * Divide every element in this matrix by a scalar.
     *
     * @param scalar is the value to divide by.
     * @return this matrix.
     */
    Matrix divideAssign(double scalar);
  }

  /**
   * Base type for matrix that implements specialized element-wise operations.
   */
  public interface ElementwiseOperations extends Matrix
  {
    /**
     * Add a matrix to this matrix in an element-wise fashion.
     *
     * @param matrix
     * @return this matrix.
     * @throws UnsupportedOperationException
     */
    Matrix addAssign(Matrix matrix) throws UnsupportedOperationException;

    /**
     * Multiply a matrix to this matrix in an element-wise fashion.
     *
     * @param matrix
     * @return this matrix.
     */
    Matrix multiplyAssignElements(Matrix matrix);
  }

  /**
   * Base type for matrix that implements specialized divide operations.
   */
  public interface DivideOperation extends Matrix
  {
    /**
     * Specialized divide operation. This should not be called directly, but
     * will be involved by the MatrixOps.divideLeft front.
     *
     * @param ro is the target of the divide.
     */
    void divideLeft(MatrixRowOperations ro);
  }

//  /**
//   * Base type for matrices that have special transpose methods.
//   */
//  public interface TransposeOperation extends Matrix
//  {
//    /**
//     * Produces a transpose view of a matrix. The resulting matrix will be a
//     * proxy to the same memory with a transpose layout. Any modifications to
//     * the object will affect the contents of the transpose.
//     *
//     * @return the transpose view of the matrix.
//     */
//    Matrix transpose();
//  }
  /**
   * Base type for matrices that are guaranteed to be symmetric.
   */
  public interface SymmetricAccess extends
          Matrix.ColumnReadAccess,
          Matrix.RowReadAccess,
          Matrix.ScalarOperations,
          Matrix.ElementwiseOperations
  {
  }

  /**
   * Base type for upper and lower triangular matrices.
   */
  public interface Triangular extends Matrix.DivideOperation
  {
    boolean isUpper();
  }

//</editor-fold>
}
