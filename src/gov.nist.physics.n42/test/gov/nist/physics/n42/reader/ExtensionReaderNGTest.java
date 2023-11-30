/*
 * Copyright 2022, Lawrence Livermore National Security, LLC.
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.nist.physics.n42.reader;

import gov.llnl.utility.xml.DomBuilder;
import gov.llnl.utility.xml.bind.Reader;
import gov.llnl.utility.xml.bind.ReaderContext;
import gov.llnl.utility.xml.bind.SchemaBuilder;
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
public class ExtensionReaderNGTest
{
  
  public ExtensionReaderNGTest()
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
   * Test of getDeclaration method, of class ExtensionReader.
   */
  @Test
  public void testGetDeclaration()
  {
    System.out.println("getDeclaration");
    ExtensionReader instance = null;
    Reader.Declaration expResult = null;
    Reader.Declaration result = instance.getDeclaration();
    assertEquals(result, expResult);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of start method, of class ExtensionReader.
   */
  @Test
  public void testStart() throws Exception
  {
    System.out.println("start");
    ReaderContext context = null;
    Attributes attributes = null;
    ExtensionReader instance = null;
    Object expResult = null;
    Object result = instance.start(context, attributes);
    assertEquals(result, expResult);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of getHandlers method, of class ExtensionReader.
   */
  @Test
  public void testGetHandlers() throws Exception
  {
    System.out.println("getHandlers");
    ReaderContext context = null;
    ExtensionReader instance = null;
    Reader.ElementHandlerMap expResult = null;
    Reader.ElementHandlerMap result = instance.getHandlers(context);
    assertEquals(result, expResult);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of getSchemaType method, of class ExtensionReader.
   */
  @Test
  public void testGetSchemaType()
  {
    System.out.println("getSchemaType");
    ExtensionReader instance = null;
    String expResult = "";
    String result = instance.getSchemaType();
    assertEquals(result, expResult);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of createSchemaElement method, of class ExtensionReader.
   */
  @Test
  public void testCreateSchemaElement() throws Exception
  {
    System.out.println("createSchemaElement");
    SchemaBuilder builder = null;
    String name = "";
    DomBuilder group = null;
    boolean topLevel = false;
    ExtensionReader instance = null;
    DomBuilder expResult = null;
    DomBuilder result = instance.createSchemaElement(builder, name, group, topLevel);
    assertEquals(result, expResult);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of createSchemaType method, of class ExtensionReader.
   */
  @Test
  public void testCreateSchemaType() throws Exception
  {
    System.out.println("createSchemaType");
    SchemaBuilder builder = null;
    ExtensionReader instance = null;
    instance.createSchemaType(builder);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of findReader method, of class ExtensionReader.
   */
  @Test
  public void testFindReader()
  {
    System.out.println("findReader");
    String namespaceURI = "";
    String localName = "";
    String qualifiedName = "";
    Attributes attr = null;
    ExtensionReader instance = null;
    Reader expResult = null;
    Reader result = instance.findReader(namespaceURI, localName, qualifiedName, attr);
    assertEquals(result, expResult);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }
  
}
