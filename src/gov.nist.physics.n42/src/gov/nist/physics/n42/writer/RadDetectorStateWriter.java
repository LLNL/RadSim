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
import gov.nist.physics.n42.data.Characteristics;
import gov.nist.physics.n42.data.Fault;
import gov.nist.physics.n42.data.RadDetectorState;

/**
 *
 * @author her1
 */
public class RadDetectorStateWriter extends ObjectWriter<RadDetectorState>
{
  public RadDetectorStateWriter()
  {
    super(Options.NONE, "RadDetectorState", N42Package.getInstance());
  }

  @Override
  public void attributes(WriterAttributes attributes, RadDetectorState object) throws WriterException
  {
    // Register id
    attributes.setId(object.getId());

    // Get references
    String detectorRef = WriterUtilities.getObjectReference(object.getDetector(), getContext());
    attributes.add("radDetectorInformationReference", detectorRef);
  }

  @Override
  public void contents(RadDetectorState object) throws WriterException
  {
    WriterBuilder builder = newBuilder();
    WriterUtilities.writeRemarkContents(builder, object);

    if (object.getState() != null)
    {
      builder.element("StateVector").writer(new StateVectorWriter()).put(object.getState());
    }

    if (!object.getFaults().isEmpty())
    {
      WriteObject<Fault> wo1 = builder
              .element("Fault")
              .writer(new FaultWriter());
      wo1.putAll(object.getFaults());
    }

    if (!object.getCharacteristics().isEmpty())
    {
      WriteObject<Characteristics> wo2 = builder
              .element("RadDetectorCharacteristics")
              .writer(new CharacteristicsWriter());
      wo2.putAll(object.getCharacteristics());
    }

    // RadDetectorStateExtension
  }

}
