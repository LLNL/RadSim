/*
 * Copyright 2022, Lawrence Livermore National Security, LLC.
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.nist.physics.n42.data;

import static org.testng.Assert.*;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 *
 * @author pham21
 */
public class GrossCountAnalysisResultsNGTest
{
  
  public GrossCountAnalysisResultsNGTest()
  {
  }

  @BeforeClass
  public static void setUpClass() throws Exception
  {
  }

  @AfterClass
  public static void tearDownClass() throws Exception
  {
  }

  @BeforeMethod
  public void setUpMethod() throws Exception
  {
  }

  @AfterMethod
  public void tearDownMethod() throws Exception
  {
  }

  /**
   * Test of getAverageCountRate method, of class GrossCountAnalysisResults.
   */
  @Test
  public void testGetAverageCountRate()
  {
    System.out.println("getAverageCountRate");
    GrossCountAnalysisResults instance = new GrossCountAnalysisResults();
    Quantity expResult = null;
    Quantity result = instance.getAverageCountRate();
    assertEquals(result, expResult);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of getAverageCountRateUncertainty method, of class GrossCountAnalysisResults.
   */
  @Test
  public void testGetAverageCountRateUncertainty()
  {
    System.out.println("getAverageCountRateUncertainty");
    GrossCountAnalysisResults instance = new GrossCountAnalysisResults();
    Quantity expResult = null;
    Quantity result = instance.getAverageCountRateUncertainty();
    assertEquals(result, expResult);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of getMaximumCountRate method, of class GrossCountAnalysisResults.
   */
  @Test
  public void testGetMaximumCountRate()
  {
    System.out.println("getMaximumCountRate");
    GrossCountAnalysisResults instance = new GrossCountAnalysisResults();
    Quantity expResult = null;
    Quantity result = instance.getMaximumCountRate();
    assertEquals(result, expResult);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of getMinimumCountRate method, of class GrossCountAnalysisResults.
   */
  @Test
  public void testGetMinimumCountRate()
  {
    System.out.println("getMinimumCountRate");
    GrossCountAnalysisResults instance = new GrossCountAnalysisResults();
    Quantity expResult = null;
    Quantity result = instance.getMinimumCountRate();
    assertEquals(result, expResult);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of getBackgroundCountRate method, of class GrossCountAnalysisResults.
   */
  @Test
  public void testGetBackgroundCountRate()
  {
    System.out.println("getBackgroundCountRate");
    GrossCountAnalysisResults instance = new GrossCountAnalysisResults();
    Quantity expResult = null;
    Quantity result = instance.getBackgroundCountRate();
    assertEquals(result, expResult);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of getBackgroundCountRateUncertainty method, of class GrossCountAnalysisResults.
   */
  @Test
  public void testGetBackgroundCountRateUncertainty()
  {
    System.out.println("getBackgroundCountRateUncertainty");
    GrossCountAnalysisResults instance = new GrossCountAnalysisResults();
    Quantity expResult = null;
    Quantity result = instance.getBackgroundCountRateUncertainty();
    assertEquals(result, expResult);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of setAverageCountRate method, of class GrossCountAnalysisResults.
   */
  @Test
  public void testSetAverageCountRate()
  {
    System.out.println("setAverageCountRate");
    Quantity value = null;
    GrossCountAnalysisResults instance = new GrossCountAnalysisResults();
    instance.setAverageCountRate(value);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of setAverageCountRateUncertainty method, of class GrossCountAnalysisResults.
   */
  @Test
  public void testSetAverageCountRateUncertainty()
  {
    System.out.println("setAverageCountRateUncertainty");
    Quantity value = null;
    GrossCountAnalysisResults instance = new GrossCountAnalysisResults();
    instance.setAverageCountRateUncertainty(value);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of setMaximumCountRate method, of class GrossCountAnalysisResults.
   */
  @Test
  public void testSetMaximumCountRate()
  {
    System.out.println("setMaximumCountRate");
    Quantity value = null;
    GrossCountAnalysisResults instance = new GrossCountAnalysisResults();
    instance.setMaximumCountRate(value);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of setMinimumCountRate method, of class GrossCountAnalysisResults.
   */
  @Test
  public void testSetMinimumCountRate()
  {
    System.out.println("setMinimumCountRate");
    Quantity value = null;
    GrossCountAnalysisResults instance = new GrossCountAnalysisResults();
    instance.setMinimumCountRate(value);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of setBackgroundCountRate method, of class GrossCountAnalysisResults.
   */
  @Test
  public void testSetBackgroundCountRate()
  {
    System.out.println("setBackgroundCountRate");
    Quantity value = null;
    GrossCountAnalysisResults instance = new GrossCountAnalysisResults();
    instance.setBackgroundCountRate(value);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of setBackgroundCountRateUncertainty method, of class GrossCountAnalysisResults.
   */
  @Test
  public void testSetBackgroundCountRateUncertainty()
  {
    System.out.println("setBackgroundCountRateUncertainty");
    Quantity value = null;
    GrossCountAnalysisResults instance = new GrossCountAnalysisResults();
    instance.setBackgroundCountRateUncertainty(value);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of getTotalCounts method, of class GrossCountAnalysisResults.
   */
  @Test
  public void testGetTotalCounts()
  {
    System.out.println("getTotalCounts");
    GrossCountAnalysisResults instance = new GrossCountAnalysisResults();
    Quantity expResult = null;
    Quantity result = instance.getTotalCounts();
    assertEquals(result, expResult);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of setTotalCounts method, of class GrossCountAnalysisResults.
   */
  @Test
  public void testSetTotalCounts()
  {
    System.out.println("setTotalCounts");
    Quantity totalCounts = null;
    GrossCountAnalysisResults instance = new GrossCountAnalysisResults();
    instance.setTotalCounts(totalCounts);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }
  
}
