/*
 * Copyright 2016, Lawrence Livermore National Security, LLC. 
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.math.random;

import java.util.stream.DoubleStream;

/**
 * Random variable are created by Random Distributions to hold the parameters.
 *
 * @author nelson85
 */
public interface RandomVariable 
{
  double next();

  /**
   * Generate an infinite stream of random numbers from this source.
   *
   * @return a new DoubleStream.
   */
  default DoubleStream stream()
  {
    return DoubleStream.generate(() -> this.next());
  }
}
