/*
 * Copyright 2016, Lawrence Livermore National Security, LLC.
 * All rights reserved
 *
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.math.spline;

import gov.llnl.math.DoubleArray;
import gov.llnl.math.matrix.Matrix;
import gov.llnl.math.matrix.MatrixFactory;
import gov.llnl.math.matrix.MatrixOps;
import gov.llnl.math.matrix.MatrixViews;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author nelson85
 */
public class SplineCalculator
{
  ArrayList<ControlPoint> controls = new ArrayList<>();
  ArrayList<IntegralConstraint> constraints = new ArrayList<>();

  public interface Constraint
  {
    public void setup(Working ws);

    public void bind(List<ControlPoint> controls);
  }

  public interface Control extends Constraint
  {
    Control accel(double c);

    Control position(double c);

    Control velocity(double c);

    Control c2(boolean b);

    Control c3(boolean b);

    default Control c2()
    {
      return Control.this.c2(true);
    }

    default Control c3()
    {
      return Control.this.c3(true);
    }
  }

  private class ControlPoint implements Control
  {
    private int index;
    public double x;
    public double y;
    public double v;
    public double a;
    public double i;

    boolean yFixed = false;
    boolean vFixed = false;
    boolean aFixed = false;
    boolean isC2 = false;
    boolean isC3 = false;


    @Override
    public Control position(double c)
    {
      y = c;
      yFixed = true;
      return this;
    }

    @Override
    public Control velocity(double c)
    {
      v = c;
      vFixed = true;
      return this;
    }

    @Override
    public Control accel(double c)
    {
      a = c;
      aFixed = true;
      return this;
    }

    @Override
    public Control c2(boolean b)
    {
      this.isC2 = b;
      return this;
    }

    @Override
    public Control c3(boolean b)
    {
      this.isC3 = b;
      return this;
    }

    int constraints()
    {
      int c = 0;
      if (yFixed)
        c++;
      if (vFixed)
        c++;
      if (aFixed)
        c++;
      if (isC2)
        c++;
      if (isC3)
        c++;
      return c;
    }

    @Override
    public void setup(Working ws)
    {
      if (yFixed)
      {
        double[] q = new double[ws.variables];
        q[index * 2] = 1;
        ws.add(q, y);
      }

      if (vFixed)
      {
        double[] q = new double[ws.variables];
        q[index * 2 + 1] = 1;
        ws.add(q, y);
      }

      if (isC2)
      {
        double[] h = ws.h;
        double[] q = new double[ws.variables];
        int k = index;
        q[k * 2 - 2] = 6 / h[k - 1] / h[k - 1];
        q[k * 2 - 1] = 2 / h[k - 1];
        q[k * 2 + 0] = -6 / h[k - 1] / h[k - 1] + 6 / h[k] / h[k];
        q[k * 2 + 1] = 4 / h[k - 1] + 4 / h[k];
        q[k * 2 + 2] = -6 / h[k] / h[k];
        q[k * 2 + 3] = 2 / h[k];
        ws.add(q, 0);
      }

      if (isC3)
      {
        double[] h = ws.h;
        double[] q = new double[ws.variables];
        int k = index;
        if (k == 0)
          throw new RuntimeException("End point violation");
        q[k * 2 - 2] = 12 / h[k - 1] / h[k - 1] / h[k - 1];
        q[k * 2 - 1] = 6 / h[k - 1] / h[k - 1];
        q[k * 2 + 0] = -12 / h[k - 1] / h[k - 1] / h[k - 1] - 12 / h[k] / h[k] / h[k];
        q[k * 2 + 1] = 6 / h[k - 1] / h[k - 1] - 6 / h[k] / h[k];
        q[k * 2 + 2] = 12 / h[k] / h[k] / h[k];
        q[k * 2 + 3] = -6 / h[k] / h[k];
        ws.add(q, 0);
      }

      if (aFixed)
      {
        int k = index;
        double[] h = ws.h;
        double[] q = new double[ws.variables];
        if (k == 0)
        {
          q[k * 2 + 0] = -6 / h[k] / h[k];
          q[k * 2 + 1] = -4 / h[k];
          q[k * 2 + 2] = 6 / h[k] / h[k];
          q[k * 2 + 3] = -2 / h[k];
          ws.add(q, a);
        }

        else if (k == ws.variables / 2 - 1)
        {
          q[k * 2 - 2] = -6 / h[k - 1] / h[k - 1];
          q[k * 2 - 1] = -2 / h[k - 1];
          q[k * 2 + 0] = 6 / h[k - 1] / h[k - 1];
          q[k * 2 + 1] = -4 / h[k - 1];
          ws.add(q, a);
        }
        else
        {
          throw new RuntimeException("Accel in inner points not support");
        }
      }
    }

