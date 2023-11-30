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
public class MatrixRowArray extends MatrixArrayBase
        implements Matrix.RowAccess, Matrix.WriteAccess
{
  private static final long serialVersionUID = UUIDUtilities.createLong("MatrixRowArray");
//<editor-fold desc="ctor" defaultstate="collapsed">

  public MatrixRowArray()
  {
    super(0, 0);
  }

  public MatrixRowArray(int rows, int columns)
  {
    super(rows, columns);
  }

  /**
   * Copy constructor.
   *
   * @param matrix
   */
  public MatrixRowArray(Matrix matrix)
  {
    super(matrix.rows(), matrix.columns());
    MatrixIterators.VectorIterator iter = MatrixIterators.newRowReadIterator(matrix);
    int i0 = 0;
    while (iter.advance())
    {
      System.arraycopy(iter.access(), iter.begin(), data, i0, columns);
      i0 += columns;
    }
  }

  /**
   * Copy constructor.
   *
   * @param matrix
   */
  public MatrixRowArray(MatrixRowArray matrix)
  {
    super(DoubleArray.copyOf(matrix.data), matrix.rows(), matrix.columns());
  }

  public MatrixRowArray(double[] data, int rows, int columns)
          throws SizeException
  {
    super(DoubleArray.copyOf(data), rows, columns);
    if (rows * columns > data.length)
      throw new SizeException();
  }

  public MatrixRowArray(Object origin, double[] values, int rows, int columns) throws SizeException
  {
    super(origin, values, rows, columns);
  }

//</editor-fold>
//<editor-fold desc="basic" defaultstate="collapsed">
  @Override
  public double[] accessRow(int r)
  {
    return data;
  }

  @Override
  public int addressRow(int r)
  {
    return r * columns;
  }

  @Override
  public void set(int r, int c, double v)
  {
    data[c + r * columns] = v;
  }

  @Override
  public double get(int r, int c)
  {
    return data[c + r * columns];
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
    MatrixIterators.VectorIterator iter = MatrixIterators.newRowReadIterator(matrix);
    int i0 = 0;
    while (iter.advance())
    {
      System.arraycopy(iter.access(), iter.begin(),
              data, i0,
              columns);
      i0 += columns;
    }
    return this;
  }

  @Override
  public void assignColumn(double[] in, int index)
  {
    // Indirect assign
    for (int i0 = 0; i0 < rows; ++i0, index += columns)
      data[index] = in[i0];
  }

  @Override
  public void assignRow(double[] in, int index)
  {
    // Direct assign
    System.arraycopy(in, 0, data, index * columns, columns);
  }

  @Override
  public double[] copyColumnTo(double[] dest, int offset, int index)
  {
    // Indirect copy
    for (int i0 = 0; i0 < rows; ++i0, index += columns, ++offset)
      dest[offset] = data[index];
    return dest;
  }

  @Override
  public double[] copyRowTo(double[] dest, int offset, int index)
  {
    // Direct copy
    System.arraycopy(data, index * columns, dest, offset, columns);
    return dest;
  }

  @Override
  public Matrix copyOf()
  {
    return new MatrixRowArray(this);
  }
  
//</editor-fold>
}
