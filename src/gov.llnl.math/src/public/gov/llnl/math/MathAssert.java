/*
 * Copyright 2016, Lawrence Livermore National Security, LLC. 
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.math;

import java.lang.reflect.Array;

/**
 *
 * @author nelson85
 */
public class MathAssert
{
  /**
   * Assert that two vectors are equal length.
   *
   * @param a is the first operand.
   * @param b is the second operand.
   * @throws IndexOutOfBoundsException if the vectors are different lengths.
   */
  public static <T1, T2> void assertEqualLength(T1 a, T2 b, String... msg)
          throws IndexOutOfBoundsException
  {
    if (a == null || b == null)
      throw new NullPointerException("Null Object" + parseVariable(msg) + ", operand1=" + (a == null) + " operand2=" + (b == null));
    if (Array.getLength(a) != Array.getLength(b))
      throw new IndexOutOfBoundsException("Size mismatch" + parseVariable(msg) + ", operand1=" + Array.getLength(a)
              + " operand2=" + Array.getLength(b));
  }

  /**
   * Assert that the array covers the required range.
   *
   * @param array is the array to check
   * @param begin is the start of the range (inclusive)
   * @param end is the end or the range (exclusive)
   * @param msg is the name of the variable with the error (optional)
   * @throws IndexOutOfBoundsException
   */
  public static <T> void assertRange(T array, int begin, int end, String... msg)
          throws IndexOutOfBoundsException
  {
    int len = Array.getLength(array);
    if (end > len || begin < 0)
    {
      throw new IndexOutOfBoundsException("Range error" + parseVariable(msg) + ", size=" + len + " begin=" + begin + " end=" + end);
    }
  }

  public static <T> void assertLengthEqual(T array, int sz, String... msg)
          throws IndexOutOfBoundsException
  {
    int len = Array.getLength(array);
    if (len == sz)
      return;
    throw new IndexOutOfBoundsException("Vector length incorrect" + parseVariable(msg) + ", expected=" + sz + " size=" + len);
  }

  /**
   * Assert that the value is not NaN.
   *
   * @param x
   */
  public static void assertNotNaN(double x)
  {
    if (x != x)
      throw new RuntimeException("Value is NaN");
  }

  /**
   * Assert that all values in an array are not NaN.
   *
   * @param x
   */
  public static void assertNotNaN(double[] x)
  {
    for (int i = 0; i < x.length; ++i)
      if (x[i] != x[i])
        throw new RuntimeException("NaN in vector");
  }

  public static void assertSortedDoubleUnique(double[] array) throws RuntimeException
  {
    for (int i = 1; i < array.length; i++)
    {
      if (array[i - 1] >= array[i] || array[i - 1] != array[i - 1])
        throw new RuntimeException("Not Sorted Unique vector");
    }
  }

  private static String parseVariable(String[] msg)
  {
    if (msg == null || msg.length == 0)
      return "";
    return " on " + msg[0];
  }

}
