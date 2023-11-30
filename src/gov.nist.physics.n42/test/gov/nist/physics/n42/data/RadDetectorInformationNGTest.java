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
import java.time.Instant;
import java.util.List;
import static org.testng.Assert.*;
import org.testng.annotations.Test;

/**
 *
 * @author nelson85
 */
public class RadDetectorInformationNGTest
{

  public RadDetectorInformationNGTest()
  {
  }

  public RadDetectorInformation newInstance()
  {
    try
    {
      DocumentReader<RadInstrumentData> dr = DocumentReader.create(RadInstrumentData.class);
      RadInstrumentData rid = dr.loadFile(Paths.get("test/gov/nist/physics/n42/resources/Alarm.xml"));
      return rid.getDetectors().get(0);
    }
    catch (ReaderException | IOException ex)
    {
      throw new RuntimeException(ex);
    }
  }

  @Test
  public void testGetName()
  {
    RadDetectorInformation instance = newInstance();
    String result = instance.getName();
    assertEquals(result, "Aa1");
  }

  @Test
  public void testSetName()
  {
    String name = "Foo";
    RadDetectorInformation instance = new RadDetectorInformation();
    instance.setName(name);
    assertEquals(instance.getName(), name);
  }

  @Test
  public void testGetCategoryCode()
  {
    RadDetectorInformation instance = newInstance();
    String result = instance.getCategoryCode();
    assertEquals(result, "Gamma");
  }

  @Test
  public void testSetCategoryCode()
  {
    String categoryCode = "cat";
    RadDetectorInformation instance = new RadDetectorInformation();
    instance.setCategoryCode(categoryCode);
    assertEquals(instance.getCategoryCode(), categoryCode);
  }

  @Test
  public void testGetKindCode()
  {
    RadDetectorInformation instance = newInstance();
    String expResult = "PVT";
    String result = instance.getKindCode();
    assertEquals(result, expResult);
  }

  @Test
  public void testSetKindCode()
  {
    String kindCode = "";
    RadDetectorInformation instance = new RadDetectorInformation();
    instance.setKindCode(kindCode);
    assertEquals(instance.getKindCode(), kindCode);
  }

  @Test
  public void testGetDescription()
  {
    RadDetectorInformation instance = newInstance();
    String expResult = "Driver's side panel left slab";
    String result = instance.getDescription();
    assertEquals(result, expResult);
  }

  @Test
  public void testSetDescription()
  {
    String description = "desc";
    RadDetectorInformation instance = new RadDetectorInformation();
    instance.setDescription(description);
    assertEquals(instance.getDescription(), description);
  }

  @Test
  public void testGetLength()
  {
    RadDetectorInformation instance = newInstance();
    Quantity result = instance.getLength();
    assertEquals(result, new Quantity(76.0, "cm"));
  }

  @Test
  public void testSetLength()
  {
    Quantity length = new Quantity();
    RadDetectorInformation instance = new RadDetectorInformation();
    instance.setLength(length);
    assertSame(instance.getLength(), length);
  }

  @Test
  public void testGetWidth()
  {
    RadDetectorInformation instance = newInstance();
    Quantity result = instance.getWidth();
    assertEquals(result, new Quantity(15.0, "cm"));
  }

  @Test
  public void testSetWidth()
  {
    Quantity width = new Quantity();
    RadDetectorInformation instance = new RadDetectorInformation();
    instance.setWidth(width);
    assertSame(instance.getWidth(), width);
  }

  @Test
  public void testGetDepth()
  {
    RadDetectorInformation instance = newInstance();
    Quantity result = instance.getDepth();
    assertEquals(result, new Quantity(4.0, "cm"));
  }

  @Test
  public void testSetDepth()
  {
    Quantity depth = new Quantity();
    RadDetectorInformation instance = new RadDetectorInformation();
    instance.setDepth(depth);
    assertSame(instance.getDepth(), depth);
  }

  @Test
  public void testGetVolume()
  {
    RadDetectorInformation instance = newInstance();
    Quantity result = instance.getVolume();
    assertEquals(result, new Quantity(1.77e7, "cc"));
  }

  @Test
  public void testSetVolume()
  {
    Quantity volume = new Quantity();
    RadDetectorInformation instance = new RadDetectorInformation();
    instance.setVolume(volume);
    assertSame(instance.getVolume(), volume);
  }

  @Test
  public void testGetDiameter()
  {
    RadDetectorInformation instance = newInstance();
    Quantity result = instance.getDiameter();
    assertNull(result);
  }

  @Test
  public void testSetDiameter()
  {
    Quantity diameter = new Quantity();
    RadDetectorInformation instance = new RadDetectorInformation();
    instance.setDiameter(diameter);
    assertSame(instance.getDiameter(), diameter);
  }

  @Test
  public void testAddCharacteristics()
  {
    Characteristics c = new Characteristics();
    RadDetectorInformation instance = new RadDetectorInformation();
    instance.addCharacteristics(c);
    assertTrue(instance.getCharacteristics().contains(c));
  }

  /**
   * Test of setN42DocDateTime method, of class RadDetectorInformation.
   */
  @Test
  public void testSetN42DocDateTime()
  {
    System.out.println("setN42DocDateTime");
    Instant time = null;
    RadDetectorInformation instance = new RadDetectorInformation();
    instance.setN42DocDateTime(time);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of getN42DocDateTime method, of class RadDetectorInformation.
   */
  @Test
  public void testGetN42DocDateTime()
  {
    System.out.println("getN42DocDateTime");
    RadDetectorInformation instance = new RadDetectorInformation();
    Instant expResult = null;
    Instant result = instance.getN42DocDateTime();
    assertEquals(result, expResult);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of getCharacteristics method, of class RadDetectorInformation.
   */
  @Test
  public void testGetCharacteristics()
  {
    System.out.println("getCharacteristics");
    RadDetectorInformation instance = new RadDetectorInformation();
    List expResult = null;
    List result = instance.getCharacteristics();
    assertEquals(result, expResult);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

}
