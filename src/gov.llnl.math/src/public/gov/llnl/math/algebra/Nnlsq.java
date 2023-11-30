/*
 * Copyright 2016, Lawrence Livermore National Security, LLC. 
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.math.algebra;

import gov.llnl.math.DoubleArray;
import gov.llnl.math.IntegerArray;
import gov.llnl.math.MathExceptions;
import gov.llnl.math.matrix.Matrix;
import gov.llnl.math.matrix.MatrixColumnTable;
import gov.llnl.math.matrix.MatrixOps;
import gov.llnl.math.matrix.MatrixViews;
import gov.llnl.utility.UUIDUtilities;
import gov.llnl.utility.annotation.Matlab;
import java.io.Serializable;
import java.util.Collection;

/**
 * Front end for non-negative least mean squared solver.
 *
 * Note the nnlsq solver will write cores on fail. To disable this set System
 * property gov.llnl.math.Nnlsq.dumpCore to "false".
 *
 * @author nelson85
 */
public interface Nnlsq
{
  /**
   * Initializes the memory for the problem.
   *
   * @param input
   * @throws MathExceptions.SizeException
   */
  void initialize(Input input) throws MathExceptions.SizeException;

  /**
   * Solve the problem A*x=Y subject to x&gt;=0. Initialize must be called first.
   *
   * @throws gov.llnl.math.MathExceptions.ConvergenceException
   */
  void solve() throws MathExceptions.ConvergenceException;

  /**
   * Solve the problem A*x=Y subject to x&gt;=0. Calls initialize then solve.
   *
   * @param input
   * @throws gov.llnl.math.MathExceptions.ConvergenceException
   */
  void solve(Input input) throws MathExceptions.ConvergenceException;

  /**
   * Get the representation of the solution containing the regressor ids and
   * coefficients.
   *
   * @return
   */
  Output<? extends Datum> getSolution();

  /**
   * Compute the error. MSE is defined as (Y-A*x)^t*\Sigma^-1*(Y-A*x).
   *
   * @return
   */
  double getMSE();

  /**
   * Computes the projection of the solution into the feature space. This is
   * simply A*x.
   *
   * @return
   */
  double[] getProjection();

  /**
   * Output Data holding the regressor id and the coefficient.
   */
  public interface Datum
  {
    /**
     * Get the regressor id for this datum.
     *
     * @return
     */
    int getId();

    /**
     * Get the coefficient associated with the regressor.
     *
     * @return
     */
    double getCoef();
  }

  /**
   * Collection of regressors which form the solution.
   *
   * @param <Type>
   */
  public interface Output<Type>
          extends Iterable<Type>, Serializable
  {
    @Matlab
    double[] toCoefficients();

    void copyCoefficients(double[] X);
  }

//<editor-fold desc="Input" defaultstate="collapsed">
  /**
   * Input is a generalized interface for different memory handling to the
   * Solver. Use either InputDoubleMatrix, InputArrays, or implement your own to
   * use.
   */
  public interface Input extends Serializable
  {

    /**
     * Called by the solver to inform allow any caches relating to the problem
     * to update. This routine is responsible for verifying all of the sizes are
     * correct such that error do not occur in the solver.
     */
    void initialize();

    /**
     * Get the list of regressors we will be working with.
     *
     * @return a list of regressor ids
     */
    int[] getRegressorSet();

    /**
     * Get the number of variables in the regressand.
     *
     * @return the number of variables.
     */
    int getNumVariables();

    /**
     * Get the number of variables in the regressors.
     *
     * @return the number of regressors.
     */
    int getNumRegressors();

    /**
     * Get the base addressColumn associated with the regressor.
     *
     * @param i is the regressor index requested.
     * @return the array that hold the regressor.
     */
    double[] getRegressor(int i);

    /**
     * Get the base addressColumn associated with the weighted regressor. This
     * will be the same as the regressor if the problem does not have variable
     * weighting.
     *
     * @param i is the regressor index requested.
     * @return the array that hold the regressor.
     */
    double[] getRegressorWeighted(int i);

    /**
     * Returns the offset into the regressor or weighted regressor where the
     * first row is found.
     *
     * @param i is the regressor index requested.
     * @return the offset.
     */
    int getRegressorOffset(int i);

    /**
     * Get the regressand to fit against.
     *
     * @return
     */
    double[] getRegressand();

    /**
     * Get the regressand times the inverse covariance matrix. This will be the
     * same as {@code getRegressand} if there is no covariance matrix.
     *
     * @return
     */
    double[] getRegressandWeighted();

    Collection<? extends Constraint> getConstraints();

  }
//</editor-fold>
//<editor-fold desc="Input implementations" defaultstate="collapsed">

  public static class InputDoubleMatrix implements Input
  {
    private static final long serialVersionUID
            = UUIDUtilities.createLong("Nnlsq.InputDoubleMatrix-v1");
    public double[] regressand;
    public double[] regressandWeighted;
    public double[] weight;
    public Matrix.ColumnAccess regressors;
    public Matrix.ColumnAccess regressorsWeighted;
    public int[] use;

    @Override
    public int[] getRegressorSet()
    {
      return use;
    }

    @Override
    public int getNumVariables()
    {
      return regressand.length;
    }

    @Override
    public int getNumRegressors()
    {
      return regressors.columns();
    }

