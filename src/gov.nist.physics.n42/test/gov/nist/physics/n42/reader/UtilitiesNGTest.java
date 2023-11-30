/*
 * Copyright 2022, Lawrence Livermore National Security, LLC.
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.nist.physics.n42.reader;

import static org.testng.Assert.*;
import org.testng.annotations.Test;

/**
 *
 * @author nelson85
 */
public class UtilitiesNGTest
{
  
  public UtilitiesNGTest()
  {
  }

  @Test
  public void testDoublesFromString()
  {
    double[] expResult = new double[]{1,2,3};
    double[] result = ReaderUtilities.doublesFromString("1.0 2.0 3.0");
    assertEquals(result, expResult);
  }
  
}
