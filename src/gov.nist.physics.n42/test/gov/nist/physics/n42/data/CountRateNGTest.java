/*
 * Copyright 2022, Lawrence Livermore National Security, LLC.
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.nist.physics.n42.data;

import java.util.function.Consumer;
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
public class CountRateNGTest
{
  
  public CountRateNGTest()
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
   * Test of getDetector method, of class CountRate.
   */
  @Test
  public void testGetDetector()
  {
    System.out.println("getDetector");
    CountRate instance = new CountRate();
    RadDetectorInformation expResult = null;
    RadDetectorInformation result = instance.getDetector();
    assertEquals(result, expResult);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of setDetector method, of class CountRate.
   */
  @Test
  public void testSetDetector()
  {
    System.out.println("setDetector");
    RadDetectorInformation detector = null;
    CountRate instance = new CountRate();
    instance.setDetector(detector);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of getValue method, of class CountRate.
   */
  @Test
  public void testGetValue()
  {
    System.out.println("getValue");
    CountRate instance = new CountRate();
    Quantity expResult = null;
    Quantity result = instance.getValue();
    assertEquals(result, expResult);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of setValue method, of class CountRate.
   */
  @Test
  public void testSetValue()
  {
    System.out.println("setValue");
    Quantity value = null;
    CountRate instance = new CountRate();
    instance.setValue(value);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of visitReferencedObjects method, of class CountRate.
   */
  @Test
  public void testVisitReferencedObjects()
  {
    System.out.println("visitReferencedObjects");
    Consumer<ComplexObject> visitor = null;
    CountRate instance = new CountRate();
    instance.visitReferencedObjects(visitor);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }
  
}
