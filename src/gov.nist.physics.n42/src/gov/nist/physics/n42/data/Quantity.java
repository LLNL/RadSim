/*
 * Copyright 2022, Lawrence Livermore National Security, LLC.
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.nist.physics.n42.data;

import gov.nist.physics.n42.reader.QuantityReader;
import gov.llnl.utility.xml.bind.ReaderInfo;
import gov.llnl.utility.xml.bind.WriterInfo;
import gov.nist.physics.n42.writer.QuantityWriter;

/**
 *
 * @author nelson85
 */
@ReaderInfo(QuantityReader.class)
@WriterInfo(QuantityWriter.class)
public class Quantity
{
  private String units;
  private double value;

  public Quantity()
  {
  }

  public Quantity(double value, String units)
  {
    this.value = value;
    this.units = units;
  }

  /**
   * @return the units
   */
  public String getUnits()
  {
    return units;
  }

  /**
   * @param units the units to set
   */
  public void setUnits(String units)
  {
    this.units = units;
  }

  /**
   * @return the value
   */
  public double getValue()
  {
    return value;
  }

  /**
   * @param value the value to set
   */
  public void setValue(double value)
  {
    this.value = value;
  }

  @Override
  public String toString()
  {
    return String.format("Quantity(%f,%s)", this.getValue(), this.getUnits());
  }

  @Override
  public boolean equals(Object obj)
  {
    if (!(obj instanceof Quantity))
      return false;
    Quantity obj2 = (Quantity) obj;
    if (obj2.units == null)
    {
      if (this.units != null)
        return false;
      return obj2.value == this.value;
    }
    return obj2.value == this.value && obj2.units.equals(this.units);
  }
}
