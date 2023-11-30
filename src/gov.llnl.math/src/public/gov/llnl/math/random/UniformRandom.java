/*
 * Copyright 2016, Lawrence Livermore National Security, LLC. 
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.math.random;

import gov.llnl.utility.UUIDUtilities;
import java.io.Serializable;

/**
 *
 * @author nelson85
 */
public class UniformRandom extends RandomFactory implements Serializable
{
  private static final long serialVersionUID = UUIDUtilities.createLong("UniformRandom");

  public UniformRandom()
  {
    super(getDefaultGenerator());
  }

  public UniformRandom(RandomGenerator random)
  {
    super(random);
  }

  public RandomVariable newVariable(double min, double max)
  {
    return new UniformVariable(min, max);
  }

  class UniformVariable implements RandomVariable
  {
    double min, range;

    private UniformVariable(double min, double max)
    {
      this.range = max - min;
      this.min = min;
    }

    @Override
    public double next()
    {
      return getGenerator().nextDouble() * range + min;
    }
  }  
}
