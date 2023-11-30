/*
 * Copyright 2016, Lawrence Livermore National Security, LLC.
 * All rights reserved
 *
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.math.function;

import java.util.function.DoubleUnaryOperator;

/**
 *
 * @author nelson85
 */
public class FunctionUtilities
{

  /**
   * Evaluate a function on an array of values.
   *
   * @param function is the function to evaluate.
   * @param values is the array.
   * @return is a new array evaluated for each element in values.
   */
  public static double[] evaluate(DoubleUnaryOperator function, double[] values)
  {
    return evaluateRange(function, values, 0, values.length);
  }

  /**
   * Evaluate a function on an array of values in a range.
   *
   * @param function is the function to evaluate.
   * @param values is the array.
   * @param begin is the start of the range (inclusive).
   * @param end is the end of the range (exclusive).
   * @return is a new array with the same length as the range containing the
   * evaluation for each element of values in the selected range.
   */
  public static double[] evaluateRange(DoubleUnaryOperator function, double[] values, int begin, int end)
  {
    double[] out = new double[end - begin];
    for (int i = 0; i < end - begin; ++i)
      out[i] = function.applyAsDouble(values[i + begin]);
    return out;
  }

}
