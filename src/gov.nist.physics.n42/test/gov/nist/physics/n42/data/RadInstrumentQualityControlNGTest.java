/*
 * Copyright 2022, Lawrence Livermore National Security, LLC.
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.nist.physics.n42.data;

import java.time.Instant;
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
public class RadInstrumentQualityControlNGTest
{
  
  public RadInstrumentQualityControlNGTest()
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
   * Test of getInspectionDateTime method, of class RadInstrumentQualityControl.
   */
  @Test
  public void testGetInspectionDateTime()
  {
    System.out.println("getInspectionDateTime");
    RadInstrumentQualityControl instance = new RadInstrumentQualityControl();
    Instant expResult = null;
    Instant result = instance.getInspectionDateTime();
    assertEquals(result, expResult);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of isInCalibrationIndicator method, of class RadInstrumentQualityControl.
   */
  @Test
  public void testIsInCalibrationIndicator()
  {
    System.out.println("isInCalibrationIndicator");
    RadInstrumentQualityControl instance = new RadInstrumentQualityControl();
    boolean expResult = false;
    boolean result = instance.isInCalibrationIndicator();
    assertEquals(result, expResult);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of setInspectionDateTime method, of class RadInstrumentQualityControl.
   */
  @Test
  public void testSetInspectionDateTime()
  {
    System.out.println("setInspectionDateTime");
    Instant inspectionDateTime = null;
    RadInstrumentQualityControl instance = new RadInstrumentQualityControl();
    instance.setInspectionDateTime(inspectionDateTime);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of setInCalibrationIndicator method, of class RadInstrumentQualityControl.
   */
  @Test
  public void testSetInCalibrationIndicator()
  {
    System.out.println("setInCalibrationIndicator");
    boolean value = false;
    RadInstrumentQualityControl instance = new RadInstrumentQualityControl();
    instance.setInCalibrationIndicator(value);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }
  
}
