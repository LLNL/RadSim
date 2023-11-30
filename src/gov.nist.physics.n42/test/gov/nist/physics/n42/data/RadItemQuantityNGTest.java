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
public class RadItemQuantityNGTest
{
  
  public RadItemQuantityNGTest()
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
   * Test of getValue method, of class RadItemQuantity.
   */
  @Test
  public void testGetValue()
  {
    System.out.println("getValue");
    RadItemQuantity instance = new RadItemQuantity();
    double expResult = 0.0;
    double result = instance.getValue();
    assertEquals(result, expResult, 0.0);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of getUncertainty method, of class RadItemQuantity.
   */
  @Test
  public void testGetUncertainty()
  {
    System.out.println("getUncertainty");
    RadItemQuantity instance = new RadItemQuantity();
    Double expResult = null;
    Double result = instance.getUncertainty();
    assertEquals(result, expResult);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of getUnits method, of class RadItemQuantity.
   */
  @Test
  public void testGetUnits()
  {
    System.out.println("getUnits");
    RadItemQuantity instance = new RadItemQuantity();
    String expResult = "";
    String result = instance.getUnits();
    assertEquals(result, expResult);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of setValue method, of class RadItemQuantity.
   */
  @Test
  public void testSetValue()
  {
    System.out.println("setValue");
    double u = 0.0;
    RadItemQuantity instance = new RadItemQuantity();
    instance.setValue(u);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of setUncertainty method, of class RadItemQuantity.
   */
  @Test
  public void testSetUncertainty()
  {
    System.out.println("setUncertainty");
    Double u = null;
    RadItemQuantity instance = new RadItemQuantity();
    instance.setUncertainty(u);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of setUnits method, of class RadItemQuantity.
   */
  @Test
  public void testSetUnits()
  {
    System.out.println("setUnits");
    String u = "";
    RadItemQuantity instance = new RadItemQuantity();
    instance.setUnits(u);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }
  
}
