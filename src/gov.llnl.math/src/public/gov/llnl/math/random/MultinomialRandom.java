/*
 * Copyright 2016, Lawrence Livermore National Security, LLC.
 * All rights reserved
 *
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.math.random;

import gov.llnl.math.DoubleArray;

/**
 *
 * @author nelson85
 */
public class MultinomialRandom extends RandomFactory
{

  public MultinomialRandom()
  {
    super(getDefaultGenerator());
  }

  public MultinomialRandom(RandomGenerator random)
  {
    super(random);
  }

  public int[] draw(double[] means, int k)
  {
    BinomialRandom b = new BinomialRandom(this.getGenerator());
    int n = means.length;
    double s = DoubleArray.sum(means);
    int[] out = new int[n];
    for (int i = 0; i < n; ++i)
    {
      double m = means[i];
      if (m <= 0)
        continue;
      double p = m / s;
      int n1 = (int) b.newVariable(k, p).next();
      out[i] = n1;
      k -= n1;
      s -= m;
    }
    return out;
  }

}
