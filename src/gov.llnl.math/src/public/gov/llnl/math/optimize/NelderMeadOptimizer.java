/*
 * Copyright 2016, Lawrence Livermore National Security, LLC. 
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.math.optimize;

import gov.llnl.math.DoubleArray;
import gov.llnl.math.MathExceptions;
import java.util.Arrays;
import java.util.Comparator;

/**
 * Implementation of Simplex Optimization algorithm. This is generally known as
 * Nelder-Mead.
 *
 * @author nelson85
 */
public class NelderMeadOptimizer
{
  // Nelder-Mead parameters
  // <a href="http://en.wikipedia.org/wiki/Nelder%E2%80%93Mead_method"></a>
  double reflectionCoef = 1;
  double expansionCoef = 2;
  double contractionCoef = -0.5;
  double shrinkCoef = 0.5;

  double convergenceTolerance = 5e-7;

  // Input
  public interface ObjectiveFunction
  {
    public int getNumVariables();

    public void initialize(double[] vector, int index);

    public double evaluate(double[] vector);
  }

  // Output 
  public static class Output
  {
    public double error;
    public double[] coef;
  }

  // Workspace 
  static class Working extends Output
  {
  }

  static class WorkingComparator implements Comparator<Working>
  {
    @Override
    public int compare(Working t, Working t1)
    {
      return Double.compare(t.error, t1.error);
    }
  }
  WorkingComparator comparator = new WorkingComparator();

  public Output solve(ObjectiveFunction function) throws MathExceptions.MathException
  {
    int M = function.getNumVariables();

    // Set up the initial simplex
    Working[] ws = new Working[M];
    for (int i0 = 0; i0 < M; i0++)
    {
      double[] w1 = new double[M];
      function.initialize(w1, i0);
      ws[i0] = new Working();
      ws[i0].coef = w1;

      // Compute the initial error for each vector
      double fv = function.evaluate(w1);
      ws[i0].error = fv;
      if (Double.isNaN(fv) || Double.isInfinite(fv))
        throw new MathExceptions.MathException("NAN in computeError encountered");
    }

    double[] centroid;
    for (int counter = 0; counter < 5000; counter++)
    {
      // Sort the working
      Arrays.sort(ws, comparator);

      // centroid is the average of all but the worst
      centroid = new double[M];
      for (int i0 = 0; i0 < M - 1; i0++)
      {
        DoubleArray.addAssign(centroid, ws[i0].coef);
      }
      DoubleArray.divideAssign(centroid, M - 1);

      // Reference to the worst set of parameters
      double[] xw = ws[M - 1].coef;

      // Reflection
      double[] x1 = new double[M];
      for (int i = 0; i < M; i++)
      {
        x1[i] = centroid[i] + reflectionCoef * (centroid[i] - xw[i]);
      }
      double sr = function.evaluate(x1);

      // Better than the best, look out farther
      if (sr < ws[0].error)
      {
        // Expand 
        // Replace the worst
        DoubleArray.assign(ws[M - 1].coef, x1);
        ws[M - 1].error = sr;

        // Check if expansion helps
        for (int i = 0; i < M; i++)
        {
          x1[i] = centroid[i] + expansionCoef * (centroid[i] - xw[i]);
        }

        double errExpand = function.evaluate(x1);
        if (errExpand < sr)
        {
          DoubleArray.assign(ws[M - 1].coef, x1);
          ws[M - 1].error = errExpand;
        }
        continue;
      }

      // Not better than the best but still good
      if (sr < ws[M - 2].error)
      {
        // Reflection
        DoubleArray.assign(ws[M - 1].coef, x1);
        ws[M - 1].error = sr;
        continue;
      }

      // Convergence?
      if ((ws[M - 1].error / ws[0].error - 1.0) < convergenceTolerance)
        break;

      // Still the worst, move to contraction
      {
        // Check if contraction helps by moving closer to the centroid
        for (int i = 0; i < M; i++)
        {
          x1[i] = centroid[i] + contractionCoef * (centroid[i] - xw[i]);
        }

        double errContraction = function.evaluate(x1);
        if (errContraction < ws[M - 1].error)
        {
          DoubleArray.assign(ws[M - 1].coef, x1);
          ws[M - 1].error = errContraction;
          continue;
        }
      }

      // All else has failed, last option Shrink
      {
        // Move everything closer the best.
        double[] xb = ws[0].coef;
        for (int i0 = 1; i0 < M; i0++)
        {
          double[] xn = ws[i0].coef;
          for (int i1 = 0; i1 < M; i1++)
          {
            xn[i1] = xb[i1] + shrinkCoef * (xn[i1] - xb[i1]);
          }
          ws[i0].error = function.evaluate(xn);
        }
      }
    } // while

    return ws[0];
  }
}
