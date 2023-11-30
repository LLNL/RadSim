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
import gov.nist.physics.n42.data.Characteristic;

/**
 *
 * @author her1
 */
public class CharacteristicWriter extends ObjectWriter<Characteristic>
{
  public CharacteristicWriter()
  {
    super(Options.NONE, "Characteristic", N42Package.getInstance());
  }

  @Override
  public void attributes(WriterAttributes attributes, Characteristic object) throws WriterException
  {
    // Register id
    attributes.setId(object.getId());

    // valueOutOfLimit
  }

  @Override
  public void contents(Characteristic object) throws WriterException
  {
    WriterBuilder builder = newBuilder();
    WriterUtilities.writeRemarkContents(builder, object);
    builder.element("CharacteristicName").putString(object.getName());
    builder.element("CharacteristicValue").putString(object.getValue());
    builder.element("CharacteristicValueUnits").putString(object.getValueUnits());
    builder.element("CharacteristicValueDataClassCode").putString(object.getValueDataClassCode());
  }

}
