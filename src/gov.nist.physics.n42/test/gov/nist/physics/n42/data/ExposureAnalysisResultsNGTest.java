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
public class ExposureAnalysisResultsNGTest
{
  
  public ExposureAnalysisResultsNGTest()
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
   * Test of getAverageExposureRate method, of class ExposureAnalysisResults.
   */
  @Test
  public void testGetAverageExposureRate()
  {
    System.out.println("getAverageExposureRate");
    ExposureAnalysisResults instance = new ExposureAnalysisResults();
    Quantity expResult = null;
    Quantity result = instance.getAverageExposureRate();
    assertEquals(result, expResult);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of getAverageExposureRateUncertainty method, of class ExposureAnalysisResults.
   */
  @Test
  public void testGetAverageExposureRateUncertainty()
  {
    System.out.println("getAverageExposureRateUncertainty");
    ExposureAnalysisResults instance = new ExposureAnalysisResults();
    Quantity expResult = null;
    Quantity result = instance.getAverageExposureRateUncertainty();
    assertEquals(result, expResult);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of getMaximumExposureRate method, of class ExposureAnalysisResults.
   */
  @Test
  public void testGetMaximumExposureRate()
  {
    System.out.println("getMaximumExposureRate");
    ExposureAnalysisResults instance = new ExposureAnalysisResults();
    Quantity expResult = null;
    Quantity result = instance.getMaximumExposureRate();
    assertEquals(result, expResult);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of getMinimumExposureRate method, of class ExposureAnalysisResults.
   */
  @Test
  public void testGetMinimumExposureRate()
  {
    System.out.println("getMinimumExposureRate");
    ExposureAnalysisResults instance = new ExposureAnalysisResults();
    Quantity expResult = null;
    Quantity result = instance.getMinimumExposureRate();
    assertEquals(result, expResult);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of getBackgroundExposureRate method, of class ExposureAnalysisResults.
   */
  @Test
  public void testGetBackgroundExposureRate()
  {
    System.out.println("getBackgroundExposureRate");
    ExposureAnalysisResults instance = new ExposureAnalysisResults();
    Quantity expResult = null;
    Quantity result = instance.getBackgroundExposureRate();
    assertEquals(result, expResult);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of getBackgroundExposureRateUncertainty method, of class ExposureAnalysisResults.
   */
  @Test
  public void testGetBackgroundExposureRateUncertainty()
  {
    System.out.println("getBackgroundExposureRateUncertainty");
    ExposureAnalysisResults instance = new ExposureAnalysisResults();
    Quantity expResult = null;
    Quantity result = instance.getBackgroundExposureRateUncertainty();
    assertEquals(result, expResult);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of getTotalExposure method, of class ExposureAnalysisResults.
   */
  @Test
  public void testGetTotalExposure()
  {
    System.out.println("getTotalExposure");
    ExposureAnalysisResults instance = new ExposureAnalysisResults();
    Quantity expResult = null;
    Quantity result = instance.getTotalExposure();
    assertEquals(result, expResult);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of setAverageExposureRate method, of class ExposureAnalysisResults.
   */
  @Test
  public void testSetAverageExposureRate()
  {
    System.out.println("setAverageExposureRate");
    Quantity value = null;
    ExposureAnalysisResults instance = new ExposureAnalysisResults();
    instance.setAverageExposureRate(value);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of setAverageExposureRateUncertainty method, of class ExposureAnalysisResults.
   */
  @Test
  public void testSetAverageExposureRateUncertainty()
  {
    System.out.println("setAverageExposureRateUncertainty");
    Quantity value = null;
    ExposureAnalysisResults instance = new ExposureAnalysisResults();
    instance.setAverageExposureRateUncertainty(value);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of setMaximumExposureRate method, of class ExposureAnalysisResults.
   */
  @Test
  public void testSetMaximumExposureRate()
  {
    System.out.println("setMaximumExposureRate");
    Quantity value = null;
    ExposureAnalysisResults instance = new ExposureAnalysisResults();
    instance.setMaximumExposureRate(value);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of setMinimumExposureRate method, of class ExposureAnalysisResults.
   */
  @Test
  public void testSetMinimumExposureRate()
  {
    System.out.println("setMinimumExposureRate");
    Quantity value = null;
    ExposureAnalysisResults instance = new ExposureAnalysisResults();
    instance.setMinimumExposureRate(value);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of setBackgroundExposureRate method, of class ExposureAnalysisResults.
   */
  @Test
  public void testSetBackgroundExposureRate()
  {
    System.out.println("setBackgroundExposureRate");
    Quantity value = null;
    ExposureAnalysisResults instance = new ExposureAnalysisResults();
    instance.setBackgroundExposureRate(value);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of setBackgroundExposureRateUncertainty method, of class ExposureAnalysisResults.
   */
  @Test
  public void testSetBackgroundExposureRateUncertainty()
  {
    System.out.println("setBackgroundExposureRateUncertainty");
    Quantity value = null;
    ExposureAnalysisResults instance = new ExposureAnalysisResults();
    instance.setBackgroundExposureRateUncertainty(value);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of setTotalExposure method, of class ExposureAnalysisResults.
   */
  @Test
  public void testSetTotalExposure()
  {
    System.out.println("setTotalExposure");
    Quantity value = null;
    ExposureAnalysisResults instance = new ExposureAnalysisResults();
    instance.setTotalExposure(value);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }
  
}
