/*
 * Copyright 2022, Lawrence Livermore National Security, LLC.
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.nist.physics.n42.utility;

import gov.llnl.utility.xml.bind.Reader;
import gov.llnl.utility.xml.bind.ReaderContext;
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
public class IgnoreReaderNGTest
{
  
  public IgnoreReaderNGTest()
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
   * Test of getHandlers method, of class IgnoreReader.
   */
  @Test
  public void testGetHandlers() throws Exception
  {
    System.out.println("getHandlers");
    ReaderContext context = null;
    IgnoreReader instance = new IgnoreReader();
    Reader.ElementHandlerMap expResult = null;
    Reader.ElementHandlerMap result = instance.getHandlers(context);
    assertEquals(result, expResult);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of findReader method, of class IgnoreReader.
   */
  @Test
  public void testFindReader()
  {
    System.out.println("findReader");
    String namespaceURI = "";
    String localName = "";
    String qualifiedName = "";
    Attributes attributes = null;
    IgnoreReader instance = new IgnoreReader();
    Reader expResult = null;
    Reader result = instance.findReader(namespaceURI, localName, qualifiedName, attributes);
    assertEquals(result, expResult);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }
  
}
