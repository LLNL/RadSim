/* 
 * Copyright 2016, Lawrence Livermore National Security, LLC.
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.math;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author nelson85
 */
public class FloatArray
{

//<editor-fold defaultstate="collapsed" desc="string">
  /**
   * Convert a string with separated floats into a float array. This method uses
   * regular expressions so it can be rather slow for long lists.
   *
   * @param str is a string with whitespace delimited floats
   * @return the array of floats converted
   */
  public static float[] parseFloatArray(String str)
  {
    ArrayList<Float> out = new ArrayList<>();
    Matcher matcher = Pattern.compile("[-+]?\\d*\\.?\\d+([eE][-+]?\\d+)?").matcher(str);

    while (matcher.find())
    {
      float element = Float.parseFloat(matcher.group());
      out.add(element);
    }

    // Unbox
    float out2[] = new float[out.size()];
    int i = 0;
    for (float d : out)
    {
      out2[i++] = d;
    }
    return out2;
  }

  public static String toString(float[] values)
  {
    StringBuilder sb = new StringBuilder();
    boolean first = true;
    for (float i : values)
    {
      if (!first)
      {
        sb.append(" ");
      }
      else
      {
        first = false;
      }
      sb.append(Float.toString(i));
    }
    return sb.toString();
  }

  public static String toString(float[] values, String format)
  {
    StringBuilder sb = new StringBuilder();
    boolean first = true;
    for (float i : values)
    {
      if (!first)
      {
        sb.append(" ");
      }
      else
      {
        first = false;
      }
      sb.append(String.format(format, i));
    }
    return sb.toString();
  }
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="fill">
  /**
   * Assign all of the elements of a float array to a value.
   *
   * @param x
   * @param value
   */
  public static void fill(float x[], float value)
  {
    fillRange(x, 0, x.length, value);
  }

  /**
   * Assign all of the elements of a float array to a value.
   *
   * @param x
   * @param value
   * @param start
   * @param end
   */
  public static void fillRange(float[] x, int start, int end, float value)
  {
    for (int i = start; i < end; ++i)
    {
      x[i] = value;
    }
  }
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="assign">
  public static float[] assign(float target[], float source[])
          throws MathExceptions.SizeException
  {
    if (target.length != source.length)
    {
      throw new MathExceptions.SizeException("Size mismatch");
    }
    return assign(target, 0, source, 0, target.length);
  }

  public static float[] assign(
          float[] target, int targetOffset,
          float[] src, int srcOffset,
          int length)
  {
    for (int i = 0; i < length; ++i)
    {
      target[targetOffset + i] = src[srcOffset + i];
    }
    return target;
  }
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="basic operators">
  /**
   * Add one float array to another. The length of the source must be at least
   * as long as the target.
   *
   * @param target
   * @param source
   * @return the target array.
   */
  public static float[] addAssign(float target[], float source[])
  {
    // This is slightly more efficient than the ranged version os we keep it
    for (int i = 0; i < target.length; ++i)
    {
      target[i] += source[i];
    }
    return target;
  }

  /**
   * Add one float array to another. The length of the source must be at least
   * as long as the target.
   *
   * @param target
   * @param source
   * @param length
   * @param targetOffset
   * @param sourceOffset
   * @return the target array.
   */
  public static float[] addAssign(
          float[] target, int targetOffset,
          float[] source, int sourceOffset,
          int length)
  {
    int i2 = sourceOffset;
    int end = targetOffset + length;
    for (int i1 = targetOffset; i1 < end; ++i1, ++i2)
    {
      target[i1] += source[i2];
    }
    return target;
  }

  /**
   * Subtract one float array to another. The length of the source must be at
   * least as long as the target.
   *
   * @param target
   * @param source
   * @param length
   * @param targetOffset
   * @param sourceOffset
   * @return the target array.
   */
  public static float[] subtractAssign(
          float[] target, int targetOffset,
          float[] source, int sourceOffset,
          int length)
  {
    int i2 = sourceOffset;
    int end = targetOffset + length;
    for (int i1 = targetOffset; i1 < end; ++i1, ++i2)
    {
      target[i1] -= source[i2];
    }
    return target;
  }

  /**
   * Add a value to every member of a float array.
   *
   * @param target
   * @param value
   * @return the target array.
   */
  public static float[] addAssign(float target[], float value)
  {
    for (int i = 0; i < target.length; ++i)
    {
      target[i] += value;
    }
    return target;
  }

  public static float[] subtractAssign(float target[], float value[])
  {
    for (int i = 0; i < target.length; ++i)
    {
      target[i] -= value[i];
    }
    return target;
  }

  public static float[] add(float target[], float x1[], float x2[])
  {
    for (int i = 0; i < x1.length; ++i)
    {
      target[i] = x1[i] + x2[i];
    }
    return target;
  }

  public static float[] addAssignRange(float[] target, int begin, int end, float value)
  {
    for (int i = begin; i < end; ++i)
    {
      target[i] += value;
    }
    return target;
  }

  public static float[] addScaled(float target[], float x1[], float x2[], float d)
  {
    for (int i = 0; i < x1.length; ++i)
    {
      target[i] = x1[i] + d * x2[i];
    }
    return target;
  }

  public static float[] subtract(float target[], float x1[], float x2[])
  {
    for (int i = 0; i < x1.length; ++i)
    {
      target[i] = x1[i] - x2[i];
    }
    return target;
  }

//  public static float[] multiplyAssignRange(float target[], float value)
//  {
//    return multiplyAssignRange(target, value, 0, target.length);
//  }
//
//  public static float[] multiplyAssignRange(float target[], float[] values)
//  {
//    computeVectorProduct(target, target, values, target.length, 0, 0, 0);
//    return target;
//  }
  public static float[] multiplyAssignRange(float[] target, int begin, int end, float value)
  {
    for (int i = begin; i < end; ++i)
    {
      target[i] *= value;
    }
    return target;
  }

