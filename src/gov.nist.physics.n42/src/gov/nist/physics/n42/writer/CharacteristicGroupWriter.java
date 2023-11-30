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
import gov.nist.physics.n42.data.CharacteristicGroup;

/**
 *
 * @author her1
 */
public class CharacteristicGroupWriter extends ObjectWriter<CharacteristicGroup>
{
  public CharacteristicGroupWriter()
  {
    super(Options.NONE, "CharacteristicGroup", N42Package.getInstance());
  }

  @Override
  public void attributes(WriterAttributes attributes, CharacteristicGroup object) throws WriterException
  {
    // Register id
    attributes.setId(object.getId());
    
    // groupOutOfLimits
  }

  @Override
  public void contents(CharacteristicGroup object) throws WriterException
  {
    WriterBuilder builder = newBuilder();
    WriterUtilities.writeRemarkContents(builder, object);
    builder.element("CharacteristicGroupName").putString(object.getName());
    
    if(object.getCharacteristics() != null)
    {
      WriteObject<Characteristic> wo = builder
        .element("Characteristic")
        .writer(new CharacteristicWriter());
      for (Characteristic o : object.getCharacteristics())
      {
        wo.put(o);
      }
    }
  }
  
}
