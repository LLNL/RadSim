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
public class SpectrumPeakNGTest
{
  
  public SpectrumPeakNGTest()
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
   * Test of getEnergy method, of class SpectrumPeak.
   */
  @Test
  public void testGetEnergy()
  {
    System.out.println("getEnergy");
    SpectrumPeak instance = new SpectrumPeak();
    Double expResult = null;
    Double result = instance.getEnergy();
    assertEquals(result, expResult);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of getNetArea method, of class SpectrumPeak.
   */
  @Test
  public void testGetNetArea()
  {
    System.out.println("getNetArea");
    SpectrumPeak instance = new SpectrumPeak();
    Double expResult = null;
    Double result = instance.getNetArea();
    assertEquals(result, expResult);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of getNetAreaUncertainty method, of class SpectrumPeak.
   */
  @Test
  public void testGetNetAreaUncertainty()
  {
    System.out.println("getNetAreaUncertainty");
    SpectrumPeak instance = new SpectrumPeak();
    Double expResult = null;
    Double result = instance.getNetAreaUncertainty();
    assertEquals(result, expResult);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of setEnergy method, of class SpectrumPeak.
   */
  @Test
  public void testSetEnergy()
  {
    System.out.println("setEnergy");
    Double u = null;
    SpectrumPeak instance = new SpectrumPeak();
    instance.setEnergy(u);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of setNetArea method, of class SpectrumPeak.
   */
  @Test
  public void testSetNetArea()
  {
    System.out.println("setNetArea");
    Double u = null;
    SpectrumPeak instance = new SpectrumPeak();
    instance.setNetArea(u);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of setNetAreaUncertainty method, of class SpectrumPeak.
   */
  @Test
  public void testSetNetAreaUncertainty()
  {
    System.out.println("setNetAreaUncertainty");
    Double u = null;
    SpectrumPeak instance = new SpectrumPeak();
    instance.setNetAreaUncertainty(u);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }
  
}
