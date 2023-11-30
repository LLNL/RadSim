/* 
 * Copyright 2016, Lawrence Livermore National Security, LLC.
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.math;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.IntPredicate;
import java.util.function.IntSupplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utilities for common operations on Integer arrays.
 *
 * @see java.lang.System#arraycopy
 * @see java.util.Arrays#copyOfRange
 *
 * @author nelson85
 */
public class IntegerArray
{
//<editor-fold desc="assign" defaultstate="collapsed">
  /**
   * Assign a portion of one vector to another. This is just a front end for
   * System.arraycopy.
   *
   * @param target is the vector to copy to.
   * @param targetOffset is the start of the copy region in the target.
   * @param source is the vector to copy from.
   * @param sourceOffset is the start of the copy region in the source.
   * @param length is the length of the section to copy.
   * @return the target vector.
   * @throws IndexOutOfBoundsException if the range of the source or target is
   * exceeded.
   */
  public static int[] assign(
          int[] target, int targetOffset,
          int[] source, int sourceOffset, int length)
          throws IndexOutOfBoundsException
  {
    try
    {
      System.arraycopy(source, sourceOffset, target, targetOffset, length);
      return target;
    }
    catch (IndexOutOfBoundsException ex)
    {
      MathAssert.assertRange(target, targetOffset, targetOffset + length, "target");
      MathAssert.assertRange(source, sourceOffset, sourceOffset + length, "source");
      throw ex;
    }
  }
  
    /**
   * Assign one vector to another. This is just a front end for
   * System.arraycopy.
   *
   * @param target is the vector to copy to.
   * @param source is the vector to copy from.
   * @return the target vector.
   * @throws IndexOutOfBoundsException if the vectors are different lengths.
   */
  public static int[] assign(int[] target, int[] source)
          throws IndexOutOfBoundsException
  {
    MathAssert.assertEqualLength(target, source);
    System.arraycopy(source, 0, target, 0, source.length);
    return target;
  }


//</editor-fold>
//<editor-fold desc="copy" defaultstate="collapsed">
    /**
   * Create a new array with members.
   * <p>
   * This is equivalent to `new double[]{values}`.
   * <p>
   * It can be used in places where an inline initialization is required, but
   * the java formatter would otherwise expand it to multiple lines which
   * clutters up the code.
   *
   * @param values
   * @return a new array containing those values.
   */
  public static int[] of(int... values)
  {
    return values;
  }

  /**
   * Returns copy of input array
   *
   * @param input
   * @return copy of array or null
   */
  public static int[] copyOf(int[] input)
  {
    if (input == null)
      return null;
    return copyOfRange(input, 0, input.length);
  }

  /**
   * Return copy of range. Will be length end - start
   *
   * @param input
   * @param begin
   * @param end
   * @throws NullPointerException if input is null
   * @throws IndexOutOfBoundsException if the array is smaller than requested
   * range
   * @return copy of range.
   */
  public static int[] copyOfRange(int[] input, int begin, int end)
          throws NullPointerException, IndexOutOfBoundsException
  {
    if (input == null)
      throw new NullPointerException();
    if (begin < 0 || end > input.length)
      throw new IndexOutOfBoundsException();
    int out[] = new int[end - begin];
    IntegerArray.assign(out, 0, input, begin, end - begin);
    return out;
  }

//</editor-fold>
//<editor-fold desc="fill" defaultstate="collapsed">
  /**
   * Assign all of the elements of a double array to a value.
   *
   * @param target
   * @param value
   * @return the target array or null if the target is null.
   */
  public static int[] fill(int[] target, int value)
  {
    if (target == null)
      return null;
    return fillRange(target, 0, target.length, value);
  }

  /**
   * Assign all of the elements of an integer array to a value.
   *
   * @param target
   * @param value
   * @param begin
   * @param end
   * @return the target array or null if the target is null.
   */
  public static int[] fillRange(int[] target, int begin, int end, int value)
          throws IndexOutOfBoundsException
  {
    if (target == null)
      return null;
    for (int i = begin; i < end; ++i)
    {
      target[i] = value;
    }
    return target;
  }

