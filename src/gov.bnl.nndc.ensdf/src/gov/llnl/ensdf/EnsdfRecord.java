/*
 * Copyright 2022, Lawrence Livermore National Security, LLC.
 * All rights reserved.
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.ensdf;

import java.util.ArrayList;

/**
 *
 * @author nelson85
 */
public class EnsdfRecord
{

  public final EnsdfDataSet dataSet;
  public final char type;
  public final ArrayList<EnsdfComment> comments = new ArrayList<>();

  EnsdfRecord(EnsdfDataSet dataSet, char type)
  {
    this.dataSet = dataSet;
    this.type = type;
  }
  
  public EnsdfRecord() {
   this.dataSet = null;
   this.type = ' ';
  }
}
