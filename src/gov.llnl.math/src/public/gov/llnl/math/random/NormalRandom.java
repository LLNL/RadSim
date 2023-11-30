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
public class NormalRandom extends RandomFactory implements Serializable
{
    private static final long serialVersionUID = UUIDUtilities.createLong("NormalRandom");
  private double y1, y2;
  private boolean reuse = false;

  public NormalRandom()
  {
    super(getDefaultGenerator());
  }

  public NormalRandom(RandomGenerator generator)
  {
    super(generator);
  }

  /**
   * Box-Muller transform for generating two random numbers.
   *
   * @return a normal random variable from N(0,1)
   */
  public double draw()
  {
    RandomGenerator random = getGenerator();
    if (reuse)
    {
      reuse = false;
      return y2;
    }
    reuse = true;
    double x1, x2, w;
    do
    {
      x1 = 2.0 * random.nextDouble() - 1.0;
      x2 = 2.0 * random.nextDouble() - 1.0;
      w = x1 * x1 + x2 * x2;
    }
    while (w >= 1.0);

    w = Math.sqrt((-2.0 * Math.log(w)) / w);
    y1 = x1 * w;
    y2 = x2 * w;
    return y1;
  }

  public RandomVariable newVariable(double mean, double std)
  {
    return () -> std * draw() + mean;
  }

}
