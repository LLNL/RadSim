/* 
 * Copyright 2016, Lawrence Livermore National Security, LLC.
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.utility.io;

import gov.llnl.utility.xml.bind.ReaderContext;
import gov.llnl.utility.xml.bind.ElementContextImpl;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.util.Arrays;

/**
 * Exception thrown when processing with an ObjectReader.
 *
 * @author nelson85
 */
public class ReaderException extends Exception
{
  static final boolean SURPRESS = true;
  private final PathTrace pathTrace = new PathTrace();
  StackTraceElement reference;

  public ReaderException(String reason)
  {
    super(reason);
  }

  public ReaderException(Throwable ex)
  {
    super(ex);
  }

  public ReaderException(String reason, Throwable ex)
  {
    super(reason, ex);
  }

  /**
   * Prints this throwable and its backtrace to the specified print stream. Adds
   * the file location trace when the exception occurred. It also trims off
   * internal classes.
   *
   * @param s
   */
  @Override
  public void printStackTrace(PrintStream s)
  {
    PrintWriter pw = new PrintWriter(s);
    this.printStackTrace(pw);
    pw.flush();
  }

  @Override
  public void printStackTrace(PrintWriter s)
  {
    // if we have a location for the print we can capture it here
    s.println("ReaderException: " + this.getMessage());

    if (this.reference != null)
      s.println("    referenced by " + this.reference.toString());
    if (this.pathTrace != null)
      s.println(this.pathTrace.toString());

    // if we have a localion for the print we can capture it here
    dumpTrace(s, this.getStackTrace(), null);

    Throwable from = this.getCause();
    while (from != null)
    {
      s.println("Caused by " + from.getClass().getSimpleName() + ": " + from.getMessage());
      dumpTrace(s, from.getStackTrace(), this.getStackTrace()[1]);
      from = from.getCause();
    }
  }

  private void dumpTrace(PrintWriter writer, StackTraceElement[] stackTrace,
          StackTraceElement endPoint)
  {
    boolean suppressed = false;
    for (StackTraceElement st : stackTrace)
    {
      if (st.equals(endPoint))
        break;
      if (SURPRESS && (st.isNativeMethod() || st.getClassName().startsWith("com.sun.org.")
              || st.getClassName().startsWith("sun.reflect")
              || st.getClassName().startsWith("org.apache")))
      {
        suppressed = true;
        continue;
      }
      if (suppressed == true)
        writer.println("        ...");
      suppressed = false;
      writer.println("        " + st.toString());
    }
  }

  public final ReaderException addPathLocation(PathLocation context)
  {
    this.pathTrace.add(context);
    return this;
  }

  public final ReaderException addLocation(Path context)
  {
    try
    {
      this.pathTrace.add(new PathLocation(context.toUri().toURL(), -1, null));
      return this;
    }
    catch (MalformedURLException ex)
    {
      throw new RuntimeException(ex);
    }
  }
  
  public PathTrace getLocation()
  {
    return this.pathTrace;
  }

  public final ReaderException addLocation(URL context)
  {
    this.pathTrace.add(new PathLocation(context, -1, null));
    return this;
  }

  public static ReaderException throwOnFile()
  {
    return new ReaderException("Error reading file.");
  }

  public static ReaderException throwOnFile(Exception cause)
  {
    return new ReaderException("Error reading file.", cause);
  }

  public static ReaderException throwOnUnknownElement(ReaderContext context)
  {
    ElementContextImpl hc = (ElementContextImpl) context.getCurrentContext();
    return new ReaderException("Unknown element " + hc.getLocalName());
  }

  public ReaderException filterOut(String pattern)
  {
    StackTraceElement[] st = this.getStackTrace();
    boolean removed = false;
    // Remove elements
    for (int i = 0; i < st.length; i++)
    {
      if (st[i].getClassName().startsWith(pattern))
      {
        st[i] = null;
        removed = true;
      }
    }

    // compact list
    if (removed)
    {
      int i = 0;
      while (i < st.length && st[i] != null)
        i++;

      int j = i;
      while (j < st.length)
      {
        while (j < st.length && st[j] == null)
          j++;
        if (j == st.length)
          break;
        st[i] = st[j];
        i++;
        j++;
      }
      this.setStackTrace(Arrays.copyOfRange(st, 0, i));
    }
    return this;
  }

  public ReaderException setReferencePoint(StackTraceElement trace)
  {
    this.reference = trace;
    return this;
  }
}
