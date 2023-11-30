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
 * Generator for Binomial random distribution.
 *
 * This is algorithm BTPE from:
 *
 * Kachitvichyanukul, V. and Schmeiser, B. W.
 *
 * Binomial Random Variate Generation. Communications of the ACM, 31, 2
 * (February, 1988) 216.
 *
 * The original paper is was for a FORTRAN algorithm.
 *
 * @author nelson85
 */
public final class BinomialRandom extends RandomFactory implements Serializable
{
  private static final long serialVersionUID = UUIDUtilities.createLong("BinomialRandom");

  public BinomialRandom()
  {
    super(getDefaultGenerator());
  }

  public BinomialRandom(RandomGenerator random)
  {
    super(random);
  }

  @Override
  public void setGenerator(RandomGenerator random)
  {
    super.setGenerator(random);
  }

  public RandomVariable newVariable(final int n, final double p)
  {
    double r = Math.min(p, 1 - p);
    double q = 1 - r;
    if (n * r < 30)
      return new BinomialVariable1(n, p, q, r);
    return new BinomialVariable2(n, p, q, r);
  }

  class BinomialVariable1 implements RandomVariable
  {
    private final int n;
    private final double p;

    // Derived quantities used to zone the generator
    private final double r;
    private final double q;
    private final double qn;
    private final double rr;
    private final double g;

    BinomialVariable1(int n, double p, double q, double r)
    {
      this.n = n;
      this.p = p;
      this.q = q;
      this.r = r;
      qn = Math.pow(q, n);
      rr = r / q;
      g = rr * (n + 1);
    }

    @Override
    public double next()
    {
      RandomGenerator rng = getGenerator();
      // If the expected value of the number was less than 30, then it
      // is faster to use the recursion relationships
      int ix = 0;
      double f = qn;
      double u = rng.nextDouble();

      while (u >= f)
      {
        if (ix > 110)
        {
          ix = 0;
          f = qn;
          u = rng.nextDouble();
        }

        u = u - f;
        ix = ix + 1;
        f = f * (g / ix - rr);
      }

      if (p > 0.5)
        return n - ix;
      return ix;
    }
  }

  class BinomialVariable2 implements RandomVariable
  {
    private final int n;
    private final double p;

    // Derived quantities used to zone the generator
    private final double r;
    private final double q;
    private final double fm;
    private final int M;
    private final double nrq;
    private final double p1;
    private final double c;
    private final double xm;
    private final double xl;
    private final double xr;
    private final double xll;
    private final double xlr;
    private final double p2;
    private final double p3;
    private final double p4;

    BinomialVariable2(int n, double p, double q, double r)
    {
      this.n = n;
      this.p = p;
      this.q = q;
      this.r = r;

      fm = n * r + r;
      M = (int) Math.floor(fm);
      nrq = n * r * q;
      p1 = Math.floor(2.195 * Math.sqrt(nrq) - 4.6 * q) + 0.5;
      xm = M + 0.5;
      xl = xm - p1;
      xr = xm + p1;
      c = 0.134 + 20.5 / (15.3 + M);
      double a = (fm - xl) / (fm - xl * r);
      xll = a * (1 + a / 2);
      a = (xr - fm) / (xr * q);
      xlr = a * (1 + a / 2);
      p2 = p1 * (1 + 2 * c);
      p3 = p2 + c / xll;
      p4 = p3 + c / xlr;
    }

    @Override
    public double next()
    {
      RandomGenerator rng = getGenerator();
      int y;
      while (true)
      {
        // Step 1 generate u for selecting the region
        // if region 1 generate trangular distributed variate
        double u = rng.nextDouble() * p4;
        double v = rng.nextDouble();

        double x;

        if (u <= p1)
        {
          // Region 1, Triangular distributed (always accepted)
          y = (int) Math.floor(xm - p1 * v + u);
          break;
        }

        // Step 2 Region 2 parallelograms
        if (u <= p2)
        {
          x = xl + (u - p1) / c;
          v = v * c + 1 - Math.abs(M - x + 0.5) / p1;
          if (v > 1)
            continue;

          y = (int) Math.floor(x);
        }

        // Step 3 Region 3, left exponential tail
        else if (u <= p3)
        {
          y = (int) Math.floor(xl + Math.log(v) / xll);
          if (y < 0)
            continue;
          v = v * (u - p2) * xll;
        }

        // Step 4 Region 4, right exponential tail
        else
        {
          y = (int) Math.floor(xr - Math.log(v) / xlr);
          if (y > n)
            continue;
          v = v * (u - p3) * xlr;
        }

        // We have produced a number but we need to correct the
        // distribution by determining value is within the distribution
        // with the correct frequency.
        // Step 5 test for appropriate method of evaluation f(y)
        double k = Math.abs(y - M);
        if ((k > 20) && (k < nrq / 2 - 1))
        {
          // 5.2  Squeezing
          double rho = (k / nrq) * (k * (k / 3 + 0.625) + 1 / 6) / nrq + 0.5;
          double t = -k * k / (2 * nrq);
          double A = Math.log(v);

          // Accept or reject
          if (A < t - rho)
            break;
          else if (A > t + rho)
            continue;

          // 5.3 Final Acceptance test
          double x1 = y + 1;
          double f1 = M + 1;
          double z = n + 1 - M;
          double w = n - y + 1;

          double x2 = x1 * x1;
          double f2 = f1 * f1;
          double z2 = z * z;
          double w2 = w * w;
          t = xm * Math.log(f1 / x1) + (n - M + 0.5) * Math.log(z / w)
                  + (y - M) * Math.log(w * r / (x1 * q))
                  + (13860. - (462. - (132. - (99. - 140. / f2) / f2) / f2) / f2) / f1 / 166320.
                  + (13860. - (462. - (132. - (99. - 140. / z2) / z2) / z2) / z2) / z / 166320.
                  + (13860. - (462. - (132. - (99. - 140. / x2) / x2) / x2) / x2) / x1 / 166320.
                  + (13860. - (462. - (132. - (99. - 140. / w2) / w2) / w2) / w2) / w / 166320.;
          if (A <= t)
            break;
        }
        else
        {
          // 5.1 Evaluate f(y) via the recursive relationshipt f(y)=f(y-1)(a/x-s)
          double s = r / q;
          double a = s * (n + 1);
          double F = 1;
          int i;

          if (M < y)
          {
            i = M;
            while (true)
            {
              i = i + 1;
              F = F * (a / (double) i - s);
              if (i == y)
                break;
            }

          }
          else if (M > y)
          {
            i = y;
            while (1 == 1)
            {
              i = i + 1;
              F = F / (a / i - s);
              if (i == M)
                break;
            }
          }

          // Accept or reject
          if (v <= F)
            break;
        }
      }

      // Step 6 return to correct form for values about 0.5
      if (p > 0.5)
        return n - y;
      else
        return y;
    }
  }
}
