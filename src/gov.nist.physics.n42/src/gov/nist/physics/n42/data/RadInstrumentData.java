/*
 * Copyright 2022, Lawrence Livermore National Security, LLC.
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.nist.physics.n42.data;

import gov.nist.physics.n42.reader.RadInstrumentDataReader;
import gov.nist.physics.n42.writer.RadInstrumentDataWriter;
import gov.llnl.utility.xml.bind.ReaderInfo;
import gov.llnl.utility.xml.bind.WriterInfo;
import gov.nist.physics.n42.N42Package;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author nelson85
 */
@ReaderInfo(RadInstrumentDataReader.class)
@WriterInfo(RadInstrumentDataWriter.class)
public class RadInstrumentData extends ComplexObject
{
  // Force the installation of the schema on the use of this top level class.
  private static final Object SCHEMA = N42Package.getInstance();

  private String dataCreatorName;
  private RadInstrumentInformation instrument;
  private final List<RadDetectorInformation> detectors = new ArrayList<>();
  private final List<RadItemInformation> items = new ArrayList<>();
  private final List<RadMeasurement> measurements = new ArrayList<>();
  private final List<EnergyCalibration> energyCalibration = new ArrayList<>();
  private final List<AnalysisResults> analysisResults = new ArrayList<>();
  private final List<RadMeasurementGroup> measurementGroups = new ArrayList<>();
  private final List<FWHMCalibration> fwhmCalibration = new ArrayList<>();
  private final List<EfficiencyCalibration> totalEfficiencyCalibration = new ArrayList<>();
  private final List<EfficiencyCalibration> fullEnergyPeakEfficiencyCalibration = new ArrayList<>();
  private final List<EfficiencyCalibration> intrinsicFullEnergyPeakEfficiencyCalibration = new ArrayList<>();
  private final List<EfficiencyCalibration> intrinsicSingleEscapePeakEfficiencyCalibration = new ArrayList<>();
  private final List<EfficiencyCalibration> intrinsicDoubleEscapePeakEfficiencyCalibration = new ArrayList<>();
  private final List<EnergyWindows> energyWindows = new ArrayList<>();
  private final List<DerivedData> derivedData = new ArrayList<>();
  private String docUUID;
  private String docDateTime;

  /**
   * @return the instrument
   */
  public RadInstrumentInformation getInstrument()
  {
    return instrument;
  }

  /**
   * @param instrument the instrument to set
   */
  public void setInstrument(RadInstrumentInformation instrument)
  {
    this.instrument = instrument;
  }

  public void setDataCreatorName(String name)
  {
    this.dataCreatorName = name;
  }

  public void addItem(RadItemInformation item)
  {
    this.items.add(item);
  }

  public void addDetector(RadDetectorInformation detector)
  {
    this.detectors.add(detector);
  }

  public void addMeasurement(RadMeasurement measurement)
  {
    this.measurements.add(measurement);
  }

  public void addEnergyCalibration(EnergyCalibration ec)
  {
    this.energyCalibration.add(ec);
  }

  public void addFwhmCalibration(FWHMCalibration fwhm)
  {
    this.fwhmCalibration.add(fwhm);
  }

  public void addTotalEfficiencyCalibration(EfficiencyCalibration totalEff)
  {
    this.totalEfficiencyCalibration.add(totalEff);
  }

  public void addFullEnergyPeakEfficiencyCalibration(EfficiencyCalibration cal)
  {
    this.fullEnergyPeakEfficiencyCalibration.add(cal);
  }

  public void addIntrinsicFullEnergyPeakEfficiencyCalibration(EfficiencyCalibration cal)
  {
    this.intrinsicFullEnergyPeakEfficiencyCalibration.add(cal);
  }

  public void addIntrinsicSingleEscapePeakEfficiencyCalibration(EfficiencyCalibration cal)
  {
    this.intrinsicSingleEscapePeakEfficiencyCalibration.add(cal);
  }

  public void addIntrinsicDoubleEscapePeakEfficiencyCalibration(EfficiencyCalibration cal)
  {
    this.intrinsicDoubleEscapePeakEfficiencyCalibration.add(cal);
  }

