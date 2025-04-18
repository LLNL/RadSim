/*
 * Copyright (c) 2016, Lawrence Livermore National Security, LLC.
 * All rights reserved.
 *
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.rtk.physics;

/**
 * Representation of a nuclide. Created from the
 * {@link gov.llnl.rtk.physics.Nuclides#get(java.lang.String) Nuclides.get}.
 *
 * @author nelson85
 */
public interface Nuclide extends Comparable<Nuclide>
{
  public static double LN2 = Math.log(2);
  static final double AVAGADROS_CONSTANT = 6.02214076e23;

  /**
   * Get the name of the nuclide.
   *
   * @return
   */
  String getName();

  Element getElement();

  /**
   * Get the atomic mass.
   *
   * @return g/mole
   */
  double getAtomicMass();

  /**
   * Get the atomic number.
   *
   * @return
   */
  int getAtomicNumber();

  /**
   * Get halflife. Units are seconds.
   *
   * @return the halflife in seconds, or Double.POSITIVE_INFINITY if stable.
   */
  double getHalfLife();

  /**
   * Get the isomer number.
   *
   * @return
   */
  int getIsomerNumber();

  /**
   * Get the mass number.
   *
   * @return
   */
  int getMassNumber();

  int getId();

  default double getDecayConstant()
  {
    return LN2 / this.getHalfLife();
  }

  /**
   * Specific activity in 1 over kg seconds.
   *
   * @return
   */
  default double getSpecificActivity()
  {
    return (LN2 * AVAGADROS_CONSTANT) / (getHalfLife() * getAtomicMass());
  }

  /**
   * Is this nuclide stable.
   *
   * @return true if is stable.
   */
  default boolean isStable()
  {
    return Double.isInfinite(getHalfLife());
  }

  default public int getZaid()
  {
    return 10000 * this.getAtomicNumber() + 10 * this.getMassNumber() + this.getIsomerNumber(); //Karl's method in int format
  }

}
