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
import gov.nist.physics.n42.data.DoseAnalysisResults;
import gov.nist.physics.n42.data.Quantity;

/**
 *
 * @author her1
 */
public class DoseAnalysisResultsWriter extends ObjectWriter<DoseAnalysisResults>
{
  public DoseAnalysisResultsWriter()
  {
    super(Options.NONE, "DoseAnalysisResults", N42Package.getInstance());
  }

  @Override
  public void attributes(WriterAttributes attributes, DoseAnalysisResults object) throws WriterException
  {
    // Register id
    attributes.setId(object.getId());
  }

  @Override
  public void contents(DoseAnalysisResults object) throws WriterException
  {
    WriterBuilder builder = newBuilder();
    WriterUtilities.writeRemarkContents(builder, object);
    
    if(object.getAverageDoseRate() != null)
    {
      builder.element("AverageDoseRateValue").contents(Quantity.class).put(object.getAverageDoseRate());
    }
    
    if(object.getAverageDoseRateUncertainty() != null)
    {
      builder.element("AverageDoseRateUncertaintyValue").contents(Quantity.class).put(object.getAverageDoseRateUncertainty());
    }
    
    if(object.getMaximumDoseRate() != null)
    {
      builder.element("MaximumDoseRateValue").contents(Quantity.class).put(object.getMaximumDoseRate());
    }
    
    if(object.getMinimumDoseRate() != null)
    {
      builder.element("MinimumDoseRateValue").contents(Quantity.class).put(object.getMinimumDoseRate());
    }
    
    if(object.getBackgroundDoseRate() != null)
    {
      builder.element("BackgroundDoseRateValue").contents(Quantity.class).put(object.getBackgroundDoseRate());
    }
    
    if(object.getBackgroundDoseRateUncertainty() != null)
    {
      builder.element("BackgroundDoseRateUncertaintyValue").contents(Quantity.class).put(object.getBackgroundDoseRateUncertainty());
    }
    
    if(object.getTotalDose() != null)
    {
      builder.element("TotalDoseValue").contents(Quantity.class).put(object.getTotalDose()); 
    }
    
    // DoseAnalysisResultsExtension
  }
  
}
