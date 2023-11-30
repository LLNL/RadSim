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
import gov.nist.physics.n42.data.RadItemState;

/**
 *
 * @author her1
 */
public class RadItemStateWriter extends ObjectWriter<RadItemState>
{
  public RadItemStateWriter()
  {
    super(Options.NONE, "RadItemState", N42Package.getInstance());
  }

  @Override
  public void attributes(WriterAttributes attributes, RadItemState object) throws WriterException
  {
    // Register id
     attributes.setId(object.getId());
   
    // Get references
    String itemRef = WriterUtilities.getObjectReference(object.getItem(), getContext());
    attributes.add("radItemInformationReference", itemRef);
  }

  @Override
  public void contents(RadItemState object) throws WriterException
  {
    WriterBuilder builder = newBuilder();
    WriterUtilities.writeRemarkContents(builder, object);
    
    builder.element("StateVector").writer(new StateVectorWriter()).put(object.getStateVector());
    
    // RadItemStateExtension
  }
  
}
