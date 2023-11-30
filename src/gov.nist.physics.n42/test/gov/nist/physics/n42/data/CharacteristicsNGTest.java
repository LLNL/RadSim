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
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import static org.testng.Assert.*;
import org.testng.annotations.Test;

/**
 *
 * @author nelson85
 */
public class CharacteristicsNGTest
{
  
  public CharacteristicsNGTest()
  {
  }
  
  public Characteristics newInstance()
  {
    try
    {
      DocumentReader<RadInstrumentData> dr = DocumentReader.create(RadInstrumentData.class);
      RadInstrumentData rid = dr.loadFile(Paths.get("test/gov/nist/physics/n42/resources/SimpleSpectrometer2.xml"));
      return rid.getInstrument().getCharacteristics().get(0);
    }
    catch (ReaderException | IOException ex)
    {
      throw new RuntimeException(ex);
    }
  }


  @Test
  public void testAddCharacteristic()
  {
    Characteristic c = new Characteristic();
    Characteristics instance = new Characteristics();
    instance.addCharacteristic(c);
    assertTrue(instance.getCharacteristics().contains(c));
  }

  @Test
  public void testAddCharacteristicGroup()
  {
    CharacteristicGroup c = new CharacteristicGroup();
    Characteristics instance = new Characteristics();
    instance.addCharacteristicGroup(c);
    assertTrue(instance.getCharacteristicGroups().contains(c));
  }

  @Test
  public void testGetCharacteristics()
  {
    Characteristics instance = newInstance();
    List result = instance.getCharacteristics();
    assertNotNull(result);
   }

  @Test
  public void testGetCharacteristicGroups()
  {
    Characteristics instance = new Characteristics();
    List expResult = Collections.emptyList();
    List result = instance.getCharacteristicGroups();
    assertEquals(result, expResult);
  }
  
}
