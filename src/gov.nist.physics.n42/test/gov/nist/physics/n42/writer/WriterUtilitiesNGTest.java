/*
 * Copyright 2022, Lawrence Livermore National Security, LLC.
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.nist.physics.n42.writer;

import gov.llnl.utility.xml.bind.ObjectWriter;
import gov.llnl.utility.xml.bind.WriterContext;
import gov.nist.physics.n42.data.ComplexObject;
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
public class WriterUtilitiesNGTest
{
  
  public WriterUtilitiesNGTest()
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
   * Test of getObjectReference method, of class WriterUtilities.
   */
  @Test
  public void testGetObjectReference()
  {
    System.out.println("getObjectReference");
    ComplexObject co = null;
    WriterContext context = null;
    String expResult = "";
    String result = WriterUtilities.getObjectReference(co, context);
    assertEquals(result, expResult);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of getObjectReferences method, of class WriterUtilities.
   */
  @Test
  public void testGetObjectReferences()
  {
    System.out.println("getObjectReferences");
    List<? extends ComplexObject> list = null;
    WriterContext context = null;
    String expResult = "";
    String result = WriterUtilities.getObjectReferences(list, context);
    assertEquals(result, expResult);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of writeRemarkContents method, of class WriterUtilities.
   */
  @Test
  public void testWriteRemarkContents() throws Exception
  {
    System.out.println("writeRemarkContents");
    ObjectWriter.WriterBuilder builder = null;
    ComplexObject co = null;
    WriterUtilities.writeRemarkContents(builder, co);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of formatDoubleObject method, of class WriterUtilities.
   */
  @Test
  public void testFormatDoubleObject()
  {
    System.out.println("formatDoubleObject");
    double o = 0.0;
    String expResult = "";
    String result = WriterUtilities.formatDoubleObject(o);
    assertEquals(result, expResult);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of ensureId method, of class WriterUtilities.
   */
  @Test
  public void testEnsureId()
  {
    System.out.println("ensureId");
    ComplexObject object = null;
    WriterUtilities.ensureId(object);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }
  
}
