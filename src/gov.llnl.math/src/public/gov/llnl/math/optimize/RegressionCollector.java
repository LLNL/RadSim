/*
 * Copyright 2016, Lawrence Livermore National Security, LLC.
 * All rights reserved
 *
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.math.optimize;

import java.util.EnumSet;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.DoubleUnaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

/**
 *
 * @author nelson85
 */
public class RegressionCollector
{

  /**
   * Converts a regression method to a collector.
   *
   * @param <Model>
   * @param <Method>
   * @param method
   * @return
   */
  public static <Model extends DoubleUnaryOperator, Method extends Regression<Model>> Collector<RegressionPoint, Method, Model>
          as(Method method)
  {
    return new Collector<RegressionPoint, Method, Model>()
    {
      @Override
      public Supplier<Method> supplier()
      {
        return () -> method;
      }

      @Override
      public BiConsumer<Method, RegressionPoint> accumulator()
      {
        return (Method method, RegressionPoint datum) -> method.add(datum.getX(), datum.getY(), datum.getLambda());
      }

      @Override
      public BinaryOperator<Method> combiner()
      {
        return null;
      }

      @Override
      public Function<Method, Model> finisher()
      {
        return (Method m) -> m.compute();
      }

      @Override
      public Set<Collector.Characteristics> characteristics()
      {
        return EnumSet.of(Characteristics.UNORDERED);
      }
    };
  }
}
