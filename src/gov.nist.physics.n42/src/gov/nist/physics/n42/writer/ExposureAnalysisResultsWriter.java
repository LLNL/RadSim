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
import gov.nist.physics.n42.data.ExposureAnalysisResults;
import gov.nist.physics.n42.data.Quantity;

/**
 *
 * @author her1
 */
public class ExposureAnalysisResultsWriter extends ObjectWriter<ExposureAnalysisResults>
{
  public ExposureAnalysisResultsWriter()
  {
    super(Options.NONE, "ExposureAnalysisResults", N42Package.getInstance());
  }

  @Override
  public void attributes(WriterAttributes attributes, ExposureAnalysisResults object) throws WriterException
  {
    // Register id
    attributes.setId(object.getId());
  }

  @Override
  public void contents(ExposureAnalysisResults object) throws WriterException
  {
    WriterBuilder builder = newBuilder();
    WriterUtilities.writeRemarkContents(builder, object);
    
    if(object.getAverageExposureRate() != null)
    {
      builder.element("AverageExposureRateValue").contents(Quantity.class).put(object.getAverageExposureRate());
    }
    
    if(object.getAverageExposureRateUncertainty() != null)
    {
      builder.element("AverageExposureRateUncertaintyValue").contents(Quantity.class).put(object.getAverageExposureRateUncertainty());
    }
    
    if(object.getMaximumExposureRate() != null)
    {
      builder.element("MaximumExposureRateValue").contents(Quantity.class).put(object.getMaximumExposureRate());
    }
    
    if(object.getMinimumExposureRate() != null)
    {
      builder.element("MinimumExposureRateValue").contents(Quantity.class).put(object.getMinimumExposureRate());
    }
    
    if(object.getBackgroundExposureRate() != null)
    {
      builder.element("BackgroundExposureRateValue").contents(Quantity.class).put(object.getBackgroundExposureRate());
    }
    
    if(object.getBackgroundExposureRateUncertainty() != null)
    {
      builder.element("BackgroundExposureRateUncertaintyValue").contents(Quantity.class).put(object.getBackgroundExposureRateUncertainty());
    }
    
    if(object.getTotalExposure() != null)
    {
      builder.element("TotalExposureValue").contents(Quantity.class).put(object.getTotalExposure());
    }

    // ExposureAnalysisResultsExtension
  }
  
}
