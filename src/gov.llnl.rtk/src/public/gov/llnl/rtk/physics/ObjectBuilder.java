/*
 * Copyright 2019, Lawrence Livermore National Security, LLC. 
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.rtk.physics;


/**
 * Base class for builders so that the user can set their preferred units.
 * 
 * @author nelson85
 */
public class ObjectBuilder
{
  UnitSpecification  units = new UnitSpecification();

  public ObjectBuilder units(Units units)
  {
    this.units.setUnits(units);
    return this;
  }
}
