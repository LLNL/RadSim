/*
 * Copyright 2022, Lawrence Livermore National Security, LLC.
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.nist.physics.n42.data;

/**
 *
 * 1-sigma absolute uncertainty in the value of NuclideIDConfidenceValue.
 *
 * @author nelson85
 */
public class NuclideIdentificationConfidenceUncertainty implements NuclideIdentificationConfidence
{
  private double value;

  public NuclideIdentificationConfidenceUncertainty(double value)
  {
    this.value = value;
  }

  public double getValue()
  {
    return value;
  }

  public void setValue(double value)
  {
    this.value = value;
  }
}
