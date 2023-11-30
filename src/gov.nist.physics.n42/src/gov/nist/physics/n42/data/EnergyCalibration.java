/*
 * Copyright 2022, Lawrence Livermore National Security, LLC.
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.nist.physics.n42.data;

import gov.nist.physics.n42.reader.EnergyCalibrationReader;
import gov.llnl.utility.xml.bind.ReaderInfo;
import gov.llnl.utility.xml.bind.WriterInfo;
import gov.nist.physics.n42.writer.EnergyCalibrationWriter;
import java.time.Instant;

/**
 *
 * @author nelson85
 */
@ReaderInfo(EnergyCalibrationReader.class)
@WriterInfo(EnergyCalibrationWriter.class)
public class EnergyCalibration extends ComplexObject
{
  private double[] coefficients;
  private double[] energyBoundaries;
  private double[] energy;
  private double[] energyDeviation;
  private Instant calibrationDateTime;

  /**
   * @return the coefficients
   */
  public double[] getCoefficients()
  {
    return coefficients;
  }

  public Instant getCalibrationDateTime()
  {
    return calibrationDateTime;
  }

  public void setCalibrationDateTime(Instant calibrationDateTime)
  {
    this.calibrationDateTime = calibrationDateTime;
  }

  /**
   * @param coefficients the coefficients to set
   */
  public void setCoefficients(double[] coefficients)
  {
    this.coefficients = coefficients;
  }

  /**
   * @return the energyBoundaries
   */
  public double[] getEnergyBoundaries()
  {
    return energyBoundaries;
  }

  /**
   * @param energyBoundaries the energyBoundaries to set
   */
  public void setEnergyBoundaries(double[] energyBoundaries)
  {
    this.energyBoundaries = energyBoundaries;
  }

  /**
   * @return the energy
   */
  public double[] getEnergy()
  {
    return energy;
  }

  /**
   * @param energy the energy to set
   */
  public void setEnergy(double[] energy)
  {
    this.energy = energy;
  }

  /**
   * @return the energyDeviation
   */
  public double[] getEnergyDeviation()
  {
    return energyDeviation;
  }

  /**
   * @param energyDeviation the energyDeviation to set
   */
  public void setEnergyDeviation(double[] energyDeviation)
  {
    this.energyDeviation = energyDeviation;
  }

}
