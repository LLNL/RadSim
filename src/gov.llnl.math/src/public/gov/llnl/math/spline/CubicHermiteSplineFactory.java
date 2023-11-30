/*
 * Copyright 2016, Lawrence Livermore National Security, LLC. 
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.math.spline;

import gov.llnl.math.MathAssert;

/**
 *
 * @author nelson85
 */
public class CubicHermiteSplineFactory
{

  public static CubicHermiteSpline create(double[] x, double[] y, double[] m)
  {
    if (x.length != y.length || m.length != x.length)
      throw new IllegalArgumentException("Size mismatch " + x.length + " " + y.length + "  " + m.length);
    CubicHermiteSpline chs = new CubicHermiteSpline(x.length);
    for (int i = 0; i < x.length; i++)
      chs.control[i] = new CubicHermiteSpline.ControlPoint(x[i], y[i], m[i]);
    return chs;
  }

  public static CubicHermiteSpline createNatural(double[] x, double[] y)
  {
    MathAssert.assertNotNaN(x);
    MathAssert.assertNotNaN(y);
    CubicHermiteSpline out = new CubicHermiteSpline(x, y);
    int n = out.control.length;
    if (n < 3)
      return out;
    out.control[0].m = 1;
    out.control[out.control.length - 1].m = 1;
    CubicHermiteSplineUtilities.computeNaturalSlopes(out.control, 0, n, false, false);
    return out;
  }

  /** THIS ROUTINE IS BROKEN.
   * 
   * @param x
   * @param y
   * @return 
   */
  public static CubicHermiteSpline createNaturalMonotonic(double[] x, double[] y)
  {
    CubicHermiteSpline out = new CubicHermiteSpline(x, y);
    int n = out.control.length;
    if (n < 3)
      return out;
    CubicHermiteSplineUtilities.forceMonotonic(out.control);
    double[] dx = CubicHermiteSplineUtilities.computeDeltaX(out.control, 0, n);
    double[] dy = CubicHermiteSplineUtilities.computeDeltaY(out.control, 0, n);
    double[] del = new double[n - 1];
    for (int i = 0; i < n - 1; i++)
    {
      del[i] = dy[i] / dx[i];
    }
    int[] pinned = new int[n];
    for (int i = 0; i < n - 1; ++i)
    {
      if (out.control[i + 1].y == out.control[i].y)
      {
        pinned[i] = 1;
        pinned[i + 1] = 1;
      }
    }
    int i0 = 0;
    int i1 = 0;
    while (true)
    {
      // Find the first unpinned region
      while (i0 < n && pinned[i0] == 1)
        i0++;
      if (i0 == n)
        break;
      i1 = i0;
      while (i1 < n && pinned[i1] == 0)
        i1++;
      if (i1 < n)
        i1++;
      if (i0 > 0)
        i0--;
      CubicHermiteSplineUtilities.computeNaturalSlopes(out.control, i0, i1, pinned[i0] == 1, pinned[i1 - 1] == 1);
      if (CubicHermiteSplineUtilities.fixMonotonic(out.control, i0, i1, del, pinned))
        i0 = i1;
    }
    return out;
  }

  public static CubicHermiteSpline createMonotonic(double[] x, double[] y)
  {
    // From the procedure on the Wiki,  not very good.
    CubicHermiteSpline out = new CubicHermiteSpline(x, y);
    int n = out.control.length;
    if (n < 3)
      return out;
    double[] m = new double[n];
    double[] dx = CubicHermiteSplineUtilities.computeDeltaX(out.control, 0, n);
    double[] dy = CubicHermiteSplineUtilities.computeDeltaY(out.control, 0, n);
    double[] del = new double[n - 1];
    for (int i = 0; i < n - 1; i++)
    {
      del[i] = dy[i] / dx[i];
    }
    for (int i = 1; i < n - 1; i++)
    {
      m[i] = (del[i - 1] + del[i]) / 2;
    }
    m[0] = del[0];
    m[n - 1] = del[n - 2];
    for (int i = 0; i < n - 1; i++)
    {
      if (out.control[i].y == out.control[i + 1].y)
      {
        m[i] = 0;
        m[i + 1] = 0;
        i++;
        continue;
      }
      double alpha = m[i] / del[i];
      double beta = m[i + 1] / del[i];
      double phi = alpha - (2 * alpha + beta - 3) * (2 * alpha + beta - 3) / 3 / (alpha + beta - 2);
      if (phi >= 0)
        continue;
      double tau = 3 / Math.sqrt(alpha * alpha + beta * beta);
      m[i] = tau * alpha * del[i];
      m[i + 1] = tau * beta * del[i];
    }
    for (int i = 0; i < n; i++)
      out.control[i].m = m[i];
    return out;
  }

  public static CubicHermiteSpline createMonotonic2(double[] x, double[] y)
  {
    CubicHermiteSpline out = new CubicHermiteSpline(x, y);
    int n = out.control.length;
    if (n < 3)
      return out;
    double[] dx = CubicHermiteSplineUtilities.computeDeltaX(out.control, 0, n);
    double[] dy = CubicHermiteSplineUtilities.computeDeltaY(out.control, 0, n);
    double[] del = new double[n];
    double[] c = new double[n];
    for (int i = 0; i < n - 1; i++)
    {
      del[i] = dy[i] / dx[i];
    }
    c[0] = del[0];
    for (int i = 0; i < n - 2; i++)
    {
      double m0 = del[i];
      double m1 = del[i + 1];
      if (m0 * m1 <= 0)
      {
        c[i + 1] = 0;
      }
      else
      {
        double dx0 = dx[i];
        double dx1 = dx[i + 1];
        double dx3 = dx0 + dx1;
        c[i + 1] = (3 * dx3 / ((dx3 + dx1) / m0 + (dx3 + dx0) / m1));
      }
    }
    c[out.control.length - 1] = del[del.length - 1];
    for (int i = 0; i < n; ++i)
      out.control[i].m = c[i];
    return out;
  }

}
