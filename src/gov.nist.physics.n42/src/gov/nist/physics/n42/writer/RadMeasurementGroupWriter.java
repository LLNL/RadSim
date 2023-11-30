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
import gov.nist.physics.n42.data.RadMeasurementGroup;

/**
 *
 * @author her1
 */
public class RadMeasurementGroupWriter extends ObjectWriter<RadMeasurementGroup>
{
  public RadMeasurementGroupWriter()
  {
    super(Options.NONE, "RadMeasurementGroup", N42Package.getInstance());
  }

  @Override
  public void attributes(WriterAttributes attributes, RadMeasurementGroup object) throws WriterException
  {
    // Register id
    attributes.setId(object.getId());

    // FIXME
    if (object.getUUID() != null)
    {
      attributes.add("radMeasurementGroupUUID", object.getUUID());
    }
  }

  @Override
  public void contents(RadMeasurementGroup object) throws WriterException
  {
    WriterBuilder builder = newBuilder();
    WriterUtilities.writeRemarkContents(builder, object);

    if (object.getDescription() != null)
    {
      builder.element("RadMeasurementGroupDescription").putString(object.getDescription());
    }
  }

}
