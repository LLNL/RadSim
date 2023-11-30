/*
 * Copyright 2016, Lawrence Livermore National Security, LLC. 
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.utility;

import gov.llnl.utility.annotation.Internal;
import java.io.Serializable;
import java.util.Map;

/**
 *
 * Utility to add the context that lead to an exception to be thrown.
 * 
 * Example:
 * <pre>
 * {@code
 *     if (Double.isNaN(out[i]))
 *     {
 *       throw Exceptions.builder(new RuntimeException("NaN in output"))
 *               .put("x", x).put("y", y).put("q", q)
 *               .put("inner", inner)
 *               .put("n", nearest(x, q)).get();
 *     }
 * }
 * </pre>
 */
public class Exceptions
{

  /**
   * Get a map containing any attached contexts that have been attached to a
   * throwable.
   *
   * @param throwable is the throwable to probe.
   * @return is a map of strings to objects, it may be empty.
   */
  public static Map<String, Object> getContext(Throwable throwable)
  {
    Throwable t1 = throwable;
    while (t1.getCause() != null)
    {
      t1 = t1.getCause();
      if (t1 instanceof ExceptionContext)
      {
        return ((ExceptionContext) t1).getContext();
      }
    }
    return new ArrayMap<>();
  }

  /**
   * Attach a context to a throwable.
   *
   * @param <Type> is the base type of the throwable.
   * @param throwable is a throwable to attach context to.
   * @param key is a unique key for this object.
   * @param value is the value to be stored.
   * @return the original throwable for chaining or rethrowing.
   */
  public static <Type extends Throwable> Type add(Type throwable, String key, Serializable value)
  {
    Throwable t1 = throwable;
    while (t1.getCause() != null && !(t1 instanceof ExceptionContext))
      t1 = t1.getCause();

    ExceptionContext ex = null;
    if (t1 instanceof ExceptionContext)
    {
      ex = (ExceptionContext) t1;
    }
    else
    {
      ex = new ExceptionContext();
      t1.initCause(ex);
    }

    ex.put(key, value);
    return throwable;
  }

  /**
   * Builder to add more than one item to a context.
   *
   * @param <Type>
   * @param t
   * @return
   */
  public static <Type extends Throwable> ExceptionContextBuilder<Type> builder(Type t)
  {
    return new ExceptionContextBuilder<>(t);
  }

//<editor-fold desc="internal">
  public static class ExceptionContextBuilder<Type extends Throwable>
  {
    Type t;

    ExceptionContextBuilder(Type t)
    {
      this.t = t;
    }

    @SuppressWarnings("ThrowableResultIgnored")
    public ExceptionContextBuilder<Type> put(String key, Serializable value)
    {
      add(t, key, value);
      return this;
    }

    public Type get()
    {
      return t;
    }
  }

  /**
   * Private dummy exception used to store that any context for an exception.
   */
  @Internal
  final static class ExceptionContext extends Throwable
  {
    ArrayMap<String, Object> context = null;

    ExceptionContext()
    {
      // Force the stack trace to be empty because this is not a real exception.
      setStackTrace(new StackTraceElement[0]);
    }

    /**
     * The message to be displayed to the user. Indicates what context 
     * we can probe.
     *
     * @return
     */
    @Override
    public String toString()
    {
      StringBuilder sb = new StringBuilder();
      sb.append("Exception context with contents: ");
      sb.append(StringUtilities.join(context.keySet(), ","));
      return sb.toString();
    }

    /**
     * Add a key value to the context store.
     *
     * @param key
     * @param o
     */
    void put(String key, Serializable o)
    {
      if (context == null)
        context = new ArrayMap<>();
      context.put(key, o);
    }

    /**
     * Get the key value store.
     *
     * @return
     */
    public Map<String, Object> getContext()
    {
      if (context == null)
        context = new ArrayMap<>();
      return context;
    }
  }
//</editor-fold>
}
