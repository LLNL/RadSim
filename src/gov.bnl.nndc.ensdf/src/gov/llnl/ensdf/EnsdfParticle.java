/*
 * Copyright 2022, Lawrence Livermore National Security, LLC.
 * All rights reserved.
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.ensdf;

/**
 *
 * @author nelson85
 */
public class EnsdfParticle extends EnsdfEmission
{

  /**
   * Intensity of (delayed) particles in percent of the total (delayed-)
   * particle emissions.
   */
  final public EnsdfQuantity IP;
  /**
   * Energy of the level in the intermediate (mass=A+1 for n, p;A+4 for alpha)
   * nuclide in case of delayed particle.
   */
  final public String EI;
  /**
   * Width of the transition in keV.
   */
  final public EnsdfQuantity T;
  /**
   * Angular-momentum transfer of the emitted particle.
   */
  final public String L;
  final public char C;
  final public char COIN;
  final public char B;
  final public char Q;
  public final EnsdfParticleType PART;
  public final boolean delayed;

  EnsdfParticle(EnsdfDataSet record,
          char delayed,
          EnsdfParticleType PART,
          EnsdfQuantity E,
          EnsdfQuantity IP,
          String EI,
          EnsdfQuantity T,
          String L,
          char C,
          char COIN,
          char B,
          char Q)
  {
    super(record, delayed, E);
    this.delayed = (delayed == 'D');
    this.PART = PART;
    this.IP = IP;
    this.EI = EI;
    this.T = T;
    this.L = L;
    this.C = C;
    this.B = B;
    this.Q = Q;
    this.COIN = COIN;
  }

}
