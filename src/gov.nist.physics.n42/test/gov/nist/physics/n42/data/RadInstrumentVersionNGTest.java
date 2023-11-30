/*
 * Copyright 2022, Lawrence Livermore National Security, LLC.
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.nist.physics.n42.data;

import static org.testng.Assert.*;
import org.testng.annotations.Test;

/**
 *
 * @author nelson85
 */
public class RadInstrumentVersionNGTest
{

  public RadInstrumentVersion newInstance()
  {
    return new RadInstrumentVersion("foo", "bar");
  }

  public RadInstrumentVersionNGTest()
  {
  }

  @Test
  public void testGetComponentName()
  {
    RadInstrumentVersion instance = newInstance();
    String expResult = "foo";
    String result = instance.getComponentName();
    assertEquals(result, expResult);
  }

  @Test
  public void testGetComponentVersion()
  {
    RadInstrumentVersion instance = newInstance();
    String expResult = "bar";
    String result = instance.getComponentVersion();
    assertEquals(result, expResult);
  }

}
