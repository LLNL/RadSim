/*
 * Copyright 2022, Lawrence Livermore National Security, LLC.
 * All rights reserved.
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.ensdf;

import java.io.Serializable;

/**
 *
 * @author nelson85
 */
public class EnsdfLevel extends EnsdfEmissions implements Serializable
{

  /**
   * Level energy in keV - Must not be blank
   */
  public final EnsdfQuantity E;
  /**
   * Spin and parity
   */
  public final String J;
  /**
   * Half-life of the level; units must be given.
   *
   * Mean-life expressed as the width of a level, in units of energy, may also
   * be used.
   */
  public final EnsdfTimeQuantity T;
  /**
   * Angular momentum transfer in the reaction determining the data set.
   */
  public final String L;
  /**
   * Spectroscopic strength for this level as determined from the reaction in
   * the IDENTIFICATION record.
   */
  public final EnsdfQuantity S;
  public final char C;
  /**
   * Metastable state is denoted by `M ' or `M1' for the first (lowest energy)
   * isomer; `M2', for the second isomer, etc. For Ionized Atom Decay field
   * gives the atomic electron shell or subshell in which particle is captured.
   */
  public final String MS;
  public final char Q;

  public EnsdfLevel(EnsdfDataSet dataSet,
          EnsdfQuantity E, String J,
          EnsdfTimeQuantity T,
          String L,
          EnsdfQuantity S,
          char C,
          String MS,
          char Q)
  {
    super(dataSet, 'L');
    this.E = E;
    this.J = J;
    this.T = T;
    this.L = L;
    this.S = S;
    this.C = C;
    this.MS = MS;
    this.Q = Q;
  }
  
  public EnsdfLevel() {
    this.E = null;
    this.J = null;
    this.T = null;
    this.L = null;
    this.S = null;
    this.C = ' ';
    this.MS = null;
    this.Q = ' ';
  }

  @Override
  public String toString()
  {
    return String.format("Level(%s)", E.field);
//    return String.format("Level: E=%s J=%s T=%s L=%s S=%s C=%c MS=%s Q=%c",
//            E.toString(),
//            J,
//            T.toString(),
//            L,
//            S.toString(),
//            C,
//            MS,
//            Q);
  }
}
