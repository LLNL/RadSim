/*
 * Copyright 2022, Lawrence Livermore National Security, LLC.
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.nist.physics.n42.data;

import java.time.Instant;
import java.util.List;
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
public class RadAlarmNGTest
{
  
  public RadAlarmNGTest()
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
   * Test of getCategoryCode method, of class RadAlarm.
   */
  @Test
  public void testGetCategoryCode()
  {
    System.out.println("getCategoryCode");
    RadAlarm instance = new RadAlarm();
    RadAlarmCategoryCode expResult = null;
    RadAlarmCategoryCode result = instance.getCategoryCode();
    assertEquals(result, expResult);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of setCategoryCode method, of class RadAlarm.
   */
  @Test
  public void testSetCategoryCode()
  {
    System.out.println("setCategoryCode");
    RadAlarmCategoryCode categoryCode = null;
    RadAlarm instance = new RadAlarm();
    instance.setCategoryCode(categoryCode);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of getDescription method, of class RadAlarm.
   */
  @Test
  public void testGetDescription()
  {
    System.out.println("getDescription");
    RadAlarm instance = new RadAlarm();
    String expResult = "";
    String result = instance.getDescription();
    assertEquals(result, expResult);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of setDescription method, of class RadAlarm.
   */
  @Test
  public void testSetDescription()
  {
    System.out.println("setDescription");
    String description = "";
    RadAlarm instance = new RadAlarm();
    instance.setDescription(description);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of getAudibleIndicator method, of class RadAlarm.
   */
  @Test
  public void testGetAudibleIndicator()
  {
    System.out.println("getAudibleIndicator");
    RadAlarm instance = new RadAlarm();
    Boolean expResult = null;
    Boolean result = instance.getAudibleIndicator();
    assertEquals(result, expResult);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of setAudibleIndicator method, of class RadAlarm.
   */
  @Test
  public void testSetAudibleIndicator()
  {
    System.out.println("setAudibleIndicator");
    Boolean audibleIndicator = null;
    RadAlarm instance = new RadAlarm();
    instance.setAudibleIndicator(audibleIndicator);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of getLightColor method, of class RadAlarm.
   */
  @Test
  public void testGetLightColor()
  {
    System.out.println("getLightColor");
    RadAlarm instance = new RadAlarm();
    String expResult = "";
    String result = instance.getLightColor();
    assertEquals(result, expResult);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of setLightColor method, of class RadAlarm.
   */
  @Test
  public void testSetLightColor()
  {
    System.out.println("setLightColor");
    String lightColor = "";
    RadAlarm instance = new RadAlarm();
    instance.setLightColor(lightColor);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of getEnergyWindowIndices method, of class RadAlarm.
   */
  @Test
  public void testGetEnergyWindowIndices()
  {
    System.out.println("getEnergyWindowIndices");
    RadAlarm instance = new RadAlarm();
    int[] expResult = null;
    int[] result = instance.getEnergyWindowIndices();
    assertEquals(result, expResult);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of setEnergyWindowIndices method, of class RadAlarm.
   */
  @Test
  public void testSetEnergyWindowIndices()
  {
    System.out.println("setEnergyWindowIndices");
    int[] energyWindowIndices = null;
    RadAlarm instance = new RadAlarm();
    instance.setEnergyWindowIndices(energyWindowIndices);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of addDetector method, of class RadAlarm.
   */
  @Test
  public void testAddDetector()
  {
    System.out.println("addDetector");
    RadDetectorInformation detector = null;
    RadAlarm instance = new RadAlarm();
    instance.addDetector(detector);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of getDetectors method, of class RadAlarm.
   */
  @Test
  public void testGetDetectors()
  {
    System.out.println("getDetectors");
    RadAlarm instance = new RadAlarm();
    List expResult = null;
    List result = instance.getDetectors();
    assertEquals(result, expResult);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of getDateTime method, of class RadAlarm.
   */
  @Test
  public void testGetDateTime()
  {
    System.out.println("getDateTime");
    RadAlarm instance = new RadAlarm();
    Instant expResult = null;
    Instant result = instance.getDateTime();
    assertEquals(result, expResult);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of setDateTime method, of class RadAlarm.
   */
  @Test
  public void testSetDateTime()
  {
    System.out.println("setDateTime");
    Instant dateTime = null;
    RadAlarm instance = new RadAlarm();
    instance.setDateTime(dateTime);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of visitReferencedObjects method, of class RadAlarm.
   */
  @Test
  public void testVisitReferencedObjects()
  {
    System.out.println("visitReferencedObjects");
    Consumer<ComplexObject> visitor = null;
    RadAlarm instance = new RadAlarm();
    instance.visitReferencedObjects(visitor);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }
  
}
