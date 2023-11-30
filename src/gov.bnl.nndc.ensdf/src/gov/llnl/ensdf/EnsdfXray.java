package gov.llnl.ensdf;

/*
 * Copyright 2022, Lawrence Livermore National Security, LLC.
 * All rights reserved.
 * 
 * Terms and conditions are given in "Notice" file.
 */

import java.io.Serializable;

/**
 *
 * @author nelson85
 */
public class EnsdfXray extends EnsdfEmission implements Serializable
{
  public final EnsdfQuantity RI;
  public final String M;

  public EnsdfXray(EnsdfDataSet dataSet, EnsdfQuantity E,
          EnsdfQuantity RI,
          String M)
  {
    super(dataSet, EnsdfParticleType.XRAY.getCode(), E);
    this.RI = RI;
    this.M = M;
  }

}
