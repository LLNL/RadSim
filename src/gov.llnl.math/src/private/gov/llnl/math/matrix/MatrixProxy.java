/*
 * Copyright 2016, Lawrence Livermore National Security, LLC. 
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.math.matrix;

import gov.llnl.math.MathExceptions;
import gov.llnl.math.MathExceptions.ResizeException;
import static gov.llnl.math.matrix.MatrixAssert.assertViewResize;
import gov.llnl.utility.annotation.Internal;

/**
 * Base classes for proxies that provide views to a matrix. Used by MatrixView
 * methods.
 */
@Internal
abstract class MatrixProxy implements Matrix
{
  protected Matrix proxy;

  protected MatrixProxy(Matrix matrix)
  {
    this.proxy = matrix;
  }

//<editor-fold desc="basic" defaultstate="collapsed">
  @Override
  public void mutable() throws MathExceptions.WriteAccessException
  {
    proxy.mutable();
  }

  @Override
  public Object sync()
  {
    return this.proxy;
  }

//</editor-fold>  
//<editor-fold desc="assign/copy" defaultstate="collapsed">
  @Override
  public boolean resize(int rows, int columns) throws ResizeException
  {
    assertViewResize(this, rows, columns);
    return false;
  }

  @Override
  public Matrix copyOf()
  {
    if (rows() >= columns())
      return new MatrixColumnTable(this);
    return new MatrixRowTable(this);
  }

//</editor-fold>  
}
