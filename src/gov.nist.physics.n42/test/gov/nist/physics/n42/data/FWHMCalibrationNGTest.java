/*
 * Copyright 2022, Lawrence Livermore National Security, LLC.
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.nist.physics.n42.data;

import gov.llnl.utility.ArrayEncoding;
import gov.llnl.utility.io.ReaderException;
import gov.llnl.utility.xml.bind.DocumentReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.text.ParseException;
import java.time.Instant;
import java.util.Arrays;
import static org.testng.Assert.*;
import org.testng.annotations.Test;

/**
 *
 * @author nelson85
 */
public class FWHMCalibrationNGTest
{
  
  public FWHMCalibrationNGTest()
  {
  }

  public FWHMCalibration newInstance()
  {
    try
    {
      DocumentReader<RadInstrumentData> dr = DocumentReader.create(RadInstrumentData.class);
      RadInstrumentData rid = dr.loadFile(Paths.get("test/gov/nist/physics/n42/resources/Mobile.xml"));
      return rid.getFWHMCalibration().get(0);
    }
    catch (ReaderException | IOException ex)
    {
      throw new RuntimeException(ex);
    }
  }

  @Test
  public void testGetEnergyValues() throws ParseException
  {
    FWHMCalibration instance = newInstance();
    double[] result = instance.getEnergyValues();
    assertNotNull(result);
    assertEquals(result.length,3);
    assertTrue(Arrays.equals(result,ArrayEncoding.decodeDoubles("eJyLdrFmYGBgdvBtYAABh5a1xiDgMO0ymAYAYDEHAw==")));
  }

  @Test
  public void testSetEnergyValues()
  {
    double[] energyValues = new double[]{1,2,3};
    FWHMCalibration instance = new FWHMCalibration();
    instance.setEnergyValues(energyValues);
    assertEquals(instance.getEnergyValues(), energyValues);
  }

  @Test
  public void testGetFwhmValues() throws ParseException
  {
    FWHMCalibration instance = newInstance();
    double[] result = instance.getFwhmValues();
    assertNotNull(result);
    assertTrue(Arrays.equals(result, ArrayEncoding.decodeDoubles("eJyLdrFmYGBgdlBlAAOH+AUQuoADTAMAOvoDOg==")));
  }

  @Test
  public void testSetFwhmValues()
  {
    double[] fwhmValues = new double[]{1,2,3};
    FWHMCalibration instance = new FWHMCalibration();
    instance.setFwhmValues(fwhmValues);
    assertEquals(instance.getFwhmValues(), fwhmValues);
  }

  @Test
  public void testGetFwhmUncertaintyValues()
  {
    FWHMCalibration instance = newInstance();
    double[] expResult = null;
    double[] result = instance.getFwhmUncertaintyValues();
    assertEquals(result, expResult);
  }

  @Test
  public void testSetFwhmUncertaintyValues()
  {
    double[] fwhmUncertaintyValues = new double[]{1,2,3};
    FWHMCalibration instance = new FWHMCalibration();
    instance.setFwhmUncertaintyValues(fwhmUncertaintyValues);
    assertEquals(instance.getFwhmUncertaintyValues(), fwhmUncertaintyValues);
  }

  @Test
  public void testGetCalibrationDateTime()
  {
    FWHMCalibration instance = newInstance();
    Instant expResult = null;
    Instant result = instance.getCalibrationDateTime();
    assertEquals(result, expResult);
  }

  @Test
  public void testSetCalibrationDateTime()
  {
    Instant calibrationDateTime = Instant.now();
    FWHMCalibration instance = new FWHMCalibration();
    instance.setCalibrationDateTime(calibrationDateTime);
    assertEquals(instance.getCalibrationDateTime(), calibrationDateTime);
  }
  
}
