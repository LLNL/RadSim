/*
 * Copyright 2022, Lawrence Livermore National Security, LLC.
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.nist.physics.n42.data;

import gov.nist.physics.n42.reader.GrossCountsReader;
import gov.llnl.utility.xml.bind.ReaderInfo;
import gov.llnl.utility.xml.bind.WriterInfo;
import gov.nist.physics.n42.writer.GrossCountsWriter;
import java.time.Duration;
import java.util.function.Consumer;

/**
 *
 * @author nelson85
 */
@ReaderInfo(GrossCountsReader.class)
@WriterInfo(GrossCountsWriter.class)
public class GrossCounts extends ComplexObject
{
  private RadDetectorInformation detector;
  private double[] counts;
  private Duration duration;
  private EnergyWindows energyWindows;

  /**
   * @return the detector
   */
  public RadDetectorInformation getDetector()
  {
    return detector;
  }

  /**
   * @param detector the detector to set
   */
  public void setDetector(RadDetectorInformation detector)
  {
        this.detector = detector;
  }

  public void setEnergyWindows(EnergyWindows energyWindows)
  {
    this.energyWindows = energyWindows;
  }

  /**
   * @return the Counts
   */
  public Duration getLiveTimeDuration()
  {
    return duration;
  }

  /**
   * @param duration the value to set
   */
  public void setLiveTimeDuration(Duration duration)
  {
    this.duration = duration;
  }

  /**
   * @return the Counts
   */
  public double[] getCountData()
  {
    return counts;
  }

  /**
   * @param counts the value to set
   */
  public void setCountData(double[] counts)
  {
    this.counts = counts;
  }

  /**
   * @return the energyWindows
   */
  public EnergyWindows getEnergyWindows()
  {
    return energyWindows;
  }

  @Override
  public void visitReferencedObjects(Consumer<ComplexObject> visitor)
  {
    if (this.detector != null)
      visitor.accept(this.detector);
    if (this.energyWindows != null)
      visitor.accept(this.energyWindows);
  }

}
