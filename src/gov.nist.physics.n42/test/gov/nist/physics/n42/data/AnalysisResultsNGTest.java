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
import java.util.function.Consumer;
import static org.testng.Assert.*;
import org.testng.annotations.Test;

/**
 *
 * @author nelson85
 */
public class AnalysisResultsNGTest
{
  
  public AnalysisResultsNGTest()
  {
  }
  
  public AnalysisResults newInstance()
  {
    try
    {
      DocumentReader<RadInstrumentData> dr = DocumentReader.create(RadInstrumentData.class);
      RadInstrumentData rid = dr.loadFile(Paths.get("test/gov/nist/physics/n42/resources/SimpleSpectrometer2.xml"));
      return rid.getAnalysisResults().get(0);
    }
    catch (ReaderException | IOException ex)
    {
      throw new RuntimeException(ex);
    }
  }

  @Test
  public void testGetStartDateTime()
  {
    AnalysisResults instance = newInstance();
    Instant expResult = Instant.parse("2003-11-23T06:46:20Z");
    Instant result = instance.getStartDateTime();
    assertEquals(result, expResult);
  }

  @Test
  public void testSetStartDateTime()
  {
    Instant startDateTime = Instant.now();
    AnalysisResults instance = new AnalysisResults();
    instance.setStartDateTime(startDateTime);
    assertEquals(instance.getStartDateTime(), startDateTime);
  }

  @Test
  public void testGetAlgorithmName()
  {
    AnalysisResults instance = newInstance();
    String expResult = "SuperNID";
    String result = instance.getAlgorithmName();
    assertEquals(result, expResult);
  }

  @Test
  public void testSetAlgorithmName()
  {
    String algorithmName = "test";
    AnalysisResults instance = new AnalysisResults();
    instance.setAlgorithmName(algorithmName);
    assertEquals(instance.getAlgorithmName(), algorithmName);
  }

  @Test
  public void testGetAlgorithmCreatorName()
  {
    AnalysisResults instance = newInstance();
    String expResult = "Spectrometers R Us";
    String result = instance.getAlgorithmCreatorName();
    assertEquals(result, expResult);
  }

  @Test
  public void testGetNuclideAnalysisResults()
  {
    AnalysisResults instance = newInstance();
    NuclideAnalysisResults result = instance.getNuclideAnalysisResults();
    assertNotNull(result);
  }

  @Test
  public void testSetAlgorithmCreatorName()
  {
    String algorithmCreatorName = "test";
    AnalysisResults instance = new AnalysisResults();
    instance.setAlgorithmCreatorName(algorithmCreatorName);
    assertEquals(instance.getAlgorithmCreatorName(), algorithmCreatorName);
  }

  @Test
  public void testGetAlgorithmVersion()
  {
    AnalysisResults instance = newInstance();
    List<AnalysisAlgorithmVersion> result = instance.getAlgorithmVersion();
    assertNotNull(result);
  }

  @Test
  public void testSetAlgorithmVersion()
  {
    AnalysisAlgorithmVersion algorithmVersion = new AnalysisAlgorithmVersion();
    AnalysisResults instance = new AnalysisResults();
    instance.addAlgorithmVersion(algorithmVersion);
    assertTrue(instance.getAlgorithmVersion().contains(algorithmVersion));
  }

  @Test
  public void testAddToGroup()
  {
    Object radMeasurementGroup = new RadMeasurementGroup();
    AnalysisResults instance = new AnalysisResults();
    instance.addToGroup(radMeasurementGroup);
    assertTrue(instance.getMeasurementGroups().contains(radMeasurementGroup));
  }

  @Test
  public void testAddToMeasurement()
  {
    RadMeasurement radMeasurement = new RadMeasurement();
    AnalysisResults instance = new AnalysisResults();
    instance.addToMeasurement(radMeasurement);
    assertTrue( instance.getMeasurements().contains(radMeasurement));
  }

  @Test
  public void testSetNuclideAnalysisResults()
  {
    NuclideAnalysisResults nar = new NuclideAnalysisResults();
    AnalysisResults instance = new AnalysisResults();
    instance.setNuclideAnalysisResults(nar);
    assertEquals(instance.getNuclideAnalysisResults(),nar);
  }

