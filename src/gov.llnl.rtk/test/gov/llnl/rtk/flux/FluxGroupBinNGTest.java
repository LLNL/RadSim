/*
 * Copyright 2022, Lawrence Livermore National Security, LLC.
 * All rights reserved.
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.rtk.flux;

import gov.llnl.rtk.flux.FluxGroupBin;
import static org.testng.Assert.*;
import org.testng.annotations.Test;

/**
 *
 * @author nelson85
 */
public class FluxGroupBinNGTest
{

  public FluxGroupBinNGTest()
  {
  }

  FluxGroupBin newInstance()
  {
    return new FluxGroupBin(10.0, 20.0, 50.0);
  }

  @Test
  public void testGetEnergy0()
  {
    FluxGroupBin instance = newInstance();
    double expResult = 10.0;
    double result = instance.getEnergyLower();
    assertEquals(result, expResult, 0.0);
  }

  @Test
  public void testGetEnergy1()
  {
    FluxGroupBin instance = newInstance();
    double expResult = 20.0;
    double result = instance.getEnergyUpper();
    assertEquals(result, expResult, 0.0);
  }

  @Test
  public void testGetCounts()
  {
    FluxGroupBin instance = newInstance();
    double expResult = 50.0;
    double result = instance.getCounts();
    assertEquals(result, expResult, 0.0);
  }

  @Test
  public void testGetIntegral()
  {
    FluxGroupBin instance = newInstance();
    assertEquals(instance.getIntegral(0, 10), 0.0, 0.0);
    assertEquals(instance.getIntegral(20, 30), 0.0, 0.0);
    assertEquals(instance.getIntegral(0, 30), 50.0, 0.0);
    assertEquals(instance.getIntegral(10, 30), 50.0, 0.0);
    assertEquals(instance.getIntegral(10, 20), 50.0, 0.0);
    assertEquals(instance.getIntegral(12, 18), 30.0, 0.0);
  }

  @Test
  public void testToString()
  {
    FluxGroupBin instance = newInstance();
    String expResult = "GroupBin(e0=10.000000,e1=20.000000,c=50.000000)";
    String result = instance.toString();
    assertEquals(result, expResult);
  }

  @Test
  public void testGetEnergyLower()
  {
    FluxGroupBin instance = newInstance();
    double expResult = 10.0;
    double result = instance.getEnergyLower();
    assertEquals(result, expResult, 0.0);
  }

  @Test
  public void testGetEnergyUpper()
  {
    FluxGroupBin instance = newInstance();
    double expResult = 20.0;
    double result = instance.getEnergyUpper();
    assertEquals(result, expResult, 0.0);
  }

  @Test
  public void testEquals()
  {
    FluxGroupBin instance = newInstance();
    Object obj = newInstance();
    assertTrue(instance.equals(obj));
    assertFalse(instance.equals(null));
    assertFalse(instance.equals(new Object()));
  }

}
