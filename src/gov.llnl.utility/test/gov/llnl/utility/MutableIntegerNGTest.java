/*
 * Copyright 2016, Lawrence Livermore National Security, LLC. 
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.utility;

import static org.testng.Assert.*;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Test code for MutableInteger.
 */
public class MutableIntegerNGTest
{

  public MutableIntegerNGTest()
  {
  }

  /**
   * Test of intValue method, of class MutableInteger.
   */
  @Test
  public void testIntValue()
  {
    MutableInteger instance = new MutableInteger(1);
    int expResult = 1;
    int result = instance.intValue();
    assertEquals(result, expResult);
  }

  /**
   * Test of longValue method, of class MutableInteger.
   */
  @Test
  public void testLongValue()
  {
    MutableInteger instance = new MutableInteger(1);
    long expResult = 1L;
    long result = instance.longValue();
    assertEquals(result, expResult);
  }

  /**
   * Test of floatValue method, of class MutableInteger.
   */
  @Test
  public void testFloatValue()
  {
    MutableInteger instance = new MutableInteger(1);
    float expResult = 1.0F;
    float result = instance.floatValue();
    assertEquals(result, expResult, 0.0);
  }

  /**
   * Test of doubleValue method, of class MutableInteger.
   */
  @Test
  public void testDoubleValue()
  {
    MutableInteger instance = new MutableInteger(1);
    double expResult = 1.0;
    double result = instance.doubleValue();
    assertEquals(result, expResult, 0.0);
  }

  /**
   * Test of setValue method, of class MutableInteger.
   */
  @Test
  public void testSetValue()
  {
    int value = 2;
    MutableInteger instance = new MutableInteger();
    instance.setValue(value);
    assertEquals(instance.intValue(), 2);
  }

}
