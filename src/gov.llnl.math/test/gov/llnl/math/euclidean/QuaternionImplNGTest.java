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
 * Test code for QuaternionImpl.
 */
public class QuaternionImplNGTest
{
  
  public QuaternionImplNGTest()
  {
  }

  /**
   * Test of getI method, of class QuaternionImpl.
   */
  @Test
  public void testGetI()
  {
    QuaternionImpl instance = new QuaternionImpl(1, 2, 3, 4);
    double expResult = 2.0;
    double result = instance.getI();
    assertEquals(result, expResult, 0.0);
  }

  /**
   * Test of getJ method, of class QuaternionImpl.
   */
  @Test
  public void testGetJ()
  {
    QuaternionImpl instance = new QuaternionImpl(1, 2, 3, 4);
    double expResult = 3.0;
    double result = instance.getJ();
    assertEquals(result, expResult, 0.0);
  }

  /**
   * Test of getK method, of class QuaternionImpl.
   */
  @Test
  public void testGetK()
  {
    QuaternionImpl instance = new QuaternionImpl(1, 2, 3, 4);
    double expResult = 4.0;
    double result = instance.getK();
    assertEquals(result, expResult, 0.0);
  }

  /**
   * Test of getU method, of class QuaternionImpl.
   */
  @Test
  public void testGetU()
  {
    QuaternionImpl instance = new QuaternionImpl(1, 2, 3, 4);
    double expResult = 1.0;
    double result = instance.getU();
    assertEquals(result, expResult, 0.0);
  }

  /**
   * Test of toString method, of class QuaternionImpl.
   */
  @Test
  public void testToString()
  {
    QuaternionImpl instance = new QuaternionImpl(1, 2, 3, 4);
    String expResult = "Quaternion(1.000000,2.000000,3.000000,4.000000)";
    String result = instance.toString();
    assertEquals(result, expResult);
  }
  
}
