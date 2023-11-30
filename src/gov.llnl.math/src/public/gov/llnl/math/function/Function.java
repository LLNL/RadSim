/*
 * Copyright 2016, Lawrence Livermore National Security, LLC.
 * All rights reserved
 *
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.math.function;

import gov.llnl.math.MathExceptions.DomainException;
import gov.llnl.math.MathExceptions.RangeException;
import gov.llnl.utility.xml.bind.ReaderInfo;
import java.util.function.DoubleUnaryOperator;

/**
 * Simple single value function with one parameter.
 * consider a switch to  DoubleUnaryOperator
 * @author nelson85
 */
@ReaderInfo(FunctionReader.class)
public interface Function extends DoubleUnaryOperator
{
  /**
   * Evaluate the function at a point. The value requested must be within the
   * domain of the function.
   *
   * @param x is the point to evaluate.
   * @return the value at the point to be evaluated.
   * @throws DomainException if the point is outside of the defined domain for
   * this function.
   */
  @Override
  double applyAsDouble(double x) throws DomainException;

  public interface Parameterized extends Function
  {
    /**
     * Get a vector with the parameters. May by a copy of the values. Do not
     * alter the contents returned.
     *
     * @return the parameters for this function.
     */
    double[] toArray();
  }

  public interface Invertable extends Function
  {
    /**
     * Compute the value that evaluates to the given value. May not produce a
     * result if the value is not in the range of the function.
     *
     * @param y
     * @return the value that satisfies f(x)=y.
     */
    double inverse(double y) throws RangeException;
  }

  public interface Differentiable extends Function
  {
    /**
     * Compute the derivative for the function at a specified point.
     *
     * @param x
     * @return the derivative at the point if it exists.
     */
    double derivative(double x) throws DomainException;
  }

}
