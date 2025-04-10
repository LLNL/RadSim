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
public class PhysicalPropertyNGTest
{
  
  public PhysicalPropertyNGTest()
  {
  }

  /**
   * Test of values method, of class PhysicalProperty.
   */
  @Test
  public void testValues()
  {
  }

  /**
   * Test of valueOf method, of class PhysicalProperty.
   */
  @Test
  public void testValueOf()
  {
    String name = "LENGTH";
    PhysicalProperty expResult = PhysicalProperty.LENGTH;
    PhysicalProperty result = PhysicalProperty.valueOf(name);
    assertEquals(result, expResult);
  }

  /**
   * Test of toString method, of class PhysicalProperty.
   */
  @Test
  public void testToString()
  {
    PhysicalProperty instance = PhysicalProperty.LENGTH;;
    String expResult = "m";
    String result = instance.toString();
    assertEquals(result, expResult);
  }

  /**
   * Test of getUnit method, of class PhysicalProperty.
   */
  @Test
  public void testGetUnit()
  {
    PhysicalProperty instance = PhysicalProperty.LENGTH;
    String expResult = "length:m";
    String result = instance.getUnit();
    assertEquals(result, expResult);
  }

  /**
   * Test of getName method, of class PhysicalProperty.
   */
  @Test
  public void testGetSymbol()
  {
    PhysicalProperty instance =  PhysicalProperty.LENGTH;
    String expResult = "m";
    String result = instance.getSymbol();
    assertEquals(result, expResult);
  }

  /**
   * Test of getValue method, of class PhysicalProperty.
   */
  @Test
  public void testGetValue()
  {
    PhysicalProperty instance =  PhysicalProperty.LENGTH;
    double expResult = 1.0;
    double result = instance.getValue();
    assertEquals(result, expResult, 0.0);
  }

  /**
   * Test of getType method, of class PhysicalProperty.
   */
  @Test
  public void testGetType()
  {
    PhysicalProperty instance = PhysicalProperty.LENGTH;
    UnitType expResult = PhysicalProperty.LENGTH;
    UnitType result = instance.getType();
    assertEquals(result, expResult);
  }

  /**
   * Test of getMeasure method, of class PhysicalProperty.
   */
  @Test
  public void testGetMeasure()
  {
    PhysicalProperty instance = PhysicalProperty.LENGTH;
    String expResult = "length";
    String result = instance.getMeasure();
    assertEquals(result, expResult);
  }
  
}
