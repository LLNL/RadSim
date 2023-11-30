/*
 * Copyright 2016, Lawrence Livermore National Security, LLC. 
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.math.wavelet;

import static org.testng.Assert.*;
import org.testng.annotations.Test;

/**
 * Test code for WaveletNotFoundException.
 */
public class WaveletNotFoundExceptionNGTest
{
  
  public WaveletNotFoundExceptionNGTest()
  {
  }

  @Test
  public void testConstructor()
  {
    WaveletNotFoundException instance = new WaveletNotFoundException("hello world", null);
  }
  
}
