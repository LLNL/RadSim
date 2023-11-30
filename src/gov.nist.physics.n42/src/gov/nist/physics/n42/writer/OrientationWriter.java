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
import gov.nist.physics.n42.data.Orientation;
import gov.nist.physics.n42.data.Quantity;

/**
 *
 * @author her1
 */
public class OrientationWriter extends ObjectWriter<Orientation>
{
  public OrientationWriter()
  {
    super(Options.NONE, "Orientation", N42Package.getInstance());
  }

  @Override
  public void attributes(WriterAttributes attributes, Orientation object) throws WriterException
  {
  }

  @Override
  public void contents(Orientation object) throws WriterException
  {
    WriterBuilder builder = newBuilder();
    builder.element("AzimuthValue").contents(Quantity.class).put(object.getAzimuth());
    
    if(object.getInclination() != null)
    {
      builder.element("InclinationValue").contents(Quantity.class).put(object.getInclination());
    }
    
    if(object.getRoll() != null)
    {
      builder.element("RollValue").contents(Quantity.class).put(object.getRoll());
    }
  }
  
}
