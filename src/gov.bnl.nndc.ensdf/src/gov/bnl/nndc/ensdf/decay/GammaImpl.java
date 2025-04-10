/*
 * Copyright 2024, Lawrence Livermore National Security, LLC.
 * All rights reserved.
 *
 * Terms and conditions are given in "Notice" file.
 */
package gov.bnl.nndc.ensdf.decay;

import gov.bnl.nndc.ensdf.EnsdfGamma;
import gov.llnl.rtk.physics.Gamma;
import gov.llnl.rtk.physics.Quantity;
import gov.llnl.rtk.physics.Transition;
import java.io.Serializable;

/**
 *
 * @author nelson85
 */
public class GammaImpl implements Gamma, Serializable
{

  public EnsdfGamma record;
  Quantity energy;
  Quantity intensity; // intensity per decay following this branch (not per 100 parent decays)
  private final Transition parent;
  
  public GammaImpl(Transition parent, EnsdfGamma record, double intensity)
  {
    this.parent = parent;
    this.record= record;
    this.energy = Quantity.of(record.E.toDouble(), "keV"); // FIXME VC: When uncertainty is ready, use another Quantity constructor
    this.intensity = Quantity.scalar(intensity);
  }
  @Override
  public Quantity getEnergy()
  {    
    return energy;
  }

  @Override
  public Quantity getIntensity()
  {
    return this.intensity;
  }

  @Override
  public Transition getOrigin()
  {
    return parent;
  }
  
}