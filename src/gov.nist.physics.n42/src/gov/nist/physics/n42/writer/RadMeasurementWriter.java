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
import gov.nist.physics.n42.data.DoseRate;
import gov.nist.physics.n42.data.GrossCounts;
import gov.nist.physics.n42.data.RadDetectorState;
import gov.nist.physics.n42.data.RadItemState;
import gov.nist.physics.n42.data.RadMeasurement;
import gov.nist.physics.n42.data.Spectrum;
import java.time.Instant;

/**
 *
 * @author her1
 */
public class RadMeasurementWriter extends ObjectWriter<RadMeasurement>
{
  public RadMeasurementWriter()
  {
    super(Options.NONE, "RadMeasurement", N42Package.getInstance());
  }

  @Override
  public void attributes(WriterAttributes attributes, RadMeasurement object) throws WriterException
  {
    // Register id
    attributes.setId(object.getId());

    String measurementGroup = WriterUtilities.getObjectReferences(object.getRadMeasurementGroups(), getContext());
    if (measurementGroup != null)
      attributes.add("radMeasurementGroupReferences", measurementGroup);
  }

  @Override
  public void contents(RadMeasurement object) throws WriterException
  {
    WriterBuilder builder = newBuilder();
    WriterUtilities.writeRemarkContents(builder, object);

    if (object.getClassCode() != null)
      builder.element("MeasurementClassCode")
              .contents(String.class)
              .put(object.getClassCode().toString());

    if (object.getStartDateTime() != null)
      builder.element("StartDateTime")
              .contents(Instant.class)
              .put(object.getStartDateTime());

    if (object.getRealTimeDuration() != null)
      builder.element("RealTimeDuration")
              .writer(new DurationWriter())
              .put(object.getRealTimeDuration());

    if (object.getSpectrum() != null)
    {
      WriteObject<Spectrum> wo1 = builder
              .element("Spectrum")
              .writer(new SpectrumWriter());
      wo1.putAll(object.getSpectrum());
    }

    if (object.getGrossCounts() != null)
    {
      WriteObject<GrossCounts> wo2 = builder
              .element("GrossCounts")
              .writer(new GrossCountsWriter());
      wo2.putAll(object.getGrossCounts());
    }

    if (object.getDoseRate() != null)
    {
      WriteObject<DoseRate> wo3 = builder
              .element("DoseRate")
              .writer(new DoseRateWriter());
      wo3.putAll(object.getDoseRate());
    }

    if (object.getInstrumentState() != null)
    {
      builder.element("RadInstrumentState").writer(new RadInstrumentStateWriter()).put(object.getInstrumentState());
    }

    if (object.getDetectorState() != null)
    {
      WriteObject<RadDetectorState> wo4 = builder
              .element("RadDetectorState")
              .writer(new RadDetectorStateWriter());
      wo4.putAll(object.getDetectorState());
    }

    if (object.getItemState() != null)
    {
      WriteObject<RadItemState> wo5 = builder
              .element("RadItemState")
              .writer(new RadItemStateWriter());
      wo5.putAll(object.getItemState());
    }

    if (object.getOccupancy() != null)
    {
      builder.element("OccupancyIndicator").putInteger(object.getOccupancy() ? 1 : 0);
    }

    // RadMeasurementExtension
  }

}
