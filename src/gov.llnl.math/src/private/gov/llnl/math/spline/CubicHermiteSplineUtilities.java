/*
 * Copyright 2016, Lawrence Livermore National Security, LLC.
 * All rights reserved
 *
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.math.spline;

import gov.llnl.math.matrix.special.MatrixTriDiagonal;
import gov.llnl.utility.CoreDump;
import gov.llnl.utility.annotation.Internal;

/**
 *
 * @author nelson85
 */
@Internal
public class CubicHermiteSplineUtilities
{
  //<editor-fold desc="internal">
  /**
   * Compute the slopes necessary to have a continuous slope with the least
   * deviations.
   *
   * For some reason this implementation and
   *
   * @param control
   * @param begin
   * @param end
   * @param pinBegin
   * @param pinEnd
   */
  public static void computeNaturalSlopes(CubicHermiteSpline.ControlPoint[] control,
          int begin, int end,
          boolean pinBegin, boolean pinEnd)
  {
    int n = end - begin;
    // Slope constraining equations for bound and unbound points
    double[] l0 = new double[n - 1];
    double[] l1 = new double[n - 1];
    double[] d = new double[n];
    double[] q = new double[n];
    double u0 = 0;
    double v0 = 0;
    for (int i = 0; i < n; ++i)
    {
      double dy = 0;
      double dx = 0;
      double u1 = 0;
      double v1 = 0;
      if (i < n - 1)
      {
        dy = control[i + 1 + begin].y - control[i + begin].y;
        dx = control[i + 1 + begin].x - control[i + begin].x;
        if (dx == 0)
          throw new RuntimeException("identical points in x");
        if (dx < 0)
          throw new RuntimeException("control points out of order");
        u1 = 1 / dx;
        v1 = dy * u1 * u1;
      }
      if ((i == 0 && pinBegin == true) || ((i == n - 1) && pinEnd == true))
      {
        CubicHermiteSpline.ControlPoint c1 = control[i + begin];
        d[i] = 1;
        q[i] = c1.m;
      }
      else
      {
        if (i > 0)
          l0[i - 1] = u0;
        d[i] = 2 * (u0 + u1);
        if (i < n - 1)
          l1[i] = u1;
        q[i] = 3 * (v0 + v1);
      }
      u0 = u1;
      v0 = v1;
    }

    // Solve for constant acceleration (natural spline)
    MatrixTriDiagonal td = MatrixTriDiagonal.wrap(l0, d, l1);
    double[] m = td.divideLeft(q);
    // Copy back to the slopes
    for (int i = 0, j = begin; j < end; ++i, ++j)
    {
      if (Double.isNaN(q[i]))
      {
        CoreDump dump = new CoreDump("gov.llnl.math.spline.CubicHermiteSplineFactory", false);
        dump.add("control", control);
        dump.add("begin", begin);
        dump.add("end", end);
        dump.add("pinBegin", pinBegin);
        dump.add("pinEnd", pinEnd);
        dump.write("CubicHermiteSpline");
        throw new RuntimeException("NaN");
      }

      control[i].m = m[i];
    }
  }

  /**
   * THIS ROUTINE IS BROKEN.
   *
   * @param control
   * @param i0
   * @param i1
   * @param del
   * @param pinned
   * @return
   */
  public static boolean fixMonotonic(CubicHermiteSpline.ControlPoint[] control, int i0, int i1, double[] del, int[] pinned)
  {
    // Find the worst segment
    int worst = 0;
    double mworst = 0;
    double[] monocheck = new double[control.length];
    for (int i = i0; i < i1 - 1; ++i)
    {
      CubicHermiteSpline.ControlPoint c0 = control[i];
      CubicHermiteSpline.ControlPoint c1 = control[i + 1];
      double mm = c0.m;
      if (c1.m < mm)
        mm = c1.m;
      double dx = c1.x - c0.x;
      double dy = c1.y - c0.y;
      double m0 = dx * c0.m;
      double m1 = dx * c1.m;
      double a = 3 * (m0 + m1) - 6 * dy;
      if (a != 0)
      {
        double b = -2 * (2 * m0 + m1) + 6 * dy;
        double c = m0;
        double xv = -b / 2 / a;
        double yv = -(b * b - 4 * a * c) / 4 / a;
        if (yv < 0 && xv > 0 && xv < 1)
        {
          // inflection in interval
          m1 = yv / dx;
          if (m1 < mm)
            mm = m1;
        }
      }
      monocheck[i] = mm;
      if (mm < mworst)
      {
        worst = i;
        mworst = mm;
      }
    }
    if (mworst >= 0)
      return true;
    int i = worst;

    // Fix this worst spot
    CubicHermiteSpline.ControlPoint c0 = control[i];
    CubicHermiteSpline.ControlPoint c1 = control[i + 1];
    double dx = c1.x - c0.x;
    double dy = c1.y - c0.y;
    double m0 = c0.m;
    double m1 = c1.m;
    double f = (9 * dy * dy - 6 * dx * dy * (m0 + m1) + dx * dx * (m0 * m0 + m0 * m1 + m1 * m1)) / (3 * dy * dy - 3 * dx * dy * (m0 + m1) + dx * dx * (m0 * m0 + m0 * m1 + m1 * m1));
    c0.m = (1 - f) * c0.m + f * dy / dx;
    c1.m = (1 - f) * c1.m + f * dy / dx;
    pinned[i] = 1;
    pinned[i + 1] = 1;
    return false;
  }

  public static double[] computeDeltaX(CubicHermiteSpline.ControlPoint[] control, int start, int end)
  {
    int n = end - start;
    double[] h = new double[n - 1];
    for (int i = 0, j = start; i < n - 1; ++i, ++j)
    {
      h[i] = control[j + 1].x - control[j].x;
    }
    return h;
  }

  public static double[] computeDeltaY(CubicHermiteSpline.ControlPoint[] control, int start, int end)
  {
    int n = end - start;
    double[] h = new double[n - 1];
    for (int i = 0, j = start; i < n - 1; ++i, ++j)
    {
      h[i] = control[j + 1].y - control[j].y;
    }
    return h;
  }

  public static void forceMonotonic(CubicHermiteSpline.ControlPoint[] control)
  {
    double c1 = control[0].y;
    for (int i = 0; i < control.length; ++i)
    {
      if (control[i].y < c1)
      {
        control[i].y = c1;
      }
      c1 = control[i].y;
    }
  }

  public static boolean verifyMonotonic(CubicHermiteSpline spline)
  {
    for (int i = 0; i < spline.control.length - 1; ++i)
    {
      CubicHermiteSpline.ControlPoint p0 = spline.control[i];
      CubicHermiteSpline.ControlPoint p1 = spline.control[i + 1];
      double h = p1.x - p0.x;
      double d = p1.y - p0.y;
      // Rule from https://en.wikipedia.org/wiki/Monotone_cubic_interpolation
      if ((h * p0.m) * (h * p0.m) + (h * p1.m) * (h * p1.m) > 9 * d * d)
        return false;
    }
    return true;
  }

}
