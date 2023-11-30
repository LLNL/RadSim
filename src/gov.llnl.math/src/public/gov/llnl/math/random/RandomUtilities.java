/*
 * Copyright 2016, Lawrence Livermore National Security, LLC. 
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.math.random;

/**
 *
 * @author nelson85
 */
public class RandomUtilities
{
  static public double[] drawArray(RandomVariable rv, int length)
  {
    double[] out = new double[length];
    for (int i = 0; i < length; ++i)
    {
      out[i] = rv.next();
    }
    return out;
  }
}
