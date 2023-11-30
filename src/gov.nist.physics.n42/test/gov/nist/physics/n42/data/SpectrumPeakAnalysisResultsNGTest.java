/*
 * Copyright 2022, Lawrence Livermore National Security, LLC.
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.nist.physics.n42.data;

import java.util.List;
import static org.testng.Assert.*;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 *
 * @author pham21
 */
public class SpectrumPeakAnalysisResultsNGTest
{
  
  public SpectrumPeakAnalysisResultsNGTest()
  {
  }

  @BeforeClass
  public static void setUpClass() throws Exception
  {
  }

  @AfterClass
  public static void tearDownClass() throws Exception
  {
  }

  @BeforeMethod
  public void setUpMethod() throws Exception
  {
  }

  @AfterMethod
  public void tearDownMethod() throws Exception
  {
  }

  /**
   * Test of getPeaks method, of class SpectrumPeakAnalysisResults.
   */
  @Test
  public void testGetPeaks()
  {
    System.out.println("getPeaks");
    SpectrumPeakAnalysisResults instance = new SpectrumPeakAnalysisResults();
    List expResult = null;
    List result = instance.getPeaks();
    assertEquals(result, expResult);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of addPeak method, of class SpectrumPeakAnalysisResults.
   */
  @Test
  public void testAddPeak()
  {
    System.out.println("addPeak");
    SpectrumPeak peak = null;
    SpectrumPeakAnalysisResults instance = new SpectrumPeakAnalysisResults();
    instance.addPeak(peak);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }
  
}
