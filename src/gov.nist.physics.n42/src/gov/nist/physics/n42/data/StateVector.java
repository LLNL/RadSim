/*
 * Copyright 2022, Lawrence Livermore National Security, LLC.
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.nist.physics.n42.data;

import gov.nist.physics.n42.reader.StateVectorReader;
import gov.llnl.utility.xml.bind.ReaderInfo;
import gov.llnl.utility.xml.bind.WriterInfo;
import gov.nist.physics.n42.writer.StateVectorWriter;
import java.util.function.Consumer;

/**
 *
 * @author nelson85
 */
@ReaderInfo(StateVectorReader.class)
@WriterInfo(StateVectorWriter.class)
public class StateVector
{
  private GeographicPoint coordinate;
  private LocationDescription locationDescription;
  private RelativeLocation relativeLocation;
  private Orientation orientation;
  private Quantity speed;

  public void setLocationDescription(LocationDescription locationDescription)
  {
    this.locationDescription = locationDescription;
  }

  /**
   * @return the coordinate
   */
  public GeographicPoint getCoordinate()
  {
    return coordinate;
  }

  /**
   * @param coordinate the coordinate to set
   */
  public void setCoordinate(GeographicPoint coordinate)
  {
    this.coordinate = coordinate;
  }

  /**
   * @return the locationDescription
   */
  public LocationDescription getLocationDescription()
  {
    return locationDescription;
  }

  /**
   * @return the relativeLocation
   */
  public RelativeLocation getRelativeLocation()
  {
    return relativeLocation;
  }

  /**
   * @param relativeLocation the relativeLocation to set
   */
  public void setRelativeLocation(RelativeLocation relativeLocation)
  {
    this.relativeLocation = relativeLocation;
  }

  /**
   * @return the orientation
   */
  public Orientation getOrientation()
  {
    return orientation;
  }

  /**
   * @param orientation the orientation to set
   */
  public void setOrientation(Orientation orientation)
  {
    this.orientation = orientation;
  }

  /**
   * @return the speed
   */
  public Quantity getSpeed()
  {
    return speed;
  }

  /**
   * @param speed the speed to set
   */
  public void setSpeed(Quantity speed)
  {
    this.speed = speed;
  }

  public void visitReferencedObjects(Consumer<ComplexObject> visitor)
  {
    if (this.relativeLocation != null)
      this.relativeLocation.visitReferencedObjects(visitor);
  }

}
