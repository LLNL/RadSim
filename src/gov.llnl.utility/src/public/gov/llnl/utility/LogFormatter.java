/*
 * Copyright 2016, Lawrence Livermore National Security, LLC. 
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.utility;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.time.Instant;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;

/**
 * Specialization for a log formatting.
 *
 * This uses a cache to make sure it only repeats the header information if
 * there has been a change.
 *
 * FIXME, not sure this is worth having at the top level of the utility package.
 * It is not used much currently.
 *
 * @author nelson85
 */
public class LogFormatter extends Formatter
{
  String previousClassName = "";
  String previousMethodName = "";

  @Override
  public String format(LogRecord lr)
  {
    try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PrintStream ps = new PrintStream(baos))
    {
//      StringBuilder sb = new StringBuilder();

      // Add headers only if needed
      boolean header = false;
      if (previousClassName == null || !previousClassName.equals(lr.getSourceClassName()))
      {
        header = true;
      }
      if (previousMethodName == null || !previousMethodName.equals(lr.getSourceMethodName()))
      {
        header = true;
      }

      // Print the header
      if (header)
      {
        this.previousClassName = lr.getSourceClassName();
        this.previousMethodName = lr.getSourceMethodName();

        // Convert the date
        String timestamp = Instant.ofEpochMilli(lr.getMillis()).toString();
        ps.append(timestamp).append(": ")
                .append(this.previousClassName).append(" ")
                .append(this.previousMethodName).append("\n");
      }

      // Convert the level into a prefix
      String prefix = "";
      if (lr.getLevel() == Level.SEVERE)
        prefix = "SEVERE: ";
      else if (lr.getLevel() == Level.WARNING)
        prefix = "WARNING: ";
      else if (lr.getLevel() == Level.CONFIG)
        prefix = "CONFIG: ";
      else if (lr.getLevel() == Level.INFO)
        prefix = "  ";
      else if (lr.getLevel() == Level.FINE)
        prefix = "    ";
      else if (lr.getLevel() == Level.FINER)
        prefix = "      ";
      else if (lr.getLevel() == Level.FINEST)
        prefix = "        ";

      // Convert the message into text
      String mesg = this.formatMessage(lr);

      // Process the message with the prefix
      Tokenizer tokenizer = Tokenizer.create("[^\\n]*\\n", "[^\\n]*");
      for (Tokenizer.Token token : tokenizer.matcher(mesg))
      {
        ps.append(prefix).append(mesg, token.start(), token.end());
        if (token.id() == 1)
          ps.append("\n");
      }

      // Print any throwable information
      if (lr.getThrown() != null)
        lr.getThrown().printStackTrace(ps);
      ps.flush();
      return new String(baos.toByteArray(), "UTF8");
    }
    catch (IOException ex)
    {
      throw new RuntimeException(ex);
    }

  }
}
