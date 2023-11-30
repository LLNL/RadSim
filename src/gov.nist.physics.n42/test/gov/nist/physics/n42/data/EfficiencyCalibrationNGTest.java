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
import java.util.Collections;
import java.util.List;
import static org.testng.Assert.*;
import org.testng.annotations.Test;

/**
 *
 * @author nelson85
 */
public class EfficiencyCalibrationNGTest
{
  
  public EfficiencyCalibrationNGTest()
  {
  }
  
  public EfficiencyCalibration newInstance()
  {
    try
    {
      DocumentReader<RadInstrumentData> dr = DocumentReader.create(RadInstrumentData.class);
      RadInstrumentData rid = dr.loadFile(Paths.get("test/gov/nist/physics/n42/resources/SimpleSpectrometer2.xml"));
      return rid.getFullEnergyPeakEfficiencyCalibration().get(0);
    }
    catch (ReaderException | IOException ex)
    {
      throw new RuntimeException(ex);
    }
  }

  @Test
  public void testGetEnergyValues()
  {
    EfficiencyCalibration instance = newInstance();
    double[] result = instance.getEnergyValues();
    assertNotNull(result);
    assertEquals(result.length, 4);
    assertEquals(result[0], 88.0);
  }

  @Test
  public void testSetEnergyValues()
  {
    double[] energyValues = new double[]{1,2,3,4};
    EfficiencyCalibration instance = new EfficiencyCalibration();
    instance.setEnergyValues(energyValues);
    assertEquals(instance.getEnergyValues(), energyValues);
  }

  @Test
  public void testGetEfficiencyValues()
  {
    EfficiencyCalibration instance = newInstance();
    double[] result = instance.getEfficiencyValues();
    assertNotNull(result);
    assertEquals(result.length, 4);
    assertEquals(result[0], 0.28);
  }

  @Test
  public void testSetEfficiencyValues()
  {
    double[] efficiencyValues = new double[]{5,6,7,8};
    EfficiencyCalibration instance = new EfficiencyCalibration();
    instance.setEfficiencyValues(efficiencyValues);
    assertEquals(instance.getEfficiencyValues(), efficiencyValues);
  }

  @Test
  public void testGetEfficiencyUncertaintyValues()
  {
    EfficiencyCalibration instance = newInstance();
    double[] result = instance.getEfficiencyUncertaintyValues();
    assertNotNull(result);
    assertEquals(result.length, 4);
    assertEquals(result[0], 0.011);
  }

  @Test
  public void testSetEfficiencyUncertaintyValues()
  {
    double[] efficiencyUncertaintyValues = new double[]{4,3,2,1};
    EfficiencyCalibration instance = new EfficiencyCalibration();
    instance.setEfficiencyUncertaintyValues(efficiencyUncertaintyValues);
    assertEquals(instance.getEfficiencyUncertaintyValues(), efficiencyUncertaintyValues);
  }

  @Test
  public void testGetCalibrationDateTime()
  {
    EfficiencyCalibration instance = newInstance();
    Instant expResult = null;
    Instant result = instance.getCalibrationDateTime();
    assertEquals(result, expResult);
  }

  @Test
  public void testSetCalibrationDateTime()
  {
    Instant calibrationDateTime = Instant.now();
    EfficiencyCalibration instance = new EfficiencyCalibration();
    instance.setCalibrationDateTime(calibrationDateTime);
    assertEquals(instance.getCalibrationDateTime(), calibrationDateTime);
  }

  @Test
  public void testGetRemarks()
  {
    EfficiencyCalibration instance = new EfficiencyCalibration();
    List expResult = Collections.emptyList();
    List result = instance.getRemarks();
    assertEquals(result, expResult);
  }

  @Test
  public void testAddRemark()
  {
    String remark = "remark";
    EfficiencyCalibration instance = new EfficiencyCalibration();
    instance.addRemark(remark);
    assertTrue(instance.getRemarks().contains(remark));
  }
  
}
