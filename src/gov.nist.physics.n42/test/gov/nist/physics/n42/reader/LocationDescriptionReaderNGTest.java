/*
 * Copyright 2022, Lawrence Livermore National Security, LLC.
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.nist.physics.n42.reader;

import gov.llnl.utility.xml.bind.ReaderContext;
import gov.nist.physics.n42.data.LocationDescription;
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
public class LocationDescriptionReaderNGTest
{
  
  public LocationDescriptionReaderNGTest()
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
   * Test of contents method, of class LocationDescriptionReader.
   */
  @Test
  public void testContents() throws Exception
  {
    System.out.println("contents");
    ReaderContext context = null;
    String textContents = "";
    LocationDescriptionReader instance = new LocationDescriptionReader();
    LocationDescription expResult = null;
    LocationDescription result = instance.contents(context, textContents);
    assertEquals(result, expResult);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }
  
}
