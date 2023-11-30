/*
 * Copyright 2019, Lawrence Livermore National Security, LLC.
 * All rights reserved
 *
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.utility;

import java.time.Duration;
import java.time.temporal.TemporalAmount;

/**
 *
 * @author nelson85
 */
public class TemporalUtilities
{
  static public double toSeconds(Duration i)
  {
    // Directly calling toNanos may result in an exception thus it is better
    // to convert it from the parts.
    double out = i.getSeconds();
    out += i.getNano() * 1e-9;
    return out;
  }

  static public double toSeconds(TemporalAmount i)
  {
    if (i instanceof Duration)
      return TemporalUtilities.toSeconds((Duration) i);
    return TemporalUtilities.toSeconds(Duration.from(i));
  }

  public static Duration ofSeconds(double d)
  {
    long seconds = (long) d;
    long nanos = (long) ((d - seconds) * 1e9);
    return Duration.ofSeconds(seconds, nanos);
  }
}
