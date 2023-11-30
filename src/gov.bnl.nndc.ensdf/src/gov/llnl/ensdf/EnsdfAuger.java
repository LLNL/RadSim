package gov.llnl.ensdf;

/*
 * Copyright 2022, Lawrence Livermore National Security, LLC.
 * All rights reserved.
 * 
 * Terms and conditions are given in "Notice" file.
 */

import java.io.Serializable;

/**
 * Enhancement for storing Auger electrons computed from electron vacancy information.
 * 
 * @author nelson85
 */
public class EnsdfAuger extends EnsdfEmission implements Serializable
{
  public final EnsdfQuantity RI;
  public final String M;

  public EnsdfAuger(EnsdfDataSet dataSet, EnsdfQuantity E,
          EnsdfQuantity RI,
          String M)
  {
    super(dataSet, EnsdfParticleType.AUGER.getCode(), E);
    this.RI = RI;
    this.M = M;
  }

}
