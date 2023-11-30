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
public class EnsdfGamma extends EnsdfEmission implements Serializable
{

  // Relative photon intensity (See units in NORMALIZATION record)
  public final EnsdfQuantity RI;
  // Multipolarity of transition
  public final String M;

  // Mixing ratio, delta. (Sign must be shown explicitly if known. If no sign 
  // is given, it will be assumed to be unknown)
  public final EnsdfQuantity MR;

  // Total conversion coefficient
  public final EnsdfQuantity CC;

  //Relative total transition intensity (units in NORMALIZATION record)
  public final EnsdfQuantity TI;

  // The symbol `*' denotes a multiply-placed 
  // ray. The symbol `&' denotes a multiply-placed
  // transition with intensity not divided.
  // The symbol `@' denotes a multiply-placed
  // transition with intensity suitably divided. 
  // The symbol `%' denotes that the intensity given as
  // RI is the % branching in the Super Deformed
  // Band.
  public final char C;

  // Letter `C' denotes placement conrmed by 
  // coincidence. Symbol `?' denotes questionable
  // coincidence.
  public final char COIN;

  //The character `?' denotes an uncertain
  // placement of the transition in the level scheme
  // Letter `S' denotes an expected, but as yet unobserved, transition
  public final char Q;
  EnsdfLevel destination;

  EnsdfGamma(EnsdfDataSet dataSet, EnsdfQuantity E,
          EnsdfQuantity RI,
          String M,
          EnsdfQuantity MR,
          EnsdfQuantity CC,
          EnsdfQuantity TI,
          char C, char COIN, char Q)
  {
    super(dataSet, 'G', E);
    this.RI = RI;
    this.M = M;
    this.MR = MR;
    this.CC = CC;
    this.TI = TI;
    this.C = C;
    this.COIN = COIN;
    this.Q = Q;
  }
  
  @Override
  public String toString()
  {
    return String.format("Gamma(%s,%s)", E.field, RI.field);
  }

}
