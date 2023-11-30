/*
 * Copyright 2016, Lawrence Livermore National Security, LLC. 
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.math;

import static org.testng.Assert.*;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Test code for FloatFunctions.
 */
public class FloatFunctionsNGTest
{
  
  public FloatFunctionsNGTest()
  {
  }

  /**
   * Test constructor of class FloatFunctions.
   */
  @Test
  public void testConstructor()
  {
    FloatFunctions instance = new FloatFunctions();
  }
  
  /**
   * Test of exp method, of class FloatFunctions.
   */
  @Test
  public void testExp()
  {
    float d = 5.0F;
    float expResult = 148.4131622314453F;
    float result = FloatFunctions.exp(d);
    assertEquals(result, expResult, 0.0);
    d = -100.0F;
    expResult = 0.0F;
    result = FloatFunctions.exp(d);
    assertEquals(result, expResult, 0.0);
    d = -93.0F;
    expResult = 6.399870216417872E-41F;
    result = FloatFunctions.exp(d);
    assertEquals(result, expResult, 0.0);
  }

  /**
   * Test of tanh method, of class FloatFunctions.
   */
  @Test
  public void testTanh()
  {
    float d = 5.0F;
    float expResult = -4.53978973383E-5F;
    float result = FloatFunctions.tanh(d);
    assertEquals(result, expResult, 0.0);
    d = -5.0F;
    expResult = -0.9999091625213623F;
    result = FloatFunctions.tanh(d);
    assertEquals(result, expResult, 0.0);
  }

  /**
   * Test of expm1 method, of class FloatFunctions.
   */
  @Test
  public void testExpm1()
  {
    float z = 5.0F;
    float expResult = 147.4131622314453F;
    float result = FloatFunctions.expm1(z);
    assertEquals(result, expResult, 0.0);
    z = -5.0F;
    expResult = -0.9932620525360107F;
    result = FloatFunctions.expm1(z);
    assertEquals(result, expResult, 0.0);
    z = 0.25F;
    expResult = 0.2840254306793213F;
    result = FloatFunctions.expm1(z);
    assertEquals(result, expResult, 0.0);
  }
  
}
