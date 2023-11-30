/*
 * Copyright 2016, Lawrence Livermore National Security, LLC. 
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.math.optimize;

/**
 *
 * @author nelson85
 */
public interface RegressionPoint
{
  /**
   * Get the x coordinate for the regression point.
   *
   * @return
   */
  double getX();

  /**
   * Get the y coordinate for the regression point.
   *
   * @return
   */
  double getY();

  /**
   * Get the weight associated with this regression point.
   *
   * @return
   */
  double getLambda();
}
