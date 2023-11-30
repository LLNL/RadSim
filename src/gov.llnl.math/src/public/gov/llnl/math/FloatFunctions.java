/*
 * Copyright 2016, Lawrence Livermore National Security, LLC. 
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.math;

/**
 * These are special functions used for porting to floating point only
 * architectures.
 *
 * @author nelson85
 */
public class FloatFunctions
{
  /** 
   * Fast implementation of the exponential function.
   * 
   * @param d
   * @return 
   */
  static public float exp(float d)
  {
    // We can use log(2) to reduce the term to between 0 and 0.69
    float z = d / 0.69314718055994530942f;
    int n = (int) (z + 0.5f);

    // Watch for bottoming out the exponential term 
    if (n < -140)
      return 0;
    if (n < -126)
      n = -126;

    // Compute the fractional portion
    float f = (d - n * 0.69314718055994530942f);
    float f2 = f * f;

    float expf = 1 + 2 * f / (2 - f + f2 / (6 + f2 / (10 + f2 / 14)));

    // Our result is exp(x)= 2^(floor(x/log(2))*exp(x-log(2)*floor(x/log(2)))
    return expf * Float.intBitsToFloat((n + 127) << 23);
  }

  static public float tanh(float d)
  {
    if (d < 0)
    {
      float e2 = exp(2 * d);
      float e2m1 = expm1(2 * d);
      return e2m1 / (e2 + 1);
    }
    else
    {
      float e2 = exp(-2 * d);
      float e2m1 = exp(-2 * d);
      return -e2m1 / (1 + e2);
    }
  }

  static public float expm1(float z)
  {
    // If outside of these limits then we don't require the expm1 function
    // as round off is not really an issue.  
    if (z > 0.25 || z < -0.25)
    {
      return exp(z) - 1;
    }

    // We need 5 terms to achieve an accuracy of 3e-9 which 
    // is smaller than the expected error due to rounding at the boundary
    // conditions.  
    //
    // We can improve the speed of this method by adding an additional
    // check point where we can cut off additional terms. 
    //
    // It is unclear where the error is occurring in the lowest order
    // bits.
    //
    // We can improve the convergence speed by transforming the 
    // equation into a z^2 continued fraction if that sequence exists.
    return z / (1 - z / (2 + z / (3 - z / (2 + z / (5 - z / 2)))));
  }

}
