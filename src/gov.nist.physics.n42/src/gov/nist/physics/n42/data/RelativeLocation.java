/*
 * Copyright 2022, Lawrence Livermore National Security, LLC.
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.nist.physics.n42.data;

import gov.nist.physics.n42.reader.RelativeLocationReader;
import gov.llnl.utility.xml.bind.ReaderInfo;
import gov.llnl.utility.xml.bind.WriterInfo;
import gov.nist.physics.n42.writer.RelativeLocationWriter;
import java.util.function.Consumer;

/**
 *
 * @author nelson85
 */
@ReaderInfo(RelativeLocationReader.class)
@WriterInfo(RelativeLocationWriter.class)
public class RelativeLocation implements LocationType
{
  private Quantity azimuth;
  private Quantity inclination;
  private Quantity distance;
  private Origin origin;

  /**
   * @return the azimuth
   */
  public Quantity getAzimuth()
  {
    return azimuth;
  }

  /**
   * @param azimuth the azimuth to set
   */
  public void setAzimuth(Quantity azimuth)
  {
    this.azimuth = azimuth;
  }

  /**
   * @return the inclination
   */
  public Quantity getInclination()
  {
    return inclination;
  }

  /**
   * @param inclination the inclination to set
   */
  public void setInclination(Quantity inclination)
  {
    this.inclination = inclination;
  }

  /**
   * @return the distance
   */
  public Quantity getDistance()
  {
    return distance;
  }

  /**
   * @param distance the distance to set
   */
  public void setDistance(Quantity distance)
  {
    this.distance = distance;
  }

  /**
   * @return the origin
   */
  public Origin getOrigin()
  {
    return origin;
  }

  /**
   * @param origin the origin to set
   */
  public void setOrigin(Origin origin)
  {
    this.origin = origin;
  }

  public void visitReferencedObjects(Consumer<ComplexObject> visitor)
  {
    if (this.origin != null)
      this.origin.visitReferencedObjects(visitor);
  }

}
