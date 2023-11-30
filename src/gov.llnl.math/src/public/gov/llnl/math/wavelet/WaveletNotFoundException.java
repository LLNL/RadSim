/*
 * Copyright 2016, Lawrence Livermore National Security, LLC. 
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.math.wavelet;

import gov.llnl.utility.UUIDUtilities;

/**
 *
 * @author nelson85
 */
public class WaveletNotFoundException extends Exception
{
          private static final long serialVersionUID = UUIDUtilities.createLong("WaveletNotFoundException");
  public WaveletNotFoundException(String mesg)
  {
    super(mesg);
  }

  public WaveletNotFoundException(String mesg, Throwable th)
  {
    super(mesg, th);
  }
}
