/*
 * Copyright 2022, Lawrence Livermore National Security, LLC.
 * All rights reserved.
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.rtk.flux;

import static org.testng.Assert.*;
import org.testng.annotations.Test;

/**
 *
 * @author nelson85
 */
public class FluxGroupTrapezoidNGTest
{

  public FluxGroupTrapezoidNGTest()
  {
  }

  public FluxGroupTrapezoid newInstance()
  {
    return new FluxGroupTrapezoid(50, 100, 1, 2);
  }

  @Test
  public void testGetEnergy0()
  {
    FluxGroupTrapezoid instance = newInstance();
    double expResult = 50.0;
    double result = instance.getEnergyLower();
    assertEquals(result, expResult, 0.0);
  }

  @Test(expectedExceptions = ArithmeticException.class)
  public void testSanity()
  {
    FluxGroupTrapezoid fluxGroupTrapezoid = new FluxGroupTrapezoid(0, 1, Double.NaN, 0);
    assertNotNull(fluxGroupTrapezoid);
  }

  @Test
  public void testGetEnergy1()
  {
    FluxGroupTrapezoid instance = newInstance();
    double expResult = 100.0;
    double result = instance.getEnergyUpper();
    assertEquals(result, expResult, 0.0);
  }

  @Test
  public void testGetCounts()
  {
    FluxGroupTrapezoid instance = newInstance();
    double expResult = 1.5 * 50;
    double result = instance.getCounts();
    assertEquals(result, expResult, 0.0);
  }

  @Test
  public void testGetIntegral()
  {
    FluxGroupTrapezoid instance = newInstance();
    assertEquals(instance.getIntegral(0, 20), 0.0, 0.0);
    assertEquals(instance.getIntegral(0, 200), 1.5 * 50, 0.0);
    assertEquals(instance.getIntegral(0, 75), 1.25 * 25, 0.0);
    assertEquals(instance.getIntegral(75, 200), 1.75 * 25, 0.0);
    assertEquals(instance.getIntegral(200, 300), 0.0, 0.0);
  }

  @Test
  public void testGetDensity0()
  {
    FluxGroupTrapezoid instance = newInstance();
    double expResult = 1.0;
    double result = instance.getDensityLower();
    assertEquals(result, expResult, 0.0);
  }

  @Test
  public void testGetDensity1()
  {
    FluxGroupTrapezoid instance = newInstance();
    double expResult = 2.0;
    double result = instance.getDensityUpper();
    assertEquals(result, expResult, 0.0);
  }

  @Test
  public void testGetEnergyLower()
  {
    FluxGroupTrapezoid instance = newInstance();
    double expResult = 50.0;
    double result = instance.getEnergyLower();
    assertEquals(result, expResult, 0.0);
  }

  @Test
  public void testGetEnergyUpper()
  {
    FluxGroupTrapezoid instance = newInstance();
    double expResult = 100.0;
    double result = instance.getEnergyUpper();
    assertEquals(result, expResult, 0.0);
  }

  @Test
  public void testGetDensityLower()
  {
    FluxGroupTrapezoid instance = newInstance();
    double expResult = 1.0;
    double result = instance.getDensityLower();
    assertEquals(result, expResult, 0.0);
  }

  @Test
  public void testGetDensityUpper()
  {
    FluxGroupTrapezoid instance = newInstance();
    double expResult = 2.0;
    double result = instance.getDensityUpper();
    assertEquals(result, expResult, 0.0);
  }

  @Test
  public void testToString()
  {
    FluxGroupTrapezoid instance = newInstance();
    String expResult = "GroupTrapezoid(e0=50.000000,e1=100.000000,d0=1.000000,d1=2.000000)";
    String result = instance.toString();
    assertEquals(result, expResult);
  }

  @Test
  public void testEquals()
  {
    FluxGroupTrapezoid obj = new FluxGroupTrapezoid(50, 100, 1, 2);
    FluxGroupTrapezoid instance = new FluxGroupTrapezoid(50, 100, 1, 2);
    assertTrue(instance.equals(obj));
    assertFalse(instance.equals(false));
    assertFalse(instance.equals(new Object()));
    assertFalse(instance.equals(new FluxGroupTrapezoid(51, 100, 1, 2)));
    assertFalse(instance.equals(new FluxGroupTrapezoid(50, 101, 1, 2)));
    assertFalse(instance.equals(new FluxGroupTrapezoid(50, 100, 2, 2)));
    assertFalse(instance.equals(new FluxGroupTrapezoid(50, 100, 1, 3)));
  }

}
