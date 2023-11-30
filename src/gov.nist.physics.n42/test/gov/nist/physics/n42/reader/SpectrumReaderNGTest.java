/*
 * Copyright 2022, Lawrence Livermore National Security, LLC.
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.nist.physics.n42.reader;

import gov.nist.physics.n42.utility.SpectrumUtilities;
import static org.testng.Assert.*;
import org.testng.annotations.Test;

/**
 *
 * @author nelson85
 */
public class SpectrumReaderNGTest
{

  public SpectrumReaderNGTest()
  {
  }

  @Test
  public void testStart() throws Exception
  {
    // Tested end to end.
  }

  @Test
  public void testGetHandlers() throws Exception
  {
    // Tested end to end.
  }

  @Test
  public void testUnpackCountedZeros()
  {
    double[] compressed = new double[]
    {
      0, 3, 1, 2, 0, 2
    };
    double[] expResult = new double[]
    {
      0, 0, 0, 1, 2, 0, 0
    };
    double[] result = SpectrumUtilities.unpackCountedZeros(compressed);
    assertEquals(result, expResult);
  }

}
