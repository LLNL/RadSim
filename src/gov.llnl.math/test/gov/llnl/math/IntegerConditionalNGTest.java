/*
 * Copyright 2016, Lawrence Livermore National Security, LLC. 
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.math;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Test code for IntegerConditional.
 */
public class IntegerConditionalNGTest
{
  
  public IntegerConditionalNGTest()
  {
  }

  /**
   * Test of test method from class GreaterThan, of class IntegerConditional.
   */
  @Test
  public void testEvaluateGreaterThan()
  {
    IntegerConditional cond = new IntegerConditional.GreaterThan(5);
    Assert.assertTrue(!cond.test(1));
    Assert.assertTrue(cond.test(10));
  }

  /**
   * Test of test method from class GreaterThanEqual, of class IntegerConditional.
   */
  @Test
  public void testEvaluateGreaterThanEqual()
  {
    IntegerConditional cond = new IntegerConditional.GreaterThanEqual(5);
    Assert.assertTrue(cond.test(10));
    Assert.assertTrue(!cond.test(1));
  }

  /**
   * Test of test method from class LessThan, of class IntegerConditional.
   */
  @Test
  public void testEvaluateLessThan()
  {
    IntegerConditional cond = new IntegerConditional.LessThan(5);
    Assert.assertTrue(cond.test(4));
    Assert.assertTrue(!cond.test(6));
  }

  /**
   * Test of test method from class LessThanEqual, of class IntegerConditional.
   */
  @Test
  public void testEvaluateLessThanEqual()
  {
    IntegerConditional cond = new IntegerConditional.LessThanEqual(5);
    Assert.assertTrue(cond.test(4));
    Assert.assertTrue(!cond.test(6));
  }
}
