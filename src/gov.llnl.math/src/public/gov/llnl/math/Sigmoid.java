/*
 * Copyright 2016, Lawrence Livermore National Security, LLC. 
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.math;

import java.util.function.DoubleUnaryOperator;

/**
 *
 * @author nelson85
 */
public class Sigmoid
{
  public static double evaluate(double v, double m, double w)
  {
    if (v > m)
    {
      return 1 / (1 + Math.exp(-w * (v - m)));
    }
    else
    {
      double e = Math.exp(w * (v - m));
      return e / (e + 1);
    }
  }

  public static DoubleUnaryOperator create(double m, double w)
  {
    return p -> evaluate(p, m, w);
  }
}
