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
import gov.nist.physics.n42.data.Nuclide;
import gov.nist.physics.n42.data.NuclideAnalysisResults;

/**
 *
 * @author her1
 */
public class NuclideAnalysisResultsWriter extends ObjectWriter<NuclideAnalysisResults>
{
  public NuclideAnalysisResultsWriter()
  {
    super(Options.NONE, "NuclideAnalysisResults", N42Package.getInstance());
  }

  @Override
  public void attributes(WriterAttributes attributes, NuclideAnalysisResults object) throws WriterException
  {
    // Register id
    attributes.setId(object.getId());
  }

  @Override
  public void contents(NuclideAnalysisResults object) throws WriterException
  {
    WriterBuilder builder = newBuilder();
    WriterUtilities.writeRemarkContents(builder, object);
    
    if(!object.getNuclides().isEmpty())
    {
      WriteObject<Nuclide> wo = builder
        .element("Nuclide")
        .writer(new NuclideWriter());
      for (Nuclide o : object.getNuclides())
      {
        wo.put(o);
      }
    }
    
    // NuclideAnalysisResultsExtension
  }

}
