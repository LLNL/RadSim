/*
 * Copyright 2022, Lawrence Livermore National Security, LLC.
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.nist.physics.n42.writer;

import gov.llnl.utility.io.WriterException;
import gov.llnl.utility.xml.bind.ObjectWriter;
import gov.nist.physics.n42.N42Package;
import gov.nist.physics.n42.data.RadInstrumentVersion;

/**
 *
 * @author her1
 */
public class RadInstrumentVersionWriter extends ObjectWriter<RadInstrumentVersion>
{
  public RadInstrumentVersionWriter()
  {
    super(Options.NONE, "RadInstrumentVersion", N42Package.getInstance());
  }

  @Override
  public void attributes(WriterAttributes attributes, RadInstrumentVersion object) throws WriterException
  {
  }

  @Override
  public void contents(RadInstrumentVersion object) throws WriterException
  {
    WriterBuilder builder = newBuilder();
    WriterUtilities.writeRemarkContents(builder, object);
    builder.element("RadInstrumentComponentName").putString(object.getComponentName());
    builder.element("RadInstrumentComponentVersion").putString(object.getComponentVersion());
  }
 
}
