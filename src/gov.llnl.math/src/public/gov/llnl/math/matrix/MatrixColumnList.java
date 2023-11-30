/*
 * Copyright 2016, Lawrence Livermore National Security, LLC. 
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.math.matrix;

import gov.llnl.math.MathExceptions.ResizeException;
import static gov.llnl.math.matrix.MatrixAssert.assertNotViewResize;
import static gov.llnl.math.matrix.MatrixAssert.assertViewResize;
import gov.llnl.utility.UUIDUtilities;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author nelson85
 */
public class MatrixColumnList extends MatrixListBase
        implements Matrix.ColumnAccess, Matrix.WriteAccess
{
  private static final long serialVersionUID = UUIDUtilities.createLong("MatrixColumnList");
//<editor-fold desc="ctor" defaultstate="collapsed">

  public MatrixColumnList()
  {
    super(new ArrayList<Vector>(), 0);
  }

  public MatrixColumnList(Matrix matrix)
  {
    super(new ArrayList<Vector>(), matrix.rows());
    ArrayList<Vector> list = (ArrayList<Vector>) data;
    int columns = matrix.columns();
    list.ensureCapacity(columns);
    for (int i = 0; i < columns; ++i)
      list.add(new Vector(matrix.copyColumnTo(new double[dim], 0, i), 0));
  }

  /**
   * Create a matrix as a view of origin.
   *
   * @param origin
   * @param values
   * @param rows
   */
  public MatrixColumnList(Object origin, List<Vector> values, int rows)
  {
    super(origin, values, rows);
  }

//</editor-fold>
//<editor-fold desc="basic" defaultstate="collapsed">
  @Override
  public double[] accessColumn(int c)
  {
    return data.get(c).values;
  }

  @Override
  public int addressColumn(int c)
  {
    return data.get(c).offset;
  }

  @Override
  public int rows()
  {
    return dim;
  }

  @Override
  public int columns()
  {
    return data.size();
  }

  @Override
  public void set(int r, int c, double v)
  {
    Vector vec = data.get(c);
    vec.values[r + vec.offset] = v;
  }

  @Override
  public double get(int r, int c)
  {
    Vector vec = data.get(c);
    return vec.values[r + vec.offset];
  }

//</editor-fold>
//<editor-fold desc="assign/copy" defaultstate="collapsed">
  @Override
  public boolean resize(int rows, int columns) throws ResizeException
  {
    // Short cut if no change
    if (rows == rows() && columns == columns())
      return false;

    assertNotViewResize(this);

    // Allocate fresh memory
    this.dim = rows;
    ArrayList<Vector> list = new ArrayList<>();
    list.ensureCapacity(columns);
    for (int i = 0; i < columns; ++i)
      list.add(new Vector(new double[rows], 0));
    data = list;
    return true;
  }

  @Override
  public Matrix assign(Matrix matrix) throws ResizeException
  {
    // Check for self assignment
    if (matrix == this)
      return this;

    // Check for assignment from view of self
    if (sync() == matrix.sync())
      matrix = matrix.copyOf();

    // Resize if not a view
    if (sync() == this)
    {
      if (rows() != matrix.rows() || columns() != matrix.columns())
      {
        data.clear();
        this.dim = matrix.rows();
        int columns = matrix.columns();
        for (int i0 = 0; i0 < columns; ++i0)
          this.data.add(new Vector(matrix.copyColumnTo(new double[columns], 0, i0), 0));
        return this;
      }
    }
    else
    {
      // Verify size of view 
      assertViewResize(this, matrix.rows(), matrix.columns());
    }

    // Copy data into view
    MatrixIterators.VectorIterator iter = MatrixIterators.newColumnReadIterator(matrix);
    while (iter.advance())
    {
      Vector v = this.data.get(iter.index());
      System.arraycopy(iter.access(), iter.begin(),
              v.values, v.offset,
              dim);
    }
    return this;
  }

  @Override
  public void assignRow(double[] in, int index)
  {
    // Indirect assign
    int i0 = 0;
    for (Vector v : data)
      v.values[index + v.offset] = in[i0++];
  }

  @Override
  public void assignColumn(double[] in, int index)
  {
    // Direct assign
    Vector vec = data.get(index);
    System.arraycopy(in, 0, vec.values, vec.offset, dim);
  }

  @Override
  public double[] copyRowTo(double[] dest, int offset, int index)
  {
    // Indirect copy
    int i0 = offset;
    for (Vector v : data)
      dest[i0++] = v.values[index + v.offset];
    return dest;
  }

  @Override
  public double[] copyColumnTo(double[] dest, int offset, int index)
  {
    // Direct copy
    Vector vec = data.get(index);
    System.arraycopy(vec.values, vec.offset, dest, offset, dim);
    return dest;
  }

  @Override
  public Matrix copyOf()
  {
    return new MatrixColumnList(this);
  }

//</editor-fold>
}
