
import java.util.Random;

/*
 * Copyright 2016, Lawrence Livermore National Security, LLC. 
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
/**
 *
 * @author nelson85
 */
strictfp public class TestPow
{
  public static void main(String[] args)
  {
    // Simple Monte Carlo to determine the relative accuracy of two methods 
    // to compute A^B.   Short answer is that other than rounding they are 
    // identical.
    Random rand = new Random();
    rand.setSeed(0);
    int r1 = 0;
    int r2 = 0;
    double s1 = 0;
    double s2 = 0;
    int trials = 100000;
    for (int i = 0; i < trials; ++i)
    {
      double u = 10 * rand.nextDouble();
      double v = 3 * rand.nextDouble();
      double e1 = Math.pow(u, v);
      double e2 = Math.exp(Math.log(u) * v);
      double q1 = Math.pow(e1, 1 / v) - u;
      double q2 = Math.exp(Math.log(e2) / v) - u;
      if (q1 != 0)
        r1++;
      if (q2 != 0)
        r2++;
      s1 += q1 * q1;
      s2 += q2 * q2;
    }
  }
}
