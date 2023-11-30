/*
 * Copyright 2016, Lawrence Livermore National Security, LLC.
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.utility.io;

/**
 *
 * @author nelson85
 */
public class WriterException extends Exception
{
  public WriterException(String msg)
  {
    super(msg);
  }

  public WriterException(String msg, Throwable ex)
  {
    super(msg, ex);
  }

  public WriterException(Throwable ex)
  {
    super(ex);
  }
}
