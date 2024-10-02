/*
 * Copyright 2022, Lawrence Livermore National Security, LLC.
 * All rights reserved
 *
 * Terms and conditions are given in "Notice" file.
 */
package gov.nist.physics.n42.reader;

import gov.llnl.utility.xml.bind.ReaderContext;
import gov.nist.physics.n42.data.ComplexObject;
import java.util.function.BiConsumer;
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
public class ReaderUtilitiesNGTest
{

  public ReaderUtilitiesNGTest()
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
   * Test of doublesFromString method, of class ReaderUtilities.
   */
  @Test
  public void testDoublesFromString()
  {
    System.out.println("doublesFromString");
    String str = "";
    double[] expResult = null;
    double[] result = ReaderUtilities.doublesFromString(str);
    assertEquals(result, expResult);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of doublesFromCountedZerosString method, of class ReaderUtilities.
   */
  @Test
  public void testDoublesFromCountedZerosString()
  {
    System.out.println("doublesFromCountedZerosString");
    String str = "";
    double[] expResult = null;
    double[] result = ReaderUtilities.doublesFromCountedZerosString(str);
    assertEquals(result, expResult);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of addReferences method, of class ReaderUtilities.
   */
  @Test
  public void testAddReferences() throws Exception
  {
    System.out.println("addReferences");
    ReaderContext context = null;
    Object target = null;
    BiConsumer method = null;
    Class cls = null;
    String references = "";
    ReaderUtilities.addReferences(context, target, method, cls, references);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of register method, of class ReaderUtilities.
   */
  @Test
  public void testRegister() throws Exception
  {
    System.out.println("register");
    ReaderContext context = null;
    ComplexObject out = null;
    Attributes attr = null;
    ReaderUtilities.register(context, out, attr);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

}
