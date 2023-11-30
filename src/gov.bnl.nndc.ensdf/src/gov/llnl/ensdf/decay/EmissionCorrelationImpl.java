/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gov.llnl.ensdf.decay;

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
