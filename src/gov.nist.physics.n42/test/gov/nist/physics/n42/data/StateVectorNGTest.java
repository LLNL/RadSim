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
public class StateVectorNGTest
{
  
  public StateVectorNGTest()
  {
  }

      public StateVector newInstance()
  {
    try
    {
      DocumentReader<RadInstrumentData> dr = DocumentReader.create(RadInstrumentData.class);
      RadInstrumentData rid = dr.loadFile(Paths.get("test/gov/nist/physics/n42/resources/PRD.xml"));
      return rid.getMeasurements().get(0).getInstrumentState().getStateVector();
    }
    catch (ReaderException | IOException ex)
    {
      throw new RuntimeException(ex);
    }
  }
      
  @Test
  public void testSetLocationDescription()
  {
    LocationDescription location = new LocationDescription();
    location.setValue("location");
    StateVector instance = new StateVector();
    instance.setLocationDescription(location);
    assertEquals(instance.getLocationDescription(), location);
  }

  @Test
  public void testGetCoordinate()
  {
    StateVector instance = newInstance();
    GeographicPoint result = instance.getCoordinate();
    assertNotNull(result);
  }

  @Test
  public void testSetCoordinate()
  {
    GeographicPoint coordinate = new GeographicPoint();
    StateVector instance = new StateVector();
    instance.setCoordinate(coordinate);
    assertEquals(instance.getCoordinate(), coordinate);
  }

  @Test
  public void testGetLocationDescription()
  {
    StateVector instance = newInstance();
    LocationDescription result = instance.getLocationDescription();
    assertNull(result);
  }

  @Test
  public void testGetRelativeLocation()
  {
    StateVector instance = newInstance();
    RelativeLocation expResult = null;
    RelativeLocation result = instance.getRelativeLocation();
    assertEquals(result, expResult);
  }

  @Test
  public void testSetRelativeLocation()
  {
    RelativeLocation relativeLocation = new RelativeLocation();
    StateVector instance = new StateVector();
    instance.setRelativeLocation(relativeLocation);
    assertEquals(instance.getRelativeLocation(), relativeLocation);
  }

  @Test
  public void testGetOrientation()
  {
    StateVector instance = newInstance();
    Orientation expResult = null;
    Orientation result = instance.getOrientation();
    assertEquals(result, expResult);
  }

  @Test
  public void testSetOrientation()
  {
    Orientation orientation = new Orientation();
    StateVector instance = new StateVector();
    instance.setOrientation(orientation);
    assertEquals(instance.getOrientation(), orientation);
  }

  @Test
  public void testGetSpeed()
  {
    StateVector instance = newInstance();
    Quantity expResult = null;
    Quantity result = instance.getSpeed();
    assertEquals(result, expResult);
  }

  @Test
  public void testSetSpeed()
  {
    Quantity speed = new Quantity();
    StateVector instance = new StateVector();
    instance.setSpeed(speed);
    assertEquals(instance.getSpeed(), speed);
  }

  /**
   * Test of visitReferencedObjects method, of class StateVector.
   */
  @Test
  public void testVisitReferencedObjects()
  {
    System.out.println("visitReferencedObjects");
    Consumer<ComplexObject> visitor = null;
    StateVector instance = new StateVector();
    instance.visitReferencedObjects(visitor);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }
  
}
