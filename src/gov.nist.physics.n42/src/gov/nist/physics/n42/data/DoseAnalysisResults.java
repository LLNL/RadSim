/*
 * Copyright 2022, Lawrence Livermore National Security, LLC.
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.nist.physics.n42.data;

import gov.llnl.utility.xml.bind.ReaderInfo;
import gov.llnl.utility.xml.bind.WriterInfo;
import gov.nist.physics.n42.reader.DoseAnalysisResultsReader;
import gov.nist.physics.n42.writer.DoseAnalysisResultsWriter;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

/**
 *
 * @author nelson85
 */
@ReaderInfo(DoseAnalysisResultsReader.class)
@WriterInfo(DoseAnalysisResultsWriter.class)
public class DoseAnalysisResults extends ComplexObject
{
  private Quantity averageDoseRate;
  private Quantity averageDoseRateUncertainty;
  private Quantity maximumDoseRate;
  private Quantity minimumDoseRate;
  private Quantity backgroundDoseRate;
  private Quantity backgroundDoseRateUncertainty;
  private Quantity totalDose;

  public void setAverageDoseRate(Quantity value)
  {
    this.averageDoseRate = value;
  }

  public void setAverageDoseRateUncertainty(Quantity value)
  {
    this.averageDoseRateUncertainty = value;
  }

  public void setMaximumDoseRate(Quantity value)
  {
    this.maximumDoseRate = value;
  }

  public void setMinimumDoseRate(Quantity value)
  {
    this.minimumDoseRate = value;
  }

  public void setBackgroundDoseRate(Quantity value)
  {
    this.backgroundDoseRate = value;
  }

  public void setBackgroundDoseRateUncertainty(Quantity value)
  {
    this.backgroundDoseRateUncertainty = value;
  }

  public void setTotalDose(Quantity value)
  {
    this.totalDose = value;
  }

  public Quantity getAverageDoseRateUncertainty()
  {
    return averageDoseRateUncertainty;
  }

  public Quantity getMaximumDoseRate()
  {
    return maximumDoseRate;
  }

  public Quantity getMinimumDoseRate()
  {
    return minimumDoseRate;
  }

  public Quantity getBackgroundDoseRate()
  {
    return backgroundDoseRate;
  }

  public Quantity getBackgroundDoseRateUncertainty()
  {
    return backgroundDoseRateUncertainty;
  }

  public Quantity getAverageDoseRate()
  {
    return averageDoseRate;
  }

  public Quantity getTotalDose()
  {
    return totalDose;
  }

}
