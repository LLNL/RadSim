/*
 * Copyright 2022, Lawrence Livermore National Security, LLC.
 * All rights reserved.
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.rtk.physics;

/**
 *
 * @author nelson85
 */
public interface ScatteringDistribution
{
  /**
   * Get the cross section for a specific energy.
   * 
   * @param energyIncident is the energy of the incident photon.
   * @param energyEmitted is the energy of the emitted photon.
   * @return in SI units.
   */
  public double getCrossSection(Quantity energyIncident, Quantity energyEmitted);
}
