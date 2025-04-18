/*
 * Copyright 2024, Lawrence Livermore National Security, LLC.
 * All rights reserved.
 *
 * Terms and conditions are given in "Notice" file.
 */
package gov.bnl.nndc.ensdf.decay;

import gov.bnl.nndc.ensdf.EnsdfBeta;
import gov.llnl.rtk.physics.Beta;
import gov.llnl.rtk.physics.Transition;
import gov.llnl.rtk.physics.Quantity;
import java.io.Serializable;

/**
 * Implementation for Beta from ENSDF.
 *
 * The shape will be computed by other codes usng the maximum energy.
 *
 * @author nelson85
 */
public class BetaImpl implements Beta, Serializable
{

  final EnsdfBeta record;
  final Transition transition;
  final Quantity energy;
  final Quantity intensity;
  final Quantity logFT;
  final String forbiddenness;

  public BetaImpl(Transition transition, EnsdfBeta record, double intensity)
  {
    this.transition = transition;
    this.record = record;
    this.intensity = Quantity.scalar(intensity);

    if (record.E.isSpecified())
      this.energy = Quantity.of(record.E.toDouble(), "keV"); // FIXME VC: When uncertainty is ready, use another Quantity constructor

    else
      this.energy = Quantity.of(record.dataSet.parents.get(0).QP.toDouble() - record.level.E.toDouble(), "keV"); // FIXME VC: When uncertainty is ready, use another Quantity constructor 

    this.forbiddenness = record.UN;

    // FIXME what if it isn't specified
    if (record.LOGFT.isSpecified())
      this.logFT = Quantity.scalar(record.LOGFT.toDouble());
    else
      this.logFT = Quantity.UNSPECIFIED;
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
  public Quantity getLogFT()
  {
      return this.logFT;
  }

  @Override
  public String getForbiddenness()
  {
    return this.forbiddenness;
  }

  @Override
  public Transition getOrigin()
  {
    return this.transition;
  }

}
