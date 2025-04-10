/*
 * Copyright 2024, Lawrence Livermore National Security, LLC.
 * All rights reserved.
 *
 * Terms and conditions are given in "Notice" file.
 */
package gov.bnl.nndc.ensdf.decay;

import gov.bnl.nndc.ensdf.EnsdfAlpha;
import gov.llnl.rtk.physics.Alpha;
import gov.llnl.rtk.physics.Transition;
import gov.llnl.rtk.physics.Quantity;
import java.io.Serializable;

/**
 *
 * @author nelson85
 */
public class AlphaImpl implements Alpha, Serializable
{

  private final DecayTransitionImpl parent;
  private final EnsdfAlpha record;
  private final Quantity intensity;
  private final Quantity energy;
  private final Quantity hindrance;

  public AlphaImpl(DecayTransitionImpl dt, EnsdfAlpha alpha, double probability)
  {
    this.parent = dt;
    this.record = alpha;
    this.intensity = Quantity.scalar(probability);
    this.energy = Quantity.of(alpha.E.toDouble(), "keV"); // FIXME VC: When uncertainty is ready, use another Quantity constructor
    if (alpha.HF.isSpecified())
      this.hindrance = Quantity.scalar(alpha.HF.toDouble());
    else
      this.hindrance = Quantity.UNSPECIFIED;
  }

  @Override
  public Quantity getEnergy()
  {
    return this.energy;
  }

  @Override
  public Quantity getIntensity()
  {
    return this.intensity;
  }

  @Override
  public Quantity getHindrance()
  {
    return this.hindrance;
  }

  @Override
  public Transition getOrigin()
  {
    return this.parent;
  }

}
