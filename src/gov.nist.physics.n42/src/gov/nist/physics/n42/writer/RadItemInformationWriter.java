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
import gov.nist.physics.n42.data.Quantity;
import gov.nist.physics.n42.data.RadItemInformation;

/**
 *
 * @author her1
 */
public class RadItemInformationWriter extends ObjectWriter<RadItemInformation>
{
  public RadItemInformationWriter()
  {
    super(Options.NONE, "RadItemInformation", N42Package.getInstance());
  }
  
  @Override
  public void attributes(WriterAttributes attributes, RadItemInformation object) throws WriterException
  {
    // Register id
    attributes.setId(object.getId());
  }

  @Override
  public void contents(RadItemInformation object) throws WriterException
  {
    WriterBuilder builder = newBuilder();
    WriterUtilities.writeRemarkContents(builder, object);
    
    if(object.getDescription() != null)
    {
      builder.element("RadItemDescription").putString(object.getDescription());
    }
    
    if(object.getQuantity() != null)
    {
      builder.element("RadItemQuantity").contents(Quantity.class).put(object.getQuantity());
    }
    
    if(object.getMeasurementGeometryDescription() != null)
    {
      builder.element("RadItemMeasurementGeometryDescription").putString(object.getMeasurementGeometryDescription());
    }
    
    if(!object.getCharacteristics().isEmpty())
    {
      WriteObject<Characteristics> wo = builder
        .element("RadItemCharacteristics")
        .writer(new CharacteristicsWriter());
      for (Characteristics o : object.getCharacteristics())
      {
        wo.put(o);
      }
    }
    
    // RadItemInformationExtension
  }
  
}
