/*
 * Copyright 2016, Lawrence Livermore National Security, LLC. 
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.utility;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author nelson85
 */
public class LoggerUtilities
{
  /**
   * Convenience function for issuing logging messages.
   *
   * @param logger
   * @param level
   * @param fmt
   * @param var
   */
  static public void format(Logger logger, Level level, String fmt, Object... var)
  {
    if (logger.isLoggable(level))
      logger.log(level, String.format(fmt, var));
  }
}
