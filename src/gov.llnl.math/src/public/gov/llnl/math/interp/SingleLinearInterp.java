/*
 * Copyright 2022, Lawrence Livermore National Security, LLC.
 * All rights reserved.
 *
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.math.interp;

import gov.llnl.math.Cursor;

/**
 *
 * @author nelson85
 */
class SingleLinearInterp implements SingleInterpolator
{
  final double[] xv;
  final double[] yv;

  SingleLinearInterp(double[] x, double[] y)
  {
    this.xv = x;
    this.yv = y;
  }

  @Override
  public SingleInterpolator.Evaluator get()
  {
    return new LinearEvaluator();
  }

//<editor-fold desc="internal" defaultstate="collapsed">
  class LinearEvaluator implements SingleInterpolator.Evaluator
  {
    private final Cursor cursor;

    LinearEvaluator()
    {
      this.cursor = new Cursor(xv, 0, xv.length);
    }

    @Override
    public void seek(double x)
    {
      cursor.seek(x);
    }

    @Override
    public double evaluate()
    {
      int index = cursor.getIndex();
      double fraction = cursor.getFraction();
      if (index==yv.length-1)
      {
        index--;
        fraction+=1;
      }
      return (1 - fraction) * yv[index] + fraction * yv[index + 1];
    }
  }
//</editor-fold>

}
