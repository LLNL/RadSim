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
public class Quantity extends Number implements Serializable
{
  public static Quantity UNSPECIFIED = new Quantity(0, null, 0, false);
  
  String units;
  double value;
  boolean specified = true;
  double uncertainty = 0;
  
  static public Quantity of(double value, String units)
  {
    return new Quantity(value, units);
  }

  private Quantity(double value, String units)
  {
    this.value = value;
    this.units = units;
  }

  static public Quantity of(double value, String units, double uncertainty)
  {
    return new Quantity(value, units, uncertainty);
  }  

  private Quantity(double value, String units, double uncertainty)
  {
    this.value = value;
    this.units = units;
    this.uncertainty = uncertainty;
  }
  
  private Quantity(double value, String units, double uncertainty, boolean specified)
  {
    this.value = value;
    this.units = units;
    this.uncertainty = uncertainty;
    this.specified = specified;
  }
  
  public static Quantity ScaleBy(Quantity quantity, double scalar)
  {
    double newValue = quantity.getValue() * scalar;
    double newUncertainty = quantity.getUncertainty() * scalar;
    return new Quantity( newValue, quantity.getUnits(), newUncertainty, quantity.specified);
  }
  
  /**
   * Get the value in SI units.
   *
   * @return
   */
  public double get()
  {
    if (units == null)
      return value;

    return value * Units.get(units);
  }

  /**
   * Get the units associated with this quantity.
   *
   * @return
   */
  public String getUnits()
  {
    return units;
  }

  /**
   * Get the number portion of the quantity without conversion.
   *
   * @return
   */
  public double getValue()
  {
    return value;
  }
  
  /**
   * Get the uncertainty of the quantity without conversion.
   *
   * @return
   */  
  public double getUncertainty()
  {
    return uncertainty;
  }

  /**
   * Get the uncertainty of the quantity without conversion.
   *
   * @return
   */  
  public boolean isSpecified()
  {
    return specified;
  }
  
  /**
   * Get the uncertainty of the quantity without conversion.
   *
   * @return
   */  
  public boolean hasUncertainty()
  {
    return uncertainty!=0;
  }  
  
  @Override
  public int intValue()
  {
    if (!specified)
      throw new NullPointerException();
    return (int) value;
  }

  @Override
  public long longValue()
  {
    if (!specified)
      throw new NullPointerException();
    return (long) value;
  }

  @Override
  public float floatValue()
  {
    if (!specified)
      throw new NullPointerException();
    return (float) value;
  }

  @Override
  public double doubleValue()
  {
    if (!specified)
      throw new NullPointerException();
    return value;
  }

}
