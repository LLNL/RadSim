/*
 * Copyright 2016, Lawrence Livermore National Security, LLC.
 * All rights reserved
 *
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.math;

import gov.llnl.utility.TemporalUtilities;
import java.time.temporal.TemporalAmount;

/**
 *
 * @author seilhan3
 */
public class TimeUtilities
{
  /**
   * Convert a time constant into a forgetting factor.
   *
   * @param timeConstant is the time constant of the exponential filter in time
   * units.
   * @param dt is the update rate of the exponential filter in time units.
   * @return
   */
  public static double convertTimeConstant(double timeConstant, double dt)
  {
    return -Math.expm1(-dt / timeConstant);
  }

  public static double convertTimeConstant(TemporalAmount timeConstant, TemporalAmount dt)
  {
    return convertTimeConstant(TemporalUtilities.toSeconds(timeConstant), TemporalUtilities.toSeconds(dt));
  }

}
