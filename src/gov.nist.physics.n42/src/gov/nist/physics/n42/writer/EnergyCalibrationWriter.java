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
import gov.nist.physics.n42.data.EnergyCalibration;
import java.time.Instant;

/**
 *
 * @author her1
 */
public class EnergyCalibrationWriter extends ObjectWriter<EnergyCalibration>
{
  public EnergyCalibrationWriter()
  {
    super(Options.NONE, "EnergyCalibration", N42Package.getInstance());
  }

  @Override
  public void attributes(WriterAttributes attributes, EnergyCalibration object) throws WriterException
  {
    // Register id
    attributes.setId(object.getId());
  }

  @Override
  public void contents(EnergyCalibration object) throws WriterException
  {
    WriterBuilder builder = newBuilder();
    WriterUtilities.writeRemarkContents(builder, object);
    
    if(object.getCoefficients() != null)
    {
      builder.element("CoefficientValues")
      .writer(new DoubleListWriter())
      .put(object.getCoefficients());
    }
    else if(object.getEnergyBoundaries() != null)
    {
      builder.element("EnergyBoundaryValues")
      .writer(new DoubleListWriter())
      .put(object.getEnergyBoundaries());
    }
    else if(object.getEnergy() != null)
    {
      builder.element("EnergyValues")
        .writer(new DoubleListWriter())
        .put(object.getEnergy());
    }
    else if(object.getEnergyDeviation() != null)
    {
      builder.element("EnergyDeviationValues")
        .writer(new DoubleListWriter())
        .put(object.getEnergyDeviation());
    }
    
    if(object.getCalibrationDateTime() != null)
    {
      builder.element("CalibrationDateTime")
        .contents(Instant.class)
        .put(object.getCalibrationDateTime());
    }
  }
  
}
