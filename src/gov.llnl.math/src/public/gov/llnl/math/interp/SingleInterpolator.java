/*
 * Copyright 2022, Lawrence Livermore National Security, LLC.
 * All rights reserved.
 *
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.math.interp;

import gov.llnl.math.interp.SingleInterpolator.Evaluator;
import java.util.function.DoubleUnaryOperator;
import java.util.function.Supplier;

/**
 */
public interface SingleInterpolator extends Supplier<Evaluator>
{

  /**
   * Create a linear interpolator.
   *
   * @param x is the domain of the function.
   * @param y is an array of codomains.
   * @return a new MultiInterpolator.
   */
  static SingleInterpolator createLinear(double[] x, double[] y)
  {
    return new SingleLinearInterp(x, y);
  }

  static SingleInterpolator createLogLog(double[] x, double[] y)
  {
    return new SingleLogLogInterp(x, y);
  }

  /**
   * Cursor for SingleInterpolator.
   */
  public interface Evaluator extends DoubleUnaryOperator
  {

    /**
     * Move the cursor to a new location.
     *
     * @param x
     */
    void seek(double x);

    /**
     * Evaluate one of the functions at the current seek point.
     *
     * @return the value for that function at the seek location.
     */
    double evaluate();

    @Override
    default double applyAsDouble(double value)
    {
      seek(value);
      return evaluate();
    }

    default double[] applyAll(double[] values)
    {
      double[] out = new double[values.length];
      for (int i = 0; i < values.length; ++i)
      {
        seek(values[i]);
        out[i] = evaluate();
      }
      return out;
    }

  }

}
