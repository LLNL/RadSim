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
import gov.nist.physics.n42.data.Fault;

/**
 *
 * @author her1
 */
public class FaultWriter extends ObjectWriter<Fault>
{
  public FaultWriter()
  {
    super(Options.NONE, "Fault", N42Package.getInstance());
  }

  @Override
  public void attributes(WriterAttributes attributes, Fault object) throws WriterException
  {
    // Register id
    attributes.setId(object.getId());
  }

  @Override
  public void contents(Fault object) throws WriterException
  {
    WriterBuilder builder = newBuilder();
    WriterUtilities.writeRemarkContents(builder, object);
    builder.element("FaultCodeValue").putString(object.getCode());
    builder.element("FaultDescription").putString(object.getDescription());
    builder.element("FaultSeverityCode").putString(object.getSeverityCode().toString());
    
    // FaultExtension
  }
  
}
