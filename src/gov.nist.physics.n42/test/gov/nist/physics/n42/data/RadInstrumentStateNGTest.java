/*
 * Copyright 2022, Lawrence Livermore National Security, LLC.
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.nist.physics.n42.data;

import gov.llnl.utility.io.ReaderException;
import gov.llnl.utility.xml.bind.DocumentReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.function.Consumer;
import static org.testng.Assert.*;
import org.testng.annotations.Test;

/**
 *
 * @author nelson85
 */
public class RadInstrumentStateNGTest
{

  public RadInstrumentStateNGTest()
  {
  }

  public RadInstrumentState newInstance()
  {
    try
    {
      DocumentReader<RadInstrumentData> dr = DocumentReader.create(RadInstrumentData.class);
      RadInstrumentData rid = dr.loadFile(Paths.get("test/gov/nist/physics/n42/resources/Alarm.xml"));
      return rid.getMeasurements().get(4).getInstrumentState();
    }
    catch (ReaderException | IOException ex)
    {
      throw new RuntimeException(ex);
    }
  }

  @Test
  public void testGetStateVector()
  {
    RadInstrumentState instance = newInstance();
    StateVector expResult = null;
    StateVector result = instance.getStateVector();
    assertEquals(result, expResult);
  }

  @Test
  public void testSetStateVector()
  {
    StateVector stateVector = new StateVector();
    RadInstrumentState instance = new RadInstrumentState();
    instance.setStateVector(stateVector);
    assertSame(instance.getStateVector(), stateVector);
  }

  @Test
  public void testSetInstrument()
  {
    RadInstrumentInformation get = new RadInstrumentInformation();
    RadInstrumentState instance = new RadInstrumentState();
    instance.setInstrument(get);
    assertSame(instance.getInstrument(), get);
  }

  @Test
  public void testGetInstrument()
  {
    RadInstrumentState instance = newInstance();
    RadInstrumentInformation result = instance.getInstrument();
    assertNotNull(result);
  }

  @Test
  public void testGetModeCode()
  {
    RadInstrumentState instance = newInstance();
    String expResult = null;
    String result = instance.getModeCode();
    assertEquals(result, expResult);
  }

  @Test
  public void testSetModeCode()
  {
    String modeCode = "mode";
    RadInstrumentState instance = new RadInstrumentState();
    instance.setModeCode(modeCode);
    assertEquals(instance.getModeCode(), modeCode);
  }

  @Test
  public void testGetModeDescription()
  {
    RadInstrumentState instance = newInstance();
    String expResult = null;
    String result = instance.getModeDescription();
    assertEquals(result, expResult);
  }

  @Test
  public void testSetModeDescription()
  {
    String modeDescription = "desc";
    RadInstrumentState instance = new RadInstrumentState();
    instance.setModeDescription(modeDescription);
    assertSame(instance.getModeDescription(), modeDescription);
  }

  @Test
  public void testAddInstrumentCharacteristics()
  {
    Characteristics c = new Characteristics();
    RadInstrumentState instance = new RadInstrumentState();
    instance.addInstrumentCharacteristics(c);
    assertTrue(instance.getInstrumentCharacteristics().contains(c));
  }

  /**
   * Test of addFault method, of class RadInstrumentState.
   */
  @Test
  public void testAddFault()
  {
    System.out.println("addFault");
    Fault c = null;
    RadInstrumentState instance = new RadInstrumentState();
    instance.addFault(c);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of getInstrumentCharacteristics method, of class RadInstrumentState.
   */
  @Test
  public void testGetInstrumentCharacteristics()
  {
    System.out.println("getInstrumentCharacteristics");
    RadInstrumentState instance = new RadInstrumentState();
    List expResult = null;
    List result = instance.getInstrumentCharacteristics();
    assertEquals(result, expResult);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of getFaults method, of class RadInstrumentState.
   */
  @Test
  public void testGetFaults()
  {
    System.out.println("getFaults");
    RadInstrumentState instance = new RadInstrumentState();
    List expResult = null;
    List result = instance.getFaults();
    assertEquals(result, expResult);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of visitReferencedObjects method, of class RadInstrumentState.
   */
  @Test
  public void testVisitReferencedObjects()
  {
    System.out.println("visitReferencedObjects");
    Consumer<ComplexObject> visitor = null;
    RadInstrumentState instance = new RadInstrumentState();
    instance.visitReferencedObjects(visitor);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

}
