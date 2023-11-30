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
public class RelativeLocationNGTest
{

  public RelativeLocationNGTest()
  {
  }

  public RelativeLocation newInstance()
  {
    try
    {
      DocumentReader<RadInstrumentData> dr = DocumentReader.create(RadInstrumentData.class);
      RadInstrumentData rid = dr.loadFile(Paths.get("test/gov/nist/physics/n42/resources/SRPM.xml"));
      return rid.getMeasurements().get(4).getItemState().get(0).getStateVector().getRelativeLocation();
    }
    catch (ReaderException | IOException ex)
    {
      throw new RuntimeException(ex);
    }
  }

  @Test
  public void testGetAzimuth()
  {
    RelativeLocation instance = newInstance();
    Quantity expResult = null;
    Quantity result = instance.getAzimuth();
    assertEquals(result, expResult);
  }

  @Test
  public void testSetAzimuth()
  {
    Quantity azimuth = new Quantity();
    RelativeLocation instance = new RelativeLocation();
    instance.setAzimuth(azimuth);
    assertSame(instance.getAzimuth(), azimuth);
  }

  @Test
  public void testGetInclination()
  {
    RelativeLocation instance = newInstance();
    Quantity expResult = null;
    Quantity result = instance.getInclination();
    assertEquals(result, expResult);
  }

  @Test
  public void testSetInclination()
  {
    Quantity inclination = new Quantity();
    RelativeLocation instance = new RelativeLocation();
    instance.setInclination(inclination);
    assertSame(instance.getInclination(), inclination);
  }

  @Test
  public void testGetDistance()
  {
    RelativeLocation instance = newInstance();
    Quantity expResult = new Quantity(3.3, null);
    Quantity result = instance.getDistance();
    assertEquals(result, expResult);
  }

  @Test
  public void testSetDistance()
  {
    Quantity distance = new Quantity();
    RelativeLocation instance = new RelativeLocation();
    instance.setDistance(distance);
    assertSame(instance.getDistance(), distance);
  }

  @Test
  public void testGetOrigin()
  {
    RelativeLocation instance = newInstance();
    Origin result = instance.getOrigin();
    assertNotNull(result);
  }

  @Test
  public void testSetOrigin()
  {
    Origin origin = new Origin();
    RelativeLocation instance = new RelativeLocation();
    instance.setOrigin(origin);
    assertSame(instance.getOrigin(), origin);
  }

  /**
   * Test of visitReferencedObjects method, of class RelativeLocation.
   */
  @Test
  public void testVisitReferencedObjects()
  {
    System.out.println("visitReferencedObjects");
    Consumer<ComplexObject> visitor = null;
    RelativeLocation instance = new RelativeLocation();
    instance.visitReferencedObjects(visitor);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

}
