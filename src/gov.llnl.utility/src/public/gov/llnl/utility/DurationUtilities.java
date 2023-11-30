/*
 * Copyright 2016, Lawrence Livermore National Security, LLC.
 * All rights reserved
 *
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.utility;

import java.util.regex.Pattern;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.regex.Matcher;

/**
 *
 * @author monterial1
 */
public class DurationUtilities
{

  private final static String REGEX = "P(?!$)(\\d+(?:\\.\\d+)?Y)?(\\d+(?:\\.\\d+)?M)?(\\d+(?:\\.\\d+)?W)?(\\d+(?:\\.\\d+)?D)?(T(?=\\d)(\\d+(?:\\.\\d+)?H)?(\\d+(?:\\.\\d+)?M)?(\\d+(?:\\.\\d+)?S)?)?$";
  private final static Pattern PATTERN = Pattern.compile(REGEX);

  /**
  * @return String representation of seconds
  */
  private static String getSeconds(String duration)
  {
    Matcher matcher = PATTERN.matcher(duration);
    if (matcher.matches()){
      return duration.substring(matcher.start(8), matcher.end(8)-1);
    } else {
      return new String("");
    }
  }

  /**
   * Format the string duration.
   *
   * Some people don't follow the standard. This is to catch those error in the numbering.
   *
   * Right now only formats seconds into having maximum of 9 fractional digits.
   *
   * @param duration
   * @return
   */
  public static String format(String duration) throws ParseException
  {
      DecimalFormat formatter = (DecimalFormat) DecimalFormat.getInstance();
      formatter.setMaximumFractionDigits(9);
      formatter.setGroupingSize(100);
      if (duration.contains("S"))
      {
        String seconds = getSeconds(duration);
        String seconds2 = formatter.format(formatter.parse(seconds).doubleValue());
        String duration2 = duration.replace(seconds + "S", seconds2 + "S");
        return duration2;
      }
      return duration;
  }
}

