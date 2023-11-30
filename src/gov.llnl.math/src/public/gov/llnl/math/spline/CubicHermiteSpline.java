/*
 * Copyright 2016, Lawrence Livermore National Security, LLC.
 * All rights reserved
 *
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.math.spline;

import gov.llnl.math.HashUtilities;
import gov.llnl.math.MathExceptions;
import gov.llnl.math.MathPackage;
import gov.llnl.utility.UUIDUtilities;
import gov.llnl.utility.annotation.Matlab;
import gov.llnl.utility.xml.bind.ReaderInfo;
import gov.llnl.utility.xml.bind.Reader;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author nelson85
 */
@ReaderInfo(CubicHermiteSplineReader.class)
public final class CubicHermiteSpline
        extends Spline<CubicHermiteSpline.ControlPoint>
        implements Serializable
{
  private static final long serialVersionUID = UUIDUtilities.createLong("CubicHermiteSpline");

  @Reader.Declaration(pkg = MathPackage.class, name = "cubicControlPoint")
  public final static class ControlPoint
          extends Spline.ControlPoint
          implements Serializable
  {
    private static final long serialVersionUID = UUIDUtilities.createLong("CubicHermiteSpline.ControlPoint");
    public double m;  // slope at end point

    public ControlPoint()
    {
    }

    public ControlPoint(double x, double y, double m)
    {
      this.x = x;
      this.y = y;
      this.m = m;
    }

    private ControlPoint(ControlPoint control)
    {
      this.x = control.x;
      this.y = control.y;
      this.m = control.m;
    }

    @Override
    public boolean equals(Object t)
    {
      if (!(t instanceof ControlPoint))
        return false;
      ControlPoint cp = (ControlPoint) t;
      return (x == cp.x) && (y == cp.y) && (m == cp.m);
    }

    @Override
    public int hashCode()
    {
      return HashUtilities.hash(HashUtilities.hash(HashUtilities.hash(0, x), y), m);
    }

    @Reader.Attribute(name = "x", required = true)
    public void setX(double x)
    {
      this.x = x;
    }

    @Reader.Attribute(name = "y", required = true)
    public void setY(double y)
    {
      this.y = y;
    }

    @Reader.Attribute(name = "m")
    public void setM(double m)
    {
      this.m = m;
    }
  }

  ControlPoint[] control;
  int cache = -1;

  CubicHermiteSpline(int length)
  {
    control = new ControlPoint[length];
  }

  CubicHermiteSpline(double[] x, double[] y)
  {
    int n = x.length;
    if (n != y.length)
      throw new IllegalArgumentException("Size mismatch " + n + " " + y.length);

    control = new ControlPoint[n];
    for (int i = 0; i < n; ++i)
    {
      control[i] = new ControlPoint(x[i], y[i], 0);
    }
    SplineUtilities.sort(Arrays.asList(control));
    if (n == 2)
    {
      ControlPoint c0 = control[0];
      ControlPoint c1 = control[1];
      double m = (c1.y - c0.y) / (c1.x - c0.x);
      c0.m = m;
      c1.m = m;
    }
  }

  CubicHermiteSpline(ControlPoint[] control)
  {
    this.control = control;
  }
  
  @Override
  public double applyAsDouble(double in) throws MathExceptions.DomainException
  {
    if (in != in)
      return in;

    int i0 = cache;
    int N = control.length;

    if (N == 0)
      return 0;

    if (i0 > -1 && control[i0].x <= in && control[i0 + 1].x >= in)
    {
      // still in the existing interval
    }
    else
    {

      if (in <= control[0].x)
        return interpolateBefore(in);

      if (in >= control[N - 1].x)
        return interpolateAfter(in);

      // Find the segment that contains the point
      i0 = SplineUtilities.binarysearch(control, in);
      cache = i0;
    }

    // Use interpolation on segment (inline for speed)
    {
      ControlPoint c0 = control[i0];
      ControlPoint c1 = control[i0 + 1];
      double h = c1.x - c0.x;
      double t = (in - c0.x) / h;
      double t2 = t * t;
      double t3 = t * t * t;
      double h00 = 2 * t3 - 3 * t2 + 1;
      double h01 = t3 - 2 * t2 + t;
      double h10 = -2 * t3 + 3 * t2;
      double h11 = t3 - t2;
      return h00 * c0.y + h01 * h * c0.m + h10 * c1.y + h11 * h * c1.m;
    }

  }

  private double interpolateBefore(double in) throws MathExceptions.DomainException
  {
    if (this.endBehavior == EndBehavior.CLAMP)
      return control[0].y;
    else if (this.endBehavior == EndBehavior.LINEAR)
      return control[0].m * (in - control[0].x) + control[0].y;
    else if (this.endBehavior == EndBehavior.CUBIC)
      throw new UnsupportedOperationException("Not implemented");
    throw new MathExceptions.DomainException("Outside domain");
  }

  private double interpolateAfter(double in) throws MathExceptions.DomainException
  {
    int N = control.length;
    if (this.endBehavior == EndBehavior.CLAMP)
      return control[N - 1].y;
    else if (this.endBehavior == EndBehavior.LINEAR)
      return control[N - 1].m * (in - control[N - 1].x) + control[N - 1].y;
    else if (this.endBehavior == EndBehavior.CUBIC)
      throw new UnsupportedOperationException("Not implemented");
    throw new MathExceptions.DomainException("Outside domain");
  }

  @Override
  public boolean contains(double d)
  {
    return (d >= control[0].x) && (d < control[control.length - 1].x);
  }

  @Override
  public double[] evaluateRangeOrdered(double[] in, int start, int end)
          throws MathExceptions.DomainException
  {
    int N = control.length;
    double[] out = new double[end - start];
    int i1 = start;
    int i2 = 0;

    // Pre
    while (i1 < end && in[i1] < control[0].x)
    {
      out[i2++] = interpolateBefore(in[i1]);
      i1++;
    }

    // Within
    if (i1 < end && contains(in[i1]))
    {
      int i0 = SplineUtilities.binarysearch(control, in[i1]);
      i0++;

      while (i1 < end)
      {
        while ((i0 < N) && (in[i1] > control[i0].x))
          i0++;
        if (i0 >= N)
          break;

        // inline for speed
        {
          ControlPoint c0 = control[i0 - 1];
          ControlPoint c1 = control[i0];
          double h = c1.x - c0.x;
          double t = (in[i1] - c0.x) / h;
          double t2 = t * t;
          double t3 = t * t * t;
          double h00 = 2 * t3 - 3 * t2 + 1;
          double h01 = t3 - 2 * t2 + t;
          double h10 = -2 * t3 + 3 * t2;
          double h11 = t3 - t2;
          out[i2++] = h00 * c0.y + h01 * h * c0.m + h10 * c1.y + h11 * h * c1.m;
        }
        i1++;
      }
    }

    // Post
    // special handling for CLAMP
    if (this.endBehavior == EndBehavior.CLAMP)
    {
      double d = control[N - 1].y;
      for (; i2 < out.length; ++i2)
        out[i2] = d;
      return out;
    }

    while (i1 < end && in[i1] > control[N - 1].x)
    {
      out[i2++] = interpolateAfter(in[i1]);
      i1++;
    }
    return out;
  }

  @Matlab
  public double getControlX(int index)
  {
    return control[index].x;
  }

  @Matlab
  public double getControlY(int index)
  {
    return control[index].y;
  }

  /**
   * Copy the x control points into an array.
   *
   * @return a new array with the x points.
   */
  @Matlab
  public double[] getControlX()
  {
    double[] out = new double[control.length];
    for (int i = 0; i < control.length; ++i)
      out[i] = control[i].x;
    return out;
  }

  /**
   * Copy the y control points into an array.
   *
   * @return a new array with the y points.
   */
  @Matlab
  public double[] getControlY()
  {
    double[] out = new double[control.length];
    for (int i = 0; i < control.length; ++i)
      out[i] = control[i].y;
    return out;
  }

  /**
   * Copy the slopes at each of the control points into a new array.
   *
   * @return a new array holding the slopes at each of the control points.
   */
  @Matlab
  public double[] getSlopes()
  {
    double[] out = new double[control.length];
    for (int i = 0; i < control.length; ++i)
      out[i] = control[i].m;
    return out;
  }

  @Override
  public CubicHermiteSpline clone() throws CloneNotSupportedException
  {
    CubicHermiteSpline out = (CubicHermiteSpline) super.clone();
    int n = this.control.length;
    out.control = new ControlPoint[n];
    for (int i = 0; i < n; ++i)
      out.control[i] = new ControlPoint(this.control[i]);
    return out;
  }

  @Override
  public boolean equals(Object obj)
  {
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    final CubicHermiteSpline other = (CubicHermiteSpline) obj;
    return Arrays.equals(control, other.control);
  }

  @Override
  public int hashCode()
  {
    int h = HashUtilities.hash(0, control.length);
    return HashUtilities.hash(h, control);
  }

  @Override
  public List<ControlPoint> getControl()
  {
    return Arrays.asList(this.control);
  }

  public double[] toArray()
  {
    double[] out = new double[3 * this.control.length];
    int i = 0;
    for (ControlPoint cp : this.control)
    {
      out[i++] = cp.x;
      out[i++] = cp.y;
      out[i++] = cp.m;
    }
    return out;
  }

  /**
   * Create a spline with both constrained and unconstrained slopes.
   *
   * @return
   */
  public static CubicSplineBuilder newBuilder()
  {
    return new CubicSplineBuilderImpl();
  }

  //<editor-fold desc="builder">
  public interface CubicSplineBuilder
  {
    /**
     * Add a new control point with no constraints.
     *
     * @param x
     * @param y
     */
    public void addControl(double x, double y);

    /**
     * Add a new control point with constraints.
     *
     * @param x
     * @param y
     * @param m
     */
    public void addControl(double x, double y, double m);

    /**
     * Add a constrained segment.
     *
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     */
    default public void addSegment(double x1, double y1, double x2, double y2)
    {
      double m = (y2 - y1) / (x2 - x1);
      addControl(x1, y1, m);
      addControl(x2, y2, m);
    }

    public CubicHermiteSpline construct();
  }
//</editor-fold>

}
