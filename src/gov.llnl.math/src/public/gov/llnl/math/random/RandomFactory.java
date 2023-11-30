/*
 * Copyright 2016, Lawrence Livermore National Security, LLC.
 * All rights reserved
 *
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.math.random;

import java.io.Serializable;

/**
 *
 * @author nelson85
 */
public abstract class RandomFactory implements Serializable
{
  private static final RandomGenerator defaultGenerator = new Random48();
  private RandomGenerator generator = null;

  public RandomFactory(RandomGenerator random)
  {
    generator = random;
  }

  public void setSeed(long seed)
  {
    generator.setSeed(seed);
  }

  public RandomGenerator getGenerator()
  {
    return generator;
  }

  public static RandomGenerator getDefaultGenerator()
  {
    return defaultGenerator;
  }

  // This proved hard to manage when a random generator has multiple
  // dependent generators.  Thus the generator can only be set at
  protected void setGenerator(RandomGenerator random)
  {
    this.generator = random;
  }

}
