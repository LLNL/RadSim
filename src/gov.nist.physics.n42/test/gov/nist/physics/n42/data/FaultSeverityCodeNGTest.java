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
public class FaultSeverityCodeNGTest
{
  
  public FaultSeverityCodeNGTest()
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
   * Test of values method, of class FaultSeverityCode.
   */
  @Test
  public void testValues()
  {
    System.out.println("values");
    FaultSeverityCode[] expResult = null;
    FaultSeverityCode[] result = FaultSeverityCode.values();
    assertEquals(result, expResult);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of valueOf method, of class FaultSeverityCode.
   */
  @Test
  public void testValueOf()
  {
    System.out.println("valueOf");
    String arg0 = "";
    FaultSeverityCode expResult = null;
    FaultSeverityCode result = FaultSeverityCode.valueOf(arg0);
    assertEquals(result, expResult);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }
  
}
