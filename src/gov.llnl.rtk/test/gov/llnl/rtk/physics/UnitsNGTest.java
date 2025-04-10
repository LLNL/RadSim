/*
 * Copyright 2019, Lawrence Livermore National Security, LLC. 
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.rtk.physics;

import static org.testng.Assert.*;
import org.testng.annotations.Test;

/**
 *
 * @author nelson85
 */
public class UnitsNGTest
{
  
  public UnitsNGTest()
  {
  }

  /**
   * Test of getSymbol method, of class Units.
   */
  @Test
  public void testGetSymbol()
  {
    Units instance = Units.get("length:cm");
    String expResult = "cm";
    String result = instance.getSymbol();
    assertEquals(result, expResult);
  }

  /**
   * Test of getValue method, of class Units.
   */
  @Test
  public void testGetValue()
  {
    Units instance =  Units.get("length:cm");
    double expResult = 0.01;
    double result = instance.getValue();
    assertEquals(result, expResult, 0.0);
  }

  /**
   * Test of getType method, of class Units.
   */
  @Test
  public void testGetType()
  {
    Units instance =  Units.get("cm");
    UnitType expResult = PhysicalProperty.LENGTH;
    UnitType result = instance.getType();
    assertEquals(result, expResult);
    assertEquals(result.getMeasure(), "length");
  }

  /**
   * Test of get method, of class Units.
   */
  @Test
  public void testGet()
  {
    String name = "length:m";
    Units expResult = PhysicalProperty.LENGTH;;
    Units result = Units.get(name);
    assertEquals(result, expResult);
  }
 
}
