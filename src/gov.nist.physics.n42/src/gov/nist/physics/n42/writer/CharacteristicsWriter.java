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
import gov.nist.physics.n42.data.Characteristics;

/**
 *
 * @author her1
 */
public class CharacteristicsWriter extends ObjectWriter<Characteristics>
{
  public CharacteristicsWriter()
  {
    super(Options.NONE, "Characteristics", N42Package.getInstance());
  }
  
  @Override
  public void attributes(WriterAttributes attributes, Characteristics object) throws WriterException
  {
    // Register id
    attributes.setId(object.getId());
  }

  @Override
  public void contents(Characteristics object) throws WriterException
  {
    WriterBuilder builder = newBuilder();
    
    if(object.getCharacteristics() != null)
    {
      WriteObject<Characteristic> wo1 = builder
        .element("Characteristic")
        .writer(new CharacteristicWriter());
      for (Characteristic c : object.getCharacteristics())
      {
        wo1.put(c);
      }
    }
    
    if(object.getCharacteristicGroups() != null)
    {
      WriteObject<CharacteristicGroup> wo2 = builder
        .element("CharacteristicGroup")
        .writer(new CharacteristicGroupWriter());
      for (CharacteristicGroup cg : object.getCharacteristicGroups())
      {
        wo2.put(cg);
      }
    }
  }
  
}
