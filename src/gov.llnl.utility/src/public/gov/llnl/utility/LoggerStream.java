/*
 * Copyright 2016, Lawrence Livermore National Security, LLC. 
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.utility;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Utility to log text to a stream.
 *
 * This class is used the case in which we have a large amount of text to push
 * to a log and we want to avoid having to push it as multiple messages. Instead
 * we create a LoggerStream, push all the text to the stream and close the
 * stream to send the message to the log.
 *
 * {@code
 * <pre>
 *    try (PrintStream ls=LoggerStream.create(myLogger, Level.WARNING))
 *    {
 *       ls.println("Something went very wrong, let me explain...");
 *       ...
 *       ls.println("Okay that about wraps it up.");
 *    } // everything goes to the logger now
 *
 * </pre> }
 *
 *
 * author nelson85
 */
public class LoggerStream extends PrintStream
{
  private ByteArrayOutputStream baos;
  private final Level level;
  private final Logger logger;

  private LoggerStream(ByteArrayOutputStream baos, Logger logger, Level level)
  {
    super(baos);
    this.baos = baos;
    this.logger = logger;
    this.level = level;
  }

  static public LoggerStream create(Logger logger, Level level)
  {
    // Better to fail here than wait until we try to use the logger
    if (logger == null)
      throw new NullPointerException("logger is null");
    if (level == null)
      throw new NullPointerException("level is null");
    return new LoggerStream(new ByteArrayOutputStream(), logger, level);
  }

  @Override
  public void close()
  {
    // Flush the buffers so that we have the complete message.
    // Calling close did not work because it seems that calling close on super
    // causes a recursive behavior.
    super.flush();

    // We don't want the log to print this stack frame but rather the
    // frame that close was called from.  To handle this we will 
    // get the caller and method ourselves.
    StackTraceElement stack[] = (new Throwable()).getStackTrace();
    int i = 0;
    while (i < stack.length && stack[i].getClassName().equals("gov.llnl.utility.LoggerStream"))
    {
      i++;
    }

    // Convert the logger message into a string
    String msg = new String(baos.toByteArray());

    // Dump out the logger message with the specified level
    if (i != stack.length)
    {
      String className = stack[i].getClassName();
      String methodName = stack[i].getMethodName();
      logger.logp(level, className, methodName, msg);
    }
    else
      logger.log(level, msg);
  }
}
