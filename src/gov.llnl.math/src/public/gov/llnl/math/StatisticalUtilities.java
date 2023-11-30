/* 
 * Copyright 2016, Lawrence Livermore National Security, LLC.
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.math;

import java.util.Arrays;
import java.util.OptionalInt;

/**
 * This is currently a holder function for a bunch of statistical functions. As
 * this is reference material, it may not be using by all projects.
 *
 * Most of the functions we have here are currently Power transforms used to
 * either variance stabilize or symmetrize statistical distributions. Although
 * with the advent of high power computing, any statistical distribution can be
 * processed. We often need to combine statistical metrics for applications such
 * as detection together from multiple algorithms. Use of these transforms when
 * combining a diverse set of metrics allows for easier interpretation.
 *
 * Material needing to be incorporated.
 *
 * @see <a
 * href="http://www.math.utep.edu/Faculty/moschopoulos/Publications/1993-On_a_Data_Based_Power_Transformation_for_Reducing_Skewness.pdf">Skewness</a>
 *
 * @see <a
 * href="http://pages.cs.wisc.edu/~zifeng/849/GLM%20Transformation.pdf">GML
 * transformation</a>
 * @author nelson85
 */
public class StatisticalUtilities
{
  /**
   * Anscombe transform for variance stabilization of a Poisson random variable.
   * Converts a Poisson random variable to have a variance of 1 with a mean of
   * 2\Sqrt(m+3/8)-1/(4\Sqrt(m)). Good for distributions with a mean as low as
   * 4.
   *
   * @see
   * <a href="http://en.wikipedia.org/wiki/Anscombe_transform">wikipedia</a>
   *
   * @param d
   * @return the evaluation of the transform.
   */
  static public double poissonAnscombeTransform(double d)
  {
    return 2.0 * Math.sqrt(d + 3.0 / 8.0);
  }

  /**
   * Anscombe transform to variance stabilize a Binomial random variable.
   *
   * @see
   * <a
   * href="http://spectrum.library.concordia.ca/973582/1/Chaubey_MudholkarPreprint1983.pdf">On
   * the symmetrizing transforms of random variables</a>
   * @param d
   * @return the evaluation of the transform.
   */
  static public double binomialAnscombeTransform(double d)
  {
    return Math.asin(Math.sqrt((d + 3.0 / 8.0) / (1 + 3.0 / 4.0)));
  }

  /**
   * Freeman-Turkey transform for variance stabilization of a Poisson random
   * variable. Converts a Poisson random variable to have a variance of 1.
   *
   * @see
   * <a href="http://en.wikipedia.org/wiki/Anscombe_transform">wikipedia</a>
   *
   * @param d
   * @return the evaluation of the transform.
   */
  static public double poissonFreemanTukeyTransform(double d)
  {
    return Math.sqrt(d + 1) + Math.sqrt(d);
  }

  /**
   * Box-cox is a rank preserving transform used to variance stabilize a number
   * of empirical distributions.
   *
   * @see
   * <a href="http://en.wikipedia.org/wiki/Power_transform">wikipedia</a>
   *
   * @param d
   * @param lambda
   * @return the evaluation of the transform.
   */
  static public double boxCoxTransform1(double d, double lambda)
  {
    if (lambda == 0)
      return Math.log(d);
    return (Math.pow(d, lambda) - 1.0) / lambda;
  }

  /**
   * Linear combination method for variance reduction and symmetrization of chi
   * squared distribution with a defined number of degrees of freedom.
   *
   * Based on the L combination in referenced paper. Maximum error is less than
   * 1% for one degree of freedom and falls to 1e-4 by 5 degrees of freedom.
   *
   * We have unitized the output by subtracting the mean and dividing by the
   * variance.
   *
   * @see
   * <a
   * href="http://www.sciencedirect.com/science/article/pii/S0167947304001069">A
   * normal approximation for the chi-square distribution</a>
   *
   * @param value is the observed random variable to be transformed.
   * @param df is the number of degrees of freedom for the distribution.
   * @return a unitized, symmetrized distribution.
   */
  static public double chiSquaredTransform(double value, double df)
  {
    double df2 = df * df;
    double df3 = df * df * df;
    double R = value / df;
    double mean = 5.0 / 6.0 - 1 / 9.0 / df - 7.0 / 648.0 / df2 + 25 / 2187.0 / df3;
    double var = 1.0 / 18.0 / df + 1.0 / 162.0 / df2 - 37.0 / 11664.0 / df3;
    // Original function using pow
    //double L=Math.pow(R, 1.0/6.0)-1.0/2.0*Math.pow(R,1.0/3.0)+1.0/3.0*Math.pow(R, 0.5);
    // Log version (4.6 times faster)

    double q = Math.log(R);
    double L = Math.exp(q / 6) - Math.exp(q / 3) / 2 + Math.exp(q / 2) / 3;
    return (L - mean) / Math.sqrt(var);
  }

  static public double gammaTransform(double value, double shape, double scale)
  {
    return chiSquaredTransform(2 * value / scale, 2 * shape);
  }

  static public OptionalInt mode(int... v)
  {
    // Empty list means there is no mode.
    if (v == null || v.length == 0)
      return OptionalInt.empty();

    // We are going to mutate the input so make a copy first
    int[] v2 = v.clone();

    // Sort it so that we can perform O(1) mode determination
    Arrays.sort(v2);

    // Set up structure to find the most common value
    int lastValue = Integer.MAX_VALUE;

    // Initial guess 
    int count = 1;
    int value = v2[v2.length / 2]; // Assume the median value if all appear only once
    int maxCount = 1;
    for (int i = 0; i < v.length; ++i)
    {
      int current = v2[i];
      // If we are repeating then we may be the mode
      if (current == lastValue)
      {
        count++;

        // We have a repeat so this may be the mode
        if (count > maxCount)
        {
          value = lastValue;
          maxCount = count;
        }
      }
      else
      {
        // New value start counting from 1 again.
        count = 1;
      }
      lastValue = current;
    }

    // We have at least one mode.
    return OptionalInt.of(value);
  }
}
