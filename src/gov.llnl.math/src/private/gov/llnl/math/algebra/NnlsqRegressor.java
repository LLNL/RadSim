/*
 * Copyright 2016, Lawrence Livermore National Security, LLC. 
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.math.algebra;

import gov.llnl.utility.HashUtilities;
import gov.llnl.utility.UUIDUtilities;
import gov.llnl.utility.annotation.Internal;
import java.io.Serializable;

//<editor-fold desc="regressor">
/**
 * This represents the working definition for regressors during processing.
 */
@Internal
class NnlsqRegressor implements Nnlsq.Datum, Serializable
{  
  private static final long serialVersionUID = UUIDUtilities.createLong("NnlsqRegressor-v1");

  //<editor-fold desc="list">
  // List element behavior
  NnlsqRegressorSet set;
  NnlsqRegressor prev;
  NnlsqRegressor next;
  //</editor-fold>
  //<editor-fold desc="inputs">
  public int id;
  public long hash;
  public double[] regressor;
  /**
   * The regressor times the weighting coefficients.
   */
  public double[] regressorWeighted;
  /**
   * Offset to the first element in the regressor list.
   */
  public int offset;
  //</editor-fold>
  //<editor-fold desc="state">
  // State variable
  
  /** 
   * Cycle state last time this regressor was used.  
   * This is used to determine if the state has already been
   * evaluated before. 
   */
  public long lastUsed = -1;
  public double scale = 1.0; // 1/sqrt(A_i^T*A_i)
  public double regressandProjection; // B^T*W*A/sqrt(B^T*W*B)
  public double demand;
  public double demandOffset = 0;
  public double coef; // X
  public double update; // Zp
  public double revised; //
  public int convergenceTag = 0;
  public int constrainTag = 0;
  //</editor-fold>

  NnlsqRegressor(int id, double[] regressor, double[] regressorWeighted, int offset)
  {
    super();
    this.id = id;

    // Create a 64 bit hash code for cycle checking
    int[] hashInput = new int[]
    {
      System.identityHashCode(this), System.identityHashCode(regressor)
    };
    this.hash = HashUtilities.byteArrayToLong(HashUtilities.hash(hashInput));
    this.regressor = regressor;
    this.regressorWeighted = regressorWeighted;
    this.offset = offset;
  }

//<editor-fold desc="public api">
  @Override
  public int getId()
  {
    return this.id;
  }

  @Override
  public double getCoef()
  {
    return this.coef;
  }
  
  /**
   * @return the set
   */
  public NnlsqRegressorSet getSet()
  {
    return set;
  }
  
  @Override
  public String toString()
  {
    return String.format("Regressor(%d,%f)", this.id, this.coef);
  }
//</editor-fold>
}
