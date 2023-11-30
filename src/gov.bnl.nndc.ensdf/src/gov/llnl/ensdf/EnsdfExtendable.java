/*
 * Copyright 2022, Lawrence Livermore National Security, LLC.
 * All rights reserved.
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.ensdf;

import java.util.HashMap;

/**
 *
 * @author nelson85
 */
public class EnsdfExtendable extends EnsdfRecord
{

  public final HashMap<String, EnsdfQuantity> continuation = new HashMap<>();

  EnsdfExtendable(EnsdfDataSet dataSet, char type)
  {
    super(dataSet, type);
  }
  
  public EnsdfExtendable() {
    
  }

}
