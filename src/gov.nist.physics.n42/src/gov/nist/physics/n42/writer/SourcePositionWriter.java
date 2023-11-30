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
import gov.nist.physics.n42.data.GeographicPoint;
import gov.nist.physics.n42.data.LocationDescription;
import gov.nist.physics.n42.data.RelativeLocation;
import gov.nist.physics.n42.data.SourcePosition;

/**
 *
 * @author her1
 */
public class SourcePositionWriter extends ObjectWriter<SourcePosition>
{
  public SourcePositionWriter()
  {
    super(Options.NONE, "SourcePosition", N42Package.getInstance());
  }

  @Override
  public void attributes(WriterAttributes attributes, SourcePosition object) throws WriterException
  {
  }

  @Override
  public void contents(SourcePosition object) throws WriterException
  {
    WriterBuilder builder = newBuilder();
    WriterUtilities.writeRemarkContents(builder, object);
    
    if(object.getValue().getClass() == GeographicPoint.class)
    {
      builder.element("GeographicPoint").writer(new GeographicPointWriter()).put(object.getValue());
    }
    else if(object.getValue().getClass() == LocationDescription.class)
    {
      builder.element("LocationDescription").writer(new LocationDescriptionWriter()).put(object.getValue());
    }
    else if(object.getValue().getClass() == RelativeLocation.class)
    {
      builder.element("RelativeLocation").writer(new RelativeLocationWriter()).put(object.getValue());
    }
  }
  
}
