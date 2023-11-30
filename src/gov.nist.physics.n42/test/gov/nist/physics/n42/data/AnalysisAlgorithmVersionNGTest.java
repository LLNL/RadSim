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
import static org.testng.Assert.*;
import org.testng.annotations.Test;

/**
 *
 * @author nelson85
 */
public class AnalysisAlgorithmVersionNGTest
{

  public AnalysisAlgorithmVersionNGTest()
  {
  }

  public AnalysisAlgorithmVersion newInstance()
  {
    try
    {
      DocumentReader<RadInstrumentData> dr = DocumentReader.create(RadInstrumentData.class);
      RadInstrumentData rid = dr.loadFile(Paths.get("test/gov/nist/physics/n42/resources/SimpleSpectrometer2.xml"));
      return rid.getAnalysisResults().get(0).getAlgorithmVersion().get(0);
    }
    catch (ReaderException | IOException ex)
    {
      throw new RuntimeException(ex);
    }
  }

  @Test
  public void testGetComponentName()
  {
    AnalysisAlgorithmVersion instance = newInstance();
    String expResult = "Main";
    String result = instance.getComponentName();
    assertEquals(result, expResult);
  }

  @Test
  public void testSetComponentName()
  {
    String componentName = "test";
    AnalysisAlgorithmVersion instance = new AnalysisAlgorithmVersion();
    instance.setComponentName(componentName);
    assertEquals(instance.getComponentName(), componentName);
  }

  @Test
  public void testGetComponentVersion()
  {
    AnalysisAlgorithmVersion instance = newInstance();
    String expResult = "1.0";
    String result = instance.getComponentVersion();
    assertEquals(result, expResult);
  }

  @Test
  public void testSetComponentVersion()
  {
    String componentVersion = "test";
    AnalysisAlgorithmVersion instance = new AnalysisAlgorithmVersion();
    instance.setComponentVersion(componentVersion);
    assertEquals(instance.getComponentVersion(), componentVersion);
  }

}
