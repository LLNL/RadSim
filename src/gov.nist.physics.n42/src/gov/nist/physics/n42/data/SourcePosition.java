/*
 * Copyright 2022, Lawrence Livermore National Security, LLC.
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.nist.physics.n42.data;

import gov.llnl.utility.xml.bind.ReaderInfo;
import gov.llnl.utility.xml.bind.WriterInfo;
import gov.nist.physics.n42.reader.SourcePositionReader;
import gov.nist.physics.n42.writer.SourcePositionWriter;

/**
 *
 * @author nelson85
 */
@ReaderInfo(SourcePositionReader.class)
@WriterInfo(SourcePositionWriter.class)
public class SourcePosition extends ComplexObject
{
  private LocationType value;
  
  public SourcePosition()
  {}
  
  public SourcePosition(LocationType value)
  {
    this.value = value;
  }

  /**
   * @return the value
   */
  public LocationType getValue()
  {
    return value;
  }

  /**
   * @param value the value to set
   */
  public void setValue(LocationType value)
  {
    this.value = value;
  }
    
}
