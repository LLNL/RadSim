/*
 * Copyright 2016, Lawrence Livermore National Security, LLC. 
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.math.matrix;

import gov.llnl.utility.UUIDUtilities;

/**
 *
 * @author nelson85
 */
public abstract class MatrixTriangularBase
        extends MatrixTableBase
        implements Matrix.Triangular
{
  private static final long serialVersionUID
          = UUIDUtilities.createLong("MatrixTriangularBase-v1");

  protected boolean upper;

  public MatrixTriangularBase(double[][] data, int dim)
  {
    super(data, dim);
  }

}
