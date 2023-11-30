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
public class OrientationNGTest
{
  
  public OrientationNGTest()
  {
  }
  
    public Orientation newInstance()
  {
    try
    {
      DocumentReader<RadInstrumentData> dr = DocumentReader.create(RadInstrumentData.class);
      RadInstrumentData rid = dr.loadFile(Paths.get("test/gov/nist/physics/n42/resources/Mobile.xml"));
      return rid.getMeasurements().get(0).getInstrumentState().getStateVector().getOrientation();
    }
    catch (ReaderException | IOException ex)
    {
      throw new RuntimeException(ex);
    }
  }


  @Test
  public void testGetAzimuth()
  {
    Orientation instance = newInstance();
    Quantity expResult = new Quantity(-90,null);
    Quantity result = instance.getAzimuth();
    assertEquals(result, expResult);
  }

  @Test
  public void testSetAzimuth()
  {
    Quantity azimuth = new Quantity(50, "deg");
    Orientation instance = new Orientation();
    instance.setAzimuth(azimuth);
    assertEquals(instance.getAzimuth(), azimuth);
  }

  @Test
  public void testGetInclination()
  {
    Orientation instance = newInstance();
    Quantity expResult = null;
    Quantity result = instance.getInclination();
    assertEquals(result, expResult);
  }

  @Test
  public void testSetInclination()
  {
    Quantity inclination = new Quantity(0, "deg");
    Orientation instance = new Orientation();
    instance.setInclination(inclination);
    assertEquals(instance.getInclination(), inclination);
  }

  @Test
  public void testGetRoll()
  {
    Orientation instance = newInstance();
    Quantity expResult = null;
    Quantity result = instance.getRoll();
    assertEquals(result, expResult);
  }

  @Test
  public void testSetRoll()
  {
    Quantity roll = new Quantity(0, "deg");
    Orientation instance = new Orientation();
    instance.setRoll(roll);
    assertEquals(instance.getRoll(), roll);
  }
  
}
