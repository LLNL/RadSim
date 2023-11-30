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
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import static org.testng.Assert.*;
import org.testng.annotations.Test;

/**
 *
 * @author nelson85
 */
public class RadInstrumentDataNGTest
{

  public RadInstrumentDataNGTest()
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
      
  public static RadInstrumentData newInstance(Path file)
  {
    try
    {
      DocumentReader<RadInstrumentData> dr = DocumentReader.create(RadInstrumentData.class);
      return dr.loadFile(file);
    }
    catch (ReaderException | IOException ex)
    {
      throw new RuntimeException(ex);
    }
  }

  @Test
  public void testGetInstrument()
  {
    RadInstrumentData instance = newInstance(Paths.get("test/gov/nist/physics/n42/resources/SimpleSpectrometer.xml"));
    RadInstrumentInformation expResult = null;
    RadInstrumentInformation result = instance.getInstrument();
    assertNotNull(result);
  }

  @Test
  public void testSetInstrument()
  {
    RadInstrumentData instance = newInstance(Paths.get("test/gov/nist/physics/n42/resources/SimpleSpectrometer.xml"));
    RadInstrumentInformation instrument = new RadInstrumentInformation();
    instance.setInstrument(instrument);
    assertSame(instance.getInstrument(), instrument);
  }

  @Test
  public void testSetDataCreatorName()
  {
    String name = "test";
    RadInstrumentData instance = new RadInstrumentData();
    instance.setDataCreatorName(name);
    assertEquals(instance.getDataCreatorName(), name);
  }

  @Test
  public void testAddItem()
  {
    RadItemInformation item = new RadItemInformation();
    RadInstrumentData instance = new RadInstrumentData();
    instance.addItem(item);
    assertTrue(instance.getItems().contains(item));
  }

  @Test
  public void testAddDetector()
  {
    RadDetectorInformation detector = new RadDetectorInformation();
    RadInstrumentData instance = new RadInstrumentData();
    instance.addDetector(detector);
    assertTrue(instance.getDetectors().contains(detector));
  }

  @Test
  public void testAddRadMeasurement()
  {
    RadMeasurement measurement = new RadMeasurement();
    RadInstrumentData instance = new RadInstrumentData();
    instance.addMeasurement(measurement);
    assertTrue(instance.getMeasurements().contains(measurement));
  }

  @Test
  public void testAddEnergyCalibration()
  {
    EnergyCalibration ec = new EnergyCalibration();
    RadInstrumentData instance = new RadInstrumentData();
    instance.addEnergyCalibration(ec);
    assertTrue(instance.getEnergyCalibration().contains(ec));
  }

  @Test
  public void testAddFwhmCalibration()
  {
    FWHMCalibration fwhm = new FWHMCalibration();
    RadInstrumentData instance = new RadInstrumentData();
    instance.addFwhmCalibration(fwhm);
    assertTrue(instance.getFWHMCalibration().contains(fwhm));
  }

  @Test
  public void testAddTotalEfficiencyCalibration()
  {
    EfficiencyCalibration totalEff = new EfficiencyCalibration();
    RadInstrumentData instance = new RadInstrumentData();
    instance.addTotalEfficiencyCalibration(totalEff);
    assertTrue(instance.getTotalEfficiencyCalibration().contains(totalEff));
  }

  @Test
  public void testAddFullEnergyPeakEfficiencyCalibration()
  {
    EfficiencyCalibration cal = new EfficiencyCalibration();
    RadInstrumentData instance = new RadInstrumentData();
    instance.addFullEnergyPeakEfficiencyCalibration(cal);
    assertTrue(instance.getFullEnergyPeakEfficiencyCalibration().contains(cal));
  }

  @Test
  public void testAddIntrinsicFullEnergyPeakEfficiencyCalibration()
  {
    EfficiencyCalibration cal = new EfficiencyCalibration();
    RadInstrumentData instance = new RadInstrumentData();
    instance.addIntrinsicFullEnergyPeakEfficiencyCalibration(cal);
    assertTrue(instance.getIntrinsicFullEnergyPeakEfficiencyCalibration().contains(cal));
  }

  @Test
  public void testAddIntrinsicSingleEscapePeakEfficiencyCalibration()
  {
    EfficiencyCalibration cal = new EfficiencyCalibration();
    RadInstrumentData instance = new RadInstrumentData();
    instance.addIntrinsicSingleEscapePeakEfficiencyCalibration(cal);
    assertTrue(instance.getIntrinsicSingleEscapePeakEfficiencyCalibration().contains(cal));
  }

  @Test
  public void testAddIntrinsicDoubleEscapePeakEfficiencyCalibration()
  {
    EfficiencyCalibration cal = new EfficiencyCalibration();
    RadInstrumentData instance = new RadInstrumentData();
    instance.addIntrinsicDoubleEscapePeakEfficiencyCalibration(cal);
    assertTrue(instance.getIntrinsicDoubleEscapePeakEfficiencyCalibration().contains(cal));
  }

  @Test
  public void testAddEnergyWindows()
  {
    EnergyWindows ew = new EnergyWindows();
    RadInstrumentData instance = new RadInstrumentData();
    instance.addEnergyWindows(ew);
    assertTrue(instance.getEnergyWindows().contains(ew));
  }

  @Test
  public void testAddAnalysisResults()
  {
    AnalysisResults ar = new AnalysisResults();
    RadInstrumentData instance = new RadInstrumentData();
    instance.addAnalysisResults(ar);
    assertTrue(instance.getAnalysisResults().contains(ar));
  }

