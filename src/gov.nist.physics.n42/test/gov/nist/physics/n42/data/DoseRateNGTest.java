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
import java.util.function.Consumer;
import static org.testng.Assert.*;
import org.testng.annotations.Test;

/**
 *
 * @author nelson85
 */
public class DoseRateNGTest
{
  
  public DoseRateNGTest()
  {
  }
  
  public DoseRate newInstance()
  {
    try
    {
      DocumentReader<RadInstrumentData> dr = DocumentReader.create(RadInstrumentData.class);
      RadInstrumentData rid = dr.loadFile(Paths.get("test/gov/nist/physics/n42/resources/SimpleSpectrometer2.xml"));
      return rid.getMeasurements().get(0).getDoseRate().get(0);
    }
    catch (ReaderException | IOException ex)
    {
      throw new RuntimeException(ex);
    }
  }

  @Test
  public void testGetDetector()
  {
    DoseRate instance = newInstance();
    RadDetectorInformation result = instance.getDetector();
    assertNotNull(result);
    assertEquals(result.getName(), null);
  }

  @Test
  public void testSetDetector()
  {
    RadDetectorInformation detector = new RadDetectorInformation();
    DoseRate instance = new DoseRate();
    instance.setDetector(detector);
    assertEquals(instance.getDetector(), detector);
  }

  @Test
  public void testGetValue()
  {
    DoseRate instance = newInstance();
    Quantity result = instance.getValue();
    assertNotNull(result);
    assertEquals(result.getValue(), 0.3);
    assertEquals(result.getUnits(), null);
  }

  @Test
  public void testSetValue()
  {
    Quantity value = new Quantity();
    DoseRate instance = new DoseRate();
    instance.setValue(value);
    assertEquals(instance.getValue(), value);
  }

  @Test
  public void testGetLevelDescription()
  {
    DoseRate instance = newInstance();
    String expResult = null;
    String result = instance.getLevelDescription();
    assertEquals(result, expResult);
  }

  @Test
  public void testSetLevelDescription()
  {
    String levelDescription = "test";
    DoseRate instance = new DoseRate();
    instance.setLevelDescription(levelDescription);
    assertEquals(instance.getLevelDescription(), levelDescription);
  }

  /**
   * Test of visitReferencedObjects method, of class DoseRate.
   */
  @Test
  public void testVisitReferencedObjects()
  {
    System.out.println("visitReferencedObjects");
    Consumer<ComplexObject> visitor = null;
    DoseRate instance = new DoseRate();
    instance.visitReferencedObjects(visitor);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }
  
}
