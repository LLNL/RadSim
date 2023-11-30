/*
 * Copyright 2022, Lawrence Livermore National Security, LLC.
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.nist.physics.n42.data;

import java.util.List;
import static org.testng.Assert.*;
import org.testng.annotations.Test;

/**
 *
 * @author nelson85
 */
public class NuclideAnalysisResultsNGTest
{
  
  public NuclideAnalysisResultsNGTest()
  {
  }

  @Test
  public void testAddNuclide()
  {
    Nuclide nuclide = new Nuclide();
    NuclideAnalysisResults instance = new NuclideAnalysisResults();
    instance.addNuclide(nuclide);
    assertTrue(instance.getNuclides().contains(nuclide));
  }

  /**
   * Test of getNuclides method, of class NuclideAnalysisResults.
   */
  @Test
  public void testGetNuclides()
  {
    System.out.println("getNuclides");
    NuclideAnalysisResults instance = new NuclideAnalysisResults();
    List expResult = null;
    List result = instance.getNuclides();
    assertEquals(result, expResult);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }
  
}
