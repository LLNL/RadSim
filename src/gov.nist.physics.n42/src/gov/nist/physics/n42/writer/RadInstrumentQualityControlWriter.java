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
import gov.nist.physics.n42.data.RadInstrumentQualityControl;
import java.time.Instant;

/**
 *
 * @author her1
 */
public class RadInstrumentQualityControlWriter extends ObjectWriter<RadInstrumentQualityControl>
{
  public RadInstrumentQualityControlWriter()
  {
    super(Options.NONE, "RadInstrumentQualityControl", N42Package.getInstance());
  }
  
  @Override
  public void attributes(WriterAttributes attributes, RadInstrumentQualityControl object) throws WriterException
  {
    // Register id
    attributes.setId(object.getId());
  }

  @Override
  public void contents(RadInstrumentQualityControl object) throws WriterException
  {
    WriterBuilder builder = newBuilder();
    WriterUtilities.writeRemarkContents(builder, object);
    
    builder.element("InspectionDateTime")
      .contents(Instant.class)
      .put(object.getInspectionDateTime());
    
    builder.element("InCalibrationIndicator").putBoolean(object.isInCalibrationIndicator());
  }
  
}
