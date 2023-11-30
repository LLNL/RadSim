/*
 * Copyright 2016, Lawrence Livermore National Security, LLC. 
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.math.matrix;

import gov.llnl.math.IntegerArray;
import gov.llnl.math.MathExceptions;
import gov.llnl.utility.UUIDUtilities;
import gov.llnl.utility.annotation.Internal;

/**
 * Row view for proxies. This is used to produce views when row cuts are
 * requested on a column access matrix.
 */
@Internal
class MatrixRowProxy extends MatrixProxy implements Matrix.SelectAccess, Matrix.WriteAccess
{
  private static final long serialVersionUID = UUIDUtilities.createLong("MatrixRowProxy");
  int[] selection;

  public MatrixRowProxy(Matrix matrix, int r)
  {
    super(matrix);
    this.selection = new int[]
    {
      r
    };
  }

  public MatrixRowProxy(Matrix matrix, int[] r)
  {
    super(matrix);
    this.selection = IntegerArray.copyOf(r);
  }

//<editor-fold desc="basic" defaultstate="collapsed">
  @Override
  public int rows()
  {
    return selection.length;
  }

  @Override
  public int columns()
  {
    return proxy.columns();
  }

  @Override
  public void set(int r, int c, double v) throws MathExceptions.WriteAccessException
  {
    proxy.set(selection[r], c, v);
  }

  @Override
  public double get(int r, int c)
  {
    return proxy.get(selection[r], c);
  }

//</editor-fold>
//<editor-fold desc="assign/copy" defaultstate="collapsed">
  @Override
  public Matrix assign(Matrix matrix) throws MathExceptions.ResizeException, MathExceptions.WriteAccessException
  {
    if (matrix.rows() != rows() || matrix.columns() != columns())
      throw new MathExceptions.ResizeException();
    int columns = proxy.columns();
    int rows = selection.length;
    for (int r = 0; r < rows; r++)
      for (int c = 0; c < columns; c++)
        proxy.set(selection[r], c, matrix.get(0, c));
    return this;
  }

  @Override
  public void assignRow(double[] in, int r) throws MathExceptions.WriteAccessException
  {
    if (!(proxy instanceof Matrix.WriteAccess))
      throw new MathExceptions.WriteAccessException();
    ((Matrix.WriteAccess) proxy).assignRow(in, selection[r]);
  }

  @Override
  public void assignColumn(double[] in, int c) throws MathExceptions.WriteAccessException
  {
    for (int i = 0; i < selection.length; ++i)
      proxy.set(selection[i], c, in[i]);
  }

  @Override
  public double[] copyRowTo(double[] dest, int offset, int r)
  {
    return proxy.copyRowTo(dest, offset, selection[r]);
  }

  @Override
  public double[] copyColumnTo(double[] dest, int offset, int index)
  {
    for (int i = 0; i < selection.length; ++i, ++offset)
      dest[offset] = proxy.get(selection[i], index);
    return dest;
  }

//</editor-fold>
//<editor-fold desc="views" defaultstate="collapsed">  
  @Override
  public Matrix selectColumn(int c)
  {
    if (c < 0 || c >= proxy.columns())
      throw new IndexOutOfBoundsException();
    return new MatrixColumnProxy(this, c);
  }

  @Override
  public Matrix selectColumns(int[] c)
  {
    return new MatrixColumnProxy(this, c);
  }

  @Override
  public Matrix selectRow(int r)
  {
    if (r < 0 || r >= selection.length)
      throw new IndexOutOfBoundsException();
    return new MatrixRowProxy(this.proxy, selection[r]);
  }

  @Override
  public Matrix selectRows(int[] r)
  {
    int[] r2 = new int[r.length];
    for (int i = 0; i < r.length; ++i)
      r2[i] = selection[r[i]];
    return new MatrixRowProxy(proxy, r2);
  }

//</editor-fold>
  @Override
  public boolean equals(Object o)
  {
    if (!(o instanceof Matrix))
      return false;
    return MatrixOps.equals(this, (Matrix) o);
  }
}