  public void addEnergyWindows(EnergyWindows ew)
  {
    this.energyWindows.add(ew);
  }

  public void addDerivedData(DerivedData dd)
  {
    this.derivedData.add(dd);
  }

  public void addAnalysisResults(AnalysisResults ar)
  {
    this.analysisResults.add(ar);
  }

  public void addMeasurementGroup(RadMeasurementGroup group)
  {
    this.measurementGroups.add(group);
  }  


  /**
   * @return radiation item information
   */
  public List<RadItemInformation> getItems()
  {
    return this.items;
  }

  /**
   * @return the detectors
   */
  public List<RadDetectorInformation> getDetectors()
  {
    return detectors;
  }

  /**
   * @return the measurements
   */
  public List<RadMeasurement> getMeasurements()
  {
    return measurements;
  }

  /**
   * @return the energyCalibration
   */
  public List<EnergyCalibration> getEnergyCalibration()
  {
    return energyCalibration;
  }

  public List<FWHMCalibration> getFWHMCalibration()
  {
    return this.fwhmCalibration;
  }

  /**
   * A total efficiency calibration.
   *
   * The total efficiency at any value of energy is the ratio of the total
   * recorded pulses in a spectrum to the number of photons emitted from a
   * source at that energy.
   */
  public List<EfficiencyCalibration> getTotalEfficiencyCalibration()
  {
    return this.totalEfficiencyCalibration;
  }

  /**
   * A full-energy peak efficiency calibration.
   *
   * The full-energy peak efficiency at any value of energy is the ratio of the
   * net counts in a peak at that energy to the number of photons emitted by a
   * source at that energy.
   *
   * @return List of full energy peak calibrations.
   */
  public List<EfficiencyCalibration> getFullEnergyPeakEfficiencyCalibration()
  {
    return fullEnergyPeakEfficiencyCalibration;
  }

  /**
   * An intrinsic full-energy peak efficiency calibration.
   *
   * The intrinsic full-energy peak efficiency at any value of energy is the
   * ratio of the net counts in a peak at that energy to the number of photons
   * impinging on the radiation detector surface at that energy.
   *
   * @return List of intrinsic full-energy peak efficiency calibrations.
   */
  public List<EfficiencyCalibration> getIntrinsicFullEnergyPeakEfficiencyCalibration()
  {
    return intrinsicFullEnergyPeakEfficiencyCalibration;
  }

  /**
   * An intrinsic single-escape peak efficiency calibration.
   *
   * The intrinsic single-escape peak efficiency at any value of energy is the
   * ratio of the counts in the single-escape peak of that energy to the number
   * of photons impinging on the radiation detector surface at that energy.
   *
   * @return List of intrinsic single escape peak efficiency calibrations.
   */
  public List<EfficiencyCalibration> getIntrinsicSingleEscapePeakEfficiencyCalibration()
  {
    return intrinsicSingleEscapePeakEfficiencyCalibration;
  }

  public List<EfficiencyCalibration> getIntrinsicDoubleEscapePeakEfficiencyCalibration()
  {
    return intrinsicDoubleEscapePeakEfficiencyCalibration;
  }

  public List<EnergyWindows> getEnergyWindows()
  {
    return this.energyWindows;
  }

  /**
   * @return the analysisResults
   */
  public List<AnalysisResults> getAnalysisResults()
  {
    return analysisResults;
  }

  /**
   * @return the measurementGroups
   */
  public List<RadMeasurementGroup> getMeasurementGroups()
  {
    return measurementGroups;
  }
  
  /**
   * @return the derivedData
   */
  public List<DerivedData> getDerivedData()
  {
    return derivedData;
  }

  /**
   * @return the dataCreatorName
   */
  public String getDataCreatorName()
  {
    return dataCreatorName;
  }

  public void setDocUUID(String value)
  {
    this.docUUID = value;
  }

  public void setDocDateTime(String value)
  {
    this.docDateTime = value;
  }

  public String getDocUUID()
  {
    return this.docUUID;
  }

  public String getDocDateTime()
  {
    return this.docDateTime;
  }

}
