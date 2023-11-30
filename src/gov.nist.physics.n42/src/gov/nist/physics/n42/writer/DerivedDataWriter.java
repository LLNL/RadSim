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
import gov.nist.physics.n42.data.DerivedData;
import gov.nist.physics.n42.data.DoseRate;
import gov.nist.physics.n42.data.ExposureRate;
import gov.nist.physics.n42.data.GrossCounts;
import gov.nist.physics.n42.data.Spectrum;

/**
 *
 * @author her1
 */
public class DerivedDataWriter extends ObjectWriter<DerivedData>
{
  public DerivedDataWriter()
  {
    super(Options.NONE, "DerivedData", N42Package.getInstance());
  }

  @Override
  public void attributes(WriterAttributes attributes, DerivedData object) throws WriterException
  {
    // Register id
    if (object.getId() != null)
      attributes.add("id", object.getId());
  }

  @Override
  public void contents(DerivedData object) throws WriterException
  {
    WriterBuilder builder = newBuilder();
    WriterUtilities.writeRemarkContents(builder, object);
    builder.element("MeasurementClassCode").putString(object.getMeasurementClassCode());
    builder.element("StartDateTime").putString(object.getStartDateTime());
    builder.element("RealTimeDuration").putString(object.getRealTimeDuration());
    
    if(!object.getSpectrum().isEmpty())
    {
      WriteObject<Spectrum> wo1 = builder
        .element("Spectrum")
        .writer(new SpectrumWriter());
      for (Spectrum o : object.getSpectrum())
      {
        wo1.put(o);
      }
    }
    
    if(!object.getGrossCounts().isEmpty())
    {
      WriteObject<GrossCounts> wo2 = builder
        .element("GrossCounts")
        .writer(new GrossCountsWriter());
      for (GrossCounts o : object.getGrossCounts())
      {
        wo2.put(o);
      }
    }
    
    if(!object.getDoseRate().isEmpty())
    {
      WriteObject<DoseRate> wo3 = builder
        .element("DoseRate")
        .writer(new DoseRateWriter());
      for (DoseRate o : object.getDoseRate())
      {
        wo3.put(o);
      }
    }
    
    if(!object.getTotalDose().isEmpty())
    {
      WriteObject<DoseRate> wo4 = builder
        .element("TotalDose")
        .writer(new DoseRateWriter());
      for (DoseRate o : object.getTotalDose())
      {
        wo4.put(o);
      }
    }
    
    if(!object.getExposureRate().isEmpty())
    {
      WriteObject<ExposureRate> wo5 = builder
        .element("ExposureRate")
        .writer(new ExposureRateWriter());
      for (ExposureRate o : object.getExposureRate())
      {
        wo5.put(o);
      }
    }
    
    if(!object.getTotalExposure().isEmpty())
    {
      WriteObject<ExposureRate> wo6 = builder
        .element("TotalExposure")
        .writer(new ExposureRateWriter());
      for (ExposureRate o : object.getTotalExposure())
      {
        wo6.put(o);
      }
    }
    
    // DerivedDataExtension
  }
  
}
