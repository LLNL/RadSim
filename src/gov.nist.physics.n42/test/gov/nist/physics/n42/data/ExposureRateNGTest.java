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
public class ExposureRateNGTest
{
  
  public ExposureRateNGTest()
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
   * Test of getDetector method, of class ExposureRate.
   */
  @Test
  public void testGetDetector()
  {
    System.out.println("getDetector");
    ExposureRate instance = new ExposureRate();
    RadDetectorInformation expResult = null;
    RadDetectorInformation result = instance.getDetector();
    assertEquals(result, expResult);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of setDetector method, of class ExposureRate.
   */
  @Test
  public void testSetDetector()
  {
    System.out.println("setDetector");
    RadDetectorInformation detector = null;
    ExposureRate instance = new ExposureRate();
    instance.setDetector(detector);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of getValue method, of class ExposureRate.
   */
  @Test
  public void testGetValue()
  {
    System.out.println("getValue");
    ExposureRate instance = new ExposureRate();
    Quantity expResult = null;
    Quantity result = instance.getValue();
    assertEquals(result, expResult);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of setValue method, of class ExposureRate.
   */
  @Test
  public void testSetValue()
  {
    System.out.println("setValue");
    Quantity value = null;
    ExposureRate instance = new ExposureRate();
    instance.setValue(value);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of getLevelDescription method, of class ExposureRate.
   */
  @Test
  public void testGetLevelDescription()
  {
    System.out.println("getLevelDescription");
    ExposureRate instance = new ExposureRate();
    String expResult = "";
    String result = instance.getLevelDescription();
    assertEquals(result, expResult);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of setLevelDescription method, of class ExposureRate.
   */
  @Test
  public void testSetLevelDescription()
  {
    System.out.println("setLevelDescription");
    String levelDescription = "";
    ExposureRate instance = new ExposureRate();
    instance.setLevelDescription(levelDescription);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of visitReferencedObjects method, of class ExposureRate.
   */
  @Test
  public void testVisitReferencedObjects()
  {
    System.out.println("visitReferencedObjects");
    Consumer<ComplexObject> visitor = null;
    ExposureRate instance = new ExposureRate();
    instance.visitReferencedObjects(visitor);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }
  
}
