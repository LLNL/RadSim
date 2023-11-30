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
public class EnsdfAlpha extends EnsdfEmission implements Serializable
{

  // Intensity of alpha-decay branch in percent of the total alpha decay
  public final EnsdfQuantity IA;
  // Hindrance factor for alpha decay
  public final EnsdfQuantity HF;
  // Letter `C' denotes coincidence with a following radiation.
  // A `?' denotes probable coincidence with a following radiation
  public final char C;
  public final String UN;
  // The character `?' denotes uncertain or questionable alpha branch
  // Letter `S' denotes an expected or predicted alpha branch
  public final char Q;

  public EnsdfAlpha(EnsdfDataSet dataSet,
          EnsdfQuantity E,
          EnsdfQuantity IA,
          EnsdfQuantity HF,
          char C, String UN, char Q)
  {
    super(dataSet, 'A', E);
    this.IA = IA;
    this.HF = HF;
    this.C = C;
    this.UN = UN;
    this.Q = Q;
  }

}
