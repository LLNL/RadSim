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
public class MatrixColumnTable extends MatrixTableBase
        implements Matrix.ColumnAccess, Matrix.WriteAccess
{
  private static final long serialVersionUID
          = UUIDUtilities.createLong("MatrixColumnTable-v1");

//<editor-fold desc="ctor" defaultstate="collapsed">
  public MatrixColumnTable()
  {
    super(new double[0][], 0);
  }

  public MatrixColumnTable(int rows, int columns)
  {
    super(null, rows);
    super.allocate(columns, rows);
  }

  /**
   * Copy constructor.
   *
   * @param matrix
   */
  public MatrixColumnTable(Matrix matrix)
  {
    super(new double[matrix.columns()][], matrix.rows());
    for (int i = 0; i < data.length; ++i)
      data[i] = matrix.copyColumnTo(new double[dim], 0, i);
  }

  /**
   * Create a matrix as a view of origin.
   *
   * @param origin
   * @param values
   * @param rows
   */
  public MatrixColumnTable(Object origin, double[][] values, int rows)
  {
    super(origin, values, rows);
  }

  @Override
  public double[][] asColumns()
  {
    return this.data;
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
  public int rows()
  {
    return dim;
  }

  @Override
  public int columns()
  {
    return data.length;
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

//</editor-fold>
//<editor-fold desc="assign/copy" defaultstate="collapsed">
  @Override
  public boolean resize(int rows, int columns) throws ResizeException
  {
    if (rows == rows() && columns == columns())
      return false;

    assertNotViewResize(this);
    super.allocate(columns, rows);
    return true;
  }

  @Override
  public Matrix assign(Matrix matrix) throws ResizeException
  {
    return MatrixTableBase.assignColumnTable(this, matrix);
  }

  @Override
  public void assignRow(double[] in, int index)
  {
    // Indirect assignRowTable
    int i0 = 0;
    for (double[] v : this.data)
      v[index] = in[i0++];
  }

  @Override
  public void assignColumn(double[] in, int index)
  {
    // Direct assignRowTable
    System.arraycopy(in, 0, data[index], 0, dim);
  }

  @Override
  public double[] copyRowTo(double[] dest, int offset, int index)
  {
    return super.copyToIndirect(dest, offset, index);
  }

  @Override
  public double[] copyColumnTo(double[] dest, int offset, int index)
  {
    // Direct copy
    try
    {
      System.arraycopy(data[index], 0, dest, offset, dim);
      return dest;
    }
    catch (IndexOutOfBoundsException ex)
    {
      throw new IndexOutOfBoundsException("Size issue getting column " + index + " into dest[size="
              + dest.length + ",offset=" + offset + " from data[size=" + rows());
    }
  }

  @Override
  public Matrix copyOf()
  {
    return new MatrixColumnTable(this);
  }

//</editor-fold>
//<editor-fold desc="special" defaultstate="collapsed">
  public double[][] toArray()
  {
    return this.data;
  }

  /**
   * Create a view from a list of vectors. All vectors must be the same length.
   *
   * @param A
   * @return
   */
  public static MatrixColumnTable cat(double[]... A) throws IndexOutOfBoundsException
  {
    // Verify size
    int sz = A[0].length;
    for (double[] v : A)
    {
      MathAssert.assertLengthEqual(v, sz);
    }
    return new MatrixColumnTable(A, A, A[0].length);
  }
//</editor-fold>
}
