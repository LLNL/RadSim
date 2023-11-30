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
import static org.testng.Assert.*;
import org.testng.annotations.Test;

/**
 *
 * @author nelson85
 */
public class EnergyCalibrationNGTest
{
  
  public EnergyCalibrationNGTest()
  {
  }
  
  public EnergyCalibration newInstance()
  {
    try
    {
      DocumentReader<RadInstrumentData> dr = DocumentReader.create(RadInstrumentData.class);
      RadInstrumentData rid = dr.loadFile(Paths.get("test/gov/nist/physics/n42/resources/SimpleSpectrometer2.xml"));
      return rid.getEnergyCalibration().get(0);
    }
    catch (ReaderException | IOException ex)
    {
      throw new RuntimeException(ex);
    }
  }

  @Test
  public void testGetCoefficients()
  {
    EnergyCalibration instance = newInstance();
    double[] result = instance.getCoefficients();
    assertEquals(result.length, 3);
    assertEquals(result[0], -21.8);
  }

  @Test
  public void testGetCalibrationDateTime()
  {
    EnergyCalibration instance = newInstance();
    Instant expResult = null;
    Instant result = instance.getCalibrationDateTime();
    assertEquals(result, expResult);
  }

  @Test
  public void testSetCalibrationDateTime()
  {
    Instant calibrationDateTime = Instant.now();
    EnergyCalibration instance = new EnergyCalibration();
    instance.setCalibrationDateTime(calibrationDateTime);
    assertEquals(instance.getCalibrationDateTime(), calibrationDateTime);
  }

  @Test
  public void testSetCoefficients()
  {
    double[] coefficients = new double[]{1,2,3,4};
    EnergyCalibration instance = new EnergyCalibration();
    instance.setCoefficients(coefficients);
    assertEquals(instance.getCoefficients(), coefficients);
  }

  @Test
  public void testGetId()
  {
    EnergyCalibration instance = newInstance();
    String expResult = "EnergyCalibration-1";
    String result = instance.getId();
    assertEquals(result, expResult);
  }

  @Test
  public void testSetId()
  {
    String id = "";
    EnergyCalibration instance = new EnergyCalibration();
    instance.setId(id);
  }

  /**
   * Test of getEnergyBoundaries method, of class EnergyCalibration.
   */
  @Test
  public void testGetEnergyBoundaries()
  {
    System.out.println("getEnergyBoundaries");
    EnergyCalibration instance = new EnergyCalibration();
    double[] expResult = null;
    double[] result = instance.getEnergyBoundaries();
    assertEquals(result, expResult);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of setEnergyBoundaries method, of class EnergyCalibration.
   */
  @Test
  public void testSetEnergyBoundaries()
  {
    System.out.println("setEnergyBoundaries");
    double[] energyBoundaries = null;
    EnergyCalibration instance = new EnergyCalibration();
    instance.setEnergyBoundaries(energyBoundaries);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of getEnergy method, of class EnergyCalibration.
   */
  @Test
  public void testGetEnergy()
  {
    System.out.println("getEnergy");
    EnergyCalibration instance = new EnergyCalibration();
    double[] expResult = null;
    double[] result = instance.getEnergy();
    assertEquals(result, expResult);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of setEnergy method, of class EnergyCalibration.
   */
  @Test
  public void testSetEnergy()
  {
    System.out.println("setEnergy");
    double[] energy = null;
    EnergyCalibration instance = new EnergyCalibration();
    instance.setEnergy(energy);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of getEnergyDeviation method, of class EnergyCalibration.
   */
  @Test
  public void testGetEnergyDeviation()
  {
    System.out.println("getEnergyDeviation");
    EnergyCalibration instance = new EnergyCalibration();
    double[] expResult = null;
    double[] result = instance.getEnergyDeviation();
    assertEquals(result, expResult);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of setEnergyDeviation method, of class EnergyCalibration.
   */
  @Test
  public void testSetEnergyDeviation()
  {
    System.out.println("setEnergyDeviation");
    double[] energyDeviation = null;
    EnergyCalibration instance = new EnergyCalibration();
    instance.setEnergyDeviation(energyDeviation);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }
  
}