  /**
   * Test of getStatusCode method, of class AnalysisResults.
   */
  @Test
  public void testGetStatusCode()
  {
    System.out.println("getStatusCode");
    AnalysisResults instance = new AnalysisResults();
    AnalysisResultStatusCode expResult = null;
    AnalysisResultStatusCode result = instance.getStatusCode();
    assertEquals(result, expResult);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of addAlgorithmVersion method, of class AnalysisResults.
   */
  @Test
  public void testAddAlgorithmVersion()
  {
    System.out.println("addAlgorithmVersion");
    AnalysisAlgorithmVersion algorithmVersion = null;
    AnalysisResults instance = new AnalysisResults();
    instance.addAlgorithmVersion(algorithmVersion);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of addAlgorithmSetting method, of class AnalysisResults.
   */
  @Test
  public void testAddAlgorithmSetting()
  {
    System.out.println("addAlgorithmSetting");
    AnalysisAlgorithmSetting value = null;
    AnalysisResults instance = new AnalysisResults();
    instance.addAlgorithmSetting(value);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of addToReferences method, of class AnalysisResults.
   */
  @Test
  public void testAddToReferences()
  {
    System.out.println("addToReferences");
    DerivedData derivedData = null;
    AnalysisResults instance = new AnalysisResults();
    instance.addToReferences(derivedData);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of setSpectrumPeakAnalysisResults method, of class AnalysisResults.
   */
  @Test
  public void testSetSpectrumPeakAnalysisResults()
  {
    System.out.println("setSpectrumPeakAnalysisResults");
    SpectrumPeakAnalysisResults results = null;
    AnalysisResults instance = new AnalysisResults();
    instance.setSpectrumPeakAnalysisResults(results);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of setGrossCountAnalysisResults method, of class AnalysisResults.
   */
  @Test
  public void testSetGrossCountAnalysisResults()
  {
    System.out.println("setGrossCountAnalysisResults");
    GrossCountAnalysisResults results = null;
    AnalysisResults instance = new AnalysisResults();
    instance.setGrossCountAnalysisResults(results);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of setDoseAnalysisResults method, of class AnalysisResults.
   */
  @Test
  public void testSetDoseAnalysisResults()
  {
    System.out.println("setDoseAnalysisResults");
    DoseAnalysisResults results = null;
    AnalysisResults instance = new AnalysisResults();
    instance.setDoseAnalysisResults(results);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of setExposureAnalysisResults method, of class AnalysisResults.
   */
  @Test
  public void testSetExposureAnalysisResults()
  {
    System.out.println("setExposureAnalysisResults");
    ExposureAnalysisResults results = null;
    AnalysisResults instance = new AnalysisResults();
    instance.setExposureAnalysisResults(results);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of addFault method, of class AnalysisResults.
   */
  @Test
  public void testAddFault()
  {
    System.out.println("addFault");
    Fault fault = null;
    AnalysisResults instance = new AnalysisResults();
    instance.addFault(fault);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of getGrossCountAnalysisResults method, of class AnalysisResults.
   */
  @Test
  public void testGetGrossCountAnalysisResults()
  {
    System.out.println("getGrossCountAnalysisResults");
    AnalysisResults instance = new AnalysisResults();
    GrossCountAnalysisResults expResult = null;
    GrossCountAnalysisResults result = instance.getGrossCountAnalysisResults();
    assertEquals(result, expResult);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of getDoseAnalysisResults method, of class AnalysisResults.
   */
  @Test
  public void testGetDoseAnalysisResults()
  {
    System.out.println("getDoseAnalysisResults");
    AnalysisResults instance = new AnalysisResults();
    DoseAnalysisResults expResult = null;
    DoseAnalysisResults result = instance.getDoseAnalysisResults();
    assertEquals(result, expResult);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of getExposureAnalysisResults method, of class AnalysisResults.
   */
  @Test
  public void testGetExposureAnalysisResults()
  {
    System.out.println("getExposureAnalysisResults");
    AnalysisResults instance = new AnalysisResults();
    ExposureAnalysisResults expResult = null;
    ExposureAnalysisResults result = instance.getExposureAnalysisResults();
    assertEquals(result, expResult);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of getFaults method, of class AnalysisResults.
   */
  @Test
  public void testGetFaults()
  {
    System.out.println("getFaults");
    AnalysisResults instance = new AnalysisResults();
    List expResult = null;
    List result = instance.getFaults();
    assertEquals(result, expResult);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of getMeasurements method, of class AnalysisResults.
   */
  @Test
  public void testGetMeasurements()
  {
    System.out.println("getMeasurements");
    AnalysisResults instance = new AnalysisResults();
    List expResult = null;
    List result = instance.getMeasurements();
    assertEquals(result, expResult);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of getMeasurementGroups method, of class AnalysisResults.
   */
  @Test
  public void testGetMeasurementGroups()
  {
    System.out.println("getMeasurementGroups");
    AnalysisResults instance = new AnalysisResults();
    List expResult = null;
    List result = instance.getMeasurementGroups();
    assertEquals(result, expResult);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of getSpectrumPeakAnalysisResults method, of class AnalysisResults.
   */
  @Test
  public void testGetSpectrumPeakAnalysisResults()
  {
    System.out.println("getSpectrumPeakAnalysisResults");
    AnalysisResults instance = new AnalysisResults();
    SpectrumPeakAnalysisResults expResult = null;
    SpectrumPeakAnalysisResults result = instance.getSpectrumPeakAnalysisResults();
    assertEquals(result, expResult);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of getRadAlarm method, of class AnalysisResults.
   */
  @Test
  public void testGetRadAlarm()
  {
    System.out.println("getRadAlarm");
    AnalysisResults instance = new AnalysisResults();
    List expResult = null;
    List result = instance.getRadAlarm();
    assertEquals(result, expResult);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of addRadAlarm method, of class AnalysisResults.
   */
  @Test
  public void testAddRadAlarm()
  {
    System.out.println("addRadAlarm");
    RadAlarm result_2 = null;
    AnalysisResults instance = new AnalysisResults();
    instance.addRadAlarm(result_2);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of setConfidence method, of class AnalysisResults.
   */
  @Test
  public void testSetConfidence()
  {
    System.out.println("setConfidence");
    Double u = null;
    AnalysisResults instance = new AnalysisResults();
    instance.setConfidence(u);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of setDescription method, of class AnalysisResults.
   */
  @Test
  public void testSetDescription()
  {
    System.out.println("setDescription");
    String u = "";
    AnalysisResults instance = new AnalysisResults();
    instance.setDescription(u);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of getAlgorithmSettings method, of class AnalysisResults.
   */
  @Test
  public void testGetAlgorithmSettings()
  {
    System.out.println("getAlgorithmSettings");
    AnalysisResults instance = new AnalysisResults();
    List expResult = null;
    List result = instance.getAlgorithmSettings();
    assertEquals(result, expResult);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of setStatusCode method, of class AnalysisResults.
   */
  @Test
  public void testSetStatusCode()
  {
    System.out.println("setStatusCode");
    AnalysisResultStatusCode code = null;
    AnalysisResults instance = new AnalysisResults();
    instance.setStatusCode(code);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of getConfidence method, of class AnalysisResults.
   */
  @Test
  public void testGetConfidence()
  {
    System.out.println("getConfidence");
    AnalysisResults instance = new AnalysisResults();
    Double expResult = null;
    Double result = instance.getConfidence();
    assertEquals(result, expResult);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of getDescription method, of class AnalysisResults.
   */
  @Test
  public void testGetDescription()
  {
    System.out.println("getDescription");
    AnalysisResults instance = new AnalysisResults();
    String expResult = "";
    String result = instance.getDescription();
    assertEquals(result, expResult);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of visitReferencedObjects method, of class AnalysisResults.
   */
  @Test
  public void testVisitReferencedObjects()
  {
    System.out.println("visitReferencedObjects");
    Consumer<ComplexObject> visitor = null;
    AnalysisResults instance = new AnalysisResults();
    instance.visitReferencedObjects(visitor);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }
  
}
