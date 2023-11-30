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
import gov.nist.physics.n42.data.RadAlarm;
import java.time.Instant;

/**
 *
 * @author her1
 */
public class RadAlarmWriter extends ObjectWriter<RadAlarm>
{
  public RadAlarmWriter()
  {
    super(Options.NONE, "RadAlarm", N42Package.getInstance());
  }

  @Override
  public void attributes(WriterAttributes attributes, RadAlarm object) throws WriterException
  {
    // Register id
    attributes.setId(object.getId());

    String detectorRefs = WriterUtilities.getObjectReferences(object.getDetectors(), getContext());
    if (detectorRefs != null)
      attributes.add("radDetectorInformationReferences", detectorRefs);
  }

  @Override
  public void contents(RadAlarm object) throws WriterException
  {
    WriterBuilder builder = newBuilder();
    WriterUtilities.writeRemarkContents(builder, object);

    if (object.getDateTime() != null)
    {
      builder.element("RadAlarmDateTime")
              .contents(Instant.class)
              .put(object.getDateTime());
    }

    builder.element("RadAlarmCategoryCode").putString(object.getCategoryCode().toString());

    if (object.getDescription() != null)
    {
      builder.element("RadAlarmDescription").putString(object.getDescription());
    }

    if (object.getAudibleIndicator() != null)
    {
      builder.element("AlarmAudibleIndicator").putBoolean(object.getAudibleIndicator());
    }

    if (object.getLightColor() != null)
    {
      builder.element("RadAlarmLightColor").putString(object.getLightColor());
    }

    if (object.getEnergyWindowIndices() != null)
    {
      builder.element("RadAlarmEnergyWindowIndices")
              .contents(int[].class)
              .put(object.getEnergyWindowIndices());
    }

    // RadAlarmExtension
  }

}
