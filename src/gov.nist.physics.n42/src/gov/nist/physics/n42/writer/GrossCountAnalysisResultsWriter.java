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
import gov.nist.physics.n42.data.GrossCountAnalysisResults;
import gov.nist.physics.n42.data.Quantity;

/**
 *
 * @author her1
 */
public class GrossCountAnalysisResultsWriter extends ObjectWriter<GrossCountAnalysisResults>
{
  public GrossCountAnalysisResultsWriter()
  {
    super(Options.NONE, "GrossCountAnalysisResults", N42Package.getInstance());
  }

  @Override
  public void attributes(WriterAttributes attributes, GrossCountAnalysisResults object) throws WriterException
  {
    // Register id
    attributes.setId(object.getId());
  }

  @Override
  public void contents(GrossCountAnalysisResults object) throws WriterException
  {
    WriterBuilder builder = newBuilder();
    WriterUtilities.writeRemarkContents(builder, object);
    
    if(object.getAverageCountRate() != null)
    {
      builder.element("AverageCountRateValue").contents(Quantity.class).put(object.getAverageCountRate());
    }
    
    if(object.getAverageCountRateUncertainty() != null)
    {
      builder.element("AverageCountRateUncertaintyValue").contents(Quantity.class).put(object.getAverageCountRateUncertainty());
    }
    
    if(object.getMaximumCountRate() != null)
    {
      builder.element("MaximumCountRateValue").contents(Quantity.class).put(object.getMaximumCountRate());
    }
    
    if(object.getMinimumCountRate() != null)
    {
      builder.element("MinimumCountRateValue").contents(Quantity.class).put(object.getMinimumCountRate());
    }
    
    if(object.getTotalCounts() != null)
    {
      builder.element("TotalCountsValue").contents(Quantity.class).put(object.getTotalCounts());
    }
    
    if(object.getBackgroundCountRate() != null)
    {
      builder.element("BackgroundCountRateValue").contents(Quantity.class).put(object.getBackgroundCountRate());
    }
    
    if(object.getBackgroundCountRateUncertainty() != null)
    {
      builder.element("BackgroundCountRateUncertaintyValue").contents(Quantity.class).put(object.getBackgroundCountRateUncertainty());
    }
    
    // GrossCountAnalysisResultsExtension
  }
  
}
