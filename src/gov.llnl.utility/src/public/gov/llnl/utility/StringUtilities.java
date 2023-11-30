/* 
 * Copyright 2016, Lawrence Livermore National Security, LLC.
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.utility;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.Arrays;

/**
 * Utilities for operating on strings. Currently only has methods for joining
 * strings.
 */
public class StringUtilities
{
  /**
   * Join a collection of strings with a delimiter.
   *
   * @param strings is the collection to join
   * @param delimiter is the delimiter to place between strings
   * @return the merged strings
   */
  static public String join(Iterable<String> strings, String delimiter)
  {
    StringBuilder sb = new StringBuilder();
    String next = "";
    for (String s : strings)
    {
      sb.append(next).append(s);
      next = delimiter;
    }
    return sb.toString();
  }

  /**
   * Join a array of strings with a delimiter. Convenience function to
   * {@link #join(java.lang.Iterable, java.lang.String)}
   *
   * @param strings is the array to join
   * @param delimiter is the delimiter to place between strings
   * @return the merged strings
   */
  static public String join(String[] strings, String delimiter)
  {
    return join(Arrays.asList(strings), delimiter);
  }

  static public String getFixedString(ByteBuffer bb, int length)
  {
    try
    {
      byte content[] = new byte[length];
      bb.get(content);
      return (new String(content, "UTF8")).trim();
    }
    catch (UnsupportedEncodingException ex)
    {
      throw new RuntimeException(ex);
    }
  }
}
