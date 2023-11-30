/* 
 * Copyright 2016, Lawrence Livermore National Security, LLC.
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.math.distribution;

import java.io.Serializable;

/**
 *
 * @author nelson85
 */
public interface Distribution extends Serializable
{
  /**
   * Compute the probability density function for value x.
   *
   * @param x is a observed value and must fall in the domain.
   * @return the corresponding value.
   */
  double pdf(double x);

  /**
   * Compute the cumulative density function. The cumulative density function
   * (cdf) is the integral of the probability function between minus infinity
   * and x. Also known as the distribution function.
   *
   * @param x is the upper limit of the integrate and must be in the domain.
   * @return the corresponding value.
   */
  double cdf(double x);

  /**
   * Compute the complementary cumulative density function. The complementary
   * cumulative density function (ccdf) is the integral of the probability
   * function between x and positive infinity. Also known as the tail
   * distribution or the exceedance.
   *
   * @param x is the upper limit of the integrate and must be in the domain.
   * @return the corresponding value.
   */
  double ccdf(double x);

  /**
   * Compute the log of complementary cumulative density function. This is used
   * in computing the log significance of a finding. It should have special
   * handling when the significance is very small.
   *
   * @param x is the upper limit of the integrate and must be in the domain.
   * @return the corresponding value.
   */
  double logccdf(double x);

  /**
   * Compute the inverse cdf for value of x.
   *
   * @param x is a fraction and must be in the range of [0,1].
   * @return the corresponding value or NaN if out of range.
   */
  double cdfinv(double x);

}
