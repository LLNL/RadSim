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
public class RadAlarmCategoryCodeNGTest
{
  
  public RadAlarmCategoryCodeNGTest()
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
   * Test of values method, of class RadAlarmCategoryCode.
   */
  @Test
  public void testValues()
  {
    System.out.println("values");
    RadAlarmCategoryCode[] expResult = null;
    RadAlarmCategoryCode[] result = RadAlarmCategoryCode.values();
    assertEquals(result, expResult);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of valueOf method, of class RadAlarmCategoryCode.
   */
  @Test
  public void testValueOf()
  {
    System.out.println("valueOf");
    String arg0 = "";
    RadAlarmCategoryCode expResult = null;
    RadAlarmCategoryCode result = RadAlarmCategoryCode.valueOf(arg0);
    assertEquals(result, expResult);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }
  
}
