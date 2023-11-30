/*
 * Copyright 2022, Lawrence Livermore National Security, LLC.
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.nist.physics.n42.data;

import gov.llnl.utility.xml.bind.ReaderInfo;
import gov.llnl.utility.xml.bind.WriterInfo;
import gov.nist.physics.n42.reader.GrossCountAnalysisResultsReader;
import gov.nist.physics.n42.writer.GrossCountAnalysisResultsWriter;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

/**
 *
 * @author nelson85
 */
@ReaderInfo(GrossCountAnalysisResultsReader.class)
@WriterInfo(GrossCountAnalysisResultsWriter.class)
public class GrossCountAnalysisResults extends ComplexObject
{
  private Quantity averageCountRate;
  private Quantity averageCountRateUncertainty;
  private Quantity maximumCountRate;
  private Quantity minimumCountRate;
  private Quantity totalCounts;
  private Quantity backgroundCountRate;
  private Quantity backgroundCountRateUncertainty;

  public Quantity getAverageCountRate()
  {
    return averageCountRate;
  }

  public Quantity getAverageCountRateUncertainty()
  {
    return averageCountRateUncertainty;
  }

  public Quantity getMaximumCountRate()
  {
    return maximumCountRate;
  }

  public Quantity getMinimumCountRate()
  {
    return minimumCountRate;
  }

  public Quantity getBackgroundCountRate()
  {
    return backgroundCountRate;
  }

  public Quantity getBackgroundCountRateUncertainty()
  {
    return backgroundCountRateUncertainty;
  }

  public void setAverageCountRate(Quantity value)
  {
    this.averageCountRate = value;
  }

  public void setAverageCountRateUncertainty(Quantity value)
  {
    this.averageCountRateUncertainty = value;
  }

  public void setMaximumCountRate(Quantity value)
  {
    this.maximumCountRate = value;
  }

  public void setMinimumCountRate(Quantity value)
  {
    this.minimumCountRate = value;
  }

  public void setBackgroundCountRate(Quantity value)
  {
    this.backgroundCountRate = value;
  }

  public void setBackgroundCountRateUncertainty(Quantity value)
  {
    this.backgroundCountRateUncertainty = value;
  }

  /**
   * @return the totalCounts
   */
  public Quantity getTotalCounts()
  {
    return totalCounts;
  }

  /**
   * @param totalCounts the totalCounts to set
   */
  public void setTotalCounts(Quantity totalCounts)
  {
    this.totalCounts = totalCounts;
  }

}
