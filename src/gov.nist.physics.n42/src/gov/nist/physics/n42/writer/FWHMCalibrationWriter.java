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
import gov.nist.physics.n42.data.FWHMCalibration;
import java.time.Instant;

/**
 *
 * @author her1
 */
public class FWHMCalibrationWriter extends ObjectWriter<FWHMCalibration>
{
  public FWHMCalibrationWriter()
  {
    super(Options.NONE, "FWHMCalibration", N42Package.getInstance());
  }

  @Override
  public void attributes(WriterAttributes attributes, FWHMCalibration object) throws WriterException
  {
    // Register id
    attributes.setId(object.getId());
  }

  @Override
  public void contents(FWHMCalibration object) throws WriterException
  {
    WriterBuilder builder = newBuilder();
    WriterUtilities.writeRemarkContents(builder, object);
    
    builder.element("EnergyValues")
      .writer(new DoubleListWriter())
      .put(object.getEnergyValues());
    
    builder.element("FWHMValues")
      .writer(new DoubleListWriter())
      .put(object.getFwhmValues());
    
    if(object.getFwhmUncertaintyValues() != null)
    {
      builder.element("FWHMUncertaintyValues")
        .writer(new DoubleListWriter())
        .put(object.getFwhmUncertaintyValues());
    }
    
    if(object.getCalibrationDateTime() != null)
    {
      builder.element("CalibrationDateTime")
        .contents(Instant.class)
        .put(object.getCalibrationDateTime());
    }
  }
  
}
