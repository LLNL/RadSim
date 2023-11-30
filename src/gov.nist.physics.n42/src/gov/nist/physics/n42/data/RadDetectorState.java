/*
 * Copyright 2022, Lawrence Livermore National Security, LLC.
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.nist.physics.n42.data;

import gov.llnl.utility.xml.bind.ReaderInfo;
import gov.llnl.utility.xml.bind.WriterInfo;
import gov.nist.physics.n42.reader.RadDetectorStateReader;
import gov.nist.physics.n42.writer.RadDetectorStateWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

/**
 *
 * @author nelson85
 */
@ReaderInfo(RadDetectorStateReader.class)
@WriterInfo(RadDetectorStateWriter.class)
public class RadDetectorState extends ComplexObject
{

  private StateVector state;
  private final List<Fault> faults = new ArrayList<>();
  private final List<Characteristics> characteristics = new ArrayList<>();
  private RadDetectorInformation detector;

  /**
   * @return the state
   */
  public StateVector getState()
  {
    return state;
  }

  /**
   * @param state the state to set
   */
  public void setState(StateVector state)
  {
    this.state = state;
  }

  public void addFault(Fault fault)
  {
    this.faults.add(fault);
  }

  public void addCharacteristics(Characteristics characteristics)
  {
    this.characteristics.add(characteristics);
  }

  public void setDetector(RadDetectorInformation detector)
  {
    this.detector = detector;
  }

  /**
   * @return the detector
   */
  public RadDetectorInformation getDetector()
  {
    return detector;
  }

  /**
   * @return the faults
   */
  public List<Fault> getFaults()
  {
    return faults;
  }

  /**
   * @return the characteristics
   */
  public List<Characteristics> getCharacteristics()
  {
    return characteristics;
  }
    
 @Override
  public void visitReferencedObjects(Consumer<ComplexObject> visitor)
  {
    visitor.accept(detector);
    this.state.visitReferencedObjects(visitor);
  }

}
