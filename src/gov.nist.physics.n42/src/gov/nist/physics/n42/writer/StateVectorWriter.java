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
import gov.nist.physics.n42.data.StateVector;

/**
 *
 * @author her1
 */
public class StateVectorWriter extends ObjectWriter<StateVector>
{
  public StateVectorWriter()
  {
    super(Options.NONE, "StateVector", N42Package.getInstance());
  }

  @Override
  public void attributes(WriterAttributes attributes, StateVector object) throws WriterException
  {
  }

  @Override
  public void contents(StateVector object) throws WriterException
  {
    WriterBuilder builder = newBuilder();
    
    if(object.getCoordinate() != null)
    {
     builder.element("GeographicPoint").writer(new GeographicPointWriter()).put(object.getCoordinate()); 
    }
    else if(object.getLocationDescription() != null)
    {
     builder.element("LocationDescription").writer(new LocationDescriptionWriter()).put(object.getLocationDescription()); 
    }
    else if(object.getRelativeLocation() != null)
    {
      builder.element("RelativeLocation").writer(new RelativeLocationWriter()).put(object.getRelativeLocation());
    }
   
    if(object.getOrientation() != null)
    {
      builder.element("Orientation").writer(new OrientationWriter()).put(object.getOrientation());
    }
    
    if(object.getSpeed() != null)
    {
      builder.element("SpeedValue").contents(Quantity.class).put(object.getSpeed());
    }
  }
  
}
