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
 * Verified to match output of fpoisrand
 *
 * @author nelson85
 */
public class PoissonRandom extends RandomFactory implements Serializable
{
  private static final long serialVersionUID = UUIDUtilities.createLong("PoissonRandom");

  public PoissonRandom()
  {
    super(getDefaultGenerator());
  }

  public PoissonRandom(RandomGenerator random)
  {
    super(random);
  }

  public int draw(double lambda)
  {
    return this.draw(this.getGenerator(), lambda);
  }

  public int[] draw(double lambda[])
  {
    int[] out = new int[lambda.length];
    for (int i = 0; i < lambda.length; ++i)
    {
      out[i] = draw(lambda[i]);
    }
    return out;
  }

  @Override
  public void setGenerator(RandomGenerator rg)
  {
    super.setGenerator(rg);
  }

  public RandomVariable newVariable(double lambda)
  {
    return () -> draw(lambda);
  }

//<editor-fold desc="internal">
  public static int draw(RandomGenerator rand, double lambda)
  {
    if (lambda <= 0)
      return 0;
    if (lambda > 5)
      return drawHormannPoisson(rand, lambda);
    return drawCumulativePoisson(rand, lambda);
  }

  public static int drawCumulativePoisson(RandomGenerator random, double lambda)
  {
    double g = Math.exp(-lambda);
    double cumprod = random.nextDouble();
    int out = 0;
    while (cumprod > g)
    {
      cumprod *= random.nextDouble();
      out++;
    }
    return out;
  }
  static final double table[] =
  {
    0, 0, 0.6931471806, 1.791759469,
    3.178053830, 4.787491743, 6.579251212, 8.525161361,
    10.60460290, 12.80182748,
    15.104412573, 17.502307846, 19.987214496, 22.552163853,
    25.191221183, 27.899271384, 30.671860106, 33.505073450
  };

  static double logfac(int x)
  {
    if (x > 17)
      return 0;
    if (x < 0)
      return 0;
    return table[x];
  }

// Based on the Hormann transformational rejection generator (1993) for the
//   Poisson distribution; it appears to be both fast and "uniformly fast"
//   (see http://citeseer.csail.mit.edu/151115.html; PTRD algorithm)
//   (on average, appears to be at least twice as fast as the simple
//    cumulative exponential generator)
//
// (sorry for the magic constants throughout, but they're approximations that
//  help speed things up)
//
// Note that approximations are generally good for lambda > 10
//
  public static int drawHormannPoisson(RandomGenerator random, double lambda)
  {
    // Note that the lambda value has been floor()'d by the type system
    double smu = Math.sqrt(lambda);
    double b = 0.931 + 2.53 * smu;
    double a = -0.059 + 0.02483 * b;
    double inv_alpha = 1.1239 + 1.1328 / (b - 3.4);
    double vr = 0.9277 - 3.6224 / (b - 2.0);
    double us;
    double stirling_etc;
    double log_lambda = Math.log(lambda);
    double magic = 0.9189385332046727; // log(sqrt(2*3.1415926535897931));
    double k;

    while (true)
    {
      double U;
      double v = random.nextDouble();
      if (v < 0.86 * vr)
      {
        U = v / vr - 0.43;
        return (int) Math.floor((2.0 * a / (0.5 - (U < 0 ? -U : U)) + b) * U + lambda + 0.445);
      }
      if (v > vr)
      {
        U = random.nextDouble() - 0.5;
      }
      else
      {
        U = v / vr - 0.93;
        U = (U < 0 ? -1 : 1) * 0.5 - U;
        v = random.nextDouble() * vr;
      }
      us = 0.5 - (U < 0 ? -U : U);

      if ((us < 0.013) && (v > us))
        continue;   // continue big loop by going back to step 1

      k = Math.floor((2.0 * a / us + b) * U + lambda + 0.445);

      v *= inv_alpha / (a / (us * us) + b);

      stirling_etc = (k + 0.5) * Math.log(lambda / k)
              - lambda - magic + k
              - (1 / 12.0 - 1.0 / (360 * k * k)) / k;

      if ((k >= 18) && (Math.log(v * smu) <= stirling_etc))
      {
        return (int) k;
      }

      if ((k >= 0) && (k <= 17)
              && (Math.log(v) <= k * log_lambda - lambda - logfac((int) k)))
        return (int) k;

    }
  }
//</editor-fold>
}
