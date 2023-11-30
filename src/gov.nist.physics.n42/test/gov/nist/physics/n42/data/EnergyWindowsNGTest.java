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
import static org.testng.Assert.*;
import org.testng.annotations.Test;

/**
 *
 * @author nelson85
 */
public class EnergyWindowsNGTest
{
  
  public EnergyWindowsNGTest()
  {
  }

  public EnergyWindows newInstance()
  {
    try
    {
      DocumentReader<RadInstrumentData> dr = DocumentReader.create(RadInstrumentData.class);
      RadInstrumentData rid = dr.loadFile(Paths.get("test/gov/nist/physics/n42/resources/Alarm.xml"));
      return rid.getEnergyWindows().get(0);
    }
    catch (ReaderException | IOException ex)
    {
      throw new RuntimeException(ex);
    }
  }

  @Test
  public void testGetStartEnergyValues()
  {
    EnergyWindows instance = newInstance();
    double[] result = instance.getStartEnergyValues();
    assertEquals(result.length, 3);
    assertEquals(result[2],100.0);
  }

  @Test
  public void testSetStartEnergyValues()
  {
    double[] startEnergyValues = new double[]{1,2,3};
    EnergyWindows instance = new EnergyWindows();
    instance.setStartEnergyValues(startEnergyValues);
    assertEquals(instance.getStartEnergyValues(), startEnergyValues);
  }

  @Test
  public void testGetEndEnergyValues()
  {
    EnergyWindows instance = newInstance();
    double[] result = instance.getEndEnergyValues();
    assertEquals(result.length, 3);
    assertEquals(result[0],100.0);
  }

  @Test
  public void testSetEndEnergyValues()
  {
    double[] endEnergyValues = new double[]{1,2,3};
    EnergyWindows instance = new EnergyWindows();
    instance.setEndEnergyValues(endEnergyValues);
    assertEquals(instance.getEndEnergyValues(), endEnergyValues);
  }
  
}
