/*
 * Copyright (c) 2016, Lawrence Livermore National Security, LLC.
 * All rights reserved.
 *
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.rtk.physics;

/**
 * Conversion for units.
 *
 * Whenever receiving an item from an program multiply by the incoming units to
 * convert to SI. When passing an SI unit to something requiring a different
 * unit divide by the corresponding unit.
 *
 * {@code
 * <pre>
 *
 *     double processToLengthInKm( double mass_in_grams)
 *     {
 *        double mass_si = mass_in_gram * Units.get("mass:g");
 *        ...
 *        // Return needs to be in km
 *        return Units.to(length_si, "length:km");
 *     }
 *
 * </pre> }
 *
 * author nelson85
 */
public interface Units
{

  /**
   * Get the symbol for this unit.
   *
   * @return
   */
  public String getSymbol();

  /**
   * Value relative to the SI unit.
   *
   * @return
   */
  public double getValue();

  /**
   * Get the measure associated with this unit.
   * 
   * @return 
   */
  public UnitType getType();

  public static Units get(String name)
  {
    return UnitImpl.get(name);
  }
  
  default public void require(UnitType type)
  {
    if (type == null)
      return;
    if (!this.getType().equals(type))
      throw new IllegalArgumentException("Incorrect unit type ("+this.getType() + "!=" + type +")");
  }
}
