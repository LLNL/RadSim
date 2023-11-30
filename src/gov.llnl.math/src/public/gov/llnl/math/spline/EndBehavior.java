/* 
 * Copyright 2016, Lawrence Livermore National Security, LLC.
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.math.spline;

// Defines for end point extrapolation
public enum EndBehavior
{
  CLAMP(0), 
  LINEAR(1), 
  CUBIC(2);
  int v;

  EndBehavior(int v)
  {
    this.v = v;
  }

}
