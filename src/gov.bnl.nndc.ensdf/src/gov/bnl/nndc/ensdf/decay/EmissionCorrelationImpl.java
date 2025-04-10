/*
 * Copyright 2024, Lawrence Livermore National Security, LLC.
 * All rights reserved.
 *
 * Terms and conditions are given in "Notice" file.
 */
package gov.bnl.nndc.ensdf.decay;

import gov.llnl.rtk.physics.Emission;
import gov.llnl.rtk.physics.EmissionCorrelation;
import java.io.Serializable;

/**
 *
 * @author nelson85
 */
public class EmissionCorrelationImpl implements EmissionCorrelation, Serializable
{
  final private Emission primary;
  final private Emission secondary;
  final private double probability;

  public EmissionCorrelationImpl(Emission primary, Emission secondary, double probability)
  {
    this.primary = primary;
    this.secondary = secondary;
    this.probability = probability;
  }

  @Override
  public Emission getPrimary()
  {
    return this.primary;
  }

  @Override
  public Emission getSecondary()
  {
    return this.secondary;
  }

  @Override
  public double getProbability()
  {
    return this.probability;
  }
  
}
