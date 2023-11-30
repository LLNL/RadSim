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
public class EnsdfBeta extends EnsdfEmission implements Serializable
{

  /**
   * IB Intensity of the beta-decay br.
   */
  public final EnsdfQuantity IB;
  /**
   * The log ft for the beta transition for unique.
   */
  public final EnsdfQuantity LOGFT;

  /**
   * Letter `C' denotes coincidence with a following radiation. A `?' denotes
   * probable coincidence with a following radiation.
   */
  public final char C;

  /**
   * Forbiddenness classification for the beta decay, e.g., `1U', `2U' for
   * first-, second-unique forbidden.
   *
   * (A blank field signifies an allowed transition. Nonunique forbiddenness can
   * be indicated in col 78, with col 79)
   */
  public final String UN;

  /**
   * Qualifier for this beta.
   *
   * The character `?' denotes an uncertain or questionable beta decay. Letter
   * `S' denotes an expected or predicted transition.
   */
  public final char Q;

  public EnsdfBeta(EnsdfDataSet dataSet,
          EnsdfQuantity E,
          EnsdfQuantity IB,
          EnsdfQuantity LOGFT,
          char C,
          String UN,
          char Q
  )
  {
    super(dataSet, 'B', E);
    this.IB = IB;
    this.LOGFT = LOGFT;
    this.C = C;
    this.UN = UN;
    this.Q = Q;
  }
  
  public EnsdfBeta() {
    this.IB = null;
    this.LOGFT = null;
    this.C = ' ';
    this.UN = null;
    this.Q = ' ';
  }

  @Override
  public String toString()
  {
    return String.format("Beta(%s,%s)", E.field, this.IB.field);
  }
}
