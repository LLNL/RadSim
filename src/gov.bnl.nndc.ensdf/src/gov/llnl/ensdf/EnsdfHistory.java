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
public class EnsdfHistory extends EnsdfRecord implements Serializable
{

  public StringBuilder value = new StringBuilder();

  EnsdfHistory(EnsdfDataSet dataSet, String HIST)
  {
    super(dataSet, 'H');
    value.append(HIST);
  }
}
