/*
 * Copyright 2022, Lawrence Livermore National Security, LLC.
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.nist.physics.n42.data;

import gov.nist.physics.n42.reader.GeographicPointReader;
import gov.llnl.utility.xml.bind.ReaderInfo;
import gov.llnl.utility.xml.bind.WriterInfo;
import gov.nist.physics.n42.writer.GeographicPointWriter;

/**
 *
 * @author nelson85
 */
@ReaderInfo(GeographicPointReader.class)
@WriterInfo(GeographicPointWriter.class)
public class GeographicPoint implements LocationType
{

  private Quantity latitude;
  private Quantity longitude;
  private Double elevation;
  private Double elevationOffset;
  private Double geoPointAccuracy;
  private Double elevationAccuracy;
  private Double elevationOffsetAccuracy;

  public Quantity getLatitude()
  {
    return latitude;
  }

  public void setLatitude(Quantity latitude)
  {
    this.latitude = latitude;
  }

  public Quantity getLongitude()
  {
    return longitude;
  }

  public void setLongitude(Quantity longitude)
  {
    this.longitude = longitude;
  }

  public Double getElevation()
  {
    return elevation;
  }

  public void setElevation(double elevation)
  {
    this.elevation = elevation;
  }

  public void setElevationOffset(Double u)
  {
    this.elevationOffset = u;
  }

  public void setGeoPointAccuracy(Double u)
  {
    this.geoPointAccuracy = u;
  }

  public void setElevationAccuracy(Double u)
  {
    this.elevationAccuracy = u;
  }

  public void setElevationOffsetAccuracy(Double u)
  {
    this.elevationOffsetAccuracy = u;
  }

  public Double getElevationOffset()
  {
    return elevationOffset;
  }

  public Double getGeoPointAccuracy()
  {
    return geoPointAccuracy;
  }

  public Double getElevationAccuracy()
  {
    return elevationAccuracy;
  }

  public Double getElevationOffsetAccuracy()
  {
    return elevationOffsetAccuracy;
  }

}
