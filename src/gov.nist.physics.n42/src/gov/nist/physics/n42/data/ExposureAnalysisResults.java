/*
 * Copyright 2022, Lawrence Livermore National Security, LLC.
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.nist.physics.n42.data;

import gov.llnl.utility.xml.bind.ReaderInfo;
import gov.llnl.utility.xml.bind.WriterInfo;
import gov.nist.physics.n42.reader.ExposureAnalysisResultsReader;
import gov.nist.physics.n42.writer.ExposureAnalysisResultsWriter;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

/**
 *
 * @author nelson85
 */
@ReaderInfo(ExposureAnalysisResultsReader.class)
@WriterInfo(ExposureAnalysisResultsWriter.class)
public class ExposureAnalysisResults extends ComplexObject
{
  private Quantity averageExposureRate;
  private Quantity averageExposureRateUncertainty;
  private Quantity maximumExposureRate;
  private Quantity minimumExposureRate;
  private Quantity backgroundExposureRate;
  private Quantity backgroundExposureRateUncertainty;
  private Quantity totalExposure;
  
  /**
   * @return the averageExposureRate
   */
  public Quantity getAverageExposureRate()
  {
    return averageExposureRate;
  }

  /**
   * @return the averageExposureRateUncertainty
   */
  public Quantity getAverageExposureRateUncertainty()
  {
    return averageExposureRateUncertainty;
  }

  /**
   * @return the maximumExposureRate
   */
  public Quantity getMaximumExposureRate()
  {
    return maximumExposureRate;
  }

  /**
   * @return the minimumExposureRate
   */
  public Quantity getMinimumExposureRate()
  {
    return minimumExposureRate;
  }

  /**
   * @return the backgroundExposureRate
   */
  public Quantity getBackgroundExposureRate()
  {
    return backgroundExposureRate;
  }

  /**
   * @return the backgroundExposureRateUncertainty
   */
  public Quantity getBackgroundExposureRateUncertainty()
  {
    return backgroundExposureRateUncertainty;
  }

  /**
   * @return the totalExposure
   */
  public Quantity getTotalExposure()
  {
    return totalExposure;
  }


  public void setAverageExposureRate(Quantity value)
  {
    this.averageExposureRate = value;
  }

  public void setAverageExposureRateUncertainty(Quantity value)
  {
    this.averageExposureRateUncertainty = value;
  }

  public void setMaximumExposureRate(Quantity value)
  {
    this.maximumExposureRate = value;
  }

  public void setMinimumExposureRate(Quantity value)
  {
    this.minimumExposureRate = value;
  }

  public void setBackgroundExposureRate(Quantity value)
  {
    this.backgroundExposureRate = value;
  }

  public void setBackgroundExposureRateUncertainty(Quantity value)
  {
    this.backgroundExposureRateUncertainty = value;
  }

  public void setTotalExposure(Quantity value)
  {
    this.totalExposure = value;
  }
  
}
