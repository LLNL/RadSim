/*
 * Copyright 2019, Lawrence Livermore National Security, LLC. 
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.rtk.physics;

import gov.llnl.utility.xml.bind.ReaderInfo;
import gov.llnl.utility.xml.bind.WriterInfo;
import java.io.Serializable;

/**
 *
 * @author nelson85
 */
@ReaderInfo(QuantityReader.class)
@WriterInfo(QuantityWriter.class)
public interface Quantity extends Serializable
{
  public static Quantity UNSPECIFIED = new QuantityImpl(0, null, 0, false);

  /**
   * Create a scalar without units.
   *
   * @param value
   * @return
   */
  public static Quantity scalar(double value)
  {
    return new QuantityImpl(value, null, 0, true);
  }

  /**
   * Create a quantity with a specified units.
   *
   * @param value
   * @param units
   * @return
   */
  static public Quantity of(double value, String units)
  {
    return new QuantityImpl(value, Units.get(units), 0, true);
  }

  /**
   * Create a quantity with a specified units.
   *
   * @param value
   * @param units
   * @return
   */
  static public Quantity of(double value, Units units)
  {
    return new QuantityImpl(value, units, 0, true);
  }

  /**
   * Create a quantity with a specified units (fully specified).
   *
   * @param value
   * @param units
   * @param uncertainty
   * @param specified
   * @return
   */
  static public Quantity of(double value, Units units, double uncertainty, boolean specified)
  {
    return new QuantityImpl(value, units, uncertainty, specified);
  }

  /**
   * Get the value in SI units.
   *
   * @return
   */
  double get();

  /**
   * Get the units associated with this quantity.
   *
   * @return
   */
  Units getUnits();

  /**
   * Get the number portion of the quantity without conversion.
   *
   * @return
   */
  double getValue();

  /**
   * Get the uncertainty of the quantity without conversion.
   *
   * @return
   */
  double getUncertainty();

  /**
   * Check if the value is specified.
   *
   * @return
   */
  public boolean isSpecified();

  /**
   * Get the uncertainty of the quantity without conversion.
   *
   * @return
   */
  public boolean hasUncertainty();

  /**
   * Get the value in a specified unit.
   *
   * @param units
   * @return
   */
  double as(Units units);

  /**
   * Convert the value to a specific unit.
   *
   * @param units
   * @return
   */
  Quantity to(Units units);
//<editor-fold desc="default" defaultstate="collapsed">

  /**
   * Get the value in a specified unit.
   *
   * @param units
   * @return
   */
  default double as(String units)
  {
    return this.as(Units.get(units));
  }

  /**
   * Convert the value to a specific unit.
   *
   * @param units
   * @return
   */
  default Quantity to(String units)
  {
    return this.to(Units.get(units));
  }

  /**
   * Get a scaled copy of the Quantity.
   *
   * This is linked to the original value such that changes in the original will
   * be reflected in the scaled version.
   *
   * @param d
   * @return
   */
  default Quantity scaled(double d)
  {
    return new ScaledQuantity(this, d);
  }

  default Quantity plus(Quantity quantity)
  {
    Units units = this.getUnits();
    if (units.getType() != quantity.getUnits().getType())
      throw new UnitsException("mixed unit addition");

    // Units must be equalant
    if (!quantity.getUnits().equals(units))
      quantity = quantity.to(units);

    // Operate in unit space
    double newValue = this.getValue() + quantity.getValue();
    double u1 = this.getUncertainty();
    double u2 = quantity.getUncertainty();
    double newUncertainty = Math.sqrt(u1 * u1 + u2 * u2);

    return new QuantityImpl(newValue, units, newUncertainty, this.isSpecified());
  }

  /**
   * Require a specific unit type.
   *
   * If no unit type is given then it will always pass.
   *
   * @param type is a PhysicalProperty.
   */
  default public void require(UnitType type)
  {
    Units current = this.getUnits();
    if (current == null)
      throw new IllegalArgumentException("Scaler quantity");
    if (type == null)
      return;
    current.require(type);
  }
  
  default public Quantity immutable()
  {
    
    return new ImmutableQuantity(this);
    
  }

//</editor-fold>
}