  public static float[] negate(float[] v)
  {
    return negateRange(v, 0, v.length);
  }

  public static float[] negateRange(float[] v, int start, int end)
  {
    for (int i = start; i < end; ++i)
    {
      v[i] = -v[i];
    }
    return v;
  }

  public static float[] divideAssign(float[] v, float d)
  {
    return divideAssignRange(v, 0, v.length, d);
  }

  public static float[] divideAssignRange(float[] v, int begin, int end, float d)
  {
    for (int i = begin; i < end; i++)
    {
      v[i] /= d;
    }
    return v;
  }

  //</editor-fold>
//<editor-fold desc="equivalent">
  /**
   * Determine if two vectors are equal within roundoff. Applies consideration
   * of round of to doubles. This does not obey the standard equivalent contract
   * because two doubles may be within round off but only one of them may be
   * within round off to a third. Also two arrays may have different hashCodes
   * but still be equivalent.
   *
   * @param operand1 is the first operand.
   * @param operand2 is the second operand.
   * @return true if the vectors are equal within the accuracy of floats.
   */
  public static boolean equivalent(float[] operand1, float[] operand2)
  {
    if (operand1.length != operand2.length)
      return false;
    return FloatArray.equivalent(operand1, 0, operand2, 0, operand1.length);
  }

  /**
   * Determine if a region of two vectors are equal within the limits of
   * precision.
   *
   * @param a
   * @param aOffset
   * @param b
   * @param bOffset
   * @param length is the length of the region to compare.
   * @return true if the vectors are equal within the accuracy of floats.
   */
  public static boolean equivalent(
          float[] a, int aOffset,
          float[] b, int bOffset,
          int length)
  {
    MathAssert.assertRange(a, aOffset, aOffset+length);
    MathAssert.assertRange(b, bOffset, bOffset+length);

    while (length > 0)
    {
      float av = a[aOffset++];
      float bv = b[bOffset++];
      if (Math.abs(av - bv) > 1e-7 * (Math.abs(av) + Math.abs(bv)))
        return false;
      --length;
    }
    return true;
  }

  /**
   * Determine if any element of a double array is NaN.
   *
   * @param vector
   * @return true if one of the elements is NaN.
   */
  public static boolean isNaN(float[] vector)
  {
    return isNaNRange(vector, 0, vector.length);
  }

  /**
   * Determine if any element of a double array is NaN.
   *
   * @param vector
   * @param begin is the start of the range, inclusive.
   * @param end is the end of the range, exclusive.
   * @return true if one of the elements is NaN.
   */
  public static boolean isNaNRange(float[] vector, int begin, int end)
  {
    for (int i = begin; i < end; ++i)
    {
      if (vector[i] != vector[i])
        return true;
    }
    return false;
  }

//</editor-fold>

//<editor-fold desc="solving" defaultstate="collapsed">
  /**
   * Divide left operator for matrices to solve A*X=C.
   *
   * This destroys the contents of both the vector and the matrix. Thus we must
   * make sure we are not reusing that memory.
   *
   * For our problem the matrix will be symmetric so we could use a specialized
   * solver. But this should be sufficient for our purposes.
   *
   * @param C is the right and left hand side holding a [n,1] vector.
   * @param A is the matrix to solve stored as [n,n].
   * @param n is the length of the vector.
   * @return
   */
  public static int solveDestructive(float[] C, float[] A, int n)
  {
    int start = 0;
    /* upper tri form */
    for (int i1 = 0; i1 < n; i1++)
    {
      start = i1 * n;
      double max = Math.abs(A[start + i1]);
      int index = i1;
      for (int i2 = i1 + 1; i2 < n; i2++)
      {
        if (max < Math.abs(A[i2 + start]))
        {
          max = Math.abs(A[i2 + start]);
          index = i2;
        }
      }

      /* watch for singularity */
      if (max < 1e-16)
        return -1;

      /* use the largest row */
      if (index != i1)
      {
        {
          int i3 = index;
          int i4 = i1;
          float swap = C[i3];
          C[i3] = C[i4];
          C[i4] = swap;
        }

        {
          int i3 = start + index;
          int i4 = start + i1;
          for (; i3 < n * n; i3 += n, i4 += n)
          {
            float swap = A[i3];
            A[i3] = A[i4];
            A[i4] = swap;
          }
        }
      }

      /* normalized row */
      double tmp = A[start + i1];
      C[i1] /= tmp;
      for (int i2 = start + i1; i2 < n * n; i2 += n)
      {
        A[i2] /= tmp;
      }

      /* reduce rows under it */
      for (int i2 = i1 + 1; i2 < n; i2++)
      {
        int i3 = i1 + start;
        int i4 = i2 + start;
        double ratio = A[i4];
        if (ratio == 0.0)
          continue;
        for (; i3 < n * n; i3 += n, i4 += n)
        {
          A[i4] -= ratio * A[i3];
        }

        i3 = i2;
        i4 = i1;
        C[i3] -= ratio * C[i4];
      }
    }

    if (n == 1)
      return 0;

    /* finish the inverse */
    for (int i1 = n - 2; i1 >= 0; i1--)
    {
      for (int i2 = i1 + 1; i2 < n; i2++)
      {
        int i3 = i1;
        int i4 = i2;
        C[i3] -= A[i1 + n * i2] * C[i4];
      }
    }
    return 0;
  }
//</editor-fold> 
  
}
