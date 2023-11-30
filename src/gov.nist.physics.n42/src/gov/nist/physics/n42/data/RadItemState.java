/*
 * Copyright 2022, Lawrence Livermore National Security, LLC.
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.nist.physics.n42.data;

import gov.llnl.utility.xml.bind.ReaderInfo;
import gov.llnl.utility.xml.bind.WriterInfo;
import gov.nist.physics.n42.reader.RadItemStateReader;
import gov.nist.physics.n42.writer.RadItemStateWriter;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

/**
 *
 * @author nelson85
 */
@ReaderInfo(RadItemStateReader.class)
@WriterInfo(RadItemStateWriter.class)
public class RadItemState extends ComplexObject
{

  private RadItemInformation item;
  private StateVector stateVector;

  public RadItemState()
  {
  }

  /**
   * @return the stateVector
   */
  public StateVector getStateVector()
  {
    return stateVector;
  }

  /**
   * @param stateVector the stateVector to set
   */
  public void setStateVector(StateVector stateVector)
  {
    this.stateVector = stateVector;
  }

  /**
   * @return the info
   */
  public RadItemInformation getItem()
  {
    return item;
  }

  /**
   * @param item the info to set
   */
  public void setItem(RadItemInformation item)
  {
    this.item = item;
  }
  
}
