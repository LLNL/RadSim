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
import gov.nist.physics.n42.data.AnalysisAlgorithmVersion;
/**
 *
 * @author her1
 */
public class AnalysisAlgorithmVersionWriter extends ObjectWriter<AnalysisAlgorithmVersion>
{
  public AnalysisAlgorithmVersionWriter()
  {
    super(Options.NONE, "AnalysisAlgorithmVersion", N42Package.getInstance());
  }

  @Override
  public void attributes(WriterAttributes attributes, AnalysisAlgorithmVersion object) throws WriterException
  {
    // Register id
    attributes.setId(object.getId());
  }

  @Override
  public void contents(AnalysisAlgorithmVersion object) throws WriterException
  {
    WriterBuilder builder = newBuilder();
    WriterUtilities.writeRemarkContents(builder, object);
    builder.element("AnalysisAlgorithmComponentName").putString(object.getComponentName());
    builder.element("AnalysisAlgorithmComponentVersion").putString(object.getComponentVersion());
  }
  
}
