/*
 * Copyright 2016, Lawrence Livermore National Security, LLC. 
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.utility;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author forsyth2
 */
public class ProcessBuilderUtilities
{
  /**
   * Convert a string with quoted spaces into an array of strings. This is
   * needed to help with ProcessBuilder.
   *
   * @param str is the string to be parsed.
   * @return an array split on spaces unless quoted.
   */
  public static List<String> parseExecutable(String str)
  {
    List<String> list = new ArrayList<>();
    Matcher m = Pattern.compile("(([^\" ]*(\"[^\"]*\")*)*)\\s*").matcher(str);
    while (m.find())
    {
      list.add(m.group(1).replace("\"", ""));
    }
    return list;
  }
}