    /**
   * Assign all of the elements of a double array to a value.
   *
   * @param target
   * @param value
   * @return the target array or null if the target is null.
   */
  public static int[] fill(int[] target, IntSupplier value)
  {
    if (target == null)
      return null;
    return fillRange(target, 0, target.length, value);
  }

  /**
   * Assign all of the elements of an integer array to a value.
   *
   * @param target
   * @param value
   * @param begin
   * @param end
   * @return the target array or null if the target is null.
   */
  public static int[] fillRange(int[] target, int begin, int end, IntSupplier value)
          throws IndexOutOfBoundsException
  {
    if (target == null)
      return null;
    for (int i = begin; i < end; ++i)
    {
      target[i] = value.getAsInt();
    }
    return target;
  }

//</editor-fold>
//<editor-fold desc="colon" defaultstate="collapsed">
  /**
   * Create an array filled with elements from begin to end.
   *
   * @param begin is the start value.
   * @param end is the end value plus one.
   * @return is a new integer array containing values begin to end-1.
   */
  public static int[] colon(int begin, int end)
  {
    int n = end - begin;
    int[] out = new int[n];
    for (int i = 0; i < n; ++i, ++begin)
    {
      out[i] = begin;
    }
    return out;
  }

//</editor-fold>
//<editor-fold desc="add" defaultstate="collapsed">
  /**
   *
   * @param a
   * @param b
   * @return
   * @throws IndexOutOfBoundsException if length of a or b differ.
   */
  public static int[] add(int[] a, int[] b)
          throws IndexOutOfBoundsException
  {
    MathAssert.assertEqualLength(a, b);
    int[] out = new int[a.length];
    int i = 0;
    while (out.length - i > 8)
    {
      out[i] = a[i] + b[i];
      out[++i] = a[i] + b[i];
      out[++i] = a[i] + b[i];
      out[++i] = a[i] + b[i];
      out[++i] = a[i] + b[i];
      out[++i] = a[i] + b[i];
      out[++i] = a[i] + b[i];
      out[++i] = a[i] + b[i];
      i++;
    }
    for (; i < out.length; i++)
      out[i] = a[i] + b[i];
    return out;
  }

  /**
   * Add one integer array to another. The length of the source must be at least
   * as long as the target.
   *
   * @param target
   * @param source
   * @return the target array.
   * @throws IndexOutOfBoundsException if length of source and target differ.
   */
  public static int[] addAssign(int target[], int source[])
          throws IndexOutOfBoundsException
  {
    MathAssert.assertEqualLength(target, source);
    int i = 0;
    while (target.length - i > 8)
    {
      target[i] += source[i];
      target[++i] += source[i];
      target[++i] += source[i];
      target[++i] += source[i];
      target[++i] += source[i];
      target[++i] += source[i];
      target[++i] += source[i];
      target[++i] += source[i];
      i++;
    }
    for (; i < target.length; i++)
      target[i] += source[i];
    return target;
  }

//</editor-fold>
//<editor-fold desc="subtract" defaultstate="collapsed">
  /**
   * Subtract one integer array to another. The length of the source must be at
   * least as long as the target.
   *
   * @param target
   * @param source
   * @return the target array.
   * @throws IndexOutOfBoundsException if length of source and target differ.
   */
  public static int[] subtractAssign(int[] target, int[] source)
          throws IndexOutOfBoundsException
  {
    MathAssert.assertEqualLength(target, source);
    int i = 0;  
    while (target.length - i > 8)
    {
      target[i] -= source[i];
      target[++i] -= source[i];
      target[++i] -= source[i];
      target[++i] -= source[i];
      target[++i] -= source[i];
      target[++i] -= source[i];
      target[++i] -= source[i];
      target[++i] -= source[i];
      i++;
    }
    for (; i < target.length; i++)
      target[i] -= source[i];
    return target;
  }
  
