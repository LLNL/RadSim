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
import static org.testng.Assert.*;
import org.testng.annotations.Test;

/**
 *
 * @author nelson85
 */
public class RadItemInformationNGTest
{

  public RadItemInformationNGTest()
  {
  }

  public RadItemInformation newInstance()
  {
    try
    {
      DocumentReader<RadInstrumentData> dr = DocumentReader.create(RadInstrumentData.class);
      RadInstrumentData rid = dr.loadFile(Paths.get("test/gov/nist/physics/n42/resources/SRPM.xml"));
      return rid.getItems().get(0);
    }
    catch (ReaderException | IOException ex)
    {
      throw new RuntimeException(ex);
    }
  }

  @Test
  public void testSetDescription()
  {
    String itemDescription = "";
    RadItemInformation instance = new RadItemInformation();
    instance.setDescription(itemDescription);
    assertEquals(instance.getDescription(), itemDescription);
  }

  @Test
  public void testSetQuantity()
  {
    Quantity quantity = new Quantity();
    RadItemInformation instance = new RadItemInformation();
    instance.setQuantity(quantity);
    assertSame(instance.getQuantity(), quantity);
  }

  @Test
  public void testSetMeasurementGeometryDescription()
  {
    String description = "desc";
    RadItemInformation instance = new RadItemInformation();
    instance.setMeasurementGeometryDescription(description);
    assertEquals(instance.getMeasurementGeometryDescription(), description);
  }

  @Test
  public void testAddCharacteristics()
  {
    Characteristics chars = new Characteristics();
    RadItemInformation instance = new RadItemInformation();
    instance.addCharacteristics(chars);
    assertTrue(instance.getCharacteristics().contains(chars));
  }

  @Test
  public void testGetDescription()
  {
    RadItemInformation instance = newInstance();
    String result = instance.getDescription();
    assertNull(result);
  }

  @Test
  public void testGetCharacteristics()
  {
    RadItemInformation instance = newInstance();
    List result = instance.getCharacteristics();
    assertNotNull(result);
    assertEquals(result.size(), 1);
  }

  /**
   * Test of getQuantity method, of class RadItemInformation.
   */
  @Test
  public void testGetQuantity()
  {
    System.out.println("getQuantity");
    RadItemInformation instance = new RadItemInformation();
    Quantity expResult = null;
    Quantity result = instance.getQuantity();
    assertEquals(result, expResult);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of getMeasurementGeometryDescription method, of class RadItemInformation.
   */
  @Test
  public void testGetMeasurementGeometryDescription()
  {
    System.out.println("getMeasurementGeometryDescription");
    RadItemInformation instance = new RadItemInformation();
    String expResult = "";
    String result = instance.getMeasurementGeometryDescription();
    assertEquals(result, expResult);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

}
