/*
 * Copyright 2016, Lawrence Livermore National Security, LLC. 
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.utility;

import java.util.ArrayList;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * These utilities are to help with parsing double arrays and integer arrays
 * from files.
 *
 * @author nelson85
 */
public class ArrayUtilities
{
  public static double parseDoubleFromString(String data, int i0, int i1)
  {
    if (i1 == -1 || i1 > data.length())
      i1 = data.length();
    if (i0 > data.length())
      return 0;
    // Fast handling for 0-9
    if (i0 + 1 == i1 && data.charAt(i0) >= '0' && data.charAt(i0) <= '9')
      return (int) (data.charAt(i0) - '0');

    boolean neg = false;
    {
      // Handle sign
      char c = data.charAt(i0);
      if (c == '-')
      {
        neg = true;
        i0++;
      }
      if (c == '+')
      {
        i0++;
      }
    }

    // Handle mantissa
    int mant = 0;
    while (i0 < i1 && Character.isDigit(data.charAt(i0)))
    {
      char c = data.charAt(i0++);
      mant = mant * 10 + (int) (c - '0');
    }

    double out = mant;

    if (i0 < i1 && data.charAt(i0) == '.')
    {
      i0++;
      double place = 0.1;
      while (i0 < i1 && Character.isDigit(data.charAt(i0)))
      {
        char c = data.charAt(i0++);
        out += place * ((int) (c - '0'));
        place *= 0.1;
      }
    }

    // Combine the result
    if (neg)
      out = -out;
    if (i0 == i1)
      return out;

    {
      char c = data.charAt(i0++);
      if (c != 'e' && c != 'E')
        return out;
    }

    // Sign of exponential
    boolean negExp = false;
    {
      char c = data.charAt(i0);
      if (c == '-')
      {
        negExp = true;
        i0++;
      }
      if (c == '+')
        i0++;
    }

    // Handle exponential
    int exp = parseIntegerFromString(data, i0, i1);
    if (exp == 0)
      return out;
    return out * pow10(exp, negExp);
  }

  public static double pow10(int i, boolean neg)
  {
    double out = 1;
    double acc = 10;
    while (i > 0)
    {
      if ((i & 1) == 1)
      {
        out *= acc;
      }
      acc = acc * acc;
      i >>= 1;
    }
    return neg ? 1.0 / out : out;
  }

  public static int parseIntegerFromString(String data, int i0, int i1)
  {
    if (i0 + 1 == i1)
      return (int) (data.charAt(i0) - '0');
    if (i0 >= data.length())
      return 0;
    boolean neg = false;
    {
      // Handle sign
      char c = data.charAt(i0);
      if (c == '-')
      {
        neg = true;
        i0++;
      }
      if (c == '+')
      {
        i0++;
      }
    }
    int out = 0;
    while (i0 < i1)
    {
      char c = data.charAt(i0++);
      if (c < '0' || c > '9')
        break;
      out = out * 10 + (int) (c - '0');
    }
    if (neg)
      return -out;
    return out;
    //    return Integer.parseInt(data.substring(i0, i1));
  }

  /**
   * Fast method of converting comma separated list into an array of doubles.
   *
   * @param data
   * @param size
   * @param delim
   * @return
   */
  public static double[] parseDoubleArray(String data, int size, char delim)
  {
    double[] out = new double[size];
    // Inproved version for comma seperated fields
    int prev = 0;
    int n = data.length();
    for (int i = 0; i < size; i++)
    {
      int next = prev;
      while (next < n && data.charAt(next) != delim)
      {
        next++;
      }
      out[i] = parseDoubleFromString(data, prev, next);
      prev = next + 1;
    }
    return out;
  }

  public static int countDelimitor(String str, char delim)
  {
    int count = 0;
    for (int i = 0; i < str.length(); ++i)
    {
      if (str.charAt(i) == delim)
        count++;
    }
    return count;
  }

  /**
   * Split a string on tabs.
   *
   * @param s
   * @return
   */
  public static String[] fastSplit(String s)
  {
    int n = s.length();
    ArrayList<String> out = new ArrayList<>();
    int i0 = 0;
    int i1;
    while (i0 < n)
    {
      i1 = i0;
      while (i1 < n && s.charAt(i1) != 9)
      {
        i1++;
      }
      if (i0 != i1)
        out.add(s.substring(i0, i1));
      else
        out.add("");
      i0 = i1 + 1;
    }
    return out.toArray(new String[out.size()]);
  }

  /**
   * Fast method of converting comma separated list into an array of integers.
   *
   * @param data
   * @param size
   * @param delim
   * @return
   */
  public static int[] parseIntArray(String data, int size, char delim)
  {
    int[] out = new int[size];
    /*
    // This method was too slow for large files
    Matcher matcher = Pattern.compile("[-+]?\\d+").matcher(data);
    for (int i = 0; i < size; i++)
    {
    if (!matcher.find())
    break;
    out[i] = Integer.parseInt(matcher.group());
    }
     */
    // Inproved version for comma seperated fields
    int prev = 0;
    int n = data.length();
    for (int i = 0; i < size; i++)
    {
      int next = prev;
      while (next < n && data.charAt(next) != delim)
      {
        next++;
      }
      out[i] = parseIntegerFromString(data, prev, next);
      prev = next + 1;
    }
    return out;
  }

}
