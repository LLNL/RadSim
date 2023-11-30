/*
 * Copyright 2022, Lawrence Livermore National Security, LLC.
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.nist.physics.n42.data;

import gov.llnl.utility.xml.bind.ReaderInfo;
import gov.llnl.utility.xml.bind.WriterInfo;
import gov.nist.physics.n42.reader.FWHMCalibrationReader;
import gov.nist.physics.n42.writer.FWHMCalibrationWriter;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;


/**
 *  The FWHM calibration for a gamma radiation detector; i.e., FWHM as a function of energy.
 * 
 * @author monterial1
 */
@ReaderInfo(FWHMCalibrationReader.class)
@WriterInfo(FWHMCalibrationWriter.class)
public class FWHMCalibration extends ComplexObject
{
  private double[] energyValues;
  private double[] fwhmValues;
  private double[] fwhmUncertaintyValues;
  private Instant calibrationDateTime;

  /**
   * 
   * @return A list of energy values, in units of keV; the energies shall appear in 
   * the list in strictly increasing order.  This element appears paired with an 
   * element that provides a corresponding list of other values, such as the 
   * EnergyDeviationValues, FWHMValues, or EfficiencyValues elements. 
   * The number and order of corresponding values in the pair of lists must match.
   */
  public double[] getEnergyValues()
  {
    return energyValues;
  }

  public void setEnergyValues(double[] energyValues)
  {
    this.energyValues = energyValues;
  }

  /**
   * 
   * @return A list of FWHM values, in units of keV.  The number and order of 
   * corresponding values in the EnergyValues and FWHMValues lists must match.
   */
  public double[] getFwhmValues()
  {
    return fwhmValues;
  }

  public void setFwhmValues(double[] fwhmValues)
  {
    this.fwhmValues = fwhmValues;
  }

  public double[] getFwhmUncertaintyValues()
  {
    return fwhmUncertaintyValues;
  }

  public void setFwhmUncertaintyValues(double[] fwhmUncertaintyValues)
  {
    this.fwhmUncertaintyValues = fwhmUncertaintyValues;
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
