/*
 * Copyright 2022, Lawrence Livermore National Security, LLC.
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.nist.physics.n42.data;

import gov.nist.physics.n42.reader.SpectrumReader;
import gov.llnl.utility.xml.bind.ReaderInfo;
import gov.llnl.utility.xml.bind.WriterInfo;
import gov.nist.physics.n42.writer.SpectrumWriter;
import java.time.Duration;
import java.util.function.Consumer;

/**
 *
 * @author nelson85
 */
@ReaderInfo(SpectrumReader.class)
@WriterInfo(SpectrumWriter.class)
public class Spectrum extends ComplexObject
{
  private EnergyCalibration energyCalibration;
  private FWHMCalibration fwhmCalibration;
  private EfficiencyCalibration fullEnergyPeakEfficiencyCalibration;
  private EfficiencyCalibration intrinsicSingleEscapePeakEfficiencyCalibration;
  private EfficiencyCalibration intrinsicDoubleEscapePeakEfficiencyCalibration;
  private EfficiencyCalibration intrinsicFullEnergyPeakEfficiencyCalibration;
  private RadDetectorInformation detector;
  private Duration LiveTimeDuration;
  private double[] countData;

  /**
   * @return the energyCalibration
   */
  public EnergyCalibration getEnergyCalibration()
  {
    return energyCalibration;
  }

  /**
   * @param energyCalibration the energyCalibration to set
   */
  public void setEnergyCalibration(EnergyCalibration energyCalibration)
  {
    this.energyCalibration = energyCalibration;
  }

  public void setFWHMEfficiencyCalibration(FWHMCalibration calibration)
  {
    this.fwhmCalibration = calibration;
  }

  public void setFullEnergyPeakEfficiencyCalibration(EfficiencyCalibration calibration)
  {
    this.fullEnergyPeakEfficiencyCalibration = calibration;
  }

  public void setIntrinsicSingleEscapePeakEfficiencyCalibration(EfficiencyCalibration calibration)
  {
    this.intrinsicSingleEscapePeakEfficiencyCalibration = calibration;
  }

  public void setIntrinsicDoubleEscapePeakEfficiencyCalibration(EfficiencyCalibration calibration)
  {
    this.intrinsicDoubleEscapePeakEfficiencyCalibration = calibration;
  }

  public void setIntrinsicFullEnergyPeakEfficiencyCalibration(EfficiencyCalibration calibration)
  {
    this.intrinsicFullEnergyPeakEfficiencyCalibration = calibration;

  }

  /**
   * @return the radDetector
   */
  public RadDetectorInformation getDetector()
  {
    return detector;
  }

  /**
   * @param detector the radDetector to set
   */
  public void setDetector(RadDetectorInformation detector)
  {
    this.detector = detector;
  }

  /**
   * @return the LiveTimeDuration
   */
  public Duration getLiveTimeDuration()
  {
    return LiveTimeDuration;
  }

  /**
   * @param value the LiveTimeDuration to set
   */
  public void setLiveTimeDuration(Duration value)
  {
    this.LiveTimeDuration = value;
  }

  /**
   * @return the countData
   */
  public double[] getCountData()
  {
    return countData;
  }

  /**
   * @param countData the countData to set
   */
  public void setCountData(double[] countData)
  {
    this.countData = countData;
  }

  /**
   * @return the fwhmCalibration
   */
  public FWHMCalibration getFwhmCalibration()
  {
    return fwhmCalibration;
  }

  /**
   * @param fwhmCalibration the fwhmCalibration to set
   */
  public void setFwhmCalibration(FWHMCalibration fwhmCalibration)
  {
    this.fwhmCalibration = fwhmCalibration;
  }

  /**
   * @return the fullEnergyPeakEfficiencyCalibration
   */
  public EfficiencyCalibration getFullEnergyPeakEfficiencyCalibration()
  {
    return fullEnergyPeakEfficiencyCalibration;
  }

  /**
   * @return the intrinsicSingleEscapePeakEfficiencyCalibration
   */
  public EfficiencyCalibration getIntrinsicSingleEscapePeakEfficiencyCalibration()
  {
    return intrinsicSingleEscapePeakEfficiencyCalibration;
  }

  /**
   * @return the intrinsicDoubleEscapePeakEfficiencyCalibration
   */
  public EfficiencyCalibration getIntrinsicDoubleEscapePeakEfficiencyCalibration()
  {
    return intrinsicDoubleEscapePeakEfficiencyCalibration;
  }

  /**
   * @return the intrinsicFullEnergyPeakEfficiencyCalibration
   */
  public EfficiencyCalibration getIntrinsicFullEnergyPeakEfficiencyCalibration()
  {
    return intrinsicFullEnergyPeakEfficiencyCalibration;
  }

  @Override
  public void visitReferencedObjects(Consumer<ComplexObject> visitor)
  {

    if (this.detector != null)
      visitor.accept(this.detector);
    if (this.energyCalibration != null)
      visitor.accept(this.energyCalibration);
    if (this.fwhmCalibration != null)
      visitor.accept(this.fwhmCalibration);
    if (this.fullEnergyPeakEfficiencyCalibration != null)
      visitor.accept(this.fullEnergyPeakEfficiencyCalibration);
    if (this.intrinsicDoubleEscapePeakEfficiencyCalibration != null)
      visitor.accept(this.intrinsicDoubleEscapePeakEfficiencyCalibration);
    if (this.intrinsicFullEnergyPeakEfficiencyCalibration != null)
      visitor.accept(this.intrinsicFullEnergyPeakEfficiencyCalibration);
    if (this.intrinsicSingleEscapePeakEfficiencyCalibration != null)
      visitor.accept(this.intrinsicSingleEscapePeakEfficiencyCalibration);
  }

}
