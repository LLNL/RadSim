/*
 * Copyright 2016, Lawrence Livermore National Security, LLC. 
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.math.matrix.special;

import gov.llnl.math.DoubleArray;
import gov.llnl.math.MathExceptions;
import gov.llnl.math.MathExceptions.WriteAccessException;
import gov.llnl.math.matrix.Matrix;
import gov.llnl.math.matrix.MatrixAssert;
import gov.llnl.math.matrix.MatrixColumnTable;
import gov.llnl.math.matrix.MatrixFactory;
import gov.llnl.math.matrix.MatrixIterators;
import gov.llnl.math.matrix.MatrixOpMultiply;
import gov.llnl.math.matrix.MatrixOps;
import gov.llnl.math.matrix.MatrixRowTable;
import gov.llnl.math.matrix.MatrixTableBase;
import gov.llnl.utility.UUIDUtilities;

/**
 *
 * @author nelson85
 */
public class MatrixSymmetric extends MatrixTableBase
        implements Matrix.SymmetricAccess
{
  private static final long serialVersionUID
          = UUIDUtilities.createLong("MatrixSymmetric-v1");

//<editor-fold desc="ctor" defaultstate="collapsed">
  /**
   * Create a new symmetric matrix with zero size.
   */
  public MatrixSymmetric()
  {
    super(new double[0][], 0);
  }

  /**
   * Create a new symmetric matrix with a specified size.
   *
   * @param rows
   */
  public MatrixSymmetric(int rows)
  {
    super(new double[rows][], rows);
    for (int i = 0; i < data.length; ++i)
      data[i] = new double[rows];
  }

  /**
   * Create a copy of a symmetric matrix.
   *
   * @param matrix
   */
  public MatrixSymmetric(Matrix matrix)
  {
    super(new double[matrix.rows()][], matrix.rows());

    // Verify the contents are symmetric if it is not already guaranteed to be
    if (!(matrix instanceof MatrixSymmetric))
      MatrixAssert.assertSymmetric(matrix);

    // Use vector iterator to produce a copy
    MatrixIterators.VectorIterator iter;
    if (determinePolicy(matrix))
      iter = MatrixIterators.newColumnReadIterator(matrix);
    else
      iter = MatrixIterators.newRowReadIterator(matrix);
    while (iter.advance())
    {
      data[iter.index()] = DoubleArray.copyOfRange(iter.access(), iter.begin(), iter.end());
    }
  }

  protected MatrixSymmetric(double[][] data)
  {
    super(data, data.length);
  }

  /**
   * Create a symmetric matrix by taking A^t*A.
   *
   * @param A is a matrix that supports multiply and transpose.
   * @return a new symmetric matrix.
   */
  public static MatrixSymmetric multiplySelfInner(Matrix A)
  {
    // PENDING this does twice as much work as needed.  
    // We need special multiply operations for this purpose.
    boolean asColumn = determinePolicy(A);
    if (asColumn)
    {
      MatrixColumnTable R = new MatrixColumnTable();
      MatrixOpMultiply.multiplyColumnInner(R, A.transpose(), MatrixFactory.asColumnMatrix(A));
      return new MatrixSymmetric(R.asColumns());
    }
    else
    {
      MatrixRowTable R = new MatrixRowTable();
      MatrixOpMultiply.multiplyRowAccumulate(R, A.transpose(), MatrixFactory.asRowMatrix(A));
      return new MatrixSymmetric(R.asRows());
    }
  }

  /**
   * Create a symmetric matrix by taking A*A^t.
   *
   * @param A is a matrix that supports multiply and transpose.
   * @return a new symmetric matrix.
   */
  public static MatrixSymmetric multiplySelfOuter(Matrix A)
  {
    // PENDING this does twice as much work as needed.  
    // We need special multiply operations for this purpose.
    boolean asColumn = determinePolicy(A);
    if (asColumn)
    {
      MatrixColumnTable R = new MatrixColumnTable();
      MatrixOpMultiply.multiplyColumnAccumulate(R, MatrixFactory.asColumnMatrix(A), A.transpose());
      return new MatrixSymmetric(R.asColumns());
    }
    else
    {
      MatrixRowTable R = new MatrixRowTable();
      MatrixOpMultiply.multiplyRowInner(R, MatrixFactory.asRowMatrix(A), A.transpose());
       return new MatrixSymmetric(R.asRows());
    }
  }

//</editor-fold>
//<editor-fold desc="basic" defaultstate="collapsed">
  @Override
  public double[] accessColumn(int c)
  {
    return data[c];
  }

  @Override
  public int addressColumn(int c)
  {
    return 0;
  }

  @Override
  public double[] accessRow(int r)
  {
    return data[r];
  }

  @Override
  public int addressRow(int r)
  {
    return 0;
  }

  @Override
  public int rows()
  {
    return dim;
  }

  @Override
  public int columns()
  {
    return dim;
  }

  /**
   * Set the contents of a matrix element. This implementation checks the size
   * of the array.
   *
   * @param r is the row of the element.
   * @param c is the colum of the element.
   * @param v is the value to set.
   * @throws IndexOutOfBoundsException if the matrix element does not exist.
   * @throws WriteAccessException if the matrix is not writable.
   */
  @Override
  public void set(int r, int c, double v)
          throws IndexOutOfBoundsException, WriteAccessException
  {
    data[c][r] = v;
    data[r][c] = v;
  }

  /**
   * Get the contents of a matrix element. This implementation checks the size
   * of the array.
   *
   * @param r is the row of the element.
   * @param c is the colum of the element.
   * @return the contents of the element.
   * @throws IndexOutOfBoundsException if the matrix element does not exist.
   */
  @Override
  public double get(int r, int c)
          throws IndexOutOfBoundsException
  {
    return data[c][r];
  }

  @Override
  final public void mutable() throws WriteAccessException
  {
    throw new WriteAccessException("Symmetric matrices cannot be altered directly.");
  }

//</editor-fold>
//<editor-fold desc="assign/copy" defaultstate="collapsed">
  @Override
  public boolean resize(int rows, int columns) throws MathExceptions.ResizeException
  {
    MatrixAssert.assertResizeSquare(rows, columns);
    // Short cut if no change
    if (rows == rows() && columns == columns())
      return false;

    // Allocate fresh memory
    this.dim = rows;
    data = new double[columns][];
    for (int c = 0; c < columns; ++c)
      data[c] = new double[rows];
    return true;
  }

  @Override
  public Matrix assign(Matrix matrix) throws MathExceptions.ResizeException
  {
    // Check for self assignment
    if (matrix == this)
      return this;

    if (!(matrix instanceof MatrixSymmetric))
      MatrixAssert.assertSymmetric(matrix);

    // Check for assignment from view of self
    if (sync() == matrix.sync())
      matrix = matrix.copyOf();

    // SymmetricAccess matrix are never views
    resize(matrix.rows(), matrix.columns());

    // Copy data into view (can copy from either access policy)
    MatrixIterators.VectorIterator iter;
    if (determinePolicy(matrix))
      iter = MatrixIterators.newColumnReadIterator(matrix);
    else
      iter = MatrixIterators.newRowReadIterator(matrix);
    while (iter.advance())
    {
      System.arraycopy(iter.access(), iter.begin(),
              data[iter.index()], 0,
              dim);
    }
    return this;
  }

  @Override
  public double[] copyRowTo(double[] dest, int offset, int index)
  {
    System.arraycopy(data[index], 0, dest, offset, dim);
    return dest;
  }

  @Override
  public double[] copyColumnTo(double[] dest, int offset, int index)
  {
    System.arraycopy(data[index], 0, dest, offset, dim);
    return dest;
  }

  @Override
  public Matrix copyOf()
  {
    return new MatrixSymmetric(this);
  }

//</editor-fold>
//<editor-fold desc="special" defaultstate="collapsed">
  @Override
  public Matrix transpose()
  {
    return this;
  }

  private static boolean determinePolicy(Matrix A)
  {
    if (A instanceof ColumnAccess)
      return true;
    if (A instanceof RowAccess)
      return false;
    return A.rows() >= A.columns();
  }

  @Override
  public Matrix addAssign(double scalar)
  {
    MatrixOps.addAssign(viewAs(null), scalar);
    return this;
  }

  @Override
  public Matrix divideAssign(double scalar)
  {
    MatrixOps.divideAssign(viewAs(null), scalar);
    return this;
  }

  @Override
  public MatrixSymmetric multiplyAssign(double scalar)
  {
    MatrixOps.multiplyAssign(viewAs(null), scalar);
    return this;
  }

  @Override
  public MatrixSymmetric addAssign(Matrix matrix)
  {
    if (!(matrix instanceof SymmetricAccess))
      MatrixAssert.assertSymmetric(matrix);
    MatrixOps.addAssign(viewAs(matrix), matrix);
    return this;
  }

  @Override
  public MatrixSymmetric multiplyAssignElements(Matrix matrix)
  {
    if (!(matrix instanceof SymmetricAccess))
      MatrixAssert.assertSymmetric(matrix);
    MatrixOps.multiplyAssignElements(viewAs(matrix), matrix);
    return this;
  }

  /**
   * Produce a view of this symmetric matrix suitable for use by MatrixOps. In
   * order to preserve the symmetric properties of the matrix, we must implement
   * special versions of operations so we can verify the resulting matrix will
   * be symmetric. To avoid unnecessary reimplementation, we will create
   * temporary view of the symmetric matrix for use with the default
   * implementations.
   *
   * @param m
   * @return a view of this matrix in a table form.
   */
  private Matrix viewAs(Matrix m)
  {
    if (m == null || MatrixOps.determineVectorAccess1(m))
      return new MatrixColumnTable(this, this.data, dim);
    else
      return new MatrixRowTable(this, this.data, dim);
  }

//</editor-fold>
}
