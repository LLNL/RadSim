/*
 * Copyright 2016, Lawrence Livermore National Security, LLC. 
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.utility;

import java.lang.annotation.Annotation;
import java.lang.annotation.Repeatable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

/**
 *
 * @author nelson85
 */
public class AnnotationUtilities
{
  /**
   * Utility to help retrieve repeated annotations.
   *
   * @param <T>
   * @param cls
   * @param annotation
   * @return
   */
  @SuppressWarnings("unchecked")
  public static <T extends Annotation> Collection<T> getRepeatingAnnotation(Class cls, Class<T> annotation)
  {
    // Find the repeatable annotation type
    Repeatable repeatable = annotation.getAnnotation(Repeatable.class);
    if (repeatable != null)
    {
      Annotation list = cls.getAnnotation(repeatable.value());
      if (list != null)
      {
        try
        {
          Method method = list.getClass().getMethod("value");
          return Arrays.asList((T[]) method.invoke(list));
        }
        catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex)
        {
          throw new RuntimeException(ex);
        }
      }
    }

    // If empty list, try the single. 
    T single = (T) cls.getAnnotation(annotation);
    if (single == null)
      return Collections.emptyList();
    return Arrays.asList(single);
  }

}
