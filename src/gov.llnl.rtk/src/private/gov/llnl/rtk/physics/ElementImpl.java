/*
 * Copyright 2017, Lawrence Livermore National Security, LLC.
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.rtk.physics;

import java.io.Serializable;

public class ElementImpl implements Element, Serializable
{
  private final String symbol;
  private final int z;
  double molarMass;

  ElementImpl(String symbol, int z)
  {
    this.symbol = symbol;
    this.z = z;
  }

  @Override
  public String getSymbol()
  {
    return this.symbol;
  }

  @Override
  public int getAtomicNumber()
  {
    return this.z;
  }

  @Override
  public String toString()
  {
    return getSymbol();
  }

  @Override
  public double getMolarMass()
  {
    return molarMass;
  }

  /**
   * @param molarMass the molarMass to set
   */
  public void setMolarMass(double molarMass)
  {
    this.molarMass = molarMass;
  }

}
