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
public class FaultNGTest
{
  
  public FaultNGTest()
  {
  }
  
  public Fault newInstance()
  {
    try
    {
      DocumentReader<RadInstrumentData> dr = DocumentReader.create(RadInstrumentData.class);
      RadInstrumentData rid = dr.loadFile(Paths.get("test/gov/nist/physics/n42/resources/Alarm.xml"));
      return rid.getMeasurements().get(4).getInstrumentState().getFaults().get(0);
    }
    catch (ReaderException | IOException ex)
    {
      throw new RuntimeException(ex);
    }
  }


  @Test
  public void testSetCode()
  {
    String u = "code";
    Fault instance = new Fault();
    instance.setCode(u);
    assertEquals(instance.getCode(), u);
  }

  @Test
  public void testSetDescription()
  {
    String u = "desc";
    Fault instance = new Fault();
    instance.setDescription(u);
    assertEquals(instance.getDescription(), u);
  }

  @Test
  public void testSetSeverityCode()
  {
    FaultSeverityCode u =FaultSeverityCode.Error;
    Fault instance = new Fault();
    instance.setSeverityCode(u);
    assertEquals(instance.getSeverityCode(), u);
  }

  @Test
  public void testGetCode()
  {
    Fault instance = newInstance();
    String expResult = "5";
    String result = instance.getCode();
    assertEquals(result, expResult);
  }

  @Test
  public void testGetDescription()
  {
    Fault instance = newInstance();
    String expResult = "Overspeed";
    String result = instance.getDescription();
    assertEquals(result, expResult);
  }

  @Test
  public void testGetSeverityCode()
  {
    Fault instance = newInstance();
    FaultSeverityCode expResult = FaultSeverityCode.Warning;
    FaultSeverityCode result = instance.getSeverityCode();
    assertEquals(result, expResult);
  }
  
}
