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
public class EnsdfNormalization extends EnsdfRecord implements Serializable
{

  /**
   * Multiplier for converting relative photon intensity (RI in the GAMMA
   * record) to photons per 100 decays of the parent through the decay branch or
   * to photons per 100 neutron captures in an (n, ) reaction. Required if the
   * absolute photon intensity can be calculated.
   */
  public final EnsdfQuantity NR;
  /**
   * Multiplier for converting relative transition intensity (including
   * conversion electrons) [TI in the GAMMA record] to transitions per 100
   * decays of the parent through this decay branch or per 100 neutron captures
   * in an (n; ) reaction. Required if TI are given in the GAMMA record and the
   * normalization is known
   */
  public final EnsdfQuantity NT;
  /**
   * Branching ratio multiplier for converting intensity per 100 decays through
   * this decay branch to intensity per 100 decays of the parent nuclide.
   * Required if known.
   */
  public final EnsdfQuantity BR;
  /**
   * Multiplier for converting relative B- and ec intensities (IB in the B-
   * record; IB, IE, TI in the EC record) to intensities per 100 decays through
   * this decay branch. Required if known.
   */
  public final EnsdfQuantity NB;
  /**
   * Multiplier for converting per hundred delayed-transition intensities to
   * per hundred decays of precursor.
   */
  public final EnsdfQuantity NP;

  public EnsdfNormalization(EnsdfDataSet dataSet,
          EnsdfQuantity NR, EnsdfQuantity NT, EnsdfQuantity BR,
          EnsdfQuantity NB, EnsdfQuantity NP)
  {
    super(dataSet, 'N');
    this.NR = NR;
    this.NT = NT;
    this.BR = BR;
    this.NB = NB;
    this.NP = NP;
  }

  @Override
  public String toString()
  {
    return String.format("Normalization: NR=%s NT=%s BR=%s NB=%s NP=%s",
            NR.toString(),
            NT.toString(),
            BR.toString(),
            NB.toString(),
            NP.toString());
  }
}
