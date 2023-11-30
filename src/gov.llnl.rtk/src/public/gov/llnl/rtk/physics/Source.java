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
}
