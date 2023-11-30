/*
 * Copyright 2022, Lawrence Livermore National Security, LLC.
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.nist.physics.n42.reader;

import gov.llnl.utility.xml.bind.Reader;
import gov.llnl.utility.xml.bind.ReaderContext;
import gov.nist.physics.n42.data.SourcePosition;
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
public class SourcePositionReaderNGTest
{
  
  public SourcePositionReaderNGTest()
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
   * Test of start method, of class SourcePositionReader.
   */
  @Test
  public void testStart() throws Exception
  {
    System.out.println("start");
    ReaderContext context = null;
    Attributes attr = null;
    SourcePositionReader instance = new SourcePositionReader();
    SourcePosition expResult = null;
    SourcePosition result = instance.start(context, attr);
    assertEquals(result, expResult);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of getHandlers method, of class SourcePositionReader.
   */
  @Test
  public void testGetHandlers() throws Exception
  {
    System.out.println("getHandlers");
    ReaderContext context = null;
    SourcePositionReader instance = new SourcePositionReader();
    Reader.ElementHandlerMap expResult = null;
    Reader.ElementHandlerMap result = instance.getHandlers(context);
    assertEquals(result, expResult);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }
  
}
