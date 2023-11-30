/*
 * Copyright 2019, Lawrence Livermore National Security, LLC. 
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.rtk.physics;

import java.util.Iterator;

/**
 *
 * @author nelson85
 */
public interface Material extends Iterable<Component>
{
  Component findNuclide(Nuclide get);

  /**
   * Get the age of the material.
   *
   * @return age in seconds.
   */
  double getAge();


  /**
   * Get the density of the material.
   *
   * @return density in k/m^3.
   */
  double getDensity();

  
  @Override
  Iterator<Component> iterator();

  int size();
  
  default double getAverageZ()
  {
    double weight = 0;
    double Z = 0;
    for (Component entry : this)
    {
      weight += entry.getMassFraction();
      Z += entry.getNuclide().getAtomicNumber() * entry.getMassFraction();
    }
    if (weight == 0)
      return 0;
    return Z / weight;
  
  }
  
  default double getEffectiveZ()
           {
    double weight = 0;
    double Z = 0;
    for (Component entry : this)
    {
      weight += entry.getMassFraction();
      Z += Math.pow(entry.getNuclide().getAtomicNumber(), 2.94) * entry.getMassFraction();
    }
    if (weight == 0)
      return 0;
    return Math.pow(Z / weight, 1 / 2.94);
  }

}
