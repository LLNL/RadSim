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
public interface Layer
{
  /**
   * Get the age of the material.
   *
   * @return get age in seconds.
   */
  double getAge();

  double getDensity();

  Geometry getGeometry();

  String getLabel();

  double getMass();

  Material getMaterial();

  double getThickness();
  
}
