/*
 * Copyright 2022, Lawrence Livermore National Security, LLC.
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.nist.physics.n42.data;

import java.util.List;
import java.util.function.Consumer;
import static org.testng.Assert.*;
import org.testng.annotations.Test;

/**
 *
 * @author nelson85
 */
public class RadDetectorStateNGTest
{
  
  public RadDetectorStateNGTest()
  {
  }
    public RadDetectorState newInstance()
  {
    RadDetectorState out = new RadDetectorState();
    out.addCharacteristics(new Characteristics());
    out.addFault(new Fault("bad", "very bad", FaultSeverityCode.Fatal));
    out.setState(new StateVector());
    out.setDetector(new RadDetectorInformation());
    return out;
  }
    
  @Test
  public void testGetState()
  {
    RadDetectorState instance = newInstance();
    StateVector result = instance.getState();
    assertNotNull(result);
  }

  @Test
  public void testSetState()
  {
    StateVector state = new StateVector();
    RadDetectorState instance = new RadDetectorState();
    instance.setState(state);
    assertSame(instance.getState(), state);
  }

  @Test
  public void testAddFault()
  {
    Fault fault = new Fault();
    RadDetectorState instance = new RadDetectorState();
    instance.addFault(fault);
    assertTrue(instance.getFaults().contains(fault));
  }

  @Test
  public void testAddCharacteristics()
  {
    Characteristics c = new Characteristics();
    RadDetectorState instance = new RadDetectorState();
    instance.addCharacteristics(c);
    assertTrue(instance.getCharacteristics().contains(c));
  }

  @Test
  public void testSetDetector()
  {
    RadDetectorInformation detector = new RadDetectorInformation();
    RadDetectorState instance = new RadDetectorState();
    instance.setDetector(detector);
    assertSame(instance.getDetector(), detector);
  }

  @Test
  public void testGetDetector()
  {
    RadDetectorState instance = newInstance();
    RadDetectorInformation result = instance.getDetector();
    assertNotNull(result);
  }

  /**
   * Test of getFaults method, of class RadDetectorState.
   */
  @Test
  public void testGetFaults()
  {
    System.out.println("getFaults");
    RadDetectorState instance = new RadDetectorState();
    List expResult = null;
    List result = instance.getFaults();
    assertEquals(result, expResult);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of getCharacteristics method, of class RadDetectorState.
   */
  @Test
  public void testGetCharacteristics()
  {
    System.out.println("getCharacteristics");
    RadDetectorState instance = new RadDetectorState();
    List expResult = null;
    List result = instance.getCharacteristics();
    assertEquals(result, expResult);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of visitReferencedObjects method, of class RadDetectorState.
   */
  @Test
  public void testVisitReferencedObjects()
  {
    System.out.println("visitReferencedObjects");
    Consumer<ComplexObject> visitor = null;
    RadDetectorState instance = new RadDetectorState();
    instance.visitReferencedObjects(visitor);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }
  
}
