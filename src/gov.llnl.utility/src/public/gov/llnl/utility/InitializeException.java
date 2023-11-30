/* 
 * Copyright 2016, Lawrence Livermore National Security, LLC.
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.utility;

/**
 * Exception for problems that occur during initialization.
 *
 * @see InitializeInterface#initialize()
 */
public class InitializeException extends Exception
{
  public InitializeException(String reason)
  {
    super(reason);
  }
}
