/*
 * Copyright 2016, Lawrence Livermore National Security, LLC.
 * All rights reserved
 *
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.math.algebra;

import gov.llnl.math.IntegerArray;
import gov.llnl.math.matrix.Matrix;
import gov.llnl.math.parallel.ParallelProcessor;

/**
 * Nonnegative Least Squares Solver.
 *
 * PURPOSE: To solve a NNLS problem with big ratio between the number of columns
 * and the number of rows efficiently.
 *
 * ALGORITHM: Deferred NNLS by CS &amp; KN, with array operations and SVD (JY)
 *
 * @author nelson85 and yao2
 */
public class NnlsqFactory
{
  // ISSUES:
  // 1) Parallel processing is currently broken

  boolean useDeferred = true;
  boolean useCache = true;
  boolean useScaleDemand = false;
  boolean useUnitize = true;
  public ParallelProcessor processor = null;

//<editor-fold desc="parameters">
  /**
   * Enables or disables the use of parallel processing.
   *
   * @param use is true to enable parallel calculations.
   */
  public void setParallel(boolean use)
  {
    if (use)
    {
      if (processor == null)
        processor = new ParallelProcessor();
    }
    else
    {
      if (processor != null)
      {
        processor.dispose();
        processor = null;
      }
    }
  }

  /**
   * Controls the use of the deferred optimization. The optimization to
   * improving the original Hanson &amp; Lawson algorithm is deferring a portion
   * of the regressors thus reducing the cost of project demands step. This
   * switch enables or disables that optimization.
   *
   * @param b the new state for deferred
   */
  public void setUseDeferred(boolean b)
  {
    useDeferred = b;
  }

  /**
   * Get the current state of the deferred optimization.
   *
   * @return the current state
   */
  public boolean getUseDeferred()
  {
    return useDeferred;
  }

  /**
   * Controls the use of the unitization optimization. This parameter improves
   * convergence properties of Hanson &amp; Lawson algorithm. By scaling the
   * control matrix, we improve the condition number of the inverse.
   *
   * @param b
   */
  public void setUseUnitize(boolean b)
  {
    useUnitize = b;
  }

  /**
   * Get the current state of the unitization optimization.
   *
   * @return the current state
   */
  public boolean getUseUnitize()
  {
    return useUnitize;
  }

  public void setUseCache(boolean b)
  {
    this.useCache = b;
  }

  public boolean getUseCache()
  {
    return this.useCache;
  }

  public void setUseScaleDemand(boolean b)
  {
    this.useScaleDemand = b;
  }

  public boolean getUseScaleDemand()
  {
    return this.useScaleDemand;
  }
//</editor-fold>

  public Nnlsq createSolver()
  {
    NnlsqImpl ws = new NnlsqImpl();
//    ws.setProcessor(processor);
    ws.setOptions(useCache, useDeferred, useScaleDemand, useUnitize);
    return ws;
  }

  // Current workspace for most recently solved problem.  For use in debugging.
  NnlsqImpl workspace;

  public Nnlsq.Input createInput(double[][] A, double[] Y, int[] setUse)
  {
    Nnlsq.InputArrays input = new Nnlsq.InputArrays();
    input.regressand = Y;
    input.regressors = A;
    input.use = setUse;
    return input;
  }

  /**
   * Create input for NNLS problem A x=B.
   *
   * @param A is the regressor matrix.
   * @param B is the regressand.
   * @param setUse is a list of regressors to use or null if all to be used.
   * @return a new input.
   */
  public Nnlsq.Input createInput(Matrix.ColumnAccess A, double[] B, int[] setUse)
  {
    if (A == null)
      throw new NullPointerException("A is null");
    if (B == null)
      throw new NullPointerException("B is null");
    if (setUse == null)
      setUse = IntegerArray.colon(0, A.columns());

    Nnlsq.InputDoubleMatrix input = new Nnlsq.InputDoubleMatrix();
    input.regressand = B;
    input.regressors = A;
    input.regressorsWeighted = A;
    input.use = setUse;
    return input;
  }

  public Nnlsq.Input createInput(Matrix.ColumnAccess A, double[] B, double[] W, int setUse[])
  {
    if (A == null)
      throw new NullPointerException("A is null");
    if (B == null)
      throw new NullPointerException("B is null");
    if (W == null)
      throw new NullPointerException("W is null");
    if (setUse == null)
      setUse = IntegerArray.colon(0, A.columns());

    Nnlsq.InputDoubleMatrix input = new Nnlsq.InputDoubleMatrix();
    input.regressand = B;
    input.regressors = A;
    input.use = setUse;
    input.computeWeighted(W);
    return input;
  }

  public Nnlsq.Input createInput(Matrix.ColumnAccess A, double Y[], Matrix W, int[] setUse)
  {
    Nnlsq.InputDoubleMatrix input = new Nnlsq.InputDoubleMatrix();
    input.regressand = Y;
    input.use = setUse;
    input.regressors = A;
    input.computeWeighted(W);
    return input;
  }

  /**
   * Disposes of any threads created for parallelization.
   */
  public void dispose()
  {
    if (processor != null)
    {
      processor.dispose();
      processor = null;
    }
  }

}
