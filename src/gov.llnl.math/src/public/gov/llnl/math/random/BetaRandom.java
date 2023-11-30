/*
 * Copyright 2016, Lawrence Livermore National Security, LLC. 
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.math.random;

import gov.llnl.math.MathExceptions.DomainException;
import static gov.llnl.math.random.RandomFactory.getDefaultGenerator;
import java.io.Serializable;

/**
 *
 * @author nelson85
 */
public class BetaRandom extends RandomFactory implements Serializable
{
  GammaRandom grand;

  public BetaRandom()
  {
    super(getDefaultGenerator());
    grand = new GammaRandom(this.getGenerator());
  }

  public BetaRandom(RandomGenerator generator)
  {
    super(generator);
    grand = new GammaRandom(this.getGenerator());
  }

  /**
   * Draw a new random instance.
   *
   * @param a
   * @param b
   * @return
   */
  public double draw(double a, double b)
  {
    double ua = grand.draw(a, 1);
    double ub = grand.draw(b, 1);
    return ua / (ua + ub);
  }
 
  @Override
  protected void setGenerator(RandomGenerator random)
  {
    super.setGenerator(random);
    grand.setGenerator(random);
  }

  public RandomVariable newVariable(double a, double b)
          throws DomainException
  {
    if (a <= 0 || b <= 0)
      throw new DomainException("Distribution is not realizable");
    return () -> draw(a, b);
  }
  
  public double getMean(double a, double b)
  {
    return a/(a+b);
  }

  public double getVariance(double a, double b)
  {
   return a*b/(a+b)/(a+b)/(a+b+1); 
  }
  /**
   * Create a new Beta random variable from expected mean and variance.
   *
   * It is an error if u*(1-u) &lt; v, as this would not be a valid beta
   * distribution.
   *
   * @param u is the mean of the distribution.
   * @param v is the variance of the distribution.
   * @return
   * @throws gov.llnl.math.MathExceptions.DomainException
   */
  public RandomVariable newVariableFromMeanVar(double u, double v)
          throws DomainException
  {
    if (v<=0)
      throw new DomainException("variance is invalid");
    double x = u * (1 - u) / v - 1;
    final double a = x * u;
    final double b = x - a;
    return newVariable(a, b);
  }

//  static class StatisticsCollector
//  {
//    int n = 0;
//    double s1 = 0;
//    double s2 = 0;
//    double s3 = 0;
//
//    public void add(double d)
//    {
//      n++;
//      s1 += d;
//      s2 += d * d;
//      s3 += d * d * d;
//    }
//
//    public double getMean()
//    {
//      return s1 / n;
//    }
//
//    public double getSampleVariance()
//    {
//      return (s2 - (s1 * s1) / n) / n;
//    }
//
//    public double getSampleStd()
//    {
//      return Math.sqrt(getSampleVariance());
//    }
//  }

//  static public void main(String[] args)
//  {
//    double a = 1;
//    double b = 6;
//    RandomVariable beta = new BetaRandom().newVariable(a, b);
//  
//    StatisticsCollector sc = new StatisticsCollector();
//    for (int i = 0; i < 100000; ++i)
//    {
//      sc.add(beta.next());
//    }
//
//    System.out.println(sc.getMean());
//    System.out.println(a / (a + b));
//    System.out.println(sc.getSampleVariance());
//    System.out.println(a * b / (a + b) / (a + b) / (a + b + 1));
//  }
  
//  static public void main(String[] args)
//  {
//    RandomVariable beta = new BetaRandom().newVariableFromMeanVar(0.01, 0.02*0.02);
//  
//    StatisticsCollector sc = new StatisticsCollector();
//    for (int i = 0; i < 1000000; ++i)
//    {
//      sc.add(beta.next());
//    }
//
//    System.out.println(sc.getMean());
//    System.out.println(sc.getSampleVariance());
//  }
  
}
