/*
 * Copyright 2016, Lawrence Livermore National Security, LLC. 
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package support;

import gov.llnl.math.MathExceptions.WriteAccessException;
import gov.llnl.math.matrix.Matrix;

/**
 *
 * @author nelson85
 */
strictfp public class MatrixTestReadOnly extends MatrixTestProxy
{
  public MatrixTestReadOnly(Matrix m)
  {
    super(m);
  }

  @Override
  public void set(int r, int c, double v) throws WriteAccessException
  {
    throw new WriteAccessException();
  }

  @Override
  public void mutable() throws WriteAccessException
  {
    throw new WriteAccessException();
  }

  @Override
  public void assignRow(double[] v, int r) throws WriteAccessException
  {
    throw new WriteAccessException();
  }

  @Override
  public void assignColumn(double[] v, int r) throws WriteAccessException
  {
    throw new WriteAccessException();
  }
}
