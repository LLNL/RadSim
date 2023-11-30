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
 * Test code for WaveletFamilyDaubechies.
 */
public class WaveletFamilyDaubechiesNGTest
{

  public WaveletFamilyDaubechiesNGTest()
  {
  }

  /**
   * Test of get method, of class WaveletFamilyDaubechies.
   */
  @Test (expectedExceptions = {
    WaveletNotFoundException.class
  })
  public void testGet_Exception1() throws Exception
  {
    int i = 0;
    WaveletFamilyDaubechies instance = new WaveletFamilyDaubechies();
    double[] result = instance.get(i);
  }
  
   @Test (expectedExceptions = {
    WaveletNotFoundException.class
  })
  public void testGet_Exception2() throws Exception
  {
    int i = 1;
    WaveletFamilyDaubechies instance = new WaveletFamilyDaubechies();
    double[] result = instance.get(i);
  }
  
   @Test (expectedExceptions = {
    WaveletNotFoundException.class
  })
  public void testGet_Exception3() throws Exception
  {
    int i = 50;
    WaveletFamilyDaubechies instance = new WaveletFamilyDaubechies();
    double[] result = instance.get(i);
  }

  /**
   * Test of getAvailableWavelets method, of class WaveletFamilyDaubechies.
   */
  @Test
  public void testGetAvailableWavelets()
  {
    WaveletFamilyDaubechies instance = new WaveletFamilyDaubechies();
    String[] expResult = new String[]
    {
      "daub4", "daub6", "daub8", "daub10", "daub12", "daub14",
      "daub16", "daub18", "daub20", "daub22", "daub24"
    };
    String[] result = instance.getAvailableWavelets();
    assertEquals(result, expResult);
  }

}
