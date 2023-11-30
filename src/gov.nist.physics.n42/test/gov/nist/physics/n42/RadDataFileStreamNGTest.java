/*
 * Copyright 2022, Lawrence Livermore National Security, LLC.
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.nist.physics.n42;

import gov.nist.physics.n42.data.RadInstrumentData;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import static org.testng.Assert.*;
import org.testng.annotations.Test;

/**
 *
 * @author nelson85
 */
public class RadDataFileStreamNGTest
{
  
  public RadDataFileStreamNGTest()
  {
  }

  @Test
  public void testOpen() throws Exception
  {
    Path file = Paths.get("test/gov/nist/physics/n42/resources/SimpleSpectrometer.xml");
    RadDataFileStream instance = new RadDataFileStream();
    instance.open(file);
  }

  @Test
  public void testClose() throws Exception
  {
    Path file = Paths.get("test/gov/nist/physics/n42/resources/SimpleSpectrometer.xml");
    RadDataFileStream instance = new RadDataFileStream();
    instance.open(file);
    instance.close();
  }

  @Test
  public void testHasNext() throws IOException
  {
    Path file = Paths.get("test/gov/nist/physics/n42/resources/SimpleSpectrometer.xml");
    RadDataFileStream instance = new RadDataFileStream();
    instance.open(file);
    boolean expResult = true;
    boolean result = instance.hasNext();
    assertEquals(result, expResult);
  }

  @Test
  public void testNext() throws IOException
  {
    Path file = Paths.get("test/gov/nist/physics/n42/resources/SimpleSpectrometer.xml");
    RadDataFileStream instance = new RadDataFileStream();
    instance.open(file);
    RadInstrumentData result = instance.next();
    assertNotNull(result);
  }

  @Test
  public void testGetNextBuffer() throws Exception
  {
    Path file = Paths.get("test/gov/nist/physics/n42/resources/SimpleSpectrometer.xml");
    RadDataFileStream instance = new RadDataFileStream();
    instance.open(file);
    List result = instance.getNextBuffer();
    assertNotNull(result);
  }
  
}
