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

/**
 *
 * @author her1
 */
public class SpectrumPeakWriter extends ObjectWriter<SpectrumPeak>
{
  public SpectrumPeakWriter()
  {
    super(Options.NONE, "SpectrumPeak", N42Package.getInstance());
  }

  @Override
  public void attributes(WriterAttributes attributes, SpectrumPeak object) throws WriterException
  {
  }

  @Override
  public void contents(SpectrumPeak object) throws WriterException
  {
    WriterBuilder builder = newBuilder();
    WriterUtilities.writeRemarkContents(builder, object);

    if(object.getEnergy() != null)
    {
      builder.element("SpectrumPeakEnergyValue").putDouble(object.getEnergy());
    }

    if(object.getNetArea() != null)
    {
      builder.element("SpectrumPeakNetAreaValue").putDouble(object.getNetArea());
    }

    builder.element("SpectrumPeakNetAreaUncertaintyValue").putDouble(object.getNetAreaUncertainty());
    
    // SpectrumPeakExtension
  }
  
}
