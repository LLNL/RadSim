/*
 * Copyright 2016, Lawrence Livermore National Security, LLC. 
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.math.wavelet;

import gov.llnl.math.wavelet.Statistics1D;
import static org.testng.Assert.*;
import org.testng.annotations.Test;

/**
 * Test code for Statistics1D.
 */
public class Statistics1DNGTest
{
  
  public Statistics1DNGTest()
  {
  }

  /**
   * Test of median method, of class Statistics1D.
   */
  @Test
  public void testMedian()
  {
    Statistics1D instance = new Statistics1D(new double[]{5, 2, 3, 4, 1});
    double expResult = 3.0;
    double result = instance.median();
    assertEquals(result, expResult, 0.0);
    instance = new Statistics1D(new double[]{1, 3, 6, 9});
    expResult = 4.5;
    result = instance.median();
    assertEquals(result, expResult, 0.0);
  }

  /**
   * Test of getMin method, of class Statistics1D.
   */
  @Test
  public void testGetMin()
  {
   Statistics1D instance = new Statistics1D(new double[]{1, 2, 3, 4, 5});
    double expResult = 1.0;
    double result = instance.getMin();
    assertEquals(result, expResult, 0.0);
  }

  /**
   * Test of getMax method, of class Statistics1D.
   */
  @Test
  public void testGetMax()
  {
    Statistics1D instance = new Statistics1D(new double[]{1, 2, 3, 4, 5});
    double expResult = 5.0;
    double result = instance.getMax();
    assertEquals(result, expResult, 0.0);
  }
  
  /**
   * Test of getMean method, of class Statistics1D.
   */
  @Test
  public void testGetMean()
  {
    Statistics1D instance = new Statistics1D(new double[]{1, 2, 3, 4, 5});
    double expResult = 3.0;
    double result = instance.getMean();
    assertEquals(result, expResult, 0.0);
  }
  
  /**
   * Test of getVariance method, of class Statistics1D.
   */
  @Test
  public void testGetVariance()
  {
    Statistics1D instance = new Statistics1D(new double[]{1, 2, 3, 4, 5});
    double expResult = 2.5;
    double result = instance.getVariance();
    assertEquals(result, expResult, 0.0);
  }
  
  /**
   * Test of getStdDev method, of class Statistics1D.
   */
  @Test
  public void testGetStdDev()
  {
    Statistics1D instance = new Statistics1D(new double[]{1, 2, 3, 4, 5});
    double expResult = 1.5811388300841898;
    double result = instance.getStdDev();
    assertEquals(result, expResult, 0.0);
  }
  
}
