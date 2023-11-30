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
import gov.nist.physics.n42.data.SpectrumPeak;
import gov.nist.physics.n42.data.SpectrumPeakAnalysisResults;

/**
 *
 * @author her1
 */
public class SpectrumPeakAnalysisResultsWriter extends ObjectWriter<SpectrumPeakAnalysisResults>
{
  public SpectrumPeakAnalysisResultsWriter()
  {
    super(Options.NONE, "SpectrumPeakAnalysisResults", N42Package.getInstance());
  }

  @Override
  public void attributes(WriterAttributes attributes, SpectrumPeakAnalysisResults object) throws WriterException
  {
  }

  @Override
  public void contents(SpectrumPeakAnalysisResults object) throws WriterException
  {
    WriterBuilder builder = newBuilder();
    WriterUtilities.writeRemarkContents(builder, object);
    
    WriteObject<SpectrumPeak> wo = builder
      .element("SpectrumPeak")
      .writer(new SpectrumPeakWriter());
    for (SpectrumPeak o : object.getPeaks())
    {
      wo.put(o);
    }
    
    // SpectrumPeakAnalysisResultsExtension
  }
 
}