/* 
 * Copyright 2016, Lawrence Livermore National Security, LLC.
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.math.random;

import static gov.llnl.math.DoubleUtilities.cube;
import gov.llnl.utility.UUIDUtilities;
import java.io.Serializable;

/**
 *
 * @author nelson85
 */
public class GammaRandom extends RandomFactory implements Serializable
{
  private static final long serialVersionUID = UUIDUtilities.createLong("GammaRandom");
  NormalRandom normalGenerator = null;

  public GammaRandom()
  {
    super(getDefaultGenerator());
    normalGenerator = new NormalRandom(getGenerator());
  }

  public GammaRandom(RandomGenerator generator)
  {
    super(generator);
    normalGenerator = new NormalRandom(getGenerator());
  }

  /**
   * Generate a Gamma distributed distribution.
   * <p>
   * There are two definitions of the gamma function. One uses the "scale",
   * while the other uses "rate". The scale is one over the rate.
   *
   * @see
   * <a
   * href="http://www.hongliangjie.com/2012/12/19/how-to-generate-gamma-random-variables/">reference</a>
   * @param alpha is the shape
   * @param beta is the rate
   * @return a random draw.
   */
  public double draw(double alpha, double beta)
  {
    RandomGenerator random = getGenerator();

    // Handling alpha less that 1
    if (alpha < 1.0)
    {
      double d = draw(alpha + 1, beta);
      double u = random.nextDouble();
      return d * Math.pow(u, 1.0 / alpha);
    }

    // Marsaglia and Tsang Method
    double d = alpha - 1.0 / 3.0;
    double c = Math.sqrt(9.0 * d);
    while (true)
    {
      double u = random.nextDouble();
      double z = normalGenerator.draw();
      if (z > -c)
      {
        double v = cube(1 + z / c);
        if (Math.log(u) < 0.5 * z * z + d - d * v + d * Math.log(v))
          return d * v * beta;
      }
    }
  }

  public RandomVariable newVariable(double alpha, double beta)
  {
    return () -> draw(alpha, beta);
  }
  
  @Override
  protected void setGenerator(RandomGenerator r)
  {
    super.setGenerator(r);
    this.normalGenerator.setGenerator(r);
  }
}
