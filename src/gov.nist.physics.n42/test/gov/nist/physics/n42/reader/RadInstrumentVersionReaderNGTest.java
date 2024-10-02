/*
 * Copyright 2022, Lawrence Livermore National Security, LLC.
 * All rights reserved
 *
 * Terms and conditions are given in "Notice" file.
 */
package gov.nist.physics.n42.reader;

import gov.llnl.utility.xml.bind.ReaderContext;
import gov.nist.physics.n42.data.RadInstrumentVersion;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;
import org.testng.annotations.Test;

/**
 *
 * @author nelson85
 */
public class RadInstrumentVersionReaderNGTest
{

  public RadInstrumentVersionReaderNGTest()
  {
  }

  @Test
  public void testStart()
  {
    // Tested end to end.
  }

  @Test
  public void testGetHandlers() throws Exception
  {
    // Tested end to end.
  }

  /**
   * Test of end method, of class RadInstrumentVersionReader.
   */
  @Test
  public void testEnd()
  {
    System.out.println("end");
    ReaderContext context = null;
    RadInstrumentVersionReader instance = new RadInstrumentVersionReader();
    RadInstrumentVersion expResult = null;
    RadInstrumentVersion result = instance.end(context);
    assertEquals(result, expResult);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

}
