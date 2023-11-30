/*
 * Copyright 2016, Lawrence Livermore National Security, LLC. 
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.math.algebra;

import static org.testng.Assert.*;
import org.testng.annotations.Test;

/**
 * Test code for CycleChecker.
 */
public class CycleCheckerNGTest
{
  
  public CycleCheckerNGTest()
  {
  }
  
  /**
   * Test of add method, of class CycleChecker.
   */
  @Test
  public void testAdd()
  {
    int regressorId = 1;
    CycleChecker instance = new CycleChecker(5,5);
    boolean expResult = false;
    boolean result = instance.add(regressorId);
    assertEquals(result, expResult);
    expResult = true;
    result = instance.add(regressorId);
    assertEquals(result, expResult); 
  }

  /**
   * Test of clear method, of class CycleChecker.
   */
  @Test
  public void testClear()
  {
    CycleChecker instance = new CycleChecker(5,5);
    instance.index = 1;
    instance.clear();
    assertEquals(instance.index, 0);
    assertEquals(instance.history, new int[]{0,0,0,0,0});
    assertEquals(instance.lastUse, new int[]{-1,-1,-1,-1,-1});
  }

  /**
   * Test of main method, of class CycleChecker.
   */
  @Test
  public void testMain()
  {
    String[] args = null;
    CycleChecker.main(args);
  }
  
}
