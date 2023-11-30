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
import gov.nist.physics.n42.data.RadItemQuantity;

/**
 *
 * @author her1
 */
public class RadItemQuantityWriter extends ObjectWriter<RadItemQuantity>
{
  public RadItemQuantityWriter()
  {
    super(Options.NONE, "RadItemQuantity", N42Package.getInstance());
  }

  @Override
  public void attributes(WriterAttributes attributes, RadItemQuantity object) throws WriterException
  {
    // Register id
    attributes.setId(object.getId());
  }

  @Override
  public void contents(RadItemQuantity object) throws WriterException
  {
    WriterBuilder builder = newBuilder();
    WriterUtilities.writeRemarkContents(builder, object);
    
    builder.element("RadItemQuantityValue").putDouble(object.getValue());
    
    if(object.getUncertainty() != null)
    {
      builder.element("RadItemQuantityUncertaintyValue").putDouble(object.getUncertainty());
    }
    
    builder.element("RadItemQuantityUnits").putString(object.getUnits());
  }
  
}
