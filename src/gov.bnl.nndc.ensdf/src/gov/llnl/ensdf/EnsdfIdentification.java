/*
 * Copyright 2022, Lawrence Livermore National Security, LLC.
 * All rights reserved.
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.ensdf;

import gov.llnl.rtk.physics.Nuclide;
import java.io.Serializable;

/**
 *
 * @author nelson85
 */
public class EnsdfIdentification extends EnsdfRecord implements Serializable
{

  public final Nuclide product;
  public final String NUCID;
  public final String DSID;
  public final String DSREF;
  public final String PUB;
  public final String DATE;

  public EnsdfIdentification(EnsdfDataSet record, Nuclide product, String NUCID,
          String DSID,
          String DSREF,
          String PUB,
          String DATE)
  {
    super(record, ' ');
    this.product = product;
    this.NUCID = NUCID;
    this.DSID = DSID;
    this.DSREF = DSREF;
    this.PUB = PUB;
    this.DATE = DATE;
  }

  @Override
  public String toString()
  {
    return String.format("Identification: NUCID=%s DSID=%s DSREF=%s PUB=%s DATE=%s",
            NUCID.toString(),
            DSID,
            DSREF,
            PUB,
            DATE);
  }

}
