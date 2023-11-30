/*
 * Copyright 2022, Lawrence Livermore National Security, LLC.
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.nist.physics.n42.writer;

import gov.llnl.utility.xml.bind.ObjectWriter;
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
public class LocationDescriptionWriterNGTest
{
  
  public LocationDescriptionWriterNGTest()
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
   * Test of attributes method, of class LocationDescriptionWriter.
   */
  @Test
  public void testAttributes() throws Exception
  {
    System.out.println("attributes");
    ObjectWriter.WriterAttributes attributes = null;
    LocationDescription object = null;
    LocationDescriptionWriter instance = new LocationDescriptionWriter();
    instance.attributes(attributes, object);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of contents method, of class LocationDescriptionWriter.
   */
  @Test
  public void testContents() throws Exception
  {
    System.out.println("contents");
    LocationDescription object = null;
    LocationDescriptionWriter instance = new LocationDescriptionWriter();
    instance.contents(object);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }
  
}
