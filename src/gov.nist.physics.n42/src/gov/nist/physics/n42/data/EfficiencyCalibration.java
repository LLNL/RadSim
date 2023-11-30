/*
 * Copyright 2022, Lawrence Livermore National Security, LLC.
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.nist.physics.n42.data;

import gov.llnl.utility.xml.bind.ReaderInfo;
import gov.llnl.utility.xml.bind.WriterInfo;
import gov.nist.physics.n42.reader.EfficiencyCalibrationReader;
import gov.nist.physics.n42.writer.EfficiencyCalibrationWriter;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

/**
 * A data type for efficiency calibration.
 *
 * @author monterial1
 */
@ReaderInfo(EfficiencyCalibrationReader.class)
@WriterInfo(EfficiencyCalibrationWriter.class)
public class EfficiencyCalibration extends ComplexObject
{
  private double[] energyValues;
  private double[] efficiencyValues;
  private double[] efficiencyUncertaintyValues;
  private Instant calibrationDateTime;

  public double[] getEnergyValues()
  {
    return energyValues;
  }

  public void setEnergyValues(double[] energyValues)
  {
    this.energyValues = energyValues;
  }

  /**
   * @return The list of efficiency values as decimal fractions; i.e., normally
   * between 0.0 and 1.0.
   */
  public double[] getEfficiencyValues()
  {
    return efficiencyValues;
  }

  public void setEfficiencyValues(double[] efficiencyValues)
  {
    this.efficiencyValues = efficiencyValues;
  }

  /**
   *
   * @return The list of the 1-sigma absolute uncertainties in the
   * EfficiencyValues.
   */
  public double[] getEfficiencyUncertaintyValues()
  {
    return efficiencyUncertaintyValues;
  }

  public void setEfficiencyUncertaintyValues(double[] efficiencyUncertaintyValues)
  {
    this.efficiencyUncertaintyValues = efficiencyUncertaintyValues;
  }

  public Instant getCalibrationDateTime()
  {
    return calibrationDateTime;
  }

  public void setCalibrationDateTime(Instant calibrationDateTime)
  {
    this.calibrationDateTime = calibrationDateTime;
  }

}
