/*
 * Copyright 2022, Lawrence Livermore National Security, LLC.
 * All rights reserved.
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.math.interp;

import gov.llnl.math.Cursor;
import java.util.stream.DoubleStream;

/**
 *
 * @author nelson85
 */
class SingleLogLogInterp implements SingleInterpolator
{
  private final double[] xv;
  private final double[] yv;

  SingleLogLogInterp(double[] x, double[] y)
  {
    this.xv = DoubleStream.of(x).map(Math::log).toArray();
    this.yv = DoubleStream.of(y).map(Math::log).toArray();
  }

  @Override
  public SingleInterpolator.Evaluator get()
  {
    return new LogLogEvaluator();
  }

//<editor-fold desc="internal" defaultstate="collapsed">
  class LogLogEvaluator implements SingleInterpolator.Evaluator
  {
    private final Cursor cursor;

    LogLogEvaluator()
    {
      this.cursor = new Cursor(xv, 0, xv.length);
    }

    @Override
    public void seek(double x)
    {
      cursor.seek(Math.log(x));
    }

    @Override
    public double evaluate()
    {
      // FIXME define clip policies
      int index = cursor.getIndex();
      double fraction = cursor.getFraction();
      return Math.exp((1 - fraction) * yv[index]
              + fraction * yv[index + 1]);
    }
  }
//</editor-fold>

}
