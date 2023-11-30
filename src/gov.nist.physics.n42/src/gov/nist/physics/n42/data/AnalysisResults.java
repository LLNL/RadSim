/*
 * Copyright 2022, Lawrence Livermore National Security, LLC.
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.nist.physics.n42.data;

import gov.nist.physics.n42.reader.AnalysisResultsReader;
import gov.llnl.utility.xml.bind.ReaderInfo;
import gov.llnl.utility.xml.bind.WriterInfo;
import gov.nist.physics.n42.writer.AnalysisResultsWriter;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * The collection of information resulting from the analysis of the radiation
 * measurements or derived data.
 *
 * @author nelson85
 */
@ReaderInfo(AnalysisResultsReader.class)
@WriterInfo(AnalysisResultsWriter.class)
public class AnalysisResults extends ComplexObject
{

  // annotations, references
  private final List<RadMeasurement> measurements = new ArrayList<>();
  private final List<RadMeasurementGroup> measurementGroups = new ArrayList<>();
  private final List<DerivedData> references = new ArrayList<>();
  
  // members
  private Instant startDateTime;
  private String algorithmName;
  private String algorithmCreatorName;
  private final List<AnalysisAlgorithmVersion> algorithmVersion = new ArrayList<>();
  private final List<AnalysisAlgorithmSetting> algorithmSettings = new ArrayList<>();
  private final List<RadAlarm> radAlarm = new ArrayList<>();
  private NuclideAnalysisResults nuclideAnalysisResults;
  private SpectrumPeakAnalysisResults spectrumPeakAnalysisResults;
  private GrossCountAnalysisResults grossCountAnalysisResults;
  private DoseAnalysisResults doseAnalysisResults;
  private ExposureAnalysisResults exposureAnalysisResults;
  private final List<Fault> faults = new ArrayList<>();
  private Double confidence;
  private String description;
  private AnalysisResultStatusCode statusCode;
  
  public Instant getStartDateTime()
  {
    return startDateTime;
  }
  
  public void setStartDateTime(Instant startDateTime)
  {
    this.startDateTime = startDateTime;
  }
  
  public String getAlgorithmName()
  {
    return algorithmName;
  }
  
  public void setAlgorithmName(String algorithmName)
  {
    this.algorithmName = algorithmName;
  }
  
  public String getAlgorithmCreatorName()
  {
    return algorithmCreatorName;
  }
  
  public NuclideAnalysisResults getNuclideAnalysisResults()
  {
    return nuclideAnalysisResults;
  }
  
  public AnalysisResultStatusCode getStatusCode()
  {
    return statusCode;
  }
  
  public void setAlgorithmCreatorName(String algorithmCreatorName)
  {
    this.algorithmCreatorName = algorithmCreatorName;
  }
  
  public List<AnalysisAlgorithmVersion> getAlgorithmVersion()
  {
    return algorithmVersion;
  }
  
  public void addAlgorithmVersion(AnalysisAlgorithmVersion algorithmVersion)
  {
    this.algorithmVersion.add(algorithmVersion);
  }
  
  public void addAlgorithmSetting(AnalysisAlgorithmSetting value)
  {
    this.algorithmSettings.add(value);
  }
  
  public void addToGroup(Object radMeasurementGroup)
  {
    if (radMeasurementGroup instanceof RadMeasurementGroup)
    {
      ((RadMeasurementGroup) radMeasurementGroup).addAnalysisResult(this);
      this.measurementGroups.add(((RadMeasurementGroup) radMeasurementGroup));
      return;
    }
    throw new RuntimeException("Unhandled type");
  }
  
  public void addToMeasurement(RadMeasurement radMeasurement)
  {
    radMeasurement.addAnalysisResult(this);
    this.measurements.add(radMeasurement);
  }
  
  public void addToReferences(DerivedData derivedData)
  {
    this.references.add(derivedData);
  }
  
  public void setNuclideAnalysisResults(NuclideAnalysisResults results)
  {
    this.nuclideAnalysisResults = results;
  }
  
  public void setSpectrumPeakAnalysisResults(SpectrumPeakAnalysisResults results)
  {
    this.spectrumPeakAnalysisResults = results;
  }
  
  public void setGrossCountAnalysisResults(GrossCountAnalysisResults results)
  {
    this.grossCountAnalysisResults = results;
  }
  
  public void setDoseAnalysisResults(DoseAnalysisResults results)
  {
    this.doseAnalysisResults = results;
  }
  
  public void setExposureAnalysisResults(ExposureAnalysisResults results)
  {
    this.exposureAnalysisResults = results;
  }
  
  public void addFault(Fault fault)
  {
    this.faults.add(fault);
  }
  
  public GrossCountAnalysisResults getGrossCountAnalysisResults()
  {
    return grossCountAnalysisResults;
  }
  
  public DoseAnalysisResults getDoseAnalysisResults()
  {
    return doseAnalysisResults;
  }
  
  public ExposureAnalysisResults getExposureAnalysisResults()
  {
    return exposureAnalysisResults;
  }
  
  public List<Fault> getFaults()
  {
    return faults;
  }
  
  public List<RadMeasurement> getMeasurements()
  {
    return measurements;
  }
  
  public List<RadMeasurementGroup> getMeasurementGroups()
  {
    return measurementGroups;
  }
  
  public SpectrumPeakAnalysisResults getSpectrumPeakAnalysisResults()
  {
    return spectrumPeakAnalysisResults;
  }
  
  public List<RadAlarm> getRadAlarm()
  {
    return radAlarm;
  }
  
  public void addRadAlarm(RadAlarm result)
  {
    this.radAlarm.add(result);
  }
  
  public void setConfidence(Double u)
  {
    this.confidence = u;
  }
  
  public void setDescription(String u)
  {
    this.description = u;
  }

  /**
   * @return the algorithmSettings
   */
  public List<AnalysisAlgorithmSetting> getAlgorithmSettings()
  {
    return algorithmSettings;
  }
  
  public void setStatusCode(AnalysisResultStatusCode code)
  {
    this.statusCode = code;
  }

  /**
   * @return the confidence
   */
  public Double getConfidence()
  {
    return confidence;
  }

  /**
   * @return the description
   */
  public String getDescription()
  {
    return description;
  }
  
  @Override
  public void visitReferencedObjects(Consumer<ComplexObject> visitor)
  {
    this.measurementGroups.forEach(visitor::accept);
    this.measurements.forEach(visitor::accept);
    this.radAlarm.forEach(p->p.visitReferencedObjects(visitor));
  }
  
}
