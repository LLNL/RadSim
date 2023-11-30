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
import gov.nist.physics.n42.data.GrossCounts;

/**
 *
 * @author her1
 */
public class GrossCountsWriter extends ObjectWriter<GrossCounts>
{
  public GrossCountsWriter()
  {
    super(ObjectWriter.Options.NONE, "GrossCounts", N42Package.getInstance());
  }

  @Override
  public void attributes(ObjectWriter.WriterAttributes attributes, GrossCounts object) throws WriterException
  {
    // Register id
    attributes.setId(object.getId());

    String detectorRef = WriterUtilities.getObjectReference(object.getDetector(), getContext());
    attributes.add("radDetectorInformationReference", detectorRef);

    // Get references
    if (object.getEnergyWindows() != null)
    {
      String energyWindowRef = WriterUtilities.getObjectReference(object.getEnergyWindows(), getContext());
      attributes.add("energyWindowsReference", energyWindowRef);
    }
  }

  @Override
  public void contents(GrossCounts object) throws WriterException
  {
    ObjectWriter.WriterBuilder builder = newBuilder();
    WriterUtilities.writeRemarkContents(builder, object);

    builder.element("LiveTimeDuration")
            .writer(new DurationWriter())
            .put(object.getLiveTimeDuration());

    if (object.getCountData() != null)
    {
      builder.element("CountData")
              .writer(new DoubleListWriter())
              .put(object.getCountData());
    }
  }

}