    @Override
    public double[] getRegressor(int i)
    {
      return regressors.accessColumn(i);
    }

    @Override
    public double[] getRegressorWeighted(int i)
    {
      return regressorsWeighted.accessColumn(i);
    }

    @Override
    public int getRegressorOffset(int i)
    {
      return regressors.addressColumn(i);
    }

    @Override
    public void initialize()
    {
      if (regressorsWeighted == null)
        regressorsWeighted = regressors;
      if (regressandWeighted == null)
        regressandWeighted = regressand;
    }

    @Override
    public double[] getRegressand()
    {
      return this.regressand;
    }

    /**
     * Compute the weighted regressor from a fully populated covariance matrix.
     *
     * @param W
     * @throws MathExceptions.SizeException
     */
    void computeWeighted(Matrix W) throws MathExceptions.SizeException
    {
      regressorsWeighted = new MatrixColumnTable(regressors.rows(), regressors.columns());
      for (int slot : use)
      {
        MatrixViews.selectColumn(regressorsWeighted, slot).assign(MatrixOps.multiply(W, MatrixViews.selectColumn(regressors, slot)));
      }
      regressandWeighted = MatrixOps.multiply(W, regressand);
    }

    /**
     * Compute the weighted regressor from a diagonal covariance matrix.
     *
     * @param W
     * @throws MathExceptions.SizeException
     */
    void computeWeighted(double[] W) throws MathExceptions.SizeException
    {
      regressorsWeighted = new MatrixColumnTable(regressors.rows(), regressors.columns());
      int m = regressors.rows();
      for (int slot : use)
      {
        DoubleArray.multiply(regressorsWeighted.accessColumn(slot),
                regressorsWeighted.addressColumn(slot),
                regressors.accessColumn(slot), regressors.addressColumn(slot), W, 0, m);
      }
      regressandWeighted = DoubleArray.multiply(W, regressand);
    }

    @Override
    public double[] getRegressandWeighted()
    {
      return this.regressandWeighted;
    }

    public void clearWeighted()
    {
      this.regressandWeighted = this.regressand;
      this.regressorsWeighted = this.regressors;
    }

    @Override
    public Collection<? extends Constraint> getConstraints()
    {
      return null;
    }
  }

  public static class InputArrays implements Input
  {
    private static final long serialVersionUID = UUIDUtilities.createLong("Nnlsq.InputArrays-v1");
    public double[] regressand;
    public double[] regressandWeighted;
    public double[][] regressors;
    public double[][] regressorsWeighted;
    public int[] use;
    protected Collection<? extends Constraint> constraints;

    @Override
    public int[] getRegressorSet()
    {
      return this.use;
    }

    @Override
    public int getNumVariables()
    {
      return this.regressand.length;
    }

    @Override
    public int getNumRegressors()
    {
      return this.regressors.length;
    }

    @Override
    public double[] getRegressor(int i)
    {
      return this.regressors[i];
    }

    @Override
    public double[] getRegressorWeighted(int i)
    {
      return this.regressorsWeighted[i];
    }

    @Override
    public int getRegressorOffset(int i)
    {
      return 0;
    }

    @Override
    public void initialize()
    {
      if (regressorsWeighted == null)
        regressorsWeighted = regressors;
      if (regressandWeighted == null)
        regressandWeighted = regressand;
      // If the use is not set, use all regressors.
      if (this.use == null)
      {
        this.use = IntegerArray.colon(0, regressors.length);
      }
    }

    @Override
    public double[] getRegressand()
    {
      return this.regressand;
    }

    @Override
    public double[] getRegressandWeighted()
    {
      return regressandWeighted;
    }

    protected void computeWeighted(Matrix W) throws MathExceptions.SizeException
    {
      int m = this.getNumVariables();
      int n = this.getNumRegressors();

      regressorsWeighted = new double[m][n];
      MatrixColumnTable A = new MatrixColumnTable(m, n);
      MatrixColumnTable WA = new MatrixColumnTable(m, n);

      for (int i = 0; i < m; i++)
      {
        A.assignRow(regressors[i], i);
      }

      Matrix Av = MatrixViews.selectColumns(A, use);
      Matrix WAv = MatrixViews.selectColumns(WA, use);
      WAv.assign(MatrixOps.multiply(W, Av));

      for (int i = 0; i < m; i++)
      {
        regressorsWeighted[i] = WA.copyRow(i);
      }

      regressandWeighted = MatrixOps.multiply(W, regressand);
    }

    protected void computeWeighted(double[] W) throws MathExceptions.SizeException
    {
      int m = this.getNumVariables();
      int n = this.getNumRegressors();

      regressorsWeighted = new double[m][n];

      double A[] = new double[m * n];
      double WA[] = new double[m * n];

      int count = 0;

      for (int j = 0; j < n; j++)
      {
        for (int i = 0; i < m; i++)
        {
          A[count] = regressors[i][j];
          count++;
        }
      }

      for (int slot : use)
      {
        DoubleArray.multiply(WA, slot * m, A, slot * m, W, 0, m);
      }

      regressandWeighted = DoubleArray.multiply(W, regressand);
    }

    public void clearWeighted()
    {
      this.regressandWeighted = this.regressand;
      this.regressorsWeighted = this.regressors;
    }

    @Override
    public Collection<? extends Constraint> getConstraints()
    {
      return null;
    }
  }
//</editor-fold>
}
