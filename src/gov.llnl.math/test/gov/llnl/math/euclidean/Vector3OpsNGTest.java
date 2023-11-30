/*
 * Copyright 2016, Lawrence Livermore National Security, LLC. 
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.math.euclidean;

import static org.testng.Assert.*;
import org.testng.annotations.Test;

/**
 * Test code for Vector3Ops.
 */
public class Vector3OpsNGTest
{
  
  public Vector3OpsNGTest()
  {
  }

  @Test
  public void testConstructor()
  {
    Vector3Ops instance = new Vector3Ops();
  }
  
  /**
   * Test of add method, of class Vector3Ops.
   */
  @Test
  public void testAdd()
  {
    Vector3 v1 = new Vector3Impl(1, 1, 1);
    Vector3 v2 = new Vector3Impl(1, 1, 1);
    Vector3 expResult = new Vector3Impl(2, 2, 2);
    Vector3 result = Vector3Ops.add(v1, v2);
    assertEquals(result, expResult);
  }

  /**
   * Test of subtract method, of class Vector3Ops.
   */
  @Test
  public void testSubtract()
  {
    Vector3 v1 = new Vector3Impl(1, 2, 3);
    Vector3 v2 = new Vector3Impl(1, 1, 1);
    Vector3 expResult = new Vector3Impl(0, 1, 2);
    Vector3 result = Vector3Ops.subtract(v1, v2);
    assertEquals(result, expResult);
  }

  /**
   * Test of multiply method, of class Vector3Ops.
   */
  @Test
  public void testMultiply()
  {
    Vector3 v1 = new Vector3Impl(1, 2, 3);
    double s = 2.0;
    Vector3 expResult = new Vector3Impl(2, 4, 6);
    Vector3 result = Vector3Ops.multiply(v1, s);
    assertEquals(result, expResult);
  }

  /**
   * Test of multiplyDot method, of class Vector3Ops.
   */
  @Test
  public void testMultiplyDot()
  {
    Vector3 v1 = new Vector3Impl(1, 2, 3);
    Vector3 v2 = new Vector3Impl(1, 1, 1);
    double expResult = 6.0;
    double result = Vector3Ops.multiplyDot(v1, v2);
    assertEquals(result, expResult, 0.0);
  }

  /**
   * Test of length method, of class Vector3Ops.
   */
  @Test
  public void testLength()
  {
    Vector3 v1 = new Vector3Impl(1, 0, 0);
    double expResult = 1.0;
    double result = Vector3Ops.length(v1);
    assertEquals(result, expResult, 0.0);
  }

  /**
   * Test of correlation method, of class Vector3Ops.
   */
  @Test
  public void testCorrelation()
  {
    Vector3 v1 = new Vector3Impl(1, 1, 1);
    Vector3 v2 = new Vector3Impl(1, 1, 1);
    double expResult = 1.0;
    double result = Vector3Ops.correlation(v1, v2);
    assertEquals(result, expResult, 0.0);
  }

  /**
   * Test of interpolate method, of class Vector3Ops.
   */
  @Test
  public void testInterpolate()
  {
    double f = 1.0;
    Vector3 p0 = new Vector3Impl(1, 1, 1);
    Vector3 p1 = new Vector3Impl(1, 2, 3);
    Vector3 expResult = new Vector3Impl(1, 2, 3);
    Vector3 result = Vector3Ops.interpolate(f, p0, p1);
    assertEquals(result, expResult);
  }
  
}
