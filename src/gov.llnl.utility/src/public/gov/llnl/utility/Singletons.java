/*
 * Copyright 2016, Lawrence Livermore National Security, LLC.
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.utility;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Utility class to declare singletons.
 *
 * @author nelson85
 */
final public class Singletons
{
  /**
   * All classes that implement Singleton must implement a static method
   * {@code getInstance}.
   */
  public interface Singleton
  {
  }

  public interface SingletonRequired
  {
  }

  @SuppressWarnings("unchecked")
  static public <T> T getSingleton(Class<T> cls)
  {
    try
    {
      Method method = cls.getDeclaredMethod("getInstance");
      if (method == null)
        throw new RuntimeException("No instance method for " + cls.getCanonicalName());
      return (T) method.invoke(null);
    }
    catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex)
    {
      throw new RuntimeException("Unable to get instance for " + cls.getCanonicalName(), ex);
    }
  }
}
