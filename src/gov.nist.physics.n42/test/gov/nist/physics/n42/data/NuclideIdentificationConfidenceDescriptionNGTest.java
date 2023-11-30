/*
 * Copyright 2022, Lawrence Livermore National Security, LLC.
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.nist.physics.n42.data;

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
public class NuclideIdentificationConfidenceDescriptionNGTest
{
  
  public NuclideIdentificationConfidenceDescriptionNGTest()
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
   * Test of getValue method, of class NuclideIdentificationConfidenceDescription.
   */
  @Test
  public void testGetValue()
  {
    System.out.println("getValue");
    NuclideIdentificationConfidenceDescription instance = null;
    String expResult = "";
    String result = instance.getValue();
    assertEquals(result, expResult);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of setValue method, of class NuclideIdentificationConfidenceDescription.
   */
  @Test
  public void testSetValue()
  {
    System.out.println("setValue");
    String value = "";
    NuclideIdentificationConfidenceDescription instance = null;
    instance.setValue(value);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }
  
}
