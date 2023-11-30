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
import static org.testng.Assert.*;
import org.testng.annotations.Test;

/**
 *
 * @author nelson85
 */
public class NuclideNGTest
{

  public NuclideNGTest()
  {
  }

  public Nuclide newInstance()
  {
    try
    {
      DocumentReader<RadInstrumentData> dr = DocumentReader.create(RadInstrumentData.class);
      RadInstrumentData rid = dr.loadFile(Paths.get("test/gov/nist/physics/n42/resources/Alarm.xml"));
      return rid.getAnalysisResults().get(0).getNuclideAnalysisResults().getNuclides().get(0);
    }
    catch (ReaderException | IOException ex)
    {
      throw new RuntimeException(ex);
    }
  }

  @Test
  public void testGetCategory()
  {
    Nuclide instance = newInstance();
    String expResult = "SNM";
    String result = instance.getCategory();
    assertEquals(result, expResult);
  }

  @Test
  public void testSetCategory()
  {
    String category = "category";
    Nuclide instance = new Nuclide();
    instance.setCategory(category);
    assertEquals(instance.getCategory(), category);
  }

  @Test
  public void testSetName()
  {
    String name = "name";
    Nuclide instance = new Nuclide();
    instance.setName(name);
    assertEquals(instance.getName(), name);
  }

  @Test
  public void testSetIdentifiedIndicator()
  {
    boolean flag = true;
    Nuclide instance = new Nuclide();
    instance.setIdentifiedIndicator(flag);
    assertTrue(instance.isIdentifiedIndicator());
  }

  @Test
  public void testIsIdentifiedIndicator()
  {
    Nuclide instance = newInstance();
    boolean expResult = true;
    boolean result = instance.isIdentifiedIndicator();
    assertEquals(result, expResult);
  }

  @Test
  public void testGetName()
  {
    Nuclide instance = new Nuclide();
    String expResult = null;
    String result = instance.getName();
    assertEquals(result, expResult);
  }

  /**
   * Test of findConfidence method, of class Nuclide.
   */
  @Test
  public void testFindConfidence()
  {
    System.out.println("findConfidence");
    Class<T> type = null;
    Nuclide instance = new Nuclide();
    Object expResult = null;
    Object result = instance.findConfidence(type);
    assertEquals(result, expResult);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of addConfidence method, of class Nuclide.
   */
  @Test
  public void testAddConfidence()
  {
    System.out.println("addConfidence");
    NuclideIdentificationConfidence confidence = null;
    Nuclide instance = new Nuclide();
    instance.addConfidence(confidence);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of getConfidence method, of class Nuclide.
   */
  @Test
  public void testGetConfidence()
  {
    System.out.println("getConfidence");
    Nuclide instance = new Nuclide();
    List expResult = null;
    List result = instance.getConfidence();
    assertEquals(result, expResult);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of setShieldingAtomicNumber method, of class Nuclide.
   */
  @Test
  public void testSetShieldingAtomicNumber()
  {
    System.out.println("setShieldingAtomicNumber");
    Double u = null;
    Nuclide instance = new Nuclide();
    instance.setShieldingAtomicNumber(u);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of setShieldingArealDensity method, of class Nuclide.
   */
  @Test
  public void testSetShieldingArealDensity()
  {
    System.out.println("setShieldingArealDensity");
    Double u = null;
    Nuclide instance = new Nuclide();
    instance.setShieldingArealDensity(u);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of getActivity method, of class Nuclide.
   */
  @Test
  public void testGetActivity()
  {
    System.out.println("getActivity");
    Nuclide instance = new Nuclide();
    Quantity expResult = null;
    Quantity result = instance.getActivity();
    assertEquals(result, expResult);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of setActivity method, of class Nuclide.
   */
  @Test
  public void testSetActivity()
  {
    System.out.println("setActivity");
    Quantity activity = null;
    Nuclide instance = new Nuclide();
    instance.setActivity(activity);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of getActivityUncertainty method, of class Nuclide.
   */
  @Test
  public void testGetActivityUncertainty()
  {
    System.out.println("getActivityUncertainty");
    Nuclide instance = new Nuclide();
    Quantity expResult = null;
    Quantity result = instance.getActivityUncertainty();
    assertEquals(result, expResult);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of setActivityUncertainty method, of class Nuclide.
   */
  @Test
  public void testSetActivityUncertainty()
  {
    System.out.println("setActivityUncertainty");
    Quantity activityUncertainty = null;
    Nuclide instance = new Nuclide();
    instance.setActivityUncertainty(activityUncertainty);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of getShieldingAtomicNumber method, of class Nuclide.
   */
  @Test
  public void testGetShieldingAtomicNumber()
  {
    System.out.println("getShieldingAtomicNumber");
    Nuclide instance = new Nuclide();
    Double expResult = null;
    Double result = instance.getShieldingAtomicNumber();
    assertEquals(result, expResult);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of getShieldingArealDensity method, of class Nuclide.
   */
  @Test
  public void testGetShieldingArealDensity()
  {
    System.out.println("getShieldingArealDensity");
    Nuclide instance = new Nuclide();
    Double expResult = null;
    Double result = instance.getShieldingArealDensity();
    assertEquals(result, expResult);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of getMinimumDetectableActivity method, of class Nuclide.
   */
  @Test
  public void testGetMinimumDetectableActivity()
  {
    System.out.println("getMinimumDetectableActivity");
    Nuclide instance = new Nuclide();
    Quantity expResult = null;
    Quantity result = instance.getMinimumDetectableActivity();
    assertEquals(result, expResult);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of setMinimumDetectableActivity method, of class Nuclide.
   */
  @Test
  public void testSetMinimumDetectableActivity()
  {
    System.out.println("setMinimumDetectableActivity");
    Quantity minimumDetectableActivity = null;
    Nuclide instance = new Nuclide();
    instance.setMinimumDetectableActivity(minimumDetectableActivity);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of setSourceGeometryCode method, of class Nuclide.
   */
  @Test
  public void testSetSourceGeometryCode()
  {
    System.out.println("setSourceGeometryCode");
    SourceGeometryCode sourceGeometry = null;
    Nuclide instance = new Nuclide();
    instance.setSourceGeometryCode(sourceGeometry);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of getSourceGeometry method, of class Nuclide.
   */
  @Test
  public void testGetSourceGeometry()
  {
    System.out.println("getSourceGeometry");
    Nuclide instance = new Nuclide();
    SourceGeometryCode expResult = null;
    SourceGeometryCode result = instance.getSourceGeometry();
    assertEquals(result, expResult);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of setSourceGeometry method, of class Nuclide.
   */
  @Test
  public void testSetSourceGeometry()
  {
    System.out.println("setSourceGeometry");
    SourceGeometryCode sourceGeometry = null;
    Nuclide instance = new Nuclide();
    instance.setSourceGeometry(sourceGeometry);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of setSourcePosition method, of class Nuclide.
   */
  @Test
  public void testSetSourcePosition()
  {
    System.out.println("setSourcePosition");
    SourcePosition position = null;
    Nuclide instance = new Nuclide();
    instance.setSourcePosition(position);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of getSourcePosition method, of class Nuclide.
   */
  @Test
  public void testGetSourcePosition()
  {
    System.out.println("getSourcePosition");
    Nuclide instance = new Nuclide();
    SourcePosition expResult = null;
    SourcePosition result = instance.getSourcePosition();
    assertEquals(result, expResult);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

}
