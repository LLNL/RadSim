/*
 * Copyright 2016, Lawrence Livermore National Security, LLC. 
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */

/**
 *
 * @author nelson85
 */
public class RoundTest
{
  static public void main(String[] args)
  {
    double[] d =
    {
      0, 1, 12, 123, 1234, 12345, 123456, 1234567, 12345678, 123456789, 1234567891
    };
    for (int i = 1; i < 30; ++i)
    {
      for (int j = 0; j < d.length; ++j)
      {
        d[j] = d[j] / 10;
        System.out.println(String.format("%60s %.13e %s", Long.toBinaryString(getMantissa(d[j])), d[j], rep(d[j])));
      }
    }
  }

  static public String rep(double d)
  {
    String str = String.format("%.13e", d);
    return str.replaceAll("\\.?0+e", "e");
  }

  static public long getMantissa(double d)
  {
    long bits = Double.doubleToLongBits(d);
    boolean negative = (bits & 0x8000000000000000L) != 0;
    long exponent = bits & 0x7ff0000000000000L;
    long mantissa = (bits & 0x000fffffffffffffL) + 0x0010000000000000L;
    return mantissa;
  }
}
