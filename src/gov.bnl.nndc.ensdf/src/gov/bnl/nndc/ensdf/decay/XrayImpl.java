/*
 * Copyright 2024, Lawrence Livermore National Security, LLC.
 * All rights reserved.
 *
 * Terms and conditions are given in "Notice" file.
 */
package gov.bnl.nndc.ensdf.decay;

import gov.llnl.rtk.physics.Quantity;
import gov.llnl.rtk.physics.Transition;
import gov.llnl.rtk.physics.Xray;
import java.io.Serializable;

/**
 *
 * @author nelson85
 */
public class XrayImpl implements Xray, Serializable
{

  private final DecayTransitionImpl origin;
  private final Quantity energy;
  private final Quantity intensity;
  private final String name;

  public XrayImpl(DecayTransitionImpl dt, Xray xray, double d)
  {
    this.origin = dt;
    this.energy = xray.getEnergy();
    this.intensity = xray.getIntensity().scaled(d);
    this.name = xray.getName();
  }

  @Override
  public Transition getOrigin()
  {
    return this.origin;
  }

  @Override
  public String getName()
  {
    return this.name;
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
  
}
