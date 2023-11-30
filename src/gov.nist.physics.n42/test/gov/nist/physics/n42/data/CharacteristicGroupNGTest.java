/*
 * Copyright 2022, Lawrence Livermore National Security, LLC.
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.nist.physics.n42.data;

import java.util.Collections;
import java.util.List;
import static org.testng.Assert.*;
import org.testng.annotations.Test;

/**
 *
 * @author nelson85
 */
public class CharacteristicGroupNGTest
{
  
  public CharacteristicGroupNGTest()
  {
  }

  @Test
  public void testGetName()
  {
    CharacteristicGroup instance = new CharacteristicGroup();
    String expResult = null;
    String result = instance.getName();
    assertEquals(result, expResult);
  }

  @Test
  public void testSetName()
  {
    String name = "test";
    CharacteristicGroup instance = new CharacteristicGroup();
    instance.setName(name);
    assertEquals(instance.getName(), name);
  }

  @Test
  public void testGetCharacteristics()
  {
    CharacteristicGroup instance = new CharacteristicGroup();
    List expResult = Collections.emptyList();
    List result = instance.getCharacteristics();
    assertEquals(result, expResult);
  }

  @Test
  public void testAddCharacteristic()
  {
    Characteristic c = new Characteristic();
    CharacteristicGroup instance = new CharacteristicGroup();
    instance.addCharacteristic(c);
    assertTrue(instance.getCharacteristics().contains(c));
  }
  
}
