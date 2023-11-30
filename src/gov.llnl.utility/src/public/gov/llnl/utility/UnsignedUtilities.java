/* 
 * Copyright 2016, Lawrence Livermore National Security, LLC.
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.utility;

/**
 * Utility functions for converting to unsigned number in Java. Java lacks the
 * ability to handle unsigned numbers. This is a problem for file formats that
 * store their data as unsigned. The correct solution is to promote the number
 * to the next largest size. These utility functions do that promotion.
 */
public class UnsignedUtilities
{
  /**
   * Convert an unsigned short into a integer.
   */
  static public int getUnsignedShort(short s)
  {
    int out = s;
    if (out < 0)
      out += 65536;
    return out;
  }

  /**
   * Convert an unsigned integer into a long.
   */
  static public long getUnsignedInt(int i)
  {
    long out = i;
    if (out < 0)
      out += 4294967296l;
    return out;
  }
}