  public static int[] subtract(int[] target, int[] source)
          throws IndexOutOfBoundsException
  {
    MathAssert.assertEqualLength(target, source);
    return subtractAssign(target.clone(), source);
  }

  
//</editor-fold>
//<editor-fold desc="negate" defaultstate="collapsed">
//</editor-fold>
//<editor-fold desc="multiply" defaultstate="collapsed">
//</editor-fold>
//<editor-fold desc="divide" defaultstate="collapsed">
//</editor-fold>
//<editor-fold desc="string" defaultstate="collapsed">
  /**
   * Convert a string with separated integers into a double array. This method
   * used regular expressions so it can be rather slow for long lists.
   *
   * @param str is a string with whitespace delimited doubles
   * @return the array of integers converted
   */
  public static int[] fromString(String str)
  {
    ArrayList< Integer> out = new ArrayList<>();
    Matcher matcher = Pattern.compile("[-+]?\\d+").matcher(str);

    while (matcher.find())
    {
      int element = Integer.parseInt(matcher.group());
      out.add(element);
    }
    int out2[] = new int[out.size()];
    int i = 0;
    for (int d : out)
    {
      out2[i++] = d;
    }
    return out2;
  }

  public static String toString(int[] values)
  {
    StringBuilder sb = new StringBuilder();
    boolean first = true;
    for (int i : values)
    {
      if (!first)
        sb.append(" ");
      else
        first = false;
      sb.append(Integer.toString(i));
    }
    return sb.toString();
  }

//</editor-fold>
//<editor-fold desc="norm" defaultstate="collapsed">
//</editor-fold>
//<editor-fold desc="sum" defaultstate="collapsed">
  static public int sum(int d[])
  {
    return sumRange(d, 0, d.length);
  }

  static public int sumRange(int d[], int begin, int end)
          throws IndexOutOfBoundsException
  {
    try
    {
      int sum = 0;
      int i = begin;
      while (end - i >= 8)
      {
        sum += d[i] + d[i + 1] + d[i + 2] + d[i + 3];
        sum += d[i + 4] + d[i + 5] + d[i + 6] + d[i + 7];
        i += 8;
      }
      for (; i < end; ++i)
      {
        sum += d[i];
      }
      return sum;
    }
    catch (IndexOutOfBoundsException ex)
    {
      MathAssert.assertRange(d, begin, end);
      throw ex;
    }
  }

//  public static double sum(int[] d, int[] index)
//  {
//    int sum = 0;
//    for (int i = 0; i < index.length; ++i)
//    {
//      sum += d[index[i]];
//    }
//    return sum;
//  }
//</editor-fold>
//<editor-fold desc="find" defaultstate="collapsed">
 
  static public int findIndexOfMinimum(int values[])
  {
    return findIndexOfMinimumRange(values, 0, values.length);
  }

  /**
   * Finds the index of the maximum in a vector.
   *
   * @param vector is the vector to search.
   * @return the index of the maximum.
   * @throws IndexOutOfBoundsException if requested range is outside of the
   * vector.
   */
  public static int findIndexOfMaximum(int[] vector)
  {
    return findIndexOfMaximumRange(vector, 0, vector.length);
  }

  static public int findIndexOfMinimumRange(int values[], int begin, int end)
          throws IndexOutOfBoundsException
  {
    try
    {
      int index = begin;
      for (int i = begin + 1; i < end; i++)
      {
        if (values[index] > values[i])
          index = i;
      }
      return index;
    }
    catch (IndexOutOfBoundsException ex)
    {
      MathAssert.assertRange(values, begin, end);
      throw ex;
    }
  }

  static public int findIndexOfMaximumRange(int values[], int begin, int end)
          throws IndexOutOfBoundsException
  {
    try
    {
      int index = begin;
      for (int i = begin + 1; i < end; i++)
      {
        if (values[index] < values[i])
          index = i;
      }
      return index;
    }
    catch (IndexOutOfBoundsException ex)
    {
      MathAssert.assertRange(values, begin, end);
      throw ex;
    }
  }

  public static int max(int[] vector)
  {
    return vector[findIndexOfMaximumRange(vector, 0, vector.length)];
  }

