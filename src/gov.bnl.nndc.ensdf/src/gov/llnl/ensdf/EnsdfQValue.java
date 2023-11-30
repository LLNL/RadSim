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
public class EnsdfQValue extends EnsdfRecord implements Serializable
{

  public final EnsdfQuantity QM;
  public final EnsdfQuantity SN;
  public final EnsdfQuantity SP;
  public final EnsdfQuantity QA;
  public final String QREF;

  EnsdfQValue(EnsdfDataSet dataSet,
          EnsdfQuantity QM, EnsdfQuantity SN,
          EnsdfQuantity SP, EnsdfQuantity QA, String QREF)
  {
    super(dataSet, 'Q');
    this.QM = QM;
    this.SN = SN;
    this.SP = SP;
    this.QA = QA;
    this.QREF = QREF;
  }

}
