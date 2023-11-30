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
import gov.nist.physics.n42.data.EfficiencyCalibration;
import java.time.Instant;

/**
 *
 * @author her1
 */
public class EfficiencyCalibrationWriter extends ObjectWriter<EfficiencyCalibration>
{
  public EfficiencyCalibrationWriter()
  {
    super(Options.NONE, "EfficiencyCalibration", N42Package.getInstance());
  }

  @Override
  public void attributes(WriterAttributes attributes, EfficiencyCalibration object) throws WriterException
  {
    // Register id
    attributes.setId(object.getId());
  }

  @Override
  public void contents(EfficiencyCalibration object) throws WriterException
  {
    WriterBuilder builder = newBuilder();
    WriterUtilities.writeRemarkContents(builder, object);
    
    builder.element("EnergyValues")
      .writer(new DoubleListWriter())
      .put(object.getEnergyValues());
    
    builder.element("EfficiencyValues")
      .writer(new DoubleListWriter())
      .put(object.getEfficiencyValues());
    
    if(object.getEfficiencyUncertaintyValues() != null)
    {
      builder.element("EfficiencyUncertaintyValues")
        .writer(new DoubleListWriter())
        .put(object.getEfficiencyUncertaintyValues());
    }
    
    if(object.getCalibrationDateTime() != null)
    {
      builder.element("CalibrationDateTime")
        .contents(Instant.class)
        .put(object.getCalibrationDateTime());
    }
  }
  
}
