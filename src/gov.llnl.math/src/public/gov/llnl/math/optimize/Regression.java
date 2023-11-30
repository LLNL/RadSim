/*
 * Copyright 2016, Lawrence Livermore National Security, LLC.
 * All rights reserved
 *
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.math.optimize;

import gov.llnl.math.MathExceptions;
import java.util.function.DoubleUnaryOperator;

/**
 * Base class to compute regression onto a model.
 *
 * @param <Model>
 */
public interface Regression<Model extends DoubleUnaryOperator>
{

  /**
   * Get the total number of regression points added thus far.
   *
   * @return the number of added elements.
   */
  int getNumObservations();

  /**
   * Add a point to the regression without weighting.
   *
   * @param x
   * @param y
   */
  void add(double x, double y);

  /**
   * Add a point to the regression with a weight lambda.
   *
   * @param x
   * @param y
   * @param lambda is the weight to apply to the measurement.
   */
  void add(double x, double y, double lambda);

  /**
   * Compute the slope and intercept the represents the linear regression for
   * this point.
   *
   * @return an array holding the slope and offset.
   * @throws MathExceptions.ConvergenceException
   */
  Model compute()
          throws MathExceptions.ConvergenceException;

}
