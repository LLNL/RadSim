/*
 * Copyright 2022, Lawrence Livermore National Security, LLC.
 * All rights reserved.
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.math.interp;

import gov.llnl.math.Cursor;
import static java.lang.Math.log;
import java.util.stream.DoubleStream;
import java.util.stream.Stream;

/**
 * Specialization for LogLog interpolation.
 *
 * @author nelson85
 */
public class MultiLogLogInterp implements MultiInterpolator
{

  private final double[] xv;
  private final double[][] yv;

  MultiLogLogInterp(double[] x, double[] 
  
    ... y)
  {
    this.xv = toLog(x);
    this.yv = toLog(y);
  }

  @Override
  public Evaluator get()
  {
    return new LogLogEvaluator();
  }

//<editor-fold desc="internal" defaultstate="collapsed">
  private static double[] toLog(double[] x)
  {
    return DoubleStream.of(x).map(Math::log).toArray();
  }

  private static double[][] toLog(double[][] x)
  {
    return Stream.of(x).map(MultiLogLogInterp::toLog).toArray(double[][]::new);
  }

  class LogLogEvaluator implements MultiInterpolator.Evaluator
  {
    private final Cursor cursor;

    LogLogEvaluator()
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
      x = log(x);
      cursor.seek(x);
    }

    @Override
    public double evaluate(int vindex)
    {
      // FIXME define clip policies
      double[] y = yv[vindex];
      int index = cursor.getIndex();
      double fraction = cursor.getFraction();
      return Math.exp((1 - fraction) * y[index]
              + fraction * y[index + 1]);
    }
  }
//</editor-fold>
}
