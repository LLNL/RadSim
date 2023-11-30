/*
 * Copyright 2016, Lawrence Livermore National Security, LLC. 
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.math.statistical;

import gov.llnl.math.statistical.RangeImpl;
import gov.llnl.math.statistical.Range;
import static org.testng.Assert.*;
import org.testng.annotations.Test;

/**
 * Test code for RangeImpl.
 */
public class RangeImplNGTest
{
  
  public RangeImplNGTest()
  {
  }
  
  /**
   * Test of contains method, of class RangeImpl.
   */
  @Test
  public void testContains()
  {
    double value = 0.0;
    RangeImpl instance = new RangeImpl(1.0, 9.0);
    boolean result = instance.contains(value);
    assertFalse(result);
    value = 3.0;
    result = instance.contains(value);
    assertTrue(result);
    value = 10.0;
    result = instance.contains(value);
    assertFalse(result);
  }

  /**
   * Test of intersects method, of class RangeImpl.
   */
  @Test
  public void testIntersects()
  {
    Range range = new RangeImpl(1.0, 3.0);
    RangeImpl instance = new RangeImpl(5.0, 9.0);
    boolean result = instance.intersects(range);
    assertFalse(result);
    instance = new RangeImpl(1.0, 9.0);
    result = instance.intersects(range);
    assertTrue(result);
    range = new RangeImpl(10.0, 13.0);
    result = instance.intersects(range);
    assertFalse(result);
  }

  /**
   * Test of getBegin method, of class RangeImpl.
   */
  @Test
  public void testGetBegin()
  {
    RangeImpl instance = new RangeImpl(1.0, 9.0);
    double expResult = 1.0;
    double result = instance.getBegin();
    assertEquals(result, expResult, 0.0);
  }

  /**
   * Test of getEnd method, of class RangeImpl.
   */
  @Test
  public void testGetEnd()
  {
    RangeImpl instance = new RangeImpl(1.0, 9.0);
    double expResult = 9.0;
    double result = instance.getEnd();
    assertEquals(result, expResult, 0.0);
  }

  /**
   * Test of toString method, of class RangeImpl.
   */
  @Test
  public void testToString()
  {
    RangeImpl instance = new RangeImpl(1.0, 9.0);
    String expResult = "Range(1.000000,9.000000)";
    String result = instance.toString();
    assertEquals(result, expResult);
  }
  
}
