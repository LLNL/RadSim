/*
 * Copyright 2016, Lawrence Livermore National Security, LLC. 
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.utility;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.lang.reflect.Parameter;

/**
 *
 * @author nelson85
 */
public class ReflectionUtilities
{

  public static <T, A> BiConsumer<T, A> convertMethod(final Method method)
  {
    if (method.getParameterCount()!=1 && !Modifier.isStatic(method.getModifiers()))
      throw new RuntimeException("Must take 2 arguments");
    method.setAccessible(true);
    return (T t, A u) ->
    {
      try
      {
        method.invoke(t, u);
      }
      catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex)
      {
        throw new RuntimeException(ex);
      }
    };
  }

  /**
   * Find a method by name for a given class taking an argument. This will be
   * used during the transition from method references to lambdas for the
   * reader.
   *
   * @param <T>
   * @param <A>
   * @param methodName
   * @param cls
   * @param argument
   * @return
   */
  public static <T, A> BiConsumer<T, A> getMethod(String methodName, Class<T> cls, Class<A> argument)
  {
    try
    {
      final Method method = cls.getMethod(methodName, argument);
      return (T obj, A t) ->
      {
        try
        {
          method.invoke(obj, t);
        }
        catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex)
        {
          throw new RuntimeException(ex);
        }
      };
    }
    catch (NoSuchMethodException | SecurityException ex)
    {
      throw new RuntimeException(ex);
    }
  }

  /**
   * Find a constructor named by a java type that produces a specified class
   * given no arguments. This is used to hide the implementation of internal
   * classes by providing a factory method on the public interface to produce an
   * object from the private interface.
   *
   * @param <R>
   * @param clsName
   * @param resultType
   * @return
   */
  @SuppressWarnings("unchecked")
  public static <R> Supplier<R> getConstructor(String clsName, Class<R> resultType)
  {
    try
    {
      Class<?> cls = Class.forName(clsName);
      final Constructor<?> ctor = cls.getDeclaredConstructor();
      if (!resultType.isAssignableFrom(cls))
        throw new ClassCastException("Unable to cast " + cls + " to " + resultType);
      ctor.setAccessible(true);
      return () ->
      {
        try
        {
          return (R) ctor.newInstance();
        }
        catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex)
        {
          throw new RuntimeException(ex);
        }
      };
    }
    catch (ClassNotFoundException | NoSuchMethodException ex)
    {
      throw new RuntimeException(ex);
    }
  }

  /**
   * Find a constructor named by a java type that produces a specified class
   * given one argument. This is used to hide the implementation of internal
   * classes by providing a factory method on the public interface to produce an
   * object from the private interface.
   *
   * @param <R>
   * @param <A>
   * @param clsName
   * @param resultType
   * @param argument
   * @return
   */
  @SuppressWarnings("unchecked")
  public static <R, A> Function<A, R> getConstructor(String clsName, Class<R> resultType, Class<A> argument)
  {
    try
    {
      Class<?> cls = Class.forName(clsName);
      final Constructor<?> ctor = cls.getConstructor(argument);
      if (!resultType.isAssignableFrom(cls))
        throw new ClassCastException("Unable to cast " + cls + " to " + resultType);
      ctor.setAccessible(true);
      return (A t) ->
      {
        try
        {
          return (R) ctor.newInstance(t);
        }
        catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex)
        {
          throw new RuntimeException(ex);
        }
      };
    }
    catch (ClassNotFoundException | NoSuchMethodException ex)
    {
      throw new RuntimeException(ex);
    }
  }
//
//  /**
//   * Create a new instance of a class by name. This is used when the
//   * implementation of a class is the private package.
//   *
//   * @param <T>
//   * @param clsName
//   * @param arguments is a list of arguments. These arguments may not be null.
//   * @return a new instance of the class.
//   */
//  public static <T> T newInstance(String clsName, Object... arguments)
//  {
//    try
//    {
//      Class[] argumentCls = new Class[arguments.length];
//      for (int i = 0; i < arguments.length; ++i)
//      {
//        argumentCls[i] = arguments[i].getClass();
//      }
//      Class<T> cls = (Class<T>) Class.forName(clsName);
//      Constructor<T> ctor = null;
//
//      // For the love of all things holy, why can't reflection provide even
//      // the most basic class utilities?  Here we have to search the 
//      // constructor parameters ourselves because java reflection can't match 
//      // parameter lists with extends.
//      for (Constructor<?> init : cls.getConstructors())
//      {
//        if (argumentsMatch(init.getParameters(), argumentCls))
//        {
//          ctor = (Constructor<T>) init;
//          break;
//        }
//      }
//      if (ctor == null)
//        throw new NoSuchMethodException();
//
//      ctor.setAccessible(true);
//      return ctor.newInstance(arguments);
//    }
//    catch (ClassNotFoundException | SecurityException | NoSuchMethodException
//            | InstantiationException | IllegalAccessException | IllegalArgumentException ex)
//    {
//      throw new RuntimeException(ex);
//    }
//    catch (InvocationTargetException ex)
//    {
//      throw new RuntimeException(ex.getTargetException());
//    }
//  }
//
//  public static boolean argumentsMatch(Parameter[] parameters, Class... argumentCls)
//  {
//    if (parameters.length != argumentCls.length)
//      return false;
//    int i = 0;
//    for (Parameter parameter : parameters)
//    {
//      if (!parameter.getType().isAssignableFrom(argumentCls[i++]))
//        return false;
//    }
//    return true;
//  }

  /**
   * Create a new instance of a class by name. This is used when the
   * implementation of a class is the private package.
   *
   * @param <T>
   * @param clsName
   * @param arguments is a list of arguments. These arguments may not be null.
   * @return a new instance of the class.
   */
  @SuppressWarnings("unchecked")
  public static <T> T newInstance(String clsName, Object... arguments)
  {
    try
    {
      Class[] argumentCls = new Class[arguments.length];
      for (int i = 0; i < arguments.length; ++i)
      {
        argumentCls[i] = arguments[i].getClass();
      }
      Class<T> cls = (Class<T>) Class.forName(clsName);
      Constructor<T> ctor = null;

      // For the love of all things holy, why can't reflection provide even
      // the most basic class utilities?  Here we have to search the 
      // constructor parameters ourselves because java reflection can't match 
      // parameter lists with extends.
      for (Constructor<?> init : cls.getConstructors())
      {
        if (argumentsMatch(init.getParameters(), argumentCls))
        {
          ctor = (Constructor<T>) init;
          break;
        }
      }
      if (ctor == null)
        throw new NoSuchMethodException();

      ctor.setAccessible(true);
      return ctor.newInstance(arguments);
    }
    catch (ClassNotFoundException | SecurityException | NoSuchMethodException
            | InstantiationException | IllegalAccessException | IllegalArgumentException ex)
    {
      throw new RuntimeException(ex);
    }
    catch (InvocationTargetException ex)
    {
      throw new RuntimeException(ex.getTargetException());
    }
  }

  public static boolean argumentsMatch(Parameter[] parameters, Class... argumentCls)
  {
    if (parameters.length != argumentCls.length)
      return false;
    int i = 0;
    for (Parameter parameter : parameters)
    {
      if (!parameter.getType().isAssignableFrom(argumentCls[i++]))
        return false;
    }
    return true;
  }

}
