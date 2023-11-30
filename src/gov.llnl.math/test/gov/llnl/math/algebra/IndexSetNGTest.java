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
 * Test code for IndexSet.
 * 
 */
public class IndexSetNGTest
{
  
  public IndexSetNGTest()
  {
  }

  /**
   * Test of createFilled method, of class IndexSet.
   */
  @Test
  public void testCreateFilled()
  {
    int n = 1;
    IndexSet expResult = new IndexSet(new int[]{0});
    IndexSet result = IndexSet.createFilled(n);
    assertEquals(result.capacity_, expResult.capacity_);
    assertEquals(result.length_, expResult.length_);
    assertEquals(result.set_, expResult.set_);
  }

  /**
   * Test of resize method, of class IndexSet.
   */
  @Test
  public void testResize()
  {
    int size = 10;
    IndexSet instance = new IndexSet(1);
    instance.resize(size);
    assertEquals(instance.set_.length, size);
  }

  /**
   * Test of clear method, of class IndexSet.
   */
  @Test
  public void testClear()
  {
    IndexSet instance = new IndexSet(new int[]{1,2,3,4,5,6,7,8,9});
    instance.clear();
    assertEquals(instance.size(), 0);
  }

  /**
   * Test of set method, of class IndexSet.
   */
  @Test
  public void testSet()
  {
    int i = 0;
    int value = 10;
    IndexSet instance = new IndexSet(new int[]{1,2,3,4,5,6,7,8,9});
    instance.set(i, value);
    assertEquals(instance.get(0), value);
  }

  /**
   * Test of fill method, of class IndexSet.
   */
  @Test
  public void testFill()
  {
    int n = 3;
    IndexSet instance = new IndexSet(3);
    instance.fill(n);
    assertNotNull(instance.set_);
    for(int i = 0; i < instance.set_.length; ++i)
    {
      assertEquals(instance.set_[i], i);
    }
  }

  /**
   * Test of insert method, of class IndexSet.
   */
  @Test
  public void testInsert()
  {
    int id = 10;
    IndexSet instance = new IndexSet(3);
    instance.insert(id);
    IndexSet expResult = new IndexSet(new int[]{10,0,0});
    assertEquals(instance.set_, expResult.set_);
  }

  /**
   * Test of erase method, of class IndexSet.
   */
  @Test
  public void testErase()
  {
    int index = 0;
    IndexSet instance = new IndexSet(new int[]{1,2,3,4,5,6,7,8,9});
    instance.erase(index);
    assertEquals(instance.set_, new int[]{9,2,3,4,5,6,7,8,9});
    index = 11;
    instance.erase(index);
  }

  /**
   * Test of remove method, of class IndexSet.
   */
  @Test
  public void testRemove()
  {
    int index = 0;
    IndexSet instance = new IndexSet(new int[]{1,2,3,4,5,6,7,8,9});
    instance.remove(index);
    IndexSet expResult = new IndexSet(new int[]{2,3,4,5,6,7,8,9,9});
    assertEquals(instance.set_, expResult.set_);
    index = 1;
    instance = new IndexSet(new int[]{1,2,3,4,5,6,7,8,9});
    instance.remove(index);
    expResult = new IndexSet(new int[]{1,3,4,5,6,7,8,9,9});
    assertEquals(instance.set_, expResult.set_);
  }

  /**
   * Test of size method, of class IndexSet.
   */
  @Test
  public void testSize()
  {
    IndexSet instance = new IndexSet(new int[]{1,2,3,4,5,6,7,8,9});
    int expResult = 9;
    int result = instance.size();
    assertEquals(result, expResult);
  }

  /**
   * Test of get method, of class IndexSet.
   */
  @Test
  public void testGet()
  {
    int i = 0;
    IndexSet instance = new IndexSet(new int[]{1,2,3,4,5,6,7,8,9});
    int expResult = 1;
    int result = instance.get(i);
    assertEquals(result, expResult);
  }

  /**
   * Test of clone method, of class IndexSet.
   */
  @Test
  public void testClone()
  {
    IndexSet instance = new IndexSet(new int[]{1,2,3,4,5,6,7,8,9});
    IndexSet expResult = new IndexSet(new int[]{1,2,3,4,5,6,7,8,9});
    IndexSet result = instance.clone();
    assertEquals(result.capacity_, expResult.capacity_);
    assertEquals(result.length_, expResult.length_);
    assertEquals(result.set_, expResult.set_);
  }

  /**
   * Test of toArray method, of class IndexSet.
   */
  @Test
  public void testToArray()
  {
    IndexSet instance = new IndexSet(new int[]{1,2,3,4,5,6,7,8,9});
    int[] expResult = new int[]{1,2,3,4,5,6,7,8,9};
    int[] result = instance.toArray();
    assertEquals(result, expResult);
  }
  
}
