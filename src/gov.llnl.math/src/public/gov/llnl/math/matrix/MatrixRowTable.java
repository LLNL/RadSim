/*
 * Copyright 2016, Lawrence Livermore National Security, LLC. 
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.math.matrix;

import gov.llnl.math.MathAssert;
import gov.llnl.math.MathExceptions.ResizeException;
import gov.llnl.math.MathExceptions.WriteAccessException;
import static gov.llnl.math.matrix.MatrixAssert.assertNotViewResize;
import gov.llnl.utility.UUIDUtilities;

/**
 *
 * @author nelson85
 */
public class MatrixRowTable extends MatrixTableBase
        implements Matrix.RowAccess, Matrix.WriteAccess
{
  private static final long serialVersionUID
          = UUIDUtilities.createLong("MatrixRowTable-v1");

//<editor-fold desc="ctor" defaultstate="collapsed">
  public MatrixRowTable()
  {
    super(new double[0][], 0);
  }

  public MatrixRowTable(int rows, int columns)
  {
    super(new double[rows][columns], columns);
  }

  /**
   * Copy constructor.
   *
   * @param matrix
   */
  public MatrixRowTable(Matrix matrix)
  {
    super(matrix, new double[matrix.rows()][], matrix.columns());
    for (int i = 0; i < data.length; ++i)
      data[i] = matrix.copyRowTo(new double[dim], 0, i);
  }

  /**
   * Create a matrix as a view of origin.
   *
   * @param origin
   * @param values
   * @param columns
   */
  public MatrixRowTable(Object origin, double[][] values, int columns)
  {
    super(origin, values, columns);
  }

//</editor-fold>
//<editor-fold desc="basic" defaultstate="collapsed">
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
    return data.length;
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
    return data[r][c];
  }

//</editor-fold>
//<editor-fold desc="assign/copy" defaultstate="collapsed">
  @Override
  public boolean resize(int rows, int columns) throws ResizeException
  {
    if (rows == rows() && columns == columns())
      return false;
    assertNotViewResize(this);
    super.allocate(rows, columns);
    return true;
  }

  @Override
  public Matrix assign(Matrix matrix) throws ResizeException
  {
    return MatrixTableBase.assignRowTable(this, matrix);
  }

  @Override
  public void assignColumn(double[] in, int index)
  {
    // Indirect assignRowTable
    int i0 = 0;
    for (double[] v : this.data)
      v[index] = in[i0++];
  }

  @Override
  public void assignRow(double[] in, int index)
  {
    // Direct assignRowTable
    System.arraycopy(in, 0, data[index], 0, dim);
  }

  @Override
  public double[] copyColumnTo(double[] dest, int offset, int index)
  {
    return super.copyToIndirect(dest, offset, index);
  }

  @Override
  public double[] copyRowTo(double[] dest, int offset, int index)
  {
    // Direct copy
    System.arraycopy(data[index], 0, dest, offset, dim);
    return dest;
  }

  @Override
  public Matrix copyOf()
  {
    return new MatrixRowTable(this);
  }

//</editor-fold>
//<editor-fold desc="special" defaultstate="collapsed">
  public double[][] asRows()
  {
    return this.data;
  }
//</editor-fold>
  
    public double[][] toArray()
  {
    return this.data;
  }

    /**
   * Create a view from a list of vectors. 
   * 
   * All vectors must be the same length.
   *
   * @param A
   * @return
   * @throws IndexOutOfBoundsException
   */
  public static MatrixRowTable cat(double[]... A) throws IndexOutOfBoundsException
  {
    // Verify size
    int sz = A[0].length;
    for (double[] v : A)
    {
      MathAssert.assertLengthEqual(v, sz);
    }
    return new MatrixRowTable(A, A, A[0].length);
  }
}
