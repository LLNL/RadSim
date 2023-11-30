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
import gov.nist.physics.n42.data.RadInstrumentState;

/**
 *
 * @author her1
 */
public class RadInstrumentStateWriter extends ObjectWriter<RadInstrumentState>
{
  public RadInstrumentStateWriter()
  {
    super(Options.NONE, "RadInstrumentState", N42Package.getInstance());
  }

  @Override
  public void attributes(WriterAttributes attributes, RadInstrumentState object) throws WriterException
  {
    // Register id
    attributes.setId(object.getId());

    // Get references
    String instrumentRef = WriterUtilities.getObjectReference(object.getInstrument(), getContext());
    attributes.add("radInstrumentInformationReference", instrumentRef);
  }

  @Override
  public void contents(RadInstrumentState object) throws WriterException
  {
    WriterBuilder builder = newBuilder();
    WriterUtilities.writeRemarkContents(builder, object);

    if (object.getModeCode() != null)
    {
      builder.element("RadInstrumentModeCode").putString(object.getModeCode());
    }

    if (object.getModeDescription() != null)
    {
      builder.element("RadInstrumentModeDescription").putString(object.getModeDescription());
    }

    if (object.getStateVector() != null)
    {
      builder.element("StateVector").writer(new StateVectorWriter()).put(object.getStateVector());
    }

    if (object.getFaults() != null)
    {
      WriteObject<Fault> wo1 = builder
              .element("Fault")
              .writer(new FaultWriter());
      wo1.putAll(object.getFaults());
    }

    if (object.getInstrumentCharacteristics() != null)
    {
      WriteObject<Characteristics> wo2 = builder
              .element("RadInstrumentCharacteristics")
              .writer(new CharacteristicsWriter());
      wo2.putAll(object.getInstrumentCharacteristics());
    }

    // RadInstrumentStateExtension
  }

}
