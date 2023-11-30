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
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import static org.testng.Assert.*;
import org.testng.annotations.Test;

/**
 *
 * @author nelson85
 */
public class RadMeasurementNGTest
{

  public RadMeasurementNGTest()
  {
  }

  public RadMeasurement newInstance()
  {
    try
    {
      DocumentReader<RadInstrumentData> dr = DocumentReader.create(RadInstrumentData.class);
      RadInstrumentData rid = dr.loadFile(Paths.get("test/gov/nist/physics/n42/resources/Alarm.xml"));
      return rid.getMeasurements().get(0);
    }
    catch (ReaderException | IOException ex)
    {
      throw new RuntimeException(ex);
    }
  }

  @Test
  public void testGetClassCode()
  {
    RadMeasurement instance = newInstance();
    MeasurementClassCode expResult = MeasurementClassCode.Background;
    MeasurementClassCode result = instance.getClassCode();
    assertEquals(result, expResult);
  }

  @Test
  public void testSetClassCode()
  {
    MeasurementClassCode code = MeasurementClassCode.Foreground;
    RadMeasurement instance = new RadMeasurement();
    instance.setClassCode(code);
    assertEquals(instance.getClassCode(), code);
  }

  @Test
  public void testGetStartDateTime()
  {
    RadMeasurement instance = newInstance();
    Instant expResult = Instant.parse("2003-11-23T06:45:00Z");
    Instant result = instance.getStartDateTime();
    assertEquals(result, expResult);
  }

  @Test
  public void testSetStartDateTime()
  {
    Instant startDateTime = Instant.now();
    RadMeasurement instance = new RadMeasurement();
    instance.setStartDateTime(startDateTime);
    assertEquals(instance.getStartDateTime(), startDateTime);
  }

  @Test
  public void testGetRealTimeDuration()
  {
    RadMeasurement instance = newInstance();
    Duration expResult = Duration.ofMillis(100);
    Duration result = instance.getRealTimeDuration();
    assertEquals(result, expResult);
  }

  @Test
  public void testSetRealTimeDuration()
  {
    Duration realTimeDuration = Duration.ofMillis(1000);
    RadMeasurement instance = new RadMeasurement();
    instance.setRealTimeDuration(realTimeDuration);
    assertEquals(instance.getRealTimeDuration(), realTimeDuration);
  }

  @Test
  public void testGetSpectrum()
  {
    RadMeasurement instance = newInstance();
    List expResult = new ArrayList();
    List result = instance.getSpectrum();
    assertEquals(result, expResult);
  }

  @Test
  public void testGetGrossCounts()
  {
    RadMeasurement instance = newInstance();
    List result = instance.getGrossCounts();
    assertNotNull(result);
    assertEquals(result.size(),6);
  }

  @Test
  public void testGetDoseRate()
  {
    RadMeasurement instance = newInstance();
    List result = instance.getDoseRate();
    assertNotNull(result);
    assertEquals(result.size(),0);
  }

  @Test
  public void testAddSpectrum()
  {
    Spectrum value = new Spectrum();
    RadMeasurement instance = new RadMeasurement();
    instance.addSpectrum(value);
    assertTrue(instance.getSpectrum().contains(value));
  }

  @Test
  public void testAddGrossCounts()
  {

    GrossCounts value = new GrossCounts();
    RadMeasurement instance = new RadMeasurement();
    instance.addGrossCounts(value);
    assertTrue(instance.getGrossCounts().contains(value));

  }

  @Test
  public void testAddDoseRate()
  {
    DoseRate value = new DoseRate();
    RadMeasurement instance = new RadMeasurement();
    instance.addDoseRate(value);
    assertTrue(instance.getDoseRate().contains(value));
  }

  @Test
  public void testGetDetectorState()
  {
    RadMeasurement instance = newInstance();
    List result = instance.getDetectorState();
    assertNotNull(result);
    assertEquals(result.size(),0);
  }

  @Test
  public void testGetItemState()
  {
    RadMeasurement instance = newInstance();
    List result = instance.getItemState();
    assertNotNull(result);
    assertEquals(result.size(),0);
  }

  @Test
  public void testAddDetectorState()
  {
    RadDetectorState state = new RadDetectorState();
    RadMeasurement instance = new RadMeasurement();
    instance.addDetectorState(state);
    assertTrue(instance.getDetectorState().contains(state));
  }

  @Test
  public void testAddItemState()
  {
    RadItemState state = new RadItemState();
    RadMeasurement instance = new RadMeasurement();
    instance.addItemState(state);
    assertTrue(instance.getItemState().contains(state));
  }

  @Test
  public void testGetInstrumentState()
  {
    RadMeasurement instance = newInstance();
    RadInstrumentState expResult = null;
    RadInstrumentState result = instance.getInstrumentState();
    assertEquals(result, expResult);
  }

  @Test
  public void testSetInstrumentState()
  {
    RadInstrumentState instrumentState = new RadInstrumentState();
    RadMeasurement instance = new RadMeasurement();
    instance.setInstrumentState(instrumentState);
    assertSame(instance.getInstrumentState(), instrumentState);
  }

  @Test
  public void testGetRadMeasurementGroups()
  {
    RadMeasurement instance = newInstance();
    List result = instance.getRadMeasurementGroups();
    assertNotNull(result);
  }
  
  @Test
  public void testAddToGroup()
  {
    RadMeasurementGroup group = new RadMeasurementGroup();
    RadMeasurement instance = new RadMeasurement();
    instance.addToGroup(group);
  }

  @Test
  public void testAddAnalysisResult()
  {
    AnalysisResults results = new AnalysisResults();
    RadMeasurement instance = new RadMeasurement();
    instance.addAnalysisResult(results);
    assertTrue(instance.getAnalysisResults().contains(results));
  }

  @Test
  public void testGetOccupancy()
  {
    RadMeasurement instance = newInstance();
    Boolean expResult = false;
    Boolean result = instance.getOccupancy();
    assertEquals(result, expResult);
  }

  @Test
  public void testSetOccupancy()
  {
    Boolean occupancy = true;
    RadMeasurement instance = new RadMeasurement();
    instance.setOccupancy(occupancy);
    assertTrue(instance.getOccupancy());
  }

  /**
   * Test of addCountRate method, of class RadMeasurement.
   */
  @Test
  public void testAddCountRate()
  {
    System.out.println("addCountRate");
    CountRate value = null;
    RadMeasurement instance = new RadMeasurement();
    instance.addCountRate(value);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of getAnalysisResults method, of class RadMeasurement.
   */
  @Test
  public void testGetAnalysisResults()
  {
    System.out.println("getAnalysisResults");
    RadMeasurement instance = new RadMeasurement();
    List expResult = null;
    List result = instance.getAnalysisResults();
    assertEquals(result, expResult);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of visitReferencedObjects method, of class RadMeasurement.
   */
  @Test
  public void testVisitReferencedObjects()
  {
    System.out.println("visitReferencedObjects");
    Consumer<ComplexObject> visitor = null;
    RadMeasurement instance = new RadMeasurement();
    instance.visitReferencedObjects(visitor);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

}
