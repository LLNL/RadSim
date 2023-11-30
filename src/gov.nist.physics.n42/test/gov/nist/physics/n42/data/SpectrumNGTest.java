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
import java.util.function.Consumer;
import static org.testng.Assert.*;
import org.testng.annotations.Test;

/**
 *
 * @author nelson85
 */
public class SpectrumNGTest
{

  public SpectrumNGTest()
  {
  }

  public Spectrum newInstance()
  {
    try
    {
      DocumentReader<RadInstrumentData> dr = DocumentReader.create(RadInstrumentData.class);
      RadInstrumentData rid = dr.loadFile(Paths.get("test/gov/nist/physics/n42/resources/Mobile.xml"));
      return rid.getMeasurements().get(0).getSpectrum().get(0);
    }
    catch (ReaderException | IOException ex)
    {
      throw new RuntimeException(ex);
    }
  }

  @Test
  public void testGetId()
  {
    Spectrum instance = newInstance();
    String expResult = "GammaDetector-1222";
    String result = instance.getId();
    assertEquals(result, expResult);
  }

  @Test
  public void testGetEnergyCalibration()
  {
    Spectrum instance = newInstance();
    EnergyCalibration result = instance.getEnergyCalibration();
    assertNotNull(result);
  }

  @Test
  public void testSetEnergyCalibration()
  {
    EnergyCalibration energyCalibration = new EnergyCalibration();
    Spectrum instance = new Spectrum();
    instance.setEnergyCalibration(energyCalibration);
    assertSame(instance.getEnergyCalibration(), energyCalibration);
  }

  @Test
  public void testGetDetector()
  {
    Spectrum instance = newInstance();
    RadDetectorInformation result = instance.getDetector();
    assertNotNull(result);
  }

  @Test
  public void testSetDetector()
  {
    RadDetectorInformation detector = new RadDetectorInformation();
    Spectrum instance = new Spectrum();
    instance.setDetector(detector);
    assertSame(instance.getDetector(), detector);
  }

  @Test
  public void testGetLiveTimeDuration()
  {
    Spectrum instance = newInstance();
    Duration expResult = Duration.ofMillis(2900);
    Duration result = instance.getLiveTimeDuration();
    assertEquals(result, expResult);
  }

  @Test
  public void testSetLiveTimeDuration()
  {
    Duration value = Duration.ofSeconds(1);
    Spectrum instance = new Spectrum();
    instance.setLiveTimeDuration(value);
    assertSame(instance.getLiveTimeDuration(), value);
  }

  @Test
  public void testGetCountData()
  {
    Spectrum instance = newInstance();
    double[] expResult = null;
    double[] result = instance.getCountData();
    assertEquals(result[0], 0.0);
  }

  @Test
  public void testSetCountData()
  {
    double[] countData = new double[0];
    Spectrum instance = new Spectrum();
    instance.setCountData(countData);
    assertSame(instance.getCountData(), countData);
  }

