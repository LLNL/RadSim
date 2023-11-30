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
public class CharacteristicNGTest
{
  
  public CharacteristicNGTest()
  {
  }
  
  public Characteristic newInstance()
  {
    try
    {
      DocumentReader<RadInstrumentData> dr = DocumentReader.create(RadInstrumentData.class);
      RadInstrumentData rid = dr.loadFile(Paths.get("test/gov/nist/physics/n42/resources/SimpleSpectrometer2.xml"));
      return rid.getInstrument().getCharacteristics().get(0).getCharacteristics().get(0);
    }
    catch (ReaderException | IOException ex)
    {
      throw new RuntimeException(ex);
    }
  }


  @Test
  public void testGetName()
  {
    Characteristic instance = newInstance();
    String expResult = "IP";
    String result = instance.getName();
    assertEquals(result, expResult);
  }

  @Test
  public void testSetName()
  {
    String name = "test";
    Characteristic instance = new Characteristic();
    instance.setName(name);
    assertEquals(instance.getName(), name);
  }

  @Test
  public void testGetValue()
  {
    Characteristic instance = newInstance();
    String expResult = "192.168.100.32";
    String result = instance.getValue();
    assertEquals(result, expResult);
  }

  @Test
  public void testSetValue()
  {
    String value = "test";
    Characteristic instance = new Characteristic();
    instance.setValue(value);
    assertEquals(instance.getValue(), value);
  }

  @Test
  public void testGetValueUnits()
  {
    Characteristic instance = newInstance();
    String expResult = "unit-less";
    String result = instance.getValueUnits();
    assertEquals(result, expResult);
  }

  @Test
  public void testSetValueUnits()
  {
    String valueUnits = "units";
    Characteristic instance = new Characteristic();
    instance.setValueUnits(valueUnits);
    assertEquals(instance.getValueUnits(), valueUnits);
  }

  @Test
  public void testGetValueDataClassCode()
  {
    Characteristic instance = newInstance();
    String expResult = "string";
    String result = instance.getValueDataClassCode();
    assertEquals(result, expResult);
  }

  @Test
  public void testSetValueDataClassCode()
  {
    String valueDataClassCode = "codes";
    Characteristic instance = new Characteristic();
    instance.setValueDataClassCode(valueDataClassCode);
    assertEquals(instance.getValueDataClassCode(), valueDataClassCode);
  }
  
}
