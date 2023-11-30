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
public class RadItemStateNGTest
{
  
  public RadItemStateNGTest()
  {
  }

      public RadItemState newInstance()
  {
    try
    {
      DocumentReader<RadInstrumentData> dr = DocumentReader.create(RadInstrumentData.class);
      RadInstrumentData rid = dr.loadFile(Paths.get("test/gov/nist/physics/n42/resources/Alarm.xml"));
      return rid.getMeasurements().get(4).getItemState().get(0);
    }
    catch (ReaderException | IOException ex)
    {
      throw new RuntimeException(ex);
    }
  }
      
  @Test
  public void testGetStateVector()
  {
    RadItemState instance = newInstance();
    StateVector result = instance.getStateVector();
    assertNotNull(result);
  }

  @Test
  public void testSetStateVector()
  {
    StateVector stateVector = new StateVector();
    RadItemState instance = new RadItemState();
    instance.setStateVector(stateVector);
    assertSame(instance.getStateVector(), stateVector);
  }

  @Test
  public void testGetItem()
  {
    RadItemState instance = newInstance();
    RadItemInformation result = instance.getItem();
    assertEquals(result.getDescription(), null);
  }

  @Test
  public void testSetItem()
  {
    RadItemInformation item =new RadItemInformation();
    RadItemState instance = new RadItemState();
    instance.setItem(item);
  }
  
}