  @Test
  public void testAddRadMeasurementGroup()
  {
    RadMeasurementGroup group = new RadMeasurementGroup();
    RadInstrumentData instance = new RadInstrumentData();
    instance.addMeasurementGroup(group);
    assertTrue(instance.getMeasurementGroups().contains(group));
  }

  @Test
  public void testGetItems()
  {
    RadInstrumentData instance = new RadInstrumentData();
    List result = instance.getItems();
    assertNotNull(result);
  }

  @Test
  public void testGetDetectors()
  {
    RadInstrumentData instance = new RadInstrumentData();
    List result = instance.getDetectors();
    assertNotNull(result);
  }

  @Test
  public void testGetMeasurements()
  {
    RadInstrumentData instance = new RadInstrumentData();
    List result = instance.getMeasurements();
    assertNotNull(result);
  }

  @Test
  public void testGetEnergyCalibration()
  {
    RadInstrumentData instance = new RadInstrumentData();
    List result = instance.getEnergyCalibration();
    assertNotNull(result);
  }

  @Test
  public void testGetFWHMCalibration()
  {
    RadInstrumentData instance = new RadInstrumentData();
    List result = instance.getFWHMCalibration();
    assertNotNull(result);
  }

  @Test
  public void testGetTotalEfficiencyCalibration()
  {
    RadInstrumentData instance = new RadInstrumentData();
    List result = instance.getTotalEfficiencyCalibration();
    assertNotNull(result);
  }

  @Test
  public void testGetFullEnergyPeakEfficiencyCalibration()
  {
    RadInstrumentData instance = new RadInstrumentData();
    List result = instance.getFullEnergyPeakEfficiencyCalibration();
    assertNotNull(result);
  }

  @Test
  public void testGetIntrinsicFullEnergyPeakEfficiencyCalibration()
  {
    RadInstrumentData instance = new RadInstrumentData();
    List result = instance.getIntrinsicFullEnergyPeakEfficiencyCalibration();
    assertNotNull(result);
  }

  @Test
  public void testGetIntrinsicSingleEscapePeakEfficiencyCalibration()
  {
    RadInstrumentData instance = new RadInstrumentData();
    List result = instance.getIntrinsicSingleEscapePeakEfficiencyCalibration();
    assertNotNull(result);
  }

  @Test
  public void testGetIntrinsicDoubleEscapePeakEfficiencyCalibration()
  {
    RadInstrumentData instance = new RadInstrumentData();
    List result = instance.getIntrinsicDoubleEscapePeakEfficiencyCalibration();
    assertNotNull(result);
  }

  @Test
  public void testGetEnergyWindows()
  {
    RadInstrumentData instance = new RadInstrumentData();
    List result = instance.getEnergyWindows();
    assertNotNull(result);
  }

  @Test
  public void testGetAnalysisResults()
  {
    RadInstrumentData instance = new RadInstrumentData();
    List result = instance.getAnalysisResults();
    assertNotNull(result);
  }

  @Test
  public void testGetMeasurementGroups()
  {
    RadInstrumentData instance = new RadInstrumentData();
    List result = instance.getMeasurementGroups();
    assertNotNull(result);
  }

  /**
   * Test of addMeasurement method, of class RadInstrumentData.
   */
  @Test
  public void testAddMeasurement()
  {
    System.out.println("addMeasurement");
    RadMeasurement measurement = null;
    RadInstrumentData instance = new RadInstrumentData();
    instance.addMeasurement(measurement);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of addDerivedData method, of class RadInstrumentData.
   */
  @Test
  public void testAddDerivedData()
  {
    System.out.println("addDerivedData");
    DerivedData dd = null;
    RadInstrumentData instance = new RadInstrumentData();
    instance.addDerivedData(dd);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of addMeasurementGroup method, of class RadInstrumentData.
   */
  @Test
  public void testAddMeasurementGroup()
  {
    System.out.println("addMeasurementGroup");
    RadMeasurementGroup group = null;
    RadInstrumentData instance = new RadInstrumentData();
    instance.addMeasurementGroup(group);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of getDerivedData method, of class RadInstrumentData.
   */
  @Test
  public void testGetDerivedData()
  {
    System.out.println("getDerivedData");
    RadInstrumentData instance = new RadInstrumentData();
    List expResult = null;
    List result = instance.getDerivedData();
    assertEquals(result, expResult);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of getDataCreatorName method, of class RadInstrumentData.
   */
  @Test
  public void testGetDataCreatorName()
  {
    System.out.println("getDataCreatorName");
    RadInstrumentData instance = new RadInstrumentData();
    String expResult = "";
    String result = instance.getDataCreatorName();
    assertEquals(result, expResult);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of setDocUUID method, of class RadInstrumentData.
   */
  @Test
  public void testSetDocUUID()
  {
    System.out.println("setDocUUID");
    String value = "";
    RadInstrumentData instance = new RadInstrumentData();
    instance.setDocUUID(value);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of setDocDateTime method, of class RadInstrumentData.
   */
  @Test
  public void testSetDocDateTime()
  {
    System.out.println("setDocDateTime");
    String value = "";
    RadInstrumentData instance = new RadInstrumentData();
    instance.setDocDateTime(value);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of getDocUUID method, of class RadInstrumentData.
   */
  @Test
  public void testGetDocUUID()
  {
    System.out.println("getDocUUID");
    RadInstrumentData instance = new RadInstrumentData();
    String expResult = "";
    String result = instance.getDocUUID();
    assertEquals(result, expResult);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of getDocDateTime method, of class RadInstrumentData.
   */
  @Test
  public void testGetDocDateTime()
  {
    System.out.println("getDocDateTime");
    RadInstrumentData instance = new RadInstrumentData();
    String expResult = "";
    String result = instance.getDocDateTime();
    assertEquals(result, expResult);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

}
