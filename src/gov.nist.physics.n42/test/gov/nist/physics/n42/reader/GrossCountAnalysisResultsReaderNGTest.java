/*
 * Copyright 2022, Lawrence Livermore National Security, LLC.
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.nist.physics.n42.reader;

import gov.llnl.utility.xml.bind.Reader;
import gov.llnl.utility.xml.bind.ReaderContext;
import gov.nist.physics.n42.data.GrossCountAnalysisResults;
import static org.testng.Assert.*;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.xml.sax.Attributes;

/**
 *
 * @author pham21
 */
public class GrossCountAnalysisResultsReaderNGTest
{
  
  public GrossCountAnalysisResultsReaderNGTest()
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
   * Test of start method, of class GrossCountAnalysisResultsReader.
   */
  @Test
  public void testStart() throws Exception
  {
    System.out.println("start");
    ReaderContext context = null;
    Attributes attr = null;
    GrossCountAnalysisResultsReader instance = new GrossCountAnalysisResultsReader();
    GrossCountAnalysisResults expResult = null;
    GrossCountAnalysisResults result = instance.start(context, attr);
    assertEquals(result, expResult);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of getHandlers method, of class GrossCountAnalysisResultsReader.
   */
  @Test
  public void testGetHandlers() throws Exception
  {
    System.out.println("getHandlers");
    ReaderContext context = null;
    GrossCountAnalysisResultsReader instance = new GrossCountAnalysisResultsReader();
    Reader.ElementHandlerMap expResult = null;
    Reader.ElementHandlerMap result = instance.getHandlers(context);
    assertEquals(result, expResult);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }
  
}
