/*
 * Copyright 2016, Lawrence Livermore National Security, LLC. 
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.math.spline;

import gov.llnl.math.matrix.special.MatrixTriDiagonal;
import java.util.ArrayList;
import java.util.Collections;

/**
 *
 * @author nelson85
 */
class CubicSplineBuilderImpl implements CubicHermiteSpline.CubicSplineBuilder
{
  ArrayList<ControlConstraint> constraints = new ArrayList<>();

  @Override
  public void addControl(double x, double y)
  {
    constraints.add(new ControlConstraint(x, y, 0, false));
  }

  @Override
  public void addControl(double x, double y, double m)
  {
    constraints.add(new ControlConstraint(x, y, m, true));
  }

  @Override
  public CubicHermiteSpline construct()
  {
    // Sort the control points
    Collections.sort(constraints, (ControlConstraint c1, ControlConstraint c2) -> Double.compare(c1.x, c2.x));
    int n = constraints.size();
    double[] dy = new double[n - 1];
    double[] dx = new double[n - 1];
    double[] d = new double[n];
    double[] l0 = new double[n - 1];
    double[] l1 = new double[n - 1];
    double[] q = new double[n];
    for (int i = 0; i < n - 1; ++i)
    {
      dx[i] = constraints.get(i + 1).x - constraints.get(i).x;
      dy[i] = constraints.get(i + 1).y - constraints.get(i).y;
    }
    // Slope constraining equations for bound and unbound points
    double u0 = 0;
    double v0 = 0;
    for (int i = 0; i < n; ++i)
    {
      double u1 = 0;
      double v1 = 0;
      if (i < n - 1)
      {
        u1 = 1 / dx[i];
        v1 = dy[i] * u1 * u1;
      }
      ControlConstraint c1 = constraints.get(i);
      if (c1.constrainedSlope)
      {
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
/*    for (int i = 0; i < n - 1; i++)
    {
      double f = l0[i] / d[i];
      d[i + 1] -= f * l0[i];
      q[i + 1] -= f * q[i];
    }
    // Back substitution
    q[n - 1] /= d[n - 1];
    for (int i = 1; i < n; i++)
    {
      q[n - i - 1] = (q[n - i - 1] - q[n - i] * l1[n - i - 1]) / d[n - i - 1];
    }
*/
    MatrixTriDiagonal td = MatrixTriDiagonal.wrap(l0, d, l1);
    double[] m = td.divideLeft(q);
    
    double[] x= new double[n];
    double[] y= new double[n];
    for (int i = 0; i < n; ++i)
    {
      if (Double.isNaN(q[i]))
      {
        throw new RuntimeException("NaN");
      }
      ControlConstraint c1 = constraints.get(i);
      x[i]=c1.x;
      y[i]=c1.y;
    }
    
    return CubicHermiteSplineFactory.create(x,y,m);
  }
  
}
//</editor-fold>
