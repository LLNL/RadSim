/*
 * Copyright 2022, Lawrence Livermore National Security, LLC.
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.nist.physics.n42;

import gov.llnl.utility.io.ReaderException;
import gov.llnl.utility.xml.bind.DocumentReader;
import gov.nist.physics.n42.data.RadInstrumentData;
import java.io.IOException;
import java.nio.file.Paths;
import static org.testng.Assert.assertNotNull;
import org.testng.annotations.Test;

/**
 *
 * @author nelson85
 */
public class VendorNGTest
{
  @Test
  public void testRSI() throws ReaderException, IOException
  {
    DocumentReader<RadInstrumentData> dr = DocumentReader.create(RadInstrumentData.class);
    RadInstrumentData rid = dr.loadFile(Paths.get("test/gov/nist/physics/n42/resources/vendor/rsi.n42"));
    assertNotNull(rid);
  }
}
