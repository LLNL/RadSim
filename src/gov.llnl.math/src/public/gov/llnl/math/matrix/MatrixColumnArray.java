/*
 * Copyright 2016, Lawrence Livermore National Security, LLC.
 * All rights reserved
 *
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.math.matrix;

import gov.llnl.math.DoubleArray;
import gov.llnl.math.MathExceptions.ResizeException;
import gov.llnl.math.MathExceptions.SizeException;
import gov.llnl.math.MathExceptions.WriteAccessException;
import gov.llnl.utility.UUIDUtilities;

/**
 *
 * @author nelson85
 */
public class MatrixColumnArray extends MatrixArrayBase
        implements Matrix.ColumnAccess, Matrix.WriteAccess
{
  private static final long serialVersionUID = UUIDUtilities.createLong("MatrixColumnArray");
//<editor-fold desc="ctor" defaultstate="collapsed">

  public MatrixColumnArray()
  {
    super(0, 0);
  }

  public MatrixColumnArray(int rows, int columns)
  {
    super(rows, columns);
  }

  /**
   * Copy constructor.
   *
   * Clones the data.
   *
   * @param matrix
   */
  public MatrixColumnArray(Matrix matrix)
  {
    super(matrix.rows(), matrix.columns());
    MatrixIterators.VectorIterator iter = MatrixIterators.newColumnReadIterator(matrix);
    int i0 = 0;
    while (iter.advance())
    {
      System.arraycopy(iter.access(), iter.begin(), data, i0, rows);
      i0 += rows;
    }
  }

  /**
   * Copy constructor.
   *
   * @param matrix
   */
  public MatrixColumnArray(MatrixColumnArray matrix)
  {
    super(DoubleArray.copyOf(matrix.data), matrix.rows(), matrix.columns());
  }

  public MatrixColumnArray(double[] data, int rows, int columns)
          throws SizeException
  {
    super(DoubleArray.copyOf(data), rows, columns);
    if (rows * columns > data.length)
      throw new SizeException();
  }

  public MatrixColumnArray(Object origin, double[] values, int rows, int columns) throws SizeException
  {
    super(origin, values, rows, columns);
  }

//</editor-fold>
//<editor-fold desc="basic" defaultstate="collapsed">
  @Override
  public double[] accessColumn(int c)
  {
    return data;
  }

  @Override
  public int addressColumn(int c)
  {
    return c * rows;
  }

  @Override
  public void set(int r, int c, double v)
  {
    data[r + c * rows] = v;
  }

  @Override
  public double get(int r, int c)
  {
    return data[r + c * rows];
  }

  @Override
  public void mutable() throws WriteAccessException
  {
  }

//</editor-fold>
//<editor-fold desc="assign/copy" defaultstate="collapsed">
  @Override
  public Matrix assign(Matrix matrix) throws ResizeException
  {
    // Check for self assignment
    if (matrix == this)
      return this;

    // Check for assignment from view of self
    if (sync() == matrix.sync())
      matrix = matrix.copyOf();

    this.resize(matrix.rows(), matrix.columns());

    // Copy data into view
    MatrixIterators.VectorIterator iter = MatrixIterators.newColumnReadIterator(matrix);
    int i0 = 0;
    while (iter.advance())
    {
      System.arraycopy(iter.access(), iter.begin(),
              data, i0,
              rows);
      i0 += rows;
    }
    return this;
  }

  @Override
  public void assignRow(double[] in, int index)
  {
    // Indirect assign
    for (int i0 = 0; i0 < columns; ++i0, index += rows)
      data[index] = in[i0];
  }

  @Override
  public void assignColumn(double[] in, int index)
  {
    // Direct assign
    System.arraycopy(in, 0, data, index * rows, rows);
  }

  @Override
  public double[] copyRowTo(double[] dest, int offset, int index)
  {
    // Indirect copy
    for (int i0 = 0; i0 < columns; ++i0, index += rows, ++offset)
      dest[offset] = data[index];
    return dest;
  }

  @Override
  public double[] copyColumnTo(double[] dest, int offset, int index)
  {
    // Direct copy
    System.arraycopy(data, index * rows, dest, offset, rows);
    return dest;
  }

  @Override
  public MatrixColumnArray copyOf()
  {
    return new MatrixColumnArray(this);
  }

//</editor-fold>
}