    @Override
    public void bind(List<ControlPoint> controls)
    {
    }

    @Override
    public String toString()
    {
      StringBuilder sb = new StringBuilder();
      sb.append("x=").append(x);
      if (yFixed)
        sb.append(", y=").append(y);
      else if (y != 0)
        sb.append(", (y=").append(y).append(")");
      if (vFixed)
        sb.append(", v=").append(v);
      else if (v != 0)
        sb.append(", (v=").append(v).append(")");

      if (aFixed)
        sb.append(", a=").append(a);
      if (isC2)
        sb.append(", C2");
      if (isC3)
        sb.append(", C3");
      sb.append(" T=").append(constraints());
      return sb.toString();
    }

  }

  int findSegment(double x)
  {
    for (int i = 0; i < controls.size(); ++i)
    {
      if (x == controls.get(i).x)
        return i;
    }
    throw new RuntimeException("Unable to find control point");
  }

  public static class IntegralConstraint implements Constraint
  {
    double x1;
    double x2;
    ControlPoint left = null;
    ControlPoint right = null;
    double integral;
    private double min;
    private boolean disabled = false;
    private int index;

    private IntegralConstraint(double x1, double x2, double integral)
    {
      this.x1 = x1;
      this.x2 = x2;
      this.integral = integral;
    }

    @Override
    public void setup(Working ws)
    {
      if (disabled)
        return;
      if (left == null)
        this.bind(ws.controls);
      double[] q = new double[ws.variables];
      double[] h = ws.h;
      for (int i = left.index; i < right.index; ++i)
      {
        q[i * 2] += 0.5 * h[i];
        q[i * 2 + 1] += h[i] * h[i] / 12;
        q[i * 2 + 2] += 0.5 * h[i];
        q[i * 2 + 3] += -h[i] * h[i] / 12;
      }
      ws.add(q, integral);
    }

    @Override
    public void bind(List<ControlPoint> controls)
    {
      left = controls.stream().filter(p -> p.x == x1).findFirst().get();
      right = controls.stream().filter(p -> p.x == x2).findFirst().get();
    }

    @Override
    public String toString()
    {
      return "integral " + x1 + "," + x2 + " " + integral;
    }

  }

  public Control control(double x)
  {
    ControlPoint next = new ControlPoint();
    next.x = x;
    controls.add(next);
    return next;
  }

  public IntegralConstraint integral(double x1, double x2, double value)
  {
    IntegralConstraint ic = new IntegralConstraint(x1, x2, value);
    this.constraints.add(ic);
    return ic;

  }

  public class Working
  {
    int variables;
    ArrayList<double[]> A;
    ArrayList<Double> b;
    private final double[] h;
    private final List<ControlPoint> controls;

    Working(List<ControlPoint> controls, double[] h)
    {
      this.controls = controls;
      int n = controls.size();
      variables = n * 2;
      A = new ArrayList<>();
      A.ensureCapacity(n * 2);
      b = new ArrayList<>();
      b.ensureCapacity(n * 2);
      this.h = h;
    }

