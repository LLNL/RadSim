/*
 * Copyright 2022, Lawrence Livermore National Security, LLC.
 * All rights reserved.
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.ensdf;

import gov.llnl.rtk.physics.Nuclide;
import java.io.Serializable;

/**
 *
 * @author nelson85
 */
public class EnsdfParent extends EnsdfRecord implements Serializable
{

  public final Nuclide nuclide;
  
  public final String NUCID;
  /**
   * Energy of the decaying level in keV.
   */
  public final EnsdfQuantity E;
  /**
   * Half-life; units must be given.
   */
  public final EnsdfTimeQuantity T;
  /**
   * Ground-state Q-value in keV (total energy available for g:s: ! g:s:
   * transition); it will always be a positive number. Not needed for IT and SF
   * decay
   */
  public final EnsdfQuantity QP;
  /**
   * Ionization State (for Ionized Atom decay). blank otherwise.
   */
  public final String ION;
  /**
   * Spin and parity.
   */
  public final String J;

  public EnsdfParent(EnsdfDataSet dataSet,
          Nuclide nuclide,
          String NUCID,
          EnsdfQuantity E,
          String J,
          EnsdfTimeQuantity T,
          EnsdfQuantity QP,
          String ION)
  {
    super(dataSet, 'P');
    this.nuclide = nuclide;
    this.NUCID = NUCID;
    this.E = E;
    this.J = J;
    this.T = T;
    this.QP = QP;
    this.ION = ION;
  }

  @Override
  public String toString()
  {
    return String.format("Parent: NUCID=%s E=%s T=%s QP=%s ION=%s J=%s",
            nuclide.toString(),
            E.toString(),
            T.toString(),
            QP.toString(),
            ION,
            J);
  }

}
