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
public class DoseAnalysisResultsNGTest
{
  
  public DoseAnalysisResultsNGTest()
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
   * Test of setAverageDoseRate method, of class DoseAnalysisResults.
   */
  @Test
  public void testSetAverageDoseRate()
  {
    System.out.println("setAverageDoseRate");
    Quantity value = null;
    DoseAnalysisResults instance = new DoseAnalysisResults();
    instance.setAverageDoseRate(value);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of setAverageDoseRateUncertainty method, of class DoseAnalysisResults.
   */
  @Test
  public void testSetAverageDoseRateUncertainty()
  {
    System.out.println("setAverageDoseRateUncertainty");
    Quantity value = null;
    DoseAnalysisResults instance = new DoseAnalysisResults();
    instance.setAverageDoseRateUncertainty(value);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of setMaximumDoseRate method, of class DoseAnalysisResults.
   */
  @Test
  public void testSetMaximumDoseRate()
  {
    System.out.println("setMaximumDoseRate");
    Quantity value = null;
    DoseAnalysisResults instance = new DoseAnalysisResults();
    instance.setMaximumDoseRate(value);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of setMinimumDoseRate method, of class DoseAnalysisResults.
   */
  @Test
  public void testSetMinimumDoseRate()
  {
    System.out.println("setMinimumDoseRate");
    Quantity value = null;
    DoseAnalysisResults instance = new DoseAnalysisResults();
    instance.setMinimumDoseRate(value);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of setBackgroundDoseRate method, of class DoseAnalysisResults.
   */
  @Test
  public void testSetBackgroundDoseRate()
  {
    System.out.println("setBackgroundDoseRate");
    Quantity value = null;
    DoseAnalysisResults instance = new DoseAnalysisResults();
    instance.setBackgroundDoseRate(value);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of setBackgroundDoseRateUncertainty method, of class DoseAnalysisResults.
   */
  @Test
  public void testSetBackgroundDoseRateUncertainty()
  {
    System.out.println("setBackgroundDoseRateUncertainty");
    Quantity value = null;
    DoseAnalysisResults instance = new DoseAnalysisResults();
    instance.setBackgroundDoseRateUncertainty(value);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of setTotalDose method, of class DoseAnalysisResults.
   */
  @Test
  public void testSetTotalDose()
  {
    System.out.println("setTotalDose");
    Quantity value = null;
    DoseAnalysisResults instance = new DoseAnalysisResults();
    instance.setTotalDose(value);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of getAverageDoseRateUncertainty method, of class DoseAnalysisResults.
   */
  @Test
  public void testGetAverageDoseRateUncertainty()
  {
    System.out.println("getAverageDoseRateUncertainty");
    DoseAnalysisResults instance = new DoseAnalysisResults();
    Quantity expResult = null;
    Quantity result = instance.getAverageDoseRateUncertainty();
    assertEquals(result, expResult);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of getMaximumDoseRate method, of class DoseAnalysisResults.
   */
  @Test
  public void testGetMaximumDoseRate()
  {
    System.out.println("getMaximumDoseRate");
    DoseAnalysisResults instance = new DoseAnalysisResults();
    Quantity expResult = null;
    Quantity result = instance.getMaximumDoseRate();
    assertEquals(result, expResult);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of getMinimumDoseRate method, of class DoseAnalysisResults.
   */
  @Test
  public void testGetMinimumDoseRate()
  {
    System.out.println("getMinimumDoseRate");
    DoseAnalysisResults instance = new DoseAnalysisResults();
    Quantity expResult = null;
    Quantity result = instance.getMinimumDoseRate();
    assertEquals(result, expResult);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of getBackgroundDoseRate method, of class DoseAnalysisResults.
   */
  @Test
  public void testGetBackgroundDoseRate()
  {
    System.out.println("getBackgroundDoseRate");
    DoseAnalysisResults instance = new DoseAnalysisResults();
    Quantity expResult = null;
    Quantity result = instance.getBackgroundDoseRate();
    assertEquals(result, expResult);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of getBackgroundDoseRateUncertainty method, of class DoseAnalysisResults.
   */
  @Test
  public void testGetBackgroundDoseRateUncertainty()
  {
    System.out.println("getBackgroundDoseRateUncertainty");
    DoseAnalysisResults instance = new DoseAnalysisResults();
    Quantity expResult = null;
    Quantity result = instance.getBackgroundDoseRateUncertainty();
    assertEquals(result, expResult);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of getAverageDoseRate method, of class DoseAnalysisResults.
   */
  @Test
  public void testGetAverageDoseRate()
  {
    System.out.println("getAverageDoseRate");
    DoseAnalysisResults instance = new DoseAnalysisResults();
    Quantity expResult = null;
    Quantity result = instance.getAverageDoseRate();
    assertEquals(result, expResult);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of getTotalDose method, of class DoseAnalysisResults.
   */
  @Test
  public void testGetTotalDose()
  {
    System.out.println("getTotalDose");
    DoseAnalysisResults instance = new DoseAnalysisResults();
    Quantity expResult = null;
    Quantity result = instance.getTotalDose();
    assertEquals(result, expResult);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }
  
}