    void add(double[] a, double v)
    {
      A.add(a);
      b.add(v);
    }
  }

//<editor-fold desc="solve" defaultstate="collapsed">
  public void solve()
  {
    number();

    // We also need to check for duplicates.
    int n = controls.size();

    // Check to see if we have enough equations
    int n2 = 0;
    for (ControlPoint control : controls)
    {
      int n3 = control.constraints();
      n2 += n3;
    }

    double[] h = new double[n - 1];
    for (int i = 0; i < n - 1; ++i)
    {
      h[i] = (controls.get(i + 1).x - controls.get(i).x);
      if (h[i] == 0)
        throw new RuntimeException("Colinear points");
    }

    Working ws = new Working(controls, h);
    for (ControlPoint control : controls)
    {
      control.setup(ws);
    }

    for (Constraint control : constraints)
    {
      control.setup(ws);
    }

//    for (double[] a : ws.A)
//    {
//      System.out.println(DoubleArray.toString(a));
//    }
    Matrix a = MatrixFactory.newRowMatrix(ws.A);
    if (a.rows() < n * 2)
    {
      dump();
      throw new RuntimeException("Underspecified spline " + a.rows() + "<" + n * 2);
    }
    if (a.rows() > n * 2)
    {
      dump();
      throw new RuntimeException("over spline " + a.rows() + "<" + n * 2);
    }

    double[] b = DoubleArray.toPrimitives(ws.b);
    Matrix a2 = MatrixOps.multiply(a.transpose(), a);
    MatrixOps.addAssign(MatrixViews.diagonal(a2), 1e-6);
    double[] b2 = MatrixOps.multiply(a.transpose(), b);
    Matrix v = MatrixOps.divideLeft(a2, MatrixFactory.createColumnVector(b2));
    double[] v2 = v.flatten();

    // Write back into the structure
    int k = 0;
    ControlPoint prev = null;
    for (ControlPoint control : controls)
    {
      if (!control.yFixed)
        control.y = v2[k];
      if (!control.vFixed)
        control.v = v2[k + 1];
      if (prev != null)
      {
        double h2 = control.x - prev.x;
        double i = h2 * (prev.y + control.y) / 2 + h2 * h2 * (prev.v - control.v) / 12;
        control.i = i;
      }
      prev = control;
      k += 2;
    }
  }

  private double[] fixIntegral(double m0, double m1, double m2, double h, double s)
  {
    if (m1 == 0)
      return new double[4];
    double tau = 3 / Math.sqrt((m0 / m1) * (m0 / m1) + (m2 / m1) * (m2 / m1));
    double q0 = tau * m0;
    double q1 = tau * m2;
    double r0 = (6 * s / h / h - 4 * q0 / h - 2 * q1 / h);
    double r1 = (-6 * s / h / h + 2 * q0 / h + 4 * q1 / h);
    double s2 = h * ((q0 + q1) / 2 + (r0 * h - r1 * h) / 12);
    q0 *= s / s2;
    q1 *= s / s2;
    r0 *= s / s2;
    r1 *= s / s2;
    return new double[]
    {
      q0, r0, q1, r1
    };
  }

  private IntegralConstraint findWorst()
  {
    IntegralConstraint worst = null;
    for (IntegralConstraint ic : constraints)
    {
      if (ic.disabled)
        continue;
      if (worst == null)
        worst = ic;
      if (worst.min > ic.min)
        worst = ic;
    }
    return worst;
  }

  public CubicHermiteSpline solveNonneg()
  {
    System.out.println("XXXX");
    // Set up the integrals
    constraints.stream().forEach(p -> p.bind(controls));

    // Add extral control points
    ControlPoint prev = null;
    for (ControlPoint control : new ArrayList<>(controls))
    {
      control.c3();
      if (prev != null)

        this.control((control.x + prev.x) / 2).c2();
      prev = control;
    }
    controls.get(0).c3(false);
    prev.c3(false);

    // Order and number
    number();
    dump();

    // First zero out any segments that are all zero and remove redundant segments
    for (Constraint constraint : constraints)
    {
      if (constraint instanceof IntegralConstraint)
      {
        IntegralConstraint ic = (IntegralConstraint) constraint;
        if (ic.integral != 0)
        {
          continue;
        }
        ic.left.position(0).velocity(0).c2(false).c3(false);
        controls.remove(ic.left.index + 1);
        ic.right.position(0).velocity(0).c2(false).c3(false);
        ic.disabled = true;
      }
    }

    for (int i = 0; i < 10; ++i)
    {
      solve();
      computeMin();
      IntegralConstraint ic = findWorst();

      double i2 = ic.integral;
      double h2 = ic.right.x - ic.left.x;

      if (ic.min >= 0)
        break;
      double m0 = 0, m2 = 0;
      if (ic.index > 0)
      {
        IntegralConstraint left = constraints.get(ic.index - 1);
        m0 = left.integral / (left.x2 - left.x1);
      }
      if (ic.index + 1 < constraints.size())
      {
        IntegralConstraint right = constraints.get(ic.index + 1);
        m2 = right.integral / (right.x2 - right.x1);
      }
      double[] v = fixIntegral(m0, i2 / h2, m2, h2, i2);
      ic.left.position(v[0]).velocity(v[1]).c2(false).c3(false);
      ic.right.position(v[2]).velocity(v[3]).c2(false).c3(false);
      ic.disabled = true;
      controls.remove(ic.left.index + 1);
      if (Double.isNaN(ic.left.y) || Double.isNaN(ic.right.y))
        throw new RuntimeException("NAN");
    }
    solve();
    return toSpline();
  }

//</editor-fold>
  public CubicHermiteSpline toSpline()
  {
    CubicHermiteSpline out = new CubicHermiteSpline(this.controls.size());
    int i = 0;
    for (ControlPoint control : this.controls)
    {
      out.control[i++] = new CubicHermiteSpline.ControlPoint(control.x, control.y, control.v);
    }
    out.endBehavior = EndBehavior.LINEAR;
    return out;
  }

