/*
 * Copyright 2022, Lawrence Livermore National Security, LLC.
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.nist.physics.n42.data;

import gov.nist.physics.n42.reader.RadMeasurementReader;
import gov.llnl.utility.xml.bind.ReaderInfo;
import gov.llnl.utility.xml.bind.WriterInfo;
import gov.nist.physics.n42.writer.RadMeasurementWriter;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 *
 * @author nelson85
 */
@ReaderInfo(RadMeasurementReader.class)
@WriterInfo(RadMeasurementWriter.class)
public class RadMeasurement extends ComplexObject
{

  private MeasurementClassCode classCode;
  private Instant startDateTime;
  private Duration realTimeDuration;
  private final List<Spectrum> spectrum = new ArrayList<>();
  private final List<GrossCounts> grossCounts = new ArrayList<>();
  private final List<DoseRate> doseRate = new ArrayList<>();
  private final List<CountRate> countRate = new ArrayList<>();
  private RadInstrumentState instrumentState;
  private final List<RadDetectorState> detectorState = new ArrayList<>();
  private final List<RadItemState> itemState = new ArrayList<>();

  // references
  private List<RadMeasurementGroup> groups = new ArrayList<>();
  private List<AnalysisResults> analysisResults = new ArrayList<>();
  private Boolean occupancy = null;

  /**
   * @return the foreground
   */
  public MeasurementClassCode getClassCode()
  {
    return classCode;
  }

  /**
   * @param code the foreground to set
   */
  public void setClassCode(MeasurementClassCode code)
  {
    this.classCode = code;
  }

  /**
   * @return the startDateTime
   */
  public Instant getStartDateTime()
  {
    return startDateTime;
  }

  /**
   * @param startDateTime the startDateTime to set
   */
  public void setStartDateTime(Instant startDateTime)
  {
    this.startDateTime = startDateTime;
  }

  /**
   * @return the realTimeDuration
   */
  public Duration getRealTimeDuration()
  {
    return realTimeDuration;
  }

  /**
   * @param realTimeDuration the realTimeDuration to set
   */
  public void setRealTimeDuration(Duration realTimeDuration)
  {
    this.realTimeDuration = realTimeDuration;
  }

  /**
   * @return the spectrum
   */
  public List<Spectrum> getSpectrum()
  {
    return spectrum;
  }

  /**
   * @return the grossCounts
   */
  public List<GrossCounts> getGrossCounts()
  {
    return grossCounts;
  }

  /**
   * @return the doseRate
   */
  public List<DoseRate> getDoseRate()
  {
    return doseRate;
  }

  public void addSpectrum(Spectrum value)
  {
    spectrum.add(value);
  }

  /**
   * @param value
   */
  public void addGrossCounts(GrossCounts value)
  {
    grossCounts.add(value);
  }

  /**
   * @param value
   */
  public void addDoseRate(DoseRate value)
  {
    doseRate.add(value);
  }
  
    /**
   * @param value
   */
  public void addCountRate(CountRate value)
  {
    countRate.add(value);
  }

  /**
   * @return the state
   */
  public List<RadDetectorState> getDetectorState()
  {
    return detectorState;
  }

  /**
   * @return the state
   */
  public List<RadItemState> getItemState()
  {
    return itemState;
  }

  /**
   * @param state the state to set
   */
  public void addDetectorState(RadDetectorState state)
  {
    this.detectorState.add(state);
  }

  public void addItemState(RadItemState state)
  {
    this.itemState.add(state);
  }

  /**
   * @return the instrumentState
   */
  public RadInstrumentState getInstrumentState()
  {
    return instrumentState;
  }

  /**
   * @param instrumentState the instrumentState to set
   */
  public void setInstrumentState(RadInstrumentState instrumentState)
  {
    this.instrumentState = instrumentState;
  }

  public List<RadMeasurementGroup> getRadMeasurementGroups()
  {
    return this.groups;
  }

  /**
   * FIXME: Is this the right way to handle attribute references?
   *
   * @param group
   */
  public void addToGroup(RadMeasurementGroup group)
  {
    group.addMeasurement(this);
    this.groups.add(group);
  }
  
  public List<AnalysisResults> getAnalysisResults()
  {
    return this.analysisResults;
  }

  public void addAnalysisResult(AnalysisResults results)
  {
    this.analysisResults.add(results);
  }

  /**
   * @return the occupancy
   */
  public Boolean getOccupancy()
  {
    return occupancy;
  }

  /**
   * @param occupancy the occupancy to set
   */
  public void setOccupancy(Boolean occupancy)
  {
    this.occupancy = occupancy;
  }

   @Override
  public void visitReferencedObjects(Consumer<ComplexObject> visitor)
  {
    groups.forEach(visitor::accept);
    this.detectorState.forEach(p->p.visitReferencedObjects(visitor));
    this.doseRate.forEach(p->p.visitReferencedObjects(visitor));
    this.grossCounts.forEach(p->p.visitReferencedObjects(visitor));
    this.itemState.forEach(p->p.visitReferencedObjects(visitor));
    this.spectrum.forEach(p->p.visitReferencedObjects(visitor));
  }

}
