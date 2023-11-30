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
import gov.nist.physics.n42.data.ExposureRate;
import gov.nist.physics.n42.data.Quantity;

/**
 *
 * @author her1
 */
public class ExposureRateWriter extends ObjectWriter<ExposureRate>
{
  public ExposureRateWriter()
  {
    super(Options.NONE, "ExposureRate", N42Package.getInstance());
  }

  @Override
  public void attributes(WriterAttributes attributes, ExposureRate object) throws WriterException
  {
    // Register id
    attributes.setId(object.getId());
   
    // Get references
    String detectorRef = WriterUtilities.getObjectReference(object.getDetector(), getContext());
    attributes.add("radDetectorInformationReference", detectorRef);
    
    // FIXME
    // attributes.add("radRawExposureRateReferences", "");
  }

  @Override
  public void contents(ExposureRate object) throws WriterException
  {
    WriterBuilder builder = newBuilder();
    WriterUtilities.writeRemarkContents(builder, object);
    builder.element("ExposureRateValue").contents(Quantity.class).put(object.getValue());
    
    if(object.getLevelDescription() != null)
    {
      builder.element("ExposureRateLevelDescription").putString(object.getLevelDescription());
    }
  }
  
}
