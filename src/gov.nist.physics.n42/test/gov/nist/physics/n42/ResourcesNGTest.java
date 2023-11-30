/*
 * Copyright 2022, Lawrence Livermore National Security, LLC.
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.nist.physics.n42;

import gov.llnl.utility.xml.bind.DocumentReader;
import gov.nist.physics.n42.data.RadInstrumentData;
import java.nio.file.Paths;
import org.testng.annotations.Test;

/**
 *
 * @author nelson85
 */
public class ResourcesNGTest
{

  @Test
  public void testAlarmFile() throws Exception
  {
    DocumentReader<RadInstrumentData> dr = DocumentReader.create(RadInstrumentData.class);
    RadInstrumentData rid = dr.loadFile(Paths.get("test/gov/nist/physics/n42/resources/Alarm.xml"));
  }

  @Test
  public void testMobileFile() throws Exception
  {
    DocumentReader<RadInstrumentData> dr = DocumentReader.create(RadInstrumentData.class);
    RadInstrumentData rid = dr.loadFile(Paths.get("test/gov/nist/physics/n42/resources/Mobile.xml"));
  }

  @Test
  public void testNeutronFile() throws Exception
  {
    DocumentReader<RadInstrumentData> dr = DocumentReader.create(RadInstrumentData.class);
    RadInstrumentData rid = dr.loadFile(Paths.get("test/gov/nist/physics/n42/resources/Neutron.xml"));
  }

  @Test
  public void testNuclideIdentifierFile() throws Exception
  {
    DocumentReader<RadInstrumentData> dr = DocumentReader.create(RadInstrumentData.class);
    RadInstrumentData rid = dr.loadFile(Paths.get("test/gov/nist/physics/n42/resources/NuclideIdentifier.xml"));
  }

  @Test
  public void testPRDFile() throws Exception
  {
    DocumentReader<RadInstrumentData> dr = DocumentReader.create(RadInstrumentData.class);
    RadInstrumentData rid = dr.loadFile(Paths.get("test/gov/nist/physics/n42/resources/PRD.xml"));
  }

  @Test
  public void testSRPMFile() throws Exception
  {
    DocumentReader<RadInstrumentData> dr = DocumentReader.create(RadInstrumentData.class);
    RadInstrumentData rid = dr.loadFile(Paths.get("test/gov/nist/physics/n42/resources/SRPM.xml"));
  }

  @Test
  public void testSimpleSpectrometerFile() throws Exception
  {
    DocumentReader<RadInstrumentData> dr = DocumentReader.create(RadInstrumentData.class);
    RadInstrumentData rid = dr.loadFile(Paths.get("test/gov/nist/physics/n42/resources/SimpleSpectrometer.xml"));
  }

  
  @Test
  public void testSimpleSpectrometer2File() throws Exception
  {
    DocumentReader<RadInstrumentData> dr = DocumentReader.create(RadInstrumentData.class);
    RadInstrumentData rid = dr.loadFile(Paths.get("test/gov/nist/physics/n42/resources/SimpleSpectrometer2.xml"));
  }

}
