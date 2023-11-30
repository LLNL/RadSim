/* 
 * Copyright (c) 2016, Lawrence Livermore National Security, LLC.
 * All rights reserved.
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.rtk.physics;

import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * @author nelson85
 */
 public class MaterialImpl implements Material
{
  public ArrayList<Component> entries = new ArrayList<>();
  public double density = 1 * Units.get("density:g/cm3");
  public double age = 20 * Units.get("time:a");

  @Override
  public Iterator<Component> iterator()
  {
    return this.entries.iterator();
  }

  public MaterialImpl()
  {
  }

  @Override
  public int size()
  {
    return entries.size();
  }


  /**
   * Get the age of the material.
   *
   * @return age in seconds.
   */
  @Override
  public double getAge()
  {
    return age;
  }

  /**
   * Set the age of the material.
   *
   * Age is used for decaying radioactive material.
   *
   * @param age in seconds.
   */
  public void setAge(double age)
  {
    this.age = age;
  }

  /**
   * Get the density of the material.
   *
   * @return density in k/m^3.
   */
  @Override
  public double getDensity()
  {
    return density;
  }

  /**
   * Set the density of the material.
   *
   * @param density in k/m^3.
   */
  public void setDensity(double density)
  {
    this.density = density;
  }

  public void addEntry(Component entry)
  {
    entries.add(entry);
  }

  public void addElement(String el, double mass_fraction, double activity)
  {
    {
      Nuclide nuclide = Nuclides.get(el);
      if (nuclide == null)
        throw new NullPointerException("Nuclide for " + el + " not found");

      ComponentImpl existing = (ComponentImpl) this.findNuclide(nuclide);
      if (existing != null)
      {
        existing.setMassFraction(existing.massFraction + mass_fraction);
        existing.setActivity(existing.activity + mass_fraction);
        return;
      }
      ComponentImpl entry = new ComponentImpl();
      entry.setNuclide(nuclide);
      entry.setMassFraction(mass_fraction);
      entry.setActivity(activity);
      entries.add(entry);
    }
  }

  @Override
  public Component findNuclide(Nuclide get)
  {
    for (Component iter : this)
    {
      if (iter.getNuclide() == get)
        return iter;
    }
    return null;
  }
}
