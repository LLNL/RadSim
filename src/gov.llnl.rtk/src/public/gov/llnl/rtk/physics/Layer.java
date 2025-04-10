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
  String getLabel();

  Layer getPrevious();

  Material getMaterial();

  Geometry getGeometry();

  /**
   * Get mass.
   *
   * @return
   */
  Quantity getMass();

  /**
   * Get the thickness of the shell.
   *
   * @return
   */
  Quantity getThickness();

  default Quantity getOuter()
  {
    Layer previous = this.getPrevious();
    if (previous != null)
      return this.getThickness().plus(previous.getOuter());
    return this.getThickness();
  }

  default Quantity getInner()
  {
    Layer previous = this.getPrevious();
    if (previous != null)
      return previous.getOuter();
    return Quantity.of(0, this.getThickness().getUnits());
  }
  
  default Quantity getVolume()
  {
    return this.getGeometry().computeVolume(this.getInner(), this.getThickness());
  }

}
