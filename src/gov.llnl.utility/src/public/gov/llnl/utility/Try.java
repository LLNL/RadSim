/*
 * Copyright 2016, Lawrence Livermore National Security, LLC. 
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.utility;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 *
 * @author nelson85
 */
public class Try
{
  @FunctionalInterface
  public interface ExceptionalBiConsumer<T, T2>
  {
    void accept(T t1, T2 t2) throws Exception;
  }

  public static <T, T2> BiConsumer<T, T2> promote(ExceptionalBiConsumer<T, T2> functor)
  {
    return (T t, T2 u) ->
    {
      try
      {
        functor.accept(t, u);
      }
      catch (Exception ex)
      {
        throw new RuntimeException(ex);
      }
    };
  }

  @FunctionalInterface
  public interface ExceptionalConsumer<T>
  {
    void accept(T t1) throws Exception;
  }

  public static <T> Consumer<T> promote(ExceptionalConsumer<T> functor)
  {
    return (T t) ->
    {
      try
      {
        functor.accept(t);
      }
      catch (Exception ex)
      {
        throw new RuntimeException(ex);
      }
    };
  }

//  void foo(int i) throws FileNotFoundException
//  {
//  }
//
//  static public void main(String[] args)
//  {
//    ExceptionalBiConsumer<Try, Integer> i0 = Try::foo;
//    BiConsumer<Try, Integer> i1= Try.promote(Try::foo);
//  }
}
