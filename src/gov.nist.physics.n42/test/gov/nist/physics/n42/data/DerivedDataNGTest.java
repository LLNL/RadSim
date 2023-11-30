/*
 * Copyright 2022, Lawrence Livermore National Security, LLC.
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.nist.physics.n42.data;

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
public class DerivedDataNGTest
{
  
  public DerivedDataNGTest()
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
   * Test of getMeasurementClassCode method, of class DerivedData.
   */
  @Test
  public void testGetMeasurementClassCode()
  {
    System.out.println("getMeasurementClassCode");
    DerivedData instance = new DerivedData();
    String expResult = "";
    String result = instance.getMeasurementClassCode();
    assertEquals(result, expResult);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of getStartDateTime method, of class DerivedData.
   */
  @Test
  public void testGetStartDateTime()
  {
    System.out.println("getStartDateTime");
    DerivedData instance = new DerivedData();
    String expResult = "";
    String result = instance.getStartDateTime();
    assertEquals(result, expResult);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of getRealTimeDuration method, of class DerivedData.
   */
  @Test
  public void testGetRealTimeDuration()
  {
    System.out.println("getRealTimeDuration");
    DerivedData instance = new DerivedData();
    String expResult = "";
    String result = instance.getRealTimeDuration();
    assertEquals(result, expResult);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of getSpectrum method, of class DerivedData.
   */
  @Test
  public void testGetSpectrum()
  {
    System.out.println("getSpectrum");
    DerivedData instance = new DerivedData();
    List expResult = null;
    List result = instance.getSpectrum();
    assertEquals(result, expResult);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of getGrossCounts method, of class DerivedData.
   */
  @Test
  public void testGetGrossCounts()
  {
    System.out.println("getGrossCounts");
    DerivedData instance = new DerivedData();
    List expResult = null;
    List result = instance.getGrossCounts();
    assertEquals(result, expResult);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of getDoseRate method, of class DerivedData.
   */
  @Test
  public void testGetDoseRate()
  {
    System.out.println("getDoseRate");
    DerivedData instance = new DerivedData();
    List expResult = null;
    List result = instance.getDoseRate();
    assertEquals(result, expResult);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of getTotalDose method, of class DerivedData.
   */
  @Test
  public void testGetTotalDose()
  {
    System.out.println("getTotalDose");
    DerivedData instance = new DerivedData();
    List expResult = null;
    List result = instance.getTotalDose();
    assertEquals(result, expResult);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of getExposureRate method, of class DerivedData.
   */
  @Test
  public void testGetExposureRate()
  {
    System.out.println("getExposureRate");
    DerivedData instance = new DerivedData();
    List expResult = null;
    List result = instance.getExposureRate();
    assertEquals(result, expResult);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of getTotalExposure method, of class DerivedData.
   */
  @Test
  public void testGetTotalExposure()
  {
    System.out.println("getTotalExposure");
    DerivedData instance = new DerivedData();
    List expResult = null;
    List result = instance.getTotalExposure();
    assertEquals(result, expResult);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of setMeasurementClassCode method, of class DerivedData.
   */
  @Test
  public void testSetMeasurementClassCode()
  {
    System.out.println("setMeasurementClassCode");
    String value = "";
    DerivedData instance = new DerivedData();
    instance.setMeasurementClassCode(value);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of setStartDateTime method, of class DerivedData.
   */
  @Test
  public void testSetStartDateTime()
  {
    System.out.println("setStartDateTime");
    String value = "";
    DerivedData instance = new DerivedData();
    instance.setStartDateTime(value);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of setRealTimeDuration method, of class DerivedData.
   */
  @Test
  public void testSetRealTimeDuration()
  {
    System.out.println("setRealTimeDuration");
    String value = "";
    DerivedData instance = new DerivedData();
    instance.setRealTimeDuration(value);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of addSpectrum method, of class DerivedData.
   */
  @Test
  public void testAddSpectrum()
  {
    System.out.println("addSpectrum");
    Spectrum value = null;
    DerivedData instance = new DerivedData();
    instance.addSpectrum(value);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of addGrossCounts method, of class DerivedData.
   */
  @Test
  public void testAddGrossCounts()
  {
    System.out.println("addGrossCounts");
    GrossCounts value = null;
    DerivedData instance = new DerivedData();
    instance.addGrossCounts(value);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of addDoseRate method, of class DerivedData.
   */
  @Test
  public void testAddDoseRate()
  {
    System.out.println("addDoseRate");
    DoseRate value = null;
    DerivedData instance = new DerivedData();
    instance.addDoseRate(value);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of addTotalDose method, of class DerivedData.
   */
  @Test
  public void testAddTotalDose()
  {
    System.out.println("addTotalDose");
    DoseRate value = null;
    DerivedData instance = new DerivedData();
    instance.addTotalDose(value);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of addExposureRate method, of class DerivedData.
   */
  @Test
  public void testAddExposureRate()
  {
    System.out.println("addExposureRate");
    ExposureRate value = null;
    DerivedData instance = new DerivedData();
    instance.addExposureRate(value);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of addTotalExposure method, of class DerivedData.
   */
  @Test
  public void testAddTotalExposure()
  {
    System.out.println("addTotalExposure");
    ExposureRate value = null;
    DerivedData instance = new DerivedData();
    instance.addTotalExposure(value);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of visitReferencedObjects method, of class DerivedData.
   */
  @Test
  public void testVisitReferencedObjects()
  {
    System.out.println("visitReferencedObjects");
    Consumer<ComplexObject> visitor = null;
    DerivedData instance = new DerivedData();
    instance.visitReferencedObjects(visitor);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }
  
}
