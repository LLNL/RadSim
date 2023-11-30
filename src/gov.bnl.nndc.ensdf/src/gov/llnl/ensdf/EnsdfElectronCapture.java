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
public class EnsdfElectronCapture extends EnsdfEmission implements Serializable
{

  // Energy for electron capture to the level
  // Given only if measured or deduced from measured
  // public final EnsdfQuantity E;
  /**
   * Intensity of B+-decay branch.
   */
  public final EnsdfQuantity IB;

  /**
   * Intensity of electron capture branch.
   */
  public final EnsdfQuantity IE;

  /**
   * The log ft for (B+ E+) transition for uniqueness.
   */
  public final EnsdfQuantity LOGFT;

  /**
   * Total (B+ E+) decay intensity.
   */
  public final EnsdfQuantity TI;

  // Letter `C' denotes coincidence with a following radiation.
  // A `?' denotes probable coincidence with a following radiation.
  public final char C;
  /**
   * 9 UN Forbiddenness classification for the B-. d.
   */
  public final String UN;
  public final char Q;

  public EnsdfElectronCapture(EnsdfDataSet dataSet,
          EnsdfQuantity E,
          EnsdfQuantity IB,
          EnsdfQuantity IE,
          EnsdfQuantity LOGFT,
          EnsdfQuantity TI,
          char C,
          String UN,
          char Q)
  {
    super(dataSet, 'E', E);
    this.IB = IB;
    this.IE = IE;
    this.LOGFT = LOGFT;
//    this.DFT = DFT;
    this.TI = TI;
    this.C = C;
    this.UN = UN;
    this.Q = Q;
  }

}
