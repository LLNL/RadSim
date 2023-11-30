/*
 * Copyright 2022, Lawrence Livermore National Security, LLC.
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.nist.physics.n42.data;

import gov.llnl.utility.xml.bind.ReaderInfo;
import gov.llnl.utility.xml.bind.WriterInfo;
import gov.nist.physics.n42.reader.LocationDescriptionReader;
import gov.nist.physics.n42.writer.LocationDescriptionWriter;

/**
 *
 * @author nelson85
 */
@ReaderInfo(LocationDescriptionReader.class)
@WriterInfo(LocationDescriptionWriter.class)
public class LocationDescription implements LocationType
{
  private String value;

  public LocationDescription()
  {
  }

  public LocationDescription(String textContents)
  {
    this.value = textContents;
  }

  /**
   * @return the value
   */
  public String getValue()
  {
    return value;
  }

  /**
   * @param value the value to set
   */
  public void setValue(String value)
  {
    this.value = value;
  }

}
