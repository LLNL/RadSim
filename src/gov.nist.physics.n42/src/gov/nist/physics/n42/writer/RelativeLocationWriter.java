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
import gov.nist.physics.n42.data.Quantity;
import gov.nist.physics.n42.data.RelativeLocation;

/**
 *
 * @author her1
 */
public class RelativeLocationWriter extends ObjectWriter<RelativeLocation>
{
  public RelativeLocationWriter()
  {
    super(Options.NONE, "RelativeLocation", N42Package.getInstance());
  }

  @Override
  public void attributes(WriterAttributes attributes, RelativeLocation object) throws WriterException
  {
  }

  @Override
  public void contents(RelativeLocation object) throws WriterException
  {
    WriterBuilder builder = newBuilder();
    
    if(object.getAzimuth() != null)
    {
      builder.element("RelativeLocationAzimuthValue").contents(Quantity.class).put(object.getAzimuth());
    }
    
    if(object.getInclination() != null)
    {
      builder.element("RelativeLocationInclinationValue").contents(Quantity.class).put(object.getInclination());
    }
    
    if(object.getDistance() != null)
    {
      builder.element("DistanceValue").contents(Quantity.class).put(object.getDistance());
    }
    
    builder.element("Origin").writer(new OriginWriter()).put(object.getOrigin());
  }

}
