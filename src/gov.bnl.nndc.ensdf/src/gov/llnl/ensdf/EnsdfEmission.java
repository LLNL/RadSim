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
public class EnsdfEmission extends EnsdfExtendable
{
  public EnsdfLevel level;
  public final EnsdfQuantity E;

  EnsdfEmission(EnsdfDataSet dataSet, char type, EnsdfQuantity E)
  {
    super(dataSet, type);
    this.E = E;
  }
  
  public EnsdfEmission() {
    this.E = null;
  }

}