  public CubicHermiteSpline computeAreaMatch(double[] edges, double[] areas)
  {
    control(edges[0]).position(0).velocity(0);
    for (int i = 1; i < edges.length - 1; ++i)
      control(edges[i]).c2();
    control(edges[edges.length - 1]).position(0).velocity(0);
    for (int i = 0; i < edges.length - 1; ++i)
      integral(edges[i], edges[i + 1], areas[i]);
    dump();
    return solveNonneg();
  }

  public void dump()
  {
    System.out.println("========");
    for (ControlPoint control : this.controls)
    {
      System.out.println(control.toString());
    }
    for (Constraint constraint : this.constraints)
    {
      System.out.println(constraint.toString());
    }
  }

  public void number()
  {
    // First lets make sure they are in order
    controls.sort((p1, p2) -> Double.compare(p1.x, p2.x));
    constraints.sort((p1, p2) -> Double.compare(p1.x1, p2.x1));

    int i = 0;
    for (ControlPoint control : controls)
    {
      control.index = i++;
    }

    i = 0;
    for (IntegralConstraint control : constraints)
    {
      control.index = i++;
    }

  }

//<editor-fold desc="nonneg" defaultstate="collapsed">
  private void computeMin()
  {
    for (IntegralConstraint constraint : constraints)
    {
      constraint.min = getMinimum(constraint.left, constraint.right);
    }
  }

  private double getMinimum(ControlPoint v1, ControlPoint v2)
  {
    double min = Double.MAX_VALUE;
    for (int i = v1.index; i < v2.index; ++i)
    {
      ControlPoint c1 = controls.get(i);
      ControlPoint c2 = controls.get(i + 1);
      min = Math.min(hermiteMin(c1.x, c1.y, c1.v, c2.x, c2.y, c2.v), min);
    }
    if (Double.isNaN(min))
      throw new RuntimeException("NAN");
    return min;
  }

//</editor-fold>
  public double hermite(double u, double p0, double m0, double p1, double m1)
  {
    return (((2 * p0 - 2 * p1 + m0 + m1) * u + (-3 * p0 + 3 * p1 - 2 * m0 - m1)) * u + m0) * u + p0;
  }

  public double hermiteMin(double x0, double p0, double m0, double x1, double p1, double m1)
  {
    double h = x1 - x0;
    m0 *= h;
    m1 *= h;
    double min = Math.min(p0, p1);
    double a = 6 * p0 - 6 * p1 + 3 * m0 + 3 * m1;
    double b = -6 * p0 + 6 * p1 - 4 * m0 - 2 * m1;
    double c = 1 * m0;
    if (b * b - 4 * a * c > 0)
    {
      double r0 = (-b - Math.sqrt(b * b - 4 * a * c)) / 2 / a;
      double r1 = (-b + Math.sqrt(b * b - 4 * a * c)) / 2 / a;
      if (r0 > 0 && r0 < 1)
      {
        min = Math.min(min, hermite(r0, p0, m0, p1, m1));
      }
      if (r1 > 0 && r1 < 1)
      {
        min = Math.min(min, hermite(r1, p0, m0, p1, m1));
      }
    }
    return min;
  }

  public static void main(String[] args)
  {
    SplineCalculator sc = new SplineCalculator();

    sc.control(0).position(0).accel(0);
    sc.control(1).c2();
    sc.control(2).c2();
    sc.control(3).c2();
    sc.control(4).c2();
    sc.control(5).accel(0);

    sc.dump();
    sc.solve();
    sc.dump();
  }
}