  /**
   * Test of setFWHMEfficiencyCalibration method, of class Spectrum.
   */
  @Test
  public void testSetFWHMEfficiencyCalibration()
  {
    System.out.println("setFWHMEfficiencyCalibration");
    FWHMCalibration calibration = null;
    Spectrum instance = new Spectrum();
    instance.setFWHMEfficiencyCalibration(calibration);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of setFullEnergyPeakEfficiencyCalibration method, of class Spectrum.
   */
  @Test
  public void testSetFullEnergyPeakEfficiencyCalibration()
  {
    System.out.println("setFullEnergyPeakEfficiencyCalibration");
    EfficiencyCalibration calibration = null;
    Spectrum instance = new Spectrum();
    instance.setFullEnergyPeakEfficiencyCalibration(calibration);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of setIntrinsicSingleEscapePeakEfficiencyCalibration method, of class Spectrum.
   */
  @Test
  public void testSetIntrinsicSingleEscapePeakEfficiencyCalibration()
  {
    System.out.println("setIntrinsicSingleEscapePeakEfficiencyCalibration");
    EfficiencyCalibration calibration = null;
    Spectrum instance = new Spectrum();
    instance.setIntrinsicSingleEscapePeakEfficiencyCalibration(calibration);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of setIntrinsicDoubleEscapePeakEfficiencyCalibration method, of class Spectrum.
   */
  @Test
  public void testSetIntrinsicDoubleEscapePeakEfficiencyCalibration()
  {
    System.out.println("setIntrinsicDoubleEscapePeakEfficiencyCalibration");
    EfficiencyCalibration calibration = null;
    Spectrum instance = new Spectrum();
    instance.setIntrinsicDoubleEscapePeakEfficiencyCalibration(calibration);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of setIntrinsicFullEnergyPeakEfficiencyCalibration method, of class Spectrum.
   */
  @Test
  public void testSetIntrinsicFullEnergyPeakEfficiencyCalibration()
  {
    System.out.println("setIntrinsicFullEnergyPeakEfficiencyCalibration");
    EfficiencyCalibration calibration = null;
    Spectrum instance = new Spectrum();
    instance.setIntrinsicFullEnergyPeakEfficiencyCalibration(calibration);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of getFwhmCalibration method, of class Spectrum.
   */
  @Test
  public void testGetFwhmCalibration()
  {
    System.out.println("getFwhmCalibration");
    Spectrum instance = new Spectrum();
    FWHMCalibration expResult = null;
    FWHMCalibration result = instance.getFwhmCalibration();
    assertEquals(result, expResult);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of setFwhmCalibration method, of class Spectrum.
   */
  @Test
  public void testSetFwhmCalibration()
  {
    System.out.println("setFwhmCalibration");
    FWHMCalibration fwhmCalibration = null;
    Spectrum instance = new Spectrum();
    instance.setFwhmCalibration(fwhmCalibration);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of getFullEnergyPeakEfficiencyCalibration method, of class Spectrum.
   */
  @Test
  public void testGetFullEnergyPeakEfficiencyCalibration()
  {
    System.out.println("getFullEnergyPeakEfficiencyCalibration");
    Spectrum instance = new Spectrum();
    EfficiencyCalibration expResult = null;
    EfficiencyCalibration result = instance.getFullEnergyPeakEfficiencyCalibration();
    assertEquals(result, expResult);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of getIntrinsicSingleEscapePeakEfficiencyCalibration method, of class Spectrum.
   */
  @Test
  public void testGetIntrinsicSingleEscapePeakEfficiencyCalibration()
  {
    System.out.println("getIntrinsicSingleEscapePeakEfficiencyCalibration");
    Spectrum instance = new Spectrum();
    EfficiencyCalibration expResult = null;
    EfficiencyCalibration result = instance.getIntrinsicSingleEscapePeakEfficiencyCalibration();
    assertEquals(result, expResult);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of getIntrinsicDoubleEscapePeakEfficiencyCalibration method, of class Spectrum.
   */
  @Test
  public void testGetIntrinsicDoubleEscapePeakEfficiencyCalibration()
  {
    System.out.println("getIntrinsicDoubleEscapePeakEfficiencyCalibration");
    Spectrum instance = new Spectrum();
    EfficiencyCalibration expResult = null;
    EfficiencyCalibration result = instance.getIntrinsicDoubleEscapePeakEfficiencyCalibration();
    assertEquals(result, expResult);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of getIntrinsicFullEnergyPeakEfficiencyCalibration method, of class Spectrum.
   */
  @Test
  public void testGetIntrinsicFullEnergyPeakEfficiencyCalibration()
  {
    System.out.println("getIntrinsicFullEnergyPeakEfficiencyCalibration");
    Spectrum instance = new Spectrum();
    EfficiencyCalibration expResult = null;
    EfficiencyCalibration result = instance.getIntrinsicFullEnergyPeakEfficiencyCalibration();
    assertEquals(result, expResult);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of visitReferencedObjects method, of class Spectrum.
   */
  @Test
  public void testVisitReferencedObjects()
  {
    System.out.println("visitReferencedObjects");
    Consumer<ComplexObject> visitor = null;
    Spectrum instance = new Spectrum();
    instance.visitReferencedObjects(visitor);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

}
