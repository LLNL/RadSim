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
public interface RandomGenerator
{
  /**
   * Set a seed for the random generator.
   *
   * @param seed
   */
  void setSeed(long seed);

  int nextInt();

  /**
   * Get a new random number.
   *
   * @return a random number between 0 and 1.
   */
  double nextDouble();
}
