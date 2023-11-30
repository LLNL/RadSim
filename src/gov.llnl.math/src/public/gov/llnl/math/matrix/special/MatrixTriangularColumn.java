/*
 * Copyright 2016, Lawrence Livermore National Security, LLC. 
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.math.matrix.special;

import gov.llnl.math.MathExceptions;
import gov.llnl.math.MathExceptions.ResizeException;
import gov.llnl.math.MathExceptions.WriteAccessException;
import gov.llnl.math.matrix.Matrix;
import gov.llnl.math.matrix.MatrixAssert;
import gov.llnl.math.matrix.MatrixRowOperations;
import gov.llnl.math.matrix.MatrixTableBase;
import gov.llnl.math.matrix.MatrixTriangularBase;
import gov.llnl.utility.UUIDUtilities;

/**
 *
 * @author nelson85
 */
public class MatrixTriangularColumn
        extends MatrixTriangularBase
        implements Matrix.ColumnReadAccess, Matrix.DivideOperation
{
  private static final long serialVersionUID
          = UUIDUtilities.createLong("MatrixTriangularColumn-v1");

//<editor-fold desc="ctor" defaultstate="collapsed">
  public MatrixTriangularColumn()
  {
    super(new double[0][], 0);
  }

  public MatrixTriangularColumn(int rows, int columns, boolean upper)
  {
    super(new double[columns][], rows);
    this.upper = upper;
    for (int i = 0; i < data.length; ++i)
      data[i] = new double[columns];
  }

  public MatrixTriangularColumn(double[][] data, boolean upper)
  {
    super(data, data.length);
    this.upper = upper;
  }

  /**
   * Copy constructor.
   *
   * @param matrix
   */
  public MatrixTriangularColumn(Matrix matrix)
  {
    super(new double[matrix.columns()][], matrix.rows());
    this.upper = MatrixAssert.assertTriangular(matrix);
    for (int i = 0; i < data.length; ++i)
      data[i] = matrix.copyColumnTo(new double[dim], 0, i);
  }

//</editor-fold>
//<editor-fold desc="basic" defaultstate="collapsed">
  @Override
  final public boolean isUpper()
  {
    return upper;
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
   * @param row is the row of the element.
   * @param column is the colum of the element.
   * @param scalar is the value to set.
   * @throws IndexOutOfBoundsException if the matrix element does not exist.
   * @throws WriteAccessException if the matrix is not writable.
   */
  @Override
  public void set(int row, int column, double scalar)
          throws WriteAccessException, IndexOutOfBoundsException
  {
    if (upper)
    {
      if (row > column && scalar != 0)
        throw new WriteAccessException("Write below diagonal");
      data[column][row] = scalar;
    }
    else
    {
      if (column > row && scalar != 0)
        throw new WriteAccessException("Write above diagonal");
      data[column][row] = scalar;
    }
  }

  /**
   * Get the contents of a matrix element. This implementation checks the size
   * of the array.
   *
   * @param row is the row of the element.
   * @param column is the colum of the element.
   * @return the contents of the element.
   * @throws IndexOutOfBoundsException if the matrix element does not exist.
   */
  @Override
  public double get(int row, int column)
          throws IndexOutOfBoundsException
  {
    return data[column][row];
  }

//</editor-fold>
//<editor-fold desc="assign/copy" defaultstate="collapsed">
  @Override
  public boolean resize(int rows, int columns) throws MathExceptions.ResizeException
  {
    if (rows == rows() && columns == columns())
      return false;
    super.allocate(columns, rows);
    return true;
  }

  @Override
  public Matrix assign(Matrix matrix)
          throws ResizeException, WriteAccessException
  {
    if (this == matrix)
      return this;
    this.upper = MatrixAssert.assertTriangular(matrix);
    return MatrixTableBase.assignColumnTable(this, matrix);
  }

  @Override
  public Matrix copyOf()
  {
    return new MatrixTriangularColumn(this);
  }

  @Override
  public double[] copyRowTo(double[] destination, int offset, int index)
          throws IndexOutOfBoundsException
  {
    return super.copyToIndirect(destination, offset, index);
  }

  @Override
  public double[] copyColumnTo(double[] destination, int offset, int index)
          throws IndexOutOfBoundsException
  {
    System.arraycopy(data[index], 0, destination, offset, dim);
    return destination;
  }

//</editor-fold>
//<editor-fold desc="column access" defaultstate="collapsed">
  @Override
  public double[] accessColumn(int index)
          throws IndexOutOfBoundsException
  {
    return data[index];
  }

  @Override
  public int addressColumn(int column)
  {
    return 0;
  }

//</editor-fold>
//<editor-fold desc="divide">  
  @Override
  public void divideLeft(MatrixRowOperations ro)
  {
    MatrixAssert.assertSquare(this);
    int n = this.rows();

    if (this.upper)
    {
      for (int i = 0; i < n; ++i)
      {
        int c = n - i - 1;
        double[] column = this.data[c];
        if (column[c] == 0)
          continue;
        ro.divideAssignRow(c, column[c]);
        for (int r = 0; r < c; ++r)
        {
          ro.addScaledRows(r, c, -column[r]);
        }
      }
    }
    else
    {
      for (int c = 0; c < n; ++c)
      {
        double[] column = this.data[c];
        if (column[c] == 0)
          continue;
        ro.divideAssignRow(c, column[c]);
        for (int r = c + 1; r < n; ++r)
        {
          ro.addScaledRows(r, c, -column[r]);
        }
      }
    }

    ro.apply();
  }
//</editor-fold>
}
