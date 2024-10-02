/*
 * Copyright 2022, Lawrence Livermore National Security, LLC.
 * All rights reserved.
 *
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.math.interp;

import gov.llnl.math.interp.MultiInterpolator.Evaluator;
import java.util.function.DoubleUnaryOperator;
import java.util.function.Supplier;

/**
 * MultiFunction is a set of functions with a shared domain.
 *
 * Often we need a table in which many functions share the same domain. When
 * interpolating between points it is not necessary to recompute the location
 * unless we are changing the x value. Thus, this implementation provides
 * accelerated lookup for when the data is structured like this.
 *
 * To use this we first create a MultiFunction from the data table. Then we
 * create an evaluator which acts as a cursor within the data. Each cursor is
 * independent and only share resources stored in the MultiFunction. As cursors
 * are not reentrant, each thread should create its own cursors. Classes should
 * not hold a cursor unless they themselves are evaluators.
 *
 * @author nelson85
 */
public interface MultiInterpolator extends Supplier<Evaluator>
{

  /**
   * Create a linear interpolator.
   *
   * @param x is the domain of the function.
   * @param y is an array of codomains.
   * @return a new MultiInterpolator.
   */
  static MultiInterpolator createLinear(double[] x, double[]

    ...y)
  {
    return new MultiLinearInterp(x, y);
  }

  /**
   * Create a log-log interpolator.
   *
   * Often when working with radiation data, log-log is a better interpolator.
   *
   * @param x is the domain for the function.
   * @param y is an array of codomains.
   * @return a new MultiInterpolator.
   */
  static MultiInterpolator createLogLog(double[] x, double[]... y)
  {
    return new MultiLogLogInterp(x, y);
  }

  /**
   * Cursor for MultiInterpolator.
   */
  public interface Evaluator
  {

    /**
     * The number of functions tied to to this evaluator.
     *
     * @return the number of functions stored
     */
    int size();

    /**
     * Move the cursor to a new location.
     *
     * @param x
     */
    void seek(double x);

    /**
     * Evaluate one of the functions at the current seek point.
     *
     * @param index is the function number.
     * @return the value for that function at the seek location.
     */
    double evaluate(int index);

    /**
     * Get all of the values from the evaluate method as an array.
     *
     * @param result is the memory to hold the result.
     */
    default void evaluateAll(double[] result)
    {
      for (int i = 0; i < result.length; ++i)
      {
        result[i] = evaluate(i);
      }
    }

    /**
     * Get an operator for this function.
     *
     * This is useful when we need deal with a function independently such as
     * plotting data for validation.
     *
     * All instances are tied to the same seek method, so this code is not
     * reentrant.
     *
     * @param index is the function index.
     * @return a new double function corresponding to index.
     */
    default DoubleUnaryOperator get(int index)
    {
      return (x) ->
      {
        seek(x);
        return evaluate(index);
      };
    }
  }

}
