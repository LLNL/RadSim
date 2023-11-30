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
import java.util.List;
import java.util.Map;
import static org.testng.Assert.*;
import org.testng.annotations.Test;

/**
 *
 * @author nelson85
 */
public class RadInstrumentInformationNGTest
{

  public RadInstrumentInformationNGTest()
  {
  }

  public RadInstrumentInformation newInstance()
  {
    try
    {
      DocumentReader<RadInstrumentData> dr = DocumentReader.create(RadInstrumentData.class);
      RadInstrumentData rid = dr.loadFile(Paths.get("test/gov/nist/physics/n42/resources/Alarm.xml"));
      return rid.getInstrument();
    }
    catch (ReaderException | IOException ex)
    {
      throw new RuntimeException(ex);
    }
  }

  @Test
  public void testGetManufacturerName()
  {
    RadInstrumentInformation instance = newInstance();
    String expResult = "RPMs R Us";
    String result = instance.getManufacturerName();
    assertEquals(result, expResult);
  }

  @Test
  public void testSetManufacturerName()
  {
    String manufacturerName = "man";
    RadInstrumentInformation instance = new RadInstrumentInformation();
    instance.setManufacturerName(manufacturerName);
    assertSame(instance.getManufacturerName(), manufacturerName);
  }

  @Test
  public void testGetIdentifier()
  {
    RadInstrumentInformation instance = newInstance();
    String expResult = "A1234b";
    String result = instance.getIdentifier();
    assertEquals(result, expResult);
  }

  @Test
  public void testSetIdentifier()
  {
    String identifier = "id";
    RadInstrumentInformation instance = new RadInstrumentInformation();
    instance.setIdentifier(identifier);
    assertEquals(instance.getIdentifier(), identifier);
  }

  @Test
  public void testGetModelName()
  {
    RadInstrumentInformation instance = newInstance();
    String expResult = "RPM 3000";
    String result = instance.getModelName();
    assertEquals(result, expResult);
  }

  @Test
  public void testSetModelName()
  {
    String modelName = "model";
    RadInstrumentInformation instance = new RadInstrumentInformation();
    instance.setModelName(modelName);
    assertEquals(instance.getModelName(), modelName);
  }

  @Test
  public void testGetClassCode()
  {
    RadInstrumentInformation instance = newInstance();
    String expResult = "Portal Monitor";
    String result = instance.getClassCode();
    assertEquals(result, expResult);
  }

  @Test
  public void testSetClassCode()
  {
    String classCode = "foo";
    RadInstrumentInformation instance = new RadInstrumentInformation();
    instance.setClassCode(classCode);
    assertEquals(instance.getClassCode(), classCode);
  }

  @Test
  public void testGetVersion()
  {
    RadInstrumentInformation instance = newInstance();
    Map result = instance.getVersion();
    assertTrue(instance.getVersion().containsKey("Hardware"));
    assertTrue(instance.getVersion().containsKey("Software"));
  }

  @Test
  public void testSetVersion()
  {
    RadInstrumentVersion version = new RadInstrumentVersion("a","b");
    RadInstrumentInformation instance = new RadInstrumentInformation();
    instance.addVersion(version);
    assertEquals(instance.getVersion().get("a"),"b");
  }

  @Test
  public void testAddCharacteristics()
  {
    Characteristics chars = new Characteristics();
    RadInstrumentInformation instance = new RadInstrumentInformation();
    instance.addCharacteristics(chars);
    assertTrue(instance.getCharacteristics().contains(chars));
  }

  /**
   * Test of getDescription method, of class RadInstrumentInformation.
   */
  @Test
  public void testGetDescription()
  {
    System.out.println("getDescription");
    RadInstrumentInformation instance = new RadInstrumentInformation();
    String expResult = "";
    String result = instance.getDescription();
    assertEquals(result, expResult);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of setDescription method, of class RadInstrumentInformation.
   */
  @Test
  public void testSetDescription()
  {
    System.out.println("setDescription");
    String description = "";
    RadInstrumentInformation instance = new RadInstrumentInformation();
    instance.setDescription(description);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of addVersion method, of class RadInstrumentInformation.
   */
  @Test
  public void testAddVersion()
  {
    System.out.println("addVersion");
    RadInstrumentVersion version = null;
    RadInstrumentInformation instance = new RadInstrumentInformation();
    instance.addVersion(version);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of setQualityControl method, of class RadInstrumentInformation.
   */
  @Test
  public void testSetQualityControl()
  {
    System.out.println("setQualityControl");
    RadInstrumentQualityControl value = null;
    RadInstrumentInformation instance = new RadInstrumentInformation();
    instance.setQualityControl(value);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of getCharacteristics method, of class RadInstrumentInformation.
   */
  @Test
  public void testGetCharacteristics()
  {
    System.out.println("getCharacteristics");
    RadInstrumentInformation instance = new RadInstrumentInformation();
    List expResult = null;
    List result = instance.getCharacteristics();
    assertEquals(result, expResult);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of getQualityControl method, of class RadInstrumentInformation.
   */
  @Test
  public void testGetQualityControl()
  {
    System.out.println("getQualityControl");
    RadInstrumentInformation instance = new RadInstrumentInformation();
    RadInstrumentQualityControl expResult = null;
    RadInstrumentQualityControl result = instance.getQualityControl();
    assertEquals(result, expResult);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

}
