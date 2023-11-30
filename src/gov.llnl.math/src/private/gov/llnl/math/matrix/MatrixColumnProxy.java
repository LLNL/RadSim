/*
 * Copyright 2016, Lawrence Livermore National Security, LLC. 
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.math.matrix;

import gov.llnl.math.IntegerArray;
import gov.llnl.math.MathExceptions.ResizeException;
import gov.llnl.math.MathExceptions.WriteAccessException;
import gov.llnl.utility.UUIDUtilities;
import gov.llnl.utility.annotation.Internal;

/**
 * Column view for proxies. This is used to produce views when column cuts are
 * requested on a row access matrix.
 */
@Internal
class MatrixColumnProxy extends MatrixProxy
        implements Matrix.SelectAccess, Matrix.WriteAccess
{
  private static final long serialVersionUID = UUIDUtilities.createLong("MatrixColumnProxy");
  int[] selection;

//<editor-fold desc="ctor" defaultstate="collapsed">
  public MatrixColumnProxy(Matrix matrix, int c)
  {
    super(matrix);
    this.selection = new int[]
    {
      c
    };
  }

  public MatrixColumnProxy(Matrix matrix, int[] c)
  {
    super(matrix);
    this.selection = IntegerArray.copyOf(c);
  }

//</editor-fold>
//<editor-fold desc="basic" defaultstate="collapsed">
  @Override
  public int rows()
  {
    return proxy.rows();
  }

  @Override
  public int columns()
  {
    return selection.length;
  }

  @Override
  public void set(int r, int c, double v) throws WriteAccessException
  {
    proxy.set(r, selection[c], v);
  }

  @Override
  public double get(int r, int c)
  {
    return proxy.get(r, selection[c]);
  }

//</editor-fold>
//<editor-fold desc="assign/copy" defaultstate="collapsed">
  @Override
  public Matrix assign(Matrix matrix)
          throws ResizeException, WriteAccessException
  {
    if (matrix.rows() != rows() || matrix.columns() != columns())
      throw new ResizeException();
    int rows = rows();
    int columns = columns();
    for (int c = 0; c < columns; ++c)
      for (int r = 0; r < rows; ++r)
        proxy.set(r, selection[c], matrix.get(r, 0));
    return this;
  }

  @Override
  public void assignRow(double[] in, int r) throws WriteAccessException
  {
    for (int i = 0; i < selection.length; ++i)
      proxy.set(r, selection[i], in[i]);
  }

  @Override
  public void assignColumn(double[] in, int c) throws WriteAccessException
  {
    if (!(proxy instanceof Matrix.WriteAccess))
      throw new WriteAccessException();
    ((Matrix.WriteAccess) proxy).assignColumn(in, selection[c]);
  }

  @Override
  public double[] copyRowTo(double[] dest, int offset, int r)
  {
    for (int i = 0; i < selection.length; ++i, ++offset)
      dest[offset] = proxy.get(r, selection[i]);
    return dest;
  }

  @Override
  public double[] copyColumnTo(double[] dest, int offset, int index)
  {
    return proxy.copyColumnTo(dest, offset, selection[index]);
  }

//</editor-fold>
//<editor-fold desc="views" defaultstate="collapsed">  
  @Override
  public Matrix selectColumn(int c)
  {
    if (c < 0 || c >= selection.length)
      throw new IndexOutOfBoundsException();
    return new MatrixColumnProxy(proxy, selection[c]);
  }

  @Override
  public Matrix selectColumns(int[] c)
  {
    int[] c2 = new int[c.length];
    for (int i = 0; i < c.length; ++i)
      c2[i] = selection[c[i]];
    return new MatrixColumnProxy(proxy, c2);
  }

  @Override
  public Matrix selectRow(int r)
  {
    return new MatrixRowProxy(this, r);
  }

  @Override
  public Matrix selectRows(int[] r)
  {
    return new MatrixRowProxy(this, r);
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
