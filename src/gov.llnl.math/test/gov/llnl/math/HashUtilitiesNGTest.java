/*
 * Copyright 2016, Lawrence Livermore National Security, LLC. 
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.math;

import static org.testng.Assert.*;
import org.testng.annotations.Test;

/**
 * Test code for HashUtilities.
 */
public class HashUtilitiesNGTest
{
  
  public HashUtilitiesNGTest()
  {
  }
  
  /**
   * Test constructor of class HashUtilities.
   */
  @Test
  public void testConstructor()
  {
    HashUtilities instance = new HashUtilities();
  }

  /**
   * Test of hash method, of class HashUtilities.
   */
  @Test
  public void testHash_int_byte()
  {
    int seed = 1;
    byte bytes = 10;
    int expResult = -501417543;
    int result = HashUtilities.hash(seed, bytes);
    assertEquals(result, expResult);
  }

  /**
   * Test of hash method, of class HashUtilities.
   */
  @Test
  public void testHash_int_byteArr()
  {
    int seed = 1;
    byte[] bytes = new byte[]{1,2,3};
    int expResult = -1941264279;
    int result = HashUtilities.hash(seed, bytes);
    assertEquals(result, expResult);
  }

  /**
   * Test of hash method, of class HashUtilities.
   */
  @Test
  public void testHash_int_int()
  {
    int seed = 1;
    int d = 1;
    int expResult = 462247390;
    int result = HashUtilities.hash(seed, d);
    assertEquals(result, expResult);
  }

  /**
   * Test of hash method, of class HashUtilities.
   */
  @Test
  public void testHash_int_long()
  {
    int seed = 1;
    long d = 1L;
    int expResult = 582895574;
    int result = HashUtilities.hash(seed, d);
    assertEquals(result, expResult);
  }

  /**
   * Test of hash method, of class HashUtilities.
   */
  @Test
  public void testHash_int_double()
  {
    int seed = 1;
    double d = 1.0;
    int expResult = -1256348096;
    int result = HashUtilities.hash(seed, d);
    assertEquals(result, expResult);
  }

  /**
   * Test of hash method, of class HashUtilities.
   */
  @Test
  public void testHash_3args()
  {
    int seed = 1;
    double[] d = new double[]{1,2,3};
    int length = d.length;
    int expResult = 1485673352;
    int result = HashUtilities.hash(seed, d, length);
    assertEquals(result, expResult);
  }

  /**
   * Test of hash method, of class HashUtilities.
   */
  @Test
  public void testHash_int_GenericType()
  {
    // Modify if generic type needs to be tested differently
    int seed = 1;
    T[] in = new T[]{};
    int expResult = 1;
    int result = HashUtilities.hash(seed, in);
    assertEquals(result, expResult);
  }
  
  class T
  {
  }
}
