/* 
 * Copyright 2016, Lawrence Livermore National Security, LLC.
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.math.spline;

import gov.llnl.math.spline.Spline.ControlPoint;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 *
 * @author nelson85
 */
public class SplineUtilities
{

  /**
   * Utility for finding the nearest lower control point in the list.
   *
   * @param <T> is the control point class used by the spline.
   * @param control is the list of control points.
   * @param value is the x value to search for.
   * @return the index of the largest control point less than the value.
   */
  public static <T extends ControlPoint> int binarysearch(T[] control, double value)
  {
    if (value <= control[0].x)
      return 0;
    if (value >= control[control.length - 1].x)
      return control.length - 1;

    int i0 = 0;
    int i1 = control.length - 1;

    while (i1 > i0 + 1)
    {
      int i3 = (i0 + i1) / 2;
      if (control[i3].x == value)
        return i3;
      if (control[i3].x > value)
        i1 = i3;
      if (control[i3].x < value)
        i0 = i3;
    }
    return i0;
  }

  /**
   * Utility to sort the control points. Control points are sorted by their x
   * values. Used in the creation of a spline.
   *
   * @param <T> is the control point class used by the spline.
   * @param control is the list of control points.
   */
  public static <T extends ControlPoint> void sort(List<T> control)
  {
    for (int i = 0; i < control.size() - 1; ++i)
    {
      // If something is out of order, sort
      if (control.get(i + 1).x < control.get(i).x)
      {
        ControlPointComparator<T> comparator = new ControlPointComparator<>();
        Collections.sort(control, comparator);
        return;
      }
    }
  }

  public static class ControlPointComparator<PointType extends ControlPoint> implements Comparator<PointType>
  {
    @Override
    public int compare(PointType t, PointType t1)
    {
      return Double.compare(t.x, t1.x);
    }
  }

  static public <PointType extends ControlPoint> double[] extractControlX(List<PointType> control)
  {
    double[] out = new double[control.size()];
    int i = 0;
    for (ControlPoint cp : control)
    {
      out[i++] = cp.x;
    }
    return out;
  }

  static public <PointType extends ControlPoint> double[] extractControlY(List<PointType> control)
  {
    double[] out = new double[control.size()];
    int i = 0;
    for (ControlPoint cp : control)
    {
      out[i++] = cp.y;
    }
    return out;
  }

}
