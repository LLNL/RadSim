/*
 * Copyright 2016, Lawrence Livermore National Security, LLC. 
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package support;

import gov.llnl.math.random.Random48;

/**
 *
 * @author nelson85
 */
strictfp public class IntegerTestGenerator
{
  long seed = 1L;
  public Random48 rand = new Random48(seed);

  public int uniformRand(int min, int max)
  {
    return (int) (rand.nextDouble() * (max - min + 1) + min);
  }

  public int[] newArray(int len)
  {
    int[] out = new int[len];
    for (int i = 0; i < len; ++i)
      out[i] = uniformRand(-9999, 10000);
    return out;
  }

}
