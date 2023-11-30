/*
 * Copyright 2022, Lawrence Livermore National Security, LLC.
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.nist.physics.n42.utility;

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
public class SpectrumUtilitiesNGTest
{
  
  public SpectrumUtilitiesNGTest()
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
   * Test of unpackCountedZeros method, of class SpectrumUtilities.
   */
  @Test
  public void testUnpackCountedZeros()
  {
    System.out.println("unpackCountedZeros");
    double[] compressed = null;
    double[] expResult = null;
    double[] result = SpectrumUtilities.unpackCountedZeros(compressed);
    assertEquals(result, expResult);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of packCountedZeros method, of class SpectrumUtilities.
   */
  @Test
  public void testPackCountedZeros()
  {
    System.out.println("packCountedZeros");
    double[] uncompressed = null;
    double[] expResult = null;
    double[] result = SpectrumUtilities.packCountedZeros(uncompressed);
    assertEquals(result, expResult);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }
  
}
