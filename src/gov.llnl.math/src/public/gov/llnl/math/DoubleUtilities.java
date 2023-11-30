/* 
 * Copyright 2016, Lawrence Livermore National Security, LLC.
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.math;

import static java.lang.Math.sqrt;

/**
 *
 * @author nelson85
 */
public class DoubleUtilities
{

  public static double sqr(double d)
  {
    return d * d;
  }

  public static double cube(double d)
  {
    return d * d * d;
  }

  public static double pow32(double d)
  {
    return d * sqrt(d);
  }

  public static double clamp(double d, double min, double max)
  {
    if (d < min)
      return min;
    if (d > max)
      return max;
    return d;
  }

  /**
   * Solve A*R=B by Gauss elimination.
   *
   * @param C is used to store both R and B
   * @param A is destroyed in the process
   * @param sz
   * @return
   */
  @Deprecated
  static public double[] solveDestructive(double[] C, double[] A, int sz)
  {
    int a_row = sz;
    int a_size = sz * sz;
    int c_row = sz;
    int c_size = sz;

    int start;
    /* upper tri form */
    for (int i1 = 0; i1 < a_row; i1++)
    {
      start = i1 * a_row;
      double max = Math.abs(A[start + i1]);
      int index = i1;
      for (int i2 = i1 + 1; i2 < a_row; i2++)
      {
        if (max < Math.abs(A[i2 + start]))
        {
          max = Math.abs(A[i2 + start]);
          index = i2;
        }
      }

      /* watch for singularity */
      if (max < 1e-16)
        return null;

      /* use the largest row */
      if (index != i1)
      {
        int i3 = index;
        int i4 = i1;
        for (; i3 < c_size; i3 += c_row, i4 += c_row)
        {
          double swap = C[i3];
          C[i3] = C[i4];
          C[i4] = swap;
        }

        i3 = start + index;
        i4 = start + i1;
        for (; i3 < a_size; i3 += a_row, i4 += a_row)
        {
          double swap = A[i3];
          A[i3] = A[i4];
          A[i4] = swap;
        }
      }

      /* normalized row */
      double tmp = A[start + i1];
      for (int i2 = i1; i2 < c_size; i2 += c_row)
      {
        C[i2] /= tmp;
      }
      for (int i2 = start + i1; i2 < a_size; i2 += a_row)
      {
        A[i2] /= tmp;
      }

      /* reduce rows under it */
      for (int i2 = i1 + 1; i2 < a_row; i2++)
      {
        int i3 = i1 + start;
        int i4 = i2 + start;
        double ratio = A[i4];
        if (ratio == 0.0)
          continue;
        for (; i3 < a_size; i3 += a_row, i4 += a_row)
        {
          A[i4] -= ratio * A[i3];
        }

        i3 = i2;
        i4 = i1;
        for (; i3 < c_size; i3 += c_row, i4 += c_row)
        {
          C[i3] -= ratio * C[i4];
        }
      }
    }

    if (a_row == 1)
      return C;

    /* finish the inverse */
    for (int i1 = a_row - 2; i1 >= 0; i1--)
    {
      for (int i2 = i1 + 1; i2 < a_row; i2++)
      {
        int i3 = i1;
        int i4 = i2;
        for (; i3 < c_size; i3 += c_row, i4 += c_row)
        {
          C[i3] -= A[i1 + a_row * i2] * C[i4];
        }
      }
    }

    return C;
  }

  /**
   * Check if two double are equal within the limits of accuracy.
   *
   * @param d1
   * @param d2
   * @return true if the two values are equal within the limits of precision.
   */
  static boolean equivalent(double d1, double d2)
  {
    if (d1 == d2)
      return true;
    return Math.abs(d1-d2)< (Math.abs(d1)+Math.abs(d2))*1e-14;
  }

  public static double clip(double value, double min, double max)
  {
    if (value < min)
    {
      return min;
    }
    if (value > max)
    {
      return max;
    }
    return value;
  }
}
