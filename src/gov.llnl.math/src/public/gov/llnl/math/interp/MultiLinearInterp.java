/*
 * Copyright 2022, Lawrence Livermore National Security, LLC.
 * All rights reserved.
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.math.interp;

import gov.llnl.math.Cursor;

/**
 * Implementation of linear interpolator.
 *
 * This is used if we have many functions with the same x axis and all functions
 * should be interpolated using linear interpolation. This is a private
 * implementation as the user should not care about the method of interpolation
 * being used.
 *
 * @author nelson85
 */
class MultiLinearInterp implements MultiInterpolator
{

  private final double[] xv;
  private final double[][] yv;

  MultiLinearInterp(double[] x, double[] 
  
    ... y)
  {
    this.xv = x;
    this.yv = y;
  }

  @Override
  public Evaluator get()
  {
    return new LinearEvaluator();
  }

//<editor-fold desc="internal" defaultstate="collapsed">
  class LinearEvaluator implements MultiInterpolator.Evaluator
  {

    Cursor cursor;

    LinearEvaluator()
    {
      this.cursor = new Cursor(xv, 0, xv.length);
    }

    @Override
    public int size()
    {
      return yv.length;
    }

    @Override
    public void seek(double x)
    {
      cursor.seek(x);
    }

    @Override
    public double evaluate(int vindex)
    {
      // FIXME define clip policies
      double[] y = yv[vindex];
      int index = cursor.getIndex();
      double fraction = cursor.getFraction();
      return (1 - fraction) * y[index] + fraction * y[index + 1];
    }
  }
//</editor-fold>
}
