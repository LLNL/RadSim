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
import gov.nist.physics.n42.data.RadDetectorInformation;

/**
 *
 * @author her1
 */
public class RadDetectorInformationWriter extends ObjectWriter<RadDetectorInformation>
{
  public RadDetectorInformationWriter()
  {
    super(Options.NONE, "RadDetectorInformation", N42Package.getInstance());
  }

  @Override
  public void attributes(WriterAttributes attributes, RadDetectorInformation object) throws WriterException
  {
    // Register id
    attributes.setId(object.getId());
  }

  @Override
  public void contents(RadDetectorInformation object) throws WriterException
  {
    WriterBuilder builder = newBuilder();
    WriterUtilities.writeRemarkContents(builder, object);

    if (object.getName() != null)
    {
      builder.element("RadDetectorName").putString(object.getName());
    }

    builder.element("RadDetectorCategoryCode").putString(object.getCategoryCode());
    builder.element("RadDetectorKindCode").putString(object.getKindCode());

    if (object.getDescription() != null)
    {
      builder.element("RadDetectorDescription").putString(object.getDescription());
    }

    if (object.getLength() != null)
    {
      builder.element("RadDetectorLengthValue").contents(Quantity.class).put(object.getLength());
    }

    if (object.getWidth() != null)
    {
      builder.element("RadDetectorWidthValue").contents(Quantity.class).put(object.getWidth());
    }

    if (object.getDepth() != null)
    {
      builder.element("RadDetectorDepthValue").contents(Quantity.class).put(object.getDepth());
    }

    if (object.getDiameter() != null)
    {
      builder.element("RadDetectorDiameterValue").contents(Quantity.class).put(object.getDiameter());
    }

    if (object.getVolume() != null)
    {
      builder.element("RadDetectorVolumeValue").contents(Quantity.class).put(object.getVolume());
    }

    if (!object.getCharacteristics().isEmpty())
    {
      WriteObject<Characteristics> wo = builder
              .element("RadDetectorCharacteristics")
              .writer(new CharacteristicsWriter());
      for (Characteristics o : object.getCharacteristics())
      {
        wo.put(o);
      }
    }

    // RadDetectorInformationExtension
  }

}
