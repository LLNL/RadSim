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
public class AnalysisAlgorithmSettingNGTest
{
  
  public AnalysisAlgorithmSettingNGTest()
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
   * Test of getName method, of class AnalysisAlgorithmSetting.
   */
  @Test
  public void testGetName()
  {
    System.out.println("getName");
    AnalysisAlgorithmSetting instance = new AnalysisAlgorithmSetting();
    String expResult = "";
    String result = instance.getName();
    assertEquals(result, expResult);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of getValue method, of class AnalysisAlgorithmSetting.
   */
  @Test
  public void testGetValue()
  {
    System.out.println("getValue");
    AnalysisAlgorithmSetting instance = new AnalysisAlgorithmSetting();
    String expResult = "";
    String result = instance.getValue();
    assertEquals(result, expResult);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of getUnits method, of class AnalysisAlgorithmSetting.
   */
  @Test
  public void testGetUnits()
  {
    System.out.println("getUnits");
    AnalysisAlgorithmSetting instance = new AnalysisAlgorithmSetting();
    String expResult = "";
    String result = instance.getUnits();
    assertEquals(result, expResult);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of setName method, of class AnalysisAlgorithmSetting.
   */
  @Test
  public void testSetName()
  {
    System.out.println("setName");
    String u = "";
    AnalysisAlgorithmSetting instance = new AnalysisAlgorithmSetting();
    instance.setName(u);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of setValue method, of class AnalysisAlgorithmSetting.
   */
  @Test
  public void testSetValue()
  {
    System.out.println("setValue");
    String u = "";
    AnalysisAlgorithmSetting instance = new AnalysisAlgorithmSetting();
    instance.setValue(u);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of setUnits method, of class AnalysisAlgorithmSetting.
   */
  @Test
  public void testSetUnits()
  {
    System.out.println("setUnits");
    String u = "";
    AnalysisAlgorithmSetting instance = new AnalysisAlgorithmSetting();
    instance.setUnits(u);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }
  
}
