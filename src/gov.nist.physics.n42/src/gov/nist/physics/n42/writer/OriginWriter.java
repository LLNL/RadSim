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
import gov.nist.physics.n42.data.ComplexObject;
import gov.nist.physics.n42.data.Origin;

/**
 *
 * @author her1
 */
public class OriginWriter extends ObjectWriter<Origin>
{
  public OriginWriter()
  {
    super(Options.NONE, "Origin", N42Package.getInstance());
  }

  @Override
  public void attributes(WriterAttributes attributes, Origin object) throws WriterException
  {

    ComplexObject ref = (ComplexObject) object.getReference();
    if (ref != null)
      attributes.add("originReference", ref.getId());
  }

  @Override
  public void contents(Origin object) throws WriterException
  {
    WriterBuilder builder = newBuilder();

    if (object.getGeographicPoint() != null)
    {
      builder.element("GeographicPoint").writer(new GeographicPointWriter()).put(object.getGeographicPoint());
    }

    if (object.getDescription() != null)
    {
      builder.element("OriginDescription").putString(object.getDescription());
    }
  }

}
