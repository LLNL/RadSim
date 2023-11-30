/*
 * Copyright 2016, Lawrence Livermore National Security, LLC. 
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.math.matrix;

import gov.llnl.math.MathExceptions.ResizeException;
import gov.llnl.math.MathExceptions.WriteAccessException;
import gov.llnl.utility.UUIDUtilities;
import gov.llnl.utility.annotation.Internal;

/**
 * Implementation of diagonal view for matrix. Called by MatrixViews.diag.
 */
@Internal
class MatrixDiagProxy extends MatrixProxy
        implements Matrix.SelectAccess, Matrix.WriteAccess
{
  private static final long serialVersionUID = UUIDUtilities.createLong("MatrixDiagProxy");
//<editor-fold desc="ctor" defaultstate="collapsedstate">

  public MatrixDiagProxy(Matrix matrix)
  {
    super(matrix);
  }

//</editor-fold>
//<editor-fold desc="basic" defaultstate="collapsedstate">
  @Override
  public int rows()
  {
    return Math.min(proxy.rows(), proxy.columns());
  }

  @Override
  public int columns()
  {
    return 1;
  }

  @Override
  public void set(int r, int c, double v) throws WriteAccessException
  {
    if (c != 0)
      throw new IndexOutOfBoundsException();
    proxy.set(r, r, v);
  }

  @Override
  public double get(int r, int c)
  {
    if (c != 0)
      throw new IndexOutOfBoundsException();
    return proxy.get(r, r);
  }

//</editor-fold>
//<editor-fold desc="assign/copy" defaultstate="collapsed">
  @Override
  public Matrix assign(Matrix matrix) throws ResizeException, WriteAccessException
  {
    int sz = rows();
    if (sz != matrix.rows() || 1 != matrix.columns())
      throw new ResizeException("diagonal cannot be resized");
    for (int i = 0; i < sz; i++)
      proxy.set(i, i, matrix.get(i, 0));
    return this;
  }

  @Override
  public Matrix copyOf()
  {
    return new MatrixColumnArray(this);
  }

  @Override
  public void assignRow(double[] in, int r) throws WriteAccessException
  {
    proxy.set(r, r, in[0]);
  }

  @Override
  public void assignColumn(double[] in, int c) throws WriteAccessException
  {
    int sz = rows();
    if (c != 0)
      throw new IndexOutOfBoundsException();
    for (int i = 0; i < sz; ++i)
      proxy.set(i, i, in[i]);
  }

  @Override
  public double[] copyRowTo(double[] dest, int offset, int r)
  {
    dest[offset] = proxy.get(r, r);
    return dest;
  }

  @Override
  public double[] copyColumnTo(double[] dest, int offset, int c)
  {
    int sz = rows();
    if (c != 0)
      throw new IndexOutOfBoundsException();
    for (int i = 0; i < sz; ++i, ++offset)
      dest[offset] = proxy.get(i, i);
    return dest;
  }

//</editor-fold>
//<editor-fold desc="views" defaultstate="collapsed">  
  @Override
  public Matrix selectColumn(int c)
  {
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
    return new MatrixRowProxy(this, r);
  }

  @Override
  public Matrix selectRows(int[] r)
  {
    return new MatrixRowProxy(this, r);
  }
//</editor-fold>  
}
