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
import gov.nist.physics.n42.data.Quantity;

/**
 *
 * @author her1
 */
public class GeographicPointWriter extends ObjectWriter<GeographicPoint>
{
  public GeographicPointWriter()
  {
    super(Options.NONE, "GeographicPoint", N42Package.getInstance());
  }

  @Override
  public void attributes(WriterAttributes attributes, GeographicPoint object) throws WriterException
  {
  }

  @Override
  public void contents(GeographicPoint object) throws WriterException
  {
    WriterBuilder builder = newBuilder();
    builder.element("LatitudeValue").contents(Quantity.class).put(object.getLatitude());
    builder.element("LongitudeValue").contents(Quantity.class).put(object.getLongitude());
    
    if(object.getElevation() != null)
    {
      builder.element("ElevationValue").putString(object.getElevation().toString());
    }
    
    if(object.getElevationOffset() != null)
    {
      builder.element("ElevationOffsetValue").putString(object.getElevationOffset().toString());
    }
    
    if(object.getGeoPointAccuracy() != null)
    {
      builder.element("GeoPointAccuracyValue").putString(object.getGeoPointAccuracy().toString());
    }
    
    if(object.getElevationAccuracy() != null)
    {
      builder.element("ElevationAccuracyValue").putString(object.getElevationAccuracy().toString());
    }
    
    if(object.getElevationOffsetAccuracy() != null)
    {
      builder.element("ElevationOffsetAccuracyValue").putString(object.getElevationOffsetAccuracy().toString());
    }
  }
  
}
