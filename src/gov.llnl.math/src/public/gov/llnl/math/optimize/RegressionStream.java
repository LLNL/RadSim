/*
 * Copyright 2016, Lawrence Livermore National Security, LLC.
 * All rights reserved
 *
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.math.optimize;

import gov.llnl.math.MathAssert;
import gov.llnl.math.function.Function;
import gov.llnl.utility.StreamUtilities;
import java.util.List;
import java.util.function.DoubleUnaryOperator;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 *
 * @author nelson85
 */
public class RegressionStream
{

  /**
   * Zips to arrays of doubles into a stream of regression points.
   *
   * @param x
   * @param y
   * @return
   */
  public static Stream<RegressionPoint> of(final double[] x, final double[] y)
  {
    MathAssert.assertEqualLength(x, y);
    return StreamUtilities.from(new Supplier<RegressionPoint>()
    {
      int i = 0;

      @Override
      public RegressionPoint get()
      {
        if (i == x.length)
          return null;

        int n = i;
        ++i;
        return new RegressionPoint()
        {
          @Override
          public double getX()
          {
            return x[n];
          }

          @Override
          public double getY()
          {
            return y[n];
          }

          @Override
          public double getLambda()
          {
            return 1;
          }
        };
      }
    });
  }

  static public void main(String[] args)
  {
    double[] x = new double[]
    {
      1, 2, 3
    };
    double[] y = new double[]
    {
      4, 6, 8
    };

    List<RegressionPoint> c = RegressionStream.of(x, y).collect(Collectors.toList());
    for (RegressionPoint i : c)
    {
      System.out.println(i.getX() + " " + i.getY());
    }

    System.out.println(RegressionStream.of(x, y).isParallel());

    DoubleUnaryOperator f = RegressionStream.of(x, y).collect(RegressionCollector.as(new LinearRegression()));
    System.out.println(f);
  }
}
