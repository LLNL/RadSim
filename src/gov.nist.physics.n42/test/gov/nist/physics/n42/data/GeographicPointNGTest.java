/*
 * Copyright 2022, Lawrence Livermore National Security, LLC.
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.nist.physics.n42.data;

import gov.llnl.utility.io.ReaderException;
import gov.llnl.utility.xml.bind.DocumentReader;
import java.io.IOException;
import java.nio.file.Paths;
import static org.testng.Assert.*;
import org.testng.annotations.Test;

/**
 *
 * @author nelson85
 */
public class GeographicPointNGTest
{

  public GeographicPointNGTest()
  {
  }
  
  public GeographicPoint newInstance()
  {
    try
    {
      DocumentReader<RadInstrumentData> dr = DocumentReader.create(RadInstrumentData.class);
      RadInstrumentData rid = dr.loadFile(Paths.get("test/gov/nist/physics/n42/resources/Mobile.xml"));
      return rid.getMeasurements().get(0).getInstrumentState().getStateVector().getCoordinate();
    }
    catch (ReaderException | IOException ex)
    {
      throw new RuntimeException(ex);
    }
  }

  @Test
  public void testGetLatitude()
  {
    GeographicPoint instance = newInstance();
    double expResult = 41.608476;
    Quantity result = instance.getLatitude();
    assertEquals(result.getValue(), expResult, 0.0);
  }

  @Test
  public void testSetLatitude()
  {
    Quantity latitude = new Quantity(0, "deg");
    GeographicPoint instance = new GeographicPoint();
    instance.setLatitude(latitude);
    assertSame(instance.getLatitude(), latitude);
  }

  @Test
  public void testGetLongitude()
  {
    GeographicPoint instance = newInstance();
    double expResult = -72.885790;
    Quantity result = instance.getLongitude();
    assertEquals(result.getValue(), expResult, 0.0);
  }

  @Test
  public void testSetLongitude()
  {
    Quantity longitude = new Quantity(1.0, "deg");
    GeographicPoint instance = new GeographicPoint();
    instance.setLongitude(longitude);
    assertEquals(instance.getLongitude(), longitude);
  }

  @Test
  public void testGetElevation()
  {
    GeographicPoint instance = newInstance();
    double expResult = 84.0;
    Double result = instance.getElevation();
    assertEquals(result, expResult, 0.0);
  }

  @Test
  public void testSetElevation()
  {
    double elevation = 1.0;
    GeographicPoint instance = new GeographicPoint();
    instance.setElevation(elevation);
    assertEquals(instance.getElevation(), elevation, 0.000001);
  }

  /**
   * Test of setElevationOffset method, of class GeographicPoint.
   */
  @Test
  public void testSetElevationOffset()
  {
    System.out.println("setElevationOffset");
    Double u = null;
    GeographicPoint instance = new GeographicPoint();
    instance.setElevationOffset(u);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of setGeoPointAccuracy method, of class GeographicPoint.
   */
  @Test
  public void testSetGeoPointAccuracy()
  {
    System.out.println("setGeoPointAccuracy");
    Double u = null;
    GeographicPoint instance = new GeographicPoint();
    instance.setGeoPointAccuracy(u);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of setElevationAccuracy method, of class GeographicPoint.
   */
  @Test
  public void testSetElevationAccuracy()
  {
    System.out.println("setElevationAccuracy");
    Double u = null;
    GeographicPoint instance = new GeographicPoint();
    instance.setElevationAccuracy(u);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of setElevationOffsetAccuracy method, of class GeographicPoint.
   */
  @Test
  public void testSetElevationOffsetAccuracy()
  {
    System.out.println("setElevationOffsetAccuracy");
    Double u = null;
    GeographicPoint instance = new GeographicPoint();
    instance.setElevationOffsetAccuracy(u);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of getElevationOffset method, of class GeographicPoint.
   */
  @Test
  public void testGetElevationOffset()
  {
    System.out.println("getElevationOffset");
    GeographicPoint instance = new GeographicPoint();
    Double expResult = null;
    Double result = instance.getElevationOffset();
    assertEquals(result, expResult);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of getGeoPointAccuracy method, of class GeographicPoint.
   */
  @Test
  public void testGetGeoPointAccuracy()
  {
    System.out.println("getGeoPointAccuracy");
    GeographicPoint instance = new GeographicPoint();
    Double expResult = null;
    Double result = instance.getGeoPointAccuracy();
    assertEquals(result, expResult);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of getElevationAccuracy method, of class GeographicPoint.
   */
  @Test
  public void testGetElevationAccuracy()
  {
    System.out.println("getElevationAccuracy");
    GeographicPoint instance = new GeographicPoint();
    Double expResult = null;
    Double result = instance.getElevationAccuracy();
    assertEquals(result, expResult);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of getElevationOffsetAccuracy method, of class GeographicPoint.
   */
  @Test
  public void testGetElevationOffsetAccuracy()
  {
    System.out.println("getElevationOffsetAccuracy");
    GeographicPoint instance = new GeographicPoint();
    Double expResult = null;
    Double result = instance.getElevationOffsetAccuracy();
    assertEquals(result, expResult);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

}
