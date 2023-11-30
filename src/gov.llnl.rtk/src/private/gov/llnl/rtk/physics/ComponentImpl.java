/*
 * Copyright 2016, Lawrence Livermore National Security, LLC. 
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.rtk.physics;

import gov.llnl.utility.UUIDUtilities;
import java.io.Serializable;

/**
 *
 * @author nelson85
 */
 class ComponentImpl implements Component, Serializable
{
  private static final long serialVersionUID = UUIDUtilities.createLong("ComponentImpl");
  Nuclide nuclide;
  double doseFraction;
  double massFraction;
  double activity = 1;

  public void setDoseFraction(double doseFraction)
  {
    this.doseFraction = doseFraction;
  }

  public void setActivity(double activity)
  {
    this.activity = activity;
  }

  @Override
  public Nuclide getNuclide()
  {
    return nuclide;
  }

  @Override
  public double getDoseFraction()
  {
    return doseFraction;
  }

  @Override
  public double getActivity()
  {
    return activity;
  }

  public void setNuclide(Nuclide nuclide)
  {
    this.nuclide = nuclide;
  }

  /**
   * @return the massFraction
   */
  @Override
  public double getMassFraction()
  {
    return massFraction;
  }

  /**
   * @param massFraction the massFraction to set
   */
  public void setMassFraction(double massFraction)
  {
    this.massFraction = massFraction;
  }

}
