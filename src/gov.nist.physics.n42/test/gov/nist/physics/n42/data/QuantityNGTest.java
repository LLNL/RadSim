/*
 * Copyright 2022, Lawrence Livermore National Security, LLC.
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.nist.physics.n42.data;

import static org.testng.Assert.*;
import org.testng.annotations.Test;

/**
 *
 * @author nelson85
 */
public class QuantityNGTest
{
  
  public QuantityNGTest()
  {
  }

  @Test
  public void testGetUnits()
  {
    Quantity instance = new Quantity(1.0, "none");
    String expResult = "none";
    String result = instance.getUnits();
    assertEquals(result, expResult);
  }

  @Test
  public void testSetUnits()
  {
    String units = "";
    Quantity instance = new Quantity();
    instance.setUnits(units);
    assertSame(instance.getUnits(), units);
  }

  @Test
  public void testGetValue()
  {
    Quantity instance = new Quantity(10.0, "none");
    double expResult = 10.0;
    double result = instance.getValue();
    assertEquals(result, expResult, 0.0);
  }

  @Test
  public void testSetValue()
  {
    Quantity instance = new Quantity();
    double expResult = 10.0;
    instance.setValue(expResult);
    double result = instance.getValue();
    assertEquals(result, expResult, 0.0);
  }

  /**
   * Test of toString method, of class Quantity.
   */
  @Test
  public void testToString()
  {
    System.out.println("toString");
    Quantity instance = new Quantity();
    String expResult = "";
    String result = instance.toString();
    assertEquals(result, expResult);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of equals method, of class Quantity.
   */
  @Test
  public void testEquals()
  {
    System.out.println("equals");
    Object obj = null;
    Quantity instance = new Quantity();
    boolean expResult = false;
    boolean result = instance.equals(obj);
    assertEquals(result, expResult);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }
  
}
