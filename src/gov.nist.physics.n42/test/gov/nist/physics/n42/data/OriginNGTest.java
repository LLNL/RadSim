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
import java.util.function.Consumer;
import static org.testng.Assert.*;
import org.testng.annotations.Test;

/**
 *
 * @author nelson85
 */
public class OriginNGTest
{

  public OriginNGTest()
  {
  }

  public Origin newInstance()
  {
    try
    {
      DocumentReader<RadInstrumentData> dr = DocumentReader.create(RadInstrumentData.class);
      RadInstrumentData rid = dr.loadFile(Paths.get("test/gov/nist/physics/n42/resources/SRPM.xml"));
      return rid.getMeasurements().get(4).getItemState().get(0).getStateVector().getRelativeLocation().getOrigin();
    }
    catch (ReaderException | IOException ex)
    {
      throw new RuntimeException(ex);
    }
  }

  @Test
  public void testGetGeographicPoint()
  {
    Origin instance = newInstance();
    GeographicPoint expResult = null;
    GeographicPoint result = instance.getGeographicPoint();
    assertEquals(result, expResult);
  }

  @Test
  public void testSetGeographicPoint()
  {
    GeographicPoint geographicPoint = new GeographicPoint();
    Origin instance = new Origin();
    instance.setGeographicPoint(geographicPoint);
    assertSame(instance.getGeographicPoint(), geographicPoint);
  }

  @Test
  public void testGetDescription()
  {
    Origin instance = newInstance();
    String expResult = "SRPM centerline";
    String result = instance.getDescription();
    assertEquals(result, expResult);
  }

  @Test
  public void testSetDescription()
  {
    String description = "desc";
    Origin instance = new Origin();
    instance.setDescription(description);
    assertEquals(instance.getDescription(), description);
  }

  /**
   * Test of getReference method, of class Origin.
   */
  @Test
  public void testGetReference()
  {
    System.out.println("getReference");
    Origin instance = new Origin();
    OriginReference expResult = null;
    OriginReference result = instance.getReference();
    assertEquals(result, expResult);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of setReference method, of class Origin.
   */
  @Test
  public void testSetReference()
  {
    System.out.println("setReference");
    OriginReference reference = null;
    Origin instance = new Origin();
    instance.setReference(reference);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of visitReferencedObjects method, of class Origin.
   */
  @Test
  public void testVisitReferencedObjects()
  {
    System.out.println("visitReferencedObjects");
    Consumer<ComplexObject> visitor = null;
    Origin instance = new Origin();
    instance.visitReferencedObjects(visitor);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

}