  public static int min(int[] vector)
  {
    return vector[findIndexOfMinimumRange(vector, 0, vector.length)];
  }

  public static int[] findIndexOfExtrema(int[] data)
  {
    return findIndexOfExtremaRange(data, 0, data.length);
  }

  public static int[] findIndexOfExtremaRange(int[] data, int begin, int end)
  {
    try
    {
      int minIndex = begin;
      int maxIndex = begin;

      int min = data[begin];
      int max = min;
      for (int i = begin; i < end; ++i)
      {
        if (data[i] < min)
        {
          minIndex = i;
          min = data[i];
        }
        else if (data[i] > max)
        {
          maxIndex = i;
          max = data[i];
        }
      }
      return new int[]
      {
        minIndex, maxIndex
      };
    }
    catch (IndexOutOfBoundsException ex)
    {
      MathAssert.assertRange(data, begin, end);
      throw ex;
    }
  }

  /**
   * Find the first element that meets a specified condition.
   *
   * @param vector is the vector to search.
   * @param begin is the start of the range, inclusive.
   * @param end is the end of the range, exclusive.
   * @param cond is the condition to be met.
   * @return the index of the first element to meet the condition.
   * @throws IndexOutOfBoundsException if requested range is outside of the
   * vector.
   */
  public static int findIndexOfFirstRange(
          int[] vector, int begin, int end,
          IntPredicate cond)
          throws IndexOutOfBoundsException
  {
    try
    {
      for (int i = begin; i < end; ++i)
      {
        if (cond.test(vector[i]))
          return i;
      }
      return -1;
    }
    catch (IndexOutOfBoundsException ex)
    {
      MathAssert.assertRange(vector, begin, end);
      throw ex;
    }
  }

  /**
   * Find the last element that meets a specified condition.
   *
   * @param vector is the vector to search.
   * @param begin is the start of the range, inclusive.
   * @param end is the end of the range, exclusive.
   * @param cond is the condition to be met.
   * @return the last element to meet the condition.
   * @throws IndexOutOfBoundsException if requested range is outside of the
   * vector.
   */
  public static int findIndexOfLastRange(
          int[] vector, int begin, int end,
          IntPredicate cond)
          throws IndexOutOfBoundsException
  {
    try
    {
      for (int i = end - 1; i >= begin; --i)
      {
        if (cond.test(vector[i]))
          return i;
      }
      return -1;
    }
    catch (IndexOutOfBoundsException ex)
    {
      MathAssert.assertRange(vector, begin, end);
      throw ex;
    }
  }

//</editor-fold>
//<editor-fold desc="equals" defaultstate="collapsed">
  public static boolean anyEquals(int[] in, int value)
  {
    for (int v : in)
      if (v == value)
        return true;
    return false;
  }

//</editor-fold>
//<editor-fold desc="conversion" defaultstate="collapsed">
  /**
   * Convert a list of ints into a list of doubles.
   * 
   * @param in
   * @return 
   */
  public static double[] promoteToDoubles(int[] in)
  {
    if (in == null)
      return null;
    double[] out = new double[in.length];
    for (int i = 0; i < in.length; ++i)
      out[i] = in[i];
    return out;
  }

  public static int[] toPrimitives(Integer[] in)
  {
    int out[] = new int[in.length];
    for (int i = 0; i < in.length; ++i)
      out[i] = in[i];
    return out;
  }

  public static int[] toPrimitives(Collection<? extends Number> in)
  {
    int i = 0;
    int out[] = new int[in.size()];
    for (Number f : in)
      out[i++] = f.intValue();
    return out;
  }

  static public Integer[] toObjects(int[] v)
  {
    if (v == null)
      return null;
    Integer[] out = new Integer[v.length];
    for (int i = 0; i < v.length; ++i)
      out[i] = v[i];
    return out;
  }

//</editor-fold>
//<editor-fold desc="functions" defaultstate="collapsed">
//</editor-fold>  
//<editor-fold desc="sort" defaultstate="collapsed">
//</editor-fold>
//<editor-fold desc="bit-bucket" defaultstate="collapsed">
//</editor-fold>  
}
