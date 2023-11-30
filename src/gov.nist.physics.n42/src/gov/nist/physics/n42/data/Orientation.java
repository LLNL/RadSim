/*
 * Copyright 2022, Lawrence Livermore National Security, LLC.
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.nist.physics.n42.data;

import gov.nist.physics.n42.reader.OrientationReader;
import gov.llnl.utility.xml.bind.ReaderInfo;
import gov.llnl.utility.xml.bind.WriterInfo;
import gov.nist.physics.n42.writer.OrientationWriter;

/**
 *
 * @author nelson85
 */
@ReaderInfo(OrientationReader.class)
@WriterInfo(OrientationWriter.class)
public class Orientation
{
  private Quantity azimuth;
  private Quantity inclination;
  private Quantity roll;

  /**
   * @return the azimuthal
   */
  public Quantity getAzimuth()
  {
    return azimuth;
  }

  /**
   * @param azimuth the azimuthal to set
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
   * @return the roll
   */
  public Quantity getRoll()
  {
    return roll;
  }

  /**
   * @param roll the roll to set
   */
  public void setRoll(Quantity roll)
  {
    this.roll = roll;
  }
}
