/*
 * Copyright 2019, Lawrence Livermore National Security, LLC.
 * All rights reserved
 *
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.rtk.physics;

/**
 * Used to define units that apply to a quantity.
 *
 * @author nelson85
 */
public enum PhysicalProperty
{
  UNKNOWN,
  // base units
  MASS, // M
  LENGTH, // L
  TIME, // T
  // derived units
  ACTIVITY, // 1/T
  SPECIFIC_ACTIVITY, // 1/T/M
  DENSITY, // M/L^3
  AREAL_DENSITY;

  // M/L^2
  @Override
  public String toString()
  {
    return super.toString().toLowerCase();
  }

}
