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
import gov.nist.physics.n42.data.CountRate;
import gov.nist.physics.n42.data.Quantity;

/**
 *
 * @author her1
 */
public class CountRateWriter extends ObjectWriter<CountRate>
{
  public CountRateWriter()
  {
    super(ObjectWriter.Options.NONE, "CountRate", N42Package.getInstance());
  }

  @Override
  public void attributes(WriterAttributes attributes, CountRate object) throws WriterException
  {
    // Register id
    attributes.setId(object.getId());

    // Get references
    String detectorRef = WriterUtilities.getObjectReference(object.getDetector(), getContext());
    attributes.add("radDetectorInformationReference", detectorRef);

    // FIXME
    // attributes.add("radRawDoseRateReferences", "");
  }

  @Override
  public void contents(CountRate object) throws WriterException
  {
    ObjectWriter.WriterBuilder builder = newBuilder();
    WriterUtilities.writeRemarkContents(builder, object);
    builder.element("CountRateValue").contents(Quantity.class).put(object.getValue());
  }

}
