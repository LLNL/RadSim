/*
 * Copyright 2016, Lawrence Livermore National Security, LLC.
 * All rights reserved
 *
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.math.spline;

import gov.llnl.math.MathExceptions;
import gov.llnl.math.MathPackage;
import gov.llnl.utility.xml.bind.Reader;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author nelson85
 */
public class CubicAreaHermiteSpline extends Spline<CubicAreaHermiteSpline.ControlPoint>
{

  @Reader.Declaration(pkg = MathPackage.class, name = "cubicAreaControlPoint")
  public static class ControlPoint extends Spline.ControlPoint
  {
    public double dy;
    public double s;

    public ControlPoint()
    {
    }

    public ControlPoint(double x, double y)
    {
      this.x = x;
      this.y = y;
    }

  }
  public ControlPoint[] control;

  //<editor-fold defaultstate="collapsed" desc="Hermit Cubic Spline">
  static double H00(double t)
  {
    return ((t) * (t) * (2 * t - 3) + 1.0);
  }

  static double H10(double t)
  {
    return ((t) * (t - 1) * (t - 1));
  }

  static double H01(double t)
  {
    return ((t) * (t) * (3 - 2 * t));
  }

  static double H11(double t)
  {
    return ((t) * (t) * (t - 1));
  }

  static double D00(double t)
  {
    return 6 * t * (t - 1) + 1;
  }

  static double D10(double t)
  {
    return t * (3 * t - 4) + 1;
  }

  static double D01(double t)
  {
    return 6 * t * (1 - t);
  }

  static double D11(double t)
  {
    return (3 * t - 2) * t;
  }

// area integral between (0, u)
  static double K00(double t)
  {
    return (t * (1.0 - t * t * (1.0 - 0.5 * t)));
  }

  static double K10(double t)
  {
    return (t * t * (6.0 - t * (8.0 - 3 * t)) / 12.0);
  }

  static double K01(double t)
  {
    return ((1.0 - 0.5 * t) * t * t * t);
  }

  static double K11(double t)
  {
    return (t * t * t * (3 * t - 4.0) / 12.0);
  }

//</editor-fold>
  /**
   * Get number of control points
   *
   * @return the number of control points.
   */
  public int getNumControlPoints()
  {
    return control.length;
  }

  public void setControlX(int i, double value)
  {
    control[i].x = value;
  }

  public double getX(int i)
  {
    return control[i].x;
  }

  public void setControlY(int i, double value)
  {
    control[i].y = value;
  }

  public double getY(int i)
  {
    return control[i].y;
  }

  public void setControlDY(int i, double value)
  {
    control[i].dy = value;
  }

  public double getDY(int i)
  {
    return control[i].dy;
  }

  /**
   * set value of accumulated counts at control points
   *
   * @author yao
   * @param i
   * @param value
   */
  public void setControlS(int i, double value)
  {
    control[i].s = value;
  }

  public double getS(int i)
  {
    return control[i].s;
  }

  @Override
  public double applyAsDouble(double in) throws MathExceptions.DomainException
  {
    int i0 = 0;
    int N = control.length;

    if (N == 0)
    {
      return 0;
    }

    if (in <= control[0].x)
    {
      switch (this.endBehavior)
      {
        case CLAMP:
          return control[0].s;
        case LINEAR:
          return hermiteD(control[0].x, 0) * (in - control[0].x) + control[0].y;
        case CUBIC:
          return hermite(in, 0);
        default:
          throw new MathExceptions.DomainException("Outside domain");
      }
    }

    if (in >= control[N - 1].x)
    {
      switch (this.endBehavior)
      {
        case CLAMP:
          return control[N - 1].s;
        case LINEAR:
          return hermiteD(control[N - 1].x, N - 2) * (in - control[N - 1].x) + control[N - 1].y;
        case CUBIC:
          return hermite(in, N - 2);
        default:
          throw new MathExceptions.DomainException("Outside domain");
      }
    }

    // Linear search... TODO convert to binary search
    while (in > control[i0].x)
    {
      i0++;
    }
    i0--;

    //    left knot of the sub-bin "i" and slope
    double xL = this.getX(i0);
    double yL = this.getY(i0);
    double dyL = this.getDY(i0);

//     right knot of the sub-bin "i" and slope
    double xR = this.getX(i0 + 1);
    double yR = this.getY(i0 + 1);
    double dyR = this.getDY(i0 + 1);

    // width of sub-bin
    double dx = xR - xL;

    // parametric coordinate in x-direction
    double u = (in - control[i0].x) / dx;

    // Use interpolation on segment to compute area integral
    double f = dx * (yL * K00(u) + dyL * K10(u) + yR * K01(u) + dyR * K11(u));
    double s = control[i0].s + f;

    return s;
  }

  private double hermite(double in, int segment)
  {
    ControlPoint cp0 = control[segment];
    ControlPoint cp1 = control[segment + 1];
    double h = cp1.x - cp0.x;
    double u = (in - cp0.x) / h;
    double yL = cp0.y;
    double mL = cp0.dy;
    double yR = cp1.y;
    double mR = cp1.dy;
    return yL * H00(u) + mL * H10(u) + yR * H01(u) * mR * H11(u);
  }

  private double hermiteD(double in, int segment)
  {
    ControlPoint cp0 = control[segment];
    ControlPoint cp1 = control[segment + 1];
    double h = cp1.x - cp0.x;
    double u = (in - cp0.x) / h;
    double yL = cp0.y;
    double mL = cp0.dy;
    double yR = cp1.y;
    double mR = cp1.dy;
    return (yL * D00(u) + mL * D10(u) + yR * D01(u) * mR * D11(u)) / h;
  }

  public static CubicAreaHermiteSpline allocate(int numControlPoints)
  {
    CubicAreaHermiteSpline hs = new CubicAreaHermiteSpline();
    hs.control = new ControlPoint[numControlPoints];
    return hs;
  }

/// update sum of areas with new control points
  public void areaSumUpdate()
  {
    final double I00 = 0.5;
    final double I10 = 1.0 / 12.0;
    final double I01 = I00;
    final double I11 = -I10;

    setControlS(0, 0.0);

    // accumulated count at knots
    double s0 = 0.0;

    int numSubIntervals = getNumControlPoints() - 1;

    for (int i = 0; i < numSubIntervals; i++)
    {
      double yL = getY(i);
      double yR = getY(i + 1);

      double syL = getDY(i);
      double syR = getDY(i + 1);

      double dx = getX(i + 1) - getX(i);
      double area = (yL * I00 + syL * I10 + yR * I01 + syR * I11) * dx;
      s0 += area;
      setControlS(i + 1, s0);
    }
  }

  @Override
  public boolean contains(double d)
  {
    return (d >= control[0].x) && (d < control[control.length - 1].x);
  }

  @Override
  public double[] evaluateRangeOrdered(double[] in, int start, int end) throws MathExceptions.DomainException
  {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public List<ControlPoint> getControl()
  {
    return Arrays.asList(this.control);
  }

}
