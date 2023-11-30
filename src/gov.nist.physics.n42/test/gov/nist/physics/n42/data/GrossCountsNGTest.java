/*
 * Copyright 2022, Lawrence Livermore National Security, LLC.
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.nist.physics.n42.data;

import gov.llnl.utility.ArrayEncoding;
import gov.llnl.utility.io.ReaderException;
import gov.llnl.utility.xml.bind.DocumentReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.text.ParseException;
import java.time.Duration;
import java.util.function.Consumer;
import static org.testng.Assert.*;
import org.testng.annotations.Test;

/**
 *
 * @author nelson85
 */
public class GrossCountsNGTest
{
  
  public GrossCountsNGTest()
  {
  }
  
    public GrossCounts newInstance()
  {
    try
    {
      DocumentReader<RadInstrumentData> dr = DocumentReader.create(RadInstrumentData.class);
      RadInstrumentData rid = dr.loadFile(Paths.get("test/gov/nist/physics/n42/resources/Alarm.xml"));
      return rid.getMeasurements().get(0).getGrossCounts().get(0);
    }
    catch (ReaderException | IOException ex)
    {
      throw new RuntimeException(ex);
    }
  }

  @Test
  public void testGetDetector()
  {
    GrossCounts instance = newInstance();
    RadDetectorInformation result = instance.getDetector();
    assertNotNull(result);
  }

  @Test
  public void testSetDetector()
  {
    RadDetectorInformation detector = new RadDetectorInformation();
    GrossCounts instance = new GrossCounts();
    instance.setDetector(detector);
    assertSame(instance.getDetector(), detector);
  }

  @Test
  public void testGetLiveTimeDuration()
  {
    GrossCounts instance = newInstance();
    Duration expResult = Duration.ofMillis(100);
    Duration result = instance.getLiveTimeDuration();
    assertEquals(result, expResult);
  }

  @Test
  public void testSetLiveTimeDuration()
  {
    Duration duration = Duration.ofSeconds(300);
    GrossCounts instance = new GrossCounts();
    instance.setLiveTimeDuration(duration);
    assertSame(instance.getLiveTimeDuration(), duration);
  }

  @Test
  public void testGetCountData() throws ParseException
  {
    GrossCounts instance = newInstance();
    double[] result = instance.getCountData();
    assertEquals(result, ArrayEncoding.decodeDoubles("eJyLdrFmYGBgdhBhAAMHMyidCaEBLesCUQ=="));
  }

  @Test
  public void testSetCountData()
  {
    double[] counts = new double[]{1,2,3};
    GrossCounts instance = new GrossCounts();
    instance.setCountData(counts);
    assertSame(instance.getCountData(), counts);
  }

  /**
   * Test of setEnergyWindows method, of class GrossCounts.
   */
  @Test
  public void testSetEnergyWindows()
  {
    System.out.println("setEnergyWindows");
    EnergyWindows energyWindows = null;
    GrossCounts instance = new GrossCounts();
    instance.setEnergyWindows(energyWindows);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of getEnergyWindows method, of class GrossCounts.
   */
  @Test
  public void testGetEnergyWindows()
  {
    System.out.println("getEnergyWindows");
    GrossCounts instance = new GrossCounts();
    EnergyWindows expResult = null;
    EnergyWindows result = instance.getEnergyWindows();
    assertEquals(result, expResult);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of visitReferencedObjects method, of class GrossCounts.
   */
  @Test
  public void testVisitReferencedObjects()
  {
    System.out.println("visitReferencedObjects");
    Consumer<ComplexObject> visitor = null;
    GrossCounts instance = new GrossCounts();
    instance.visitReferencedObjects(visitor);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }
  
}
