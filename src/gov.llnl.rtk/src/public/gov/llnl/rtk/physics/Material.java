/*
 * Copyright 2019, Lawrence Livermore National Security, LLC. 
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.rtk.physics;

import java.util.Iterator;

/**
 * A Material represents the composition a unit of substance.
 *
 * Materials have property such as age and density.
 *
 * @author nelson85
 */
public interface Material extends Iterable<MaterialComponent>
{

  /**
   * Get the name of this material.
   *
   * @return
   */
  default public String getLabel()
  {
    StringBuilder b = new StringBuilder();
    for (MaterialComponent c : this)
    {
      b.append(c.getNuclide().toString());
    }
    return b.toString();
  }

  /**
   * Get the description of the material.
   *
   * Used by some material libraries.
   *
   * @return the description or null if not available.
   */
  default public String getDescription()
  {
    return null;
  }

  /**
   * Get the comment for this material.
   *
   * Used by some material libraries.
   *
   * @return the comment or null if not available.
   */
  default public String getComment()
  {
    return null;
  }

  /**
   * Get the age of the material.
   *
   * @return age as a quantity.
   */
  Quantity getAge();

  /**
   * Get the density of the material.
   *
   * @return density as a quantity.
   */
  Quantity getDensity();

  /**
   * Get an iterator for traversing the list of components.
   *
   * @return a new iterator.
   */
  @Override
  Iterator<MaterialComponent> iterator();

  /**
   * Get the number of components that make up this material.
   *
   * @return
   */
  int size();

  /**
   * Compute the average Z for the material.
   *
   * @return the average Z of the material.
   */
  default double getAverageZ()
  {
    double weight = 0;
    double Z = 0;
    for (MaterialComponent entry : this)
    {
      weight += entry.getMassFraction();
      Z += entry.getNuclide().getAtomicNumber() * entry.getMassFraction();
    }
    if (weight == 0)
      return 0;
    return Z / weight;
  }

  /**
   * Computes the effective Z for the material.
   *
   * This is based on the x-ray effective Z formula.
   *
   * @return the effective Z of the material.
   */
  default double getEffectiveZ()
  {
    double weight = 0;
    double Z = 0;
    for (MaterialComponent entry : this)
    {
      weight += entry.getMassFraction();
      Z += Math.pow(entry.getNuclide().getAtomicNumber(), 2.94) * entry.getMassFraction();
    }
    if (weight == 0)
      return 0;
    return Math.pow(Z / weight, 1 / 2.94);
  }

  /**
   * Find the component associated with a given nuclide.
   *
   * @param nuclide
   * @return the component or null if the nuclide is not found.
   */
  default MaterialComponent findNuclide(Nuclide nuclide)
  {
    for (MaterialComponent iter : this)
    {
      if (iter.getNuclide() == nuclide)
        return iter;
    }
    return null;
  }

}
