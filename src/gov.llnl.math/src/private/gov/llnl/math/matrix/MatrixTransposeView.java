/*
 * Copyright 2016, Lawrence Livermore National Security, LLC. 
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.math.matrix;

import gov.llnl.math.MathExceptions;
import gov.llnl.math.MathExceptions.WriteAccessException;
import gov.llnl.utility.UUIDUtilities;
import gov.llnl.utility.annotation.Internal;

/**
 * TransposeOperation view implementation for all types.
 *
 * @param <Type>
 */
@Internal
class MatrixTransposeView<Type extends Matrix>
        implements Matrix.WriteAccess
{
    private static final long serialVersionUID = UUIDUtilities.createLong("MatrixTransposeView");
  Type proxy;

  /**
   * Create a transpose view of a matrix.
   *
   * @param matrix is the matrix to transpose.
   */
  public MatrixTransposeView(Type matrix)
  {
    this.proxy = matrix;
  }

//<editor-fold desc="basic" defaultstate="collapsed">
  @Override
  public int rows()
  {
    return proxy.columns();
  }

  @Override
  public int columns()
  {
    return proxy.rows();
  }

  @Override
  public void set(int r, int c, double v) throws WriteAccessException
  {
    proxy.set(c, r, v);
  }

  @Override
  public double get(int r, int c)
  {
    return proxy.get(c, r);
  }

  @Override
  public void mutable() throws MathExceptions.WriteAccessException
  {
    proxy.mutable();
  }

  @Override
  public Object sync()
  {
    return proxy.sync();
  }

//</editor-fold>
//<editor-fold desc="assign/copy" defaultstate="collapsed">  
  @Override
  public boolean resize(int rows, int columns) throws MathExceptions.ResizeException
  {
    return proxy.resize(columns, rows);
  }

  @Override
  public Matrix assign(Matrix matrix)
          throws MathExceptions.ResizeException, WriteAccessException
  {
    proxy.assign(matrix.transpose());
    return this;
  }

  @Override
  public void assignRow(double[] in, int r) throws WriteAccessException
  {
    if (!(proxy instanceof Matrix.WriteAccess))
      throw new WriteAccessException();
    ((Matrix.WriteAccess) proxy).assignColumn(in, r);
  }

  @Override
  public void assignColumn(double[] in, int c) throws WriteAccessException
  {
    if (!(proxy instanceof Matrix.WriteAccess))
      throw new WriteAccessException();
    ((Matrix.WriteAccess) proxy).assignRow(in, c);
  }

  @Override
  public double[] copyRowTo(double[] dest, int offset, int r)
  {
    return proxy.copyColumnTo(dest, offset, r);
  }

  @Override
  public double[] copyColumnTo(double[] dest, int offset, int c)
  {
    return proxy.copyRowTo(dest, offset, c);
  }
//</editor-fold>
//<editor-fold desc="views" defaultstate="collapsed">

  @Override
  public Matrix transpose()
  {
    return proxy;
  }

  @Override
  public Matrix copyOf()
  {
    return MatrixFactory.newMatrix(this);
  }

  static public class TransposeColumnAccess<Type extends Matrix.RowAccess>
          extends MatrixTransposeView<Type> implements ColumnAccess
  {
    public TransposeColumnAccess(Type matrix)
    {
      super(matrix);
    }

    @Override
    public double[] accessColumn(int c)
    {
      return proxy.accessRow(c);
    }

    @Override
    public int addressColumn(int c)
    {
      return proxy.addressRow(c);
    }
  }

  static public class TransposeRowAccess<Type extends Matrix.ColumnAccess>
          extends MatrixTransposeView<Type> implements RowAccess
  {
    public TransposeRowAccess(Type matrix)
    {
      super(matrix);
    }

    @Override
    public double[] accessRow(int c)
    {
      return proxy.accessColumn(c);
    }

    @Override
    public int addressRow(int r)
    {
      return proxy.addressColumn(r);
    }
  }
//</editor-fold>
}
