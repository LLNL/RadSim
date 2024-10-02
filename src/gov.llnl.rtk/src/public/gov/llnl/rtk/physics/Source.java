/*
 * Copyright 2019, Lawrence Livermore National Security, LLC.
 * All rights reserved
 *
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.rtk.physics;

/**
 *
 * @author nelson85
 */
public interface Source
{
  Nuclide getNuclide();

  double getActivity();

  default double getAtoms()
  {
    return getActivity()/getNuclide().getDecayConstant();
  }

    public static Source of(Nuclide nuclide)
  {
    SourceImpl source = new SourceImpl();
    source.nuclide = nuclide;
    return source;
  }

  public static Source fromActivity(Nuclide nuclide, double activity, ActivityUnit units)
  {
    if (nuclide.getDecayConstant()==0)
      throw new IllegalArgumentException("Nuclide is stable");
    SourceImpl source = new SourceImpl();
    source.nuclide = nuclide;
    source.activity = activity * units.getFactor();
    source.atoms = source.activity/nuclide.getDecayConstant();
    return source;
  }

  public static Source fromAtoms(Nuclide nuclide, double atoms)
  {
    SourceImpl source = new SourceImpl();
    source.nuclide = nuclide;
    source.activity = nuclide.getDecayConstant() * atoms;
    source.atoms = atoms;
    return source;
  }

}
