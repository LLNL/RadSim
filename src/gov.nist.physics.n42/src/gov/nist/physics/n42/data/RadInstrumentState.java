/*
 * Copyright 2022, Lawrence Livermore National Security, LLC.
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.nist.physics.n42.data;

import gov.nist.physics.n42.reader.RadInstrumentStateReader;
import gov.nist.physics.n42.writer.RadInstrumentStateWriter;
import gov.llnl.utility.xml.bind.ReaderInfo;
import gov.llnl.utility.xml.bind.WriterInfo;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 *
 * @author nelson85
 */
@ReaderInfo(RadInstrumentStateReader.class)
@WriterInfo(RadInstrumentStateWriter.class)
public class RadInstrumentState extends ComplexObject
{
  private String modeCode;
  private String modeDescription;
  private StateVector stateVector;
  private RadInstrumentInformation instrument;
  private final List<Characteristics> instrumentCharacteristics = new ArrayList<>();
  private final List<Fault> faults = new ArrayList<>();


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

  public void setInstrument(RadInstrumentInformation get)
  {
    this.instrument = get;
  }

  /**
   * @return the instrument
   */
  public RadInstrumentInformation getInstrument()
  {
    return instrument;
  }

  /**
   * @return the modeCode
   */
  public String getModeCode()
  {
    return modeCode;
  }

  /**
   * @param modeCode the modeCode to set
   */
  public void setModeCode(String modeCode)
  {
    this.modeCode = modeCode;
  }

  /**
   * @return the modeDescription
   */
  public String getModeDescription()
  {
    return modeDescription;
  }

  /**
   * @param modeDescription the modeDescription to set
   */
  public void setModeDescription(String modeDescription)
  {
    this.modeDescription = modeDescription;
  }

  public void addInstrumentCharacteristics(Characteristics c)
  {
    this.instrumentCharacteristics.add(c);
  }

  public void addFault(Fault c)
  {
    this.faults.add(c);
  }

  public List<Characteristics> getInstrumentCharacteristics()
  {
    return instrumentCharacteristics;
  }

  public List<Fault> getFaults()
  {
    return faults;
  }

   @Override
  public void visitReferencedObjects(Consumer<ComplexObject> visitor)
  {
    visitor.accept(this.instrument);
    this.stateVector.visitReferencedObjects(visitor);
  }


}
