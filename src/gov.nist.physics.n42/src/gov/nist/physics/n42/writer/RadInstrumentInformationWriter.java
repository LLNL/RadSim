/*
 * Copyright 2022, Lawrence Livermore National Security, LLC.
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.nist.physics.n42.writer;

import gov.llnl.utility.io.WriterException;
import gov.llnl.utility.xml.bind.ObjectWriter;
import gov.llnl.utility.xml.bind.ObjectWriter.WriterAttributes;
import gov.llnl.utility.xml.bind.ObjectWriter.WriterBuilder;
import gov.nist.physics.n42.N42Package;
import gov.nist.physics.n42.data.Characteristics;
import gov.nist.physics.n42.data.RadInstrumentInformation;
import gov.nist.physics.n42.data.RadInstrumentQualityControl;
import gov.nist.physics.n42.data.RadInstrumentVersion;
import java.util.Map;

/**
 *
 * @author her1
 */
public class RadInstrumentInformationWriter extends ObjectWriter<RadInstrumentInformation>
{
  public RadInstrumentInformationWriter()
  {
    super(Options.NONE, "RadInstrumentInformation", N42Package.getInstance());
  }

  @Override
  public void attributes(WriterAttributes attributes, RadInstrumentInformation object) throws WriterException
  {
    // Register id
    attributes.setId(object.getId());
  }

  @Override
  public void contents(RadInstrumentInformation object) throws WriterException
  {
    WriterBuilder builder = newBuilder();
    WriterUtilities.writeRemarkContents(builder, object);
    builder.element("RadInstrumentManufacturerName").putString(object.getManufacturerName());
    
    if(object.getIdentifier() != null)
    {
      builder.element("RadInstrumentIdentifier").putString(object.getIdentifier());
    }
    
    builder.element("RadInstrumentModelName").putString(object.getModelName());

    if(object.getDescription() != null)
    {
      builder.element("RadInstrumentDescription").putString(object.getDescription());
    }
    
    builder.element("RadInstrumentClassCode").putString(object.getClassCode());
    
    for (Map.Entry<String,String> entry : object.getVersion().entrySet())
    {
      builder.element("RadInstrumentVersion").writer(new RadInstrumentVersionWriter()).put(new RadInstrumentVersion(entry.getKey(), entry.getValue()));
    }
    
    if(object.getQualityControl() != null)
    {
      builder.element("RadInstrumentQualityControl")
        .contents(RadInstrumentQualityControl.class)
        .put(object.getQualityControl());
    }
    
    if(!object.getCharacteristics().isEmpty())
    {
      WriteObject<Characteristics> wo = builder
        .element("RadInstrumentCharacteristics")
        .writer(new CharacteristicsWriter());
      for (Characteristics o : object.getCharacteristics())
      {
        wo.put(o);
      }
    }
    
    // RadInstrumentInformationExtension
  }
  
}
