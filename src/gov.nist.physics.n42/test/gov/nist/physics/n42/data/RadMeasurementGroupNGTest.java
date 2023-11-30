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
public class RadMeasurementGroupNGTest
{

  public RadMeasurementGroupNGTest()
  {
  }

  public GrossCounts newInstance()
  {
    try
    {
      DocumentReader<RadInstrumentData> dr = DocumentReader.create(RadInstrumentData.class);
      RadInstrumentData rid = dr.loadFile(Paths.get("test/gov/nist/physics/n42/resources/Alarm.xml"));
      return rid.getMeasurements().get(0).getGrossCounts().get(0);
    }
    catch (ReaderException | IOException ex)
    {
      throw new RuntimeException(ex);
    }
  }

  @Test
  public void testSetDescription()
  {
    String description = "desc";
    RadMeasurementGroup instance = new RadMeasurementGroup();
    instance.setDescription(description);
    assertSame(instance.getDescription(),  description);
  }

  @Test
  public void testSetUUID()
  {
    String uuid = "uuid";
    RadMeasurementGroup instance = new RadMeasurementGroup();
    instance.setUUID(uuid);
    assertSame(instance.getUUID(),  uuid);
  }

  @Test
  public void testAddMeasurement()
  {
    RadMeasurement measurement = new RadMeasurement();
    RadMeasurementGroup instance = new RadMeasurementGroup();
    instance.addMeasurement(measurement);
    assertTrue(instance.getMeasurements().contains(measurement));
  }

  @Test
  public void testAddAnalysisResult()
  {
    AnalysisResults results = new AnalysisResults();
    RadMeasurementGroup instance = new RadMeasurementGroup();
    instance.addAnalysisResult(results);
    assertTrue(instance.getAnalysisResults().contains(results));
  }

  /**
   * Test of getDescription method, of class RadMeasurementGroup.
   */
  @Test
  public void testGetDescription()
  {
    System.out.println("getDescription");
    RadMeasurementGroup instance = new RadMeasurementGroup();
    String expResult = "";
    String result = instance.getDescription();
    assertEquals(result, expResult);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of getUUID method, of class RadMeasurementGroup.
   */
  @Test
  public void testGetUUID()
  {
    System.out.println("getUUID");
    RadMeasurementGroup instance = new RadMeasurementGroup();
    String expResult = "";
    String result = instance.getUUID();
    assertEquals(result, expResult);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of getMeasurements method, of class RadMeasurementGroup.
   */
  @Test
  public void testGetMeasurements()
  {
    System.out.println("getMeasurements");
    RadMeasurementGroup instance = new RadMeasurementGroup();
    List expResult = null;
    List result = instance.getMeasurements();
    assertEquals(result, expResult);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of getAnalysisResults method, of class RadMeasurementGroup.
   */
  @Test
  public void testGetAnalysisResults()
  {
    System.out.println("getAnalysisResults");
    RadMeasurementGroup instance = new RadMeasurementGroup();
    List expResult = null;
    List result = instance.getAnalysisResults();
    assertEquals(result, expResult);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

}
