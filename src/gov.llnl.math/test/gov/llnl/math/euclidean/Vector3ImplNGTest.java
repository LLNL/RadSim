/*
 * Copyright 2016, Lawrence Livermore National Security, LLC. 
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.math.euclidean;

import gov.llnl.math.euclidean.Vector3Impl;
import static org.testng.Assert.*;
import org.testng.annotations.Test;

/**
 * Test code for Vector3Impl.
 */
public class Vector3ImplNGTest
{

  public Vector3ImplNGTest()
  {
  }

  /**
   * Test of getX method, of class Vector3Impl.
   */
  @Test
  public void testGetX()
  {
    Vector3Impl instance = new Vector3Impl(1, 2, 3);
    double expResult = 1.0;
    double result = instance.getX();
    assertEquals(result, expResult, 0.0);
  }

  /**
   * Test of getY method, of class Vector3Impl.
   */
  @Test
  public void testGetY()
  {
    Vector3Impl instance = new Vector3Impl(1, 2, 3);
    double expResult = 2.0;
    double result = instance.getY();
    assertEquals(result, expResult, 0.0);
  }

  /**
   * Test of getZ method, of class Vector3Impl.
   */
  @Test
  public void testGetZ()
  {
    Vector3Impl instance = new Vector3Impl(1, 2, 3);
    double expResult = 3.0;
    double result = instance.getZ();
    assertEquals(result, expResult, 0.0);
  }

  /**
   * Test of toString method, of class Vector3Impl.
   */
  @Test
  public void testToString()
  {
    Vector3Impl instance = new Vector3Impl(1, 2, 3);
    String expResult = "Vector3(1.00000000000000000000,2.00000000000000000000,3.00000000000000000000)";
    String result = instance.toString();
    assertEquals(result, expResult);
  }

  /**
   * Test of equals method, of class Vector3Impl.
   */
  @Test
  public void testEquals()
  {
    Object other = new Vector3Impl(1, 2, 3);
    Vector3Impl instance = new Vector3Impl(1, 2, 3);
    boolean result = instance.equals(other);
    assertTrue(result);
    result = instance.equals(instance);
    assertTrue(result);
    other = 1;
    result = instance.equals(other);
    assertFalse(result);
    other = new Vector3Impl(2, 2, 3);
    result = instance.equals(other);
    assertFalse(result);
    other = new Vector3Impl(1, 5, 3);
    result = instance.equals(other);
    assertFalse(result);
    other = new Vector3Impl(1, 2, 6);
    result = instance.equals(other);
    assertFalse(result);
  }

  /**
   * Test of hashCode method, of class Vector3Impl.
   */
  @Test
  public void testHashCode()
  {
    Vector3Impl instance = new Vector3Impl(1, 2, 3);
    int expResult = 1786603787;
    int result = instance.hashCode();
    assertEquals(result, expResult);
  }

}
