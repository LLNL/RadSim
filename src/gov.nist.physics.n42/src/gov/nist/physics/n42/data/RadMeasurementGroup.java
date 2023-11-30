/*
 * Copyright 2022, Lawrence Livermore National Security, LLC.
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.nist.physics.n42.data;

import gov.llnl.utility.xml.bind.ReaderInfo;
import gov.llnl.utility.xml.bind.WriterInfo;
import gov.nist.physics.n42.reader.RadMeasurementGroupReader;
import gov.nist.physics.n42.writer.RadMeasurementGroupWriter;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author monterial1
 */
@ReaderInfo(RadMeasurementGroupReader.class)
@WriterInfo(RadMeasurementGroupWriter.class)
public class RadMeasurementGroup extends ComplexObject
{
  private String description = null;
  private String uuid = null;
  private final List<RadMeasurement> measurements = new ArrayList<>();
  // references
  private final List<AnalysisResults> analysisResults = new ArrayList<>();  

  public void setDescription(String description)
  {
    this.description = description;
  }

  public void setUUID(String uuid)
  {
    this.uuid = uuid;
  }

  public void addMeasurement(RadMeasurement measurement)
  {
    this.measurements.add(measurement);
  }

  public void addAnalysisResult(AnalysisResults results)
  {
    this.analysisResults.add(results);
  }

  /**
   * @return the description
   */
  public String getDescription()
  {
    return description;
  }

  /**
   * @return the uuid
   */
  public String getUUID()
  {
    return uuid;
  }
  
  /**
   * @return the measurements
   */
  public List<RadMeasurement> getMeasurements()
  {
    return measurements;
  }

  /**
   * @return the analysisResults
   */
  public List<AnalysisResults> getAnalysisResults()
  {
    return analysisResults;
  }

}
