/* 
 * Copyright 2016, Lawrence Livermore National Security, LLC.
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.math.optimize;

import gov.llnl.math.DoubleArray;
import gov.llnl.math.IntegerArray;
import gov.llnl.math.MathExceptions.ConvergenceException;
import gov.llnl.math.SpecialFunctions;
import gov.llnl.math.function.Function;
import gov.llnl.math.matrix.Matrix;
import gov.llnl.math.matrix.MatrixColumnArray;
import gov.llnl.math.matrix.MatrixFactory;
import gov.llnl.math.matrix.MatrixOps;
import gov.llnl.math.matrix.MatrixViews;
import gov.llnl.math.matrix.special.MatrixSymmetric;
import gov.llnl.utility.Serializer;
import gov.llnl.utility.UUIDUtilities;
import gov.llnl.utility.annotation.Debug;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Paths;

/**
 * This is a currently incomplete port of Levenberg-Marquardt from the Gaussian
 * Fitter code.
 *
 * @author nelson85
 */
//  Levenberg–Marquardt
public class LevenbergMarquardtOptimizer implements Serializable
{
  private static final long serialVersionUID
          = UUIDUtilities.createLong("LevenbergMarquardtOptimizer-v1");

  public static class Limit implements Serializable
  {
    double lambda;
    double lower;
    double upper;
  }

  public static interface ObjectiveFunction extends Serializable
  {
    double[] conditionInput(double[] input);

    double[] computeInitial(double[] working);

    double updateGradiant(
            Matrix.SymmetricAccess hessian, Matrix gradient,
            double[] working, double[] k);

    void postOptimization(double[] k);
  }
  double alpha = 0.2; // used to control the descent rate
  int iterations = 100;
  // Stored for plotting purposes
  int regionStart;
  int regionEnd;
  boolean limitRange;
  ObjectiveFunction objective = new GaussianFitterObjective();
  // State variables
  double[] input;  // used to debug failures 
  double[] working;
  double[] k;
  Matrix gradient; // used to track limits
  MatrixSymmetric hessian;
  @Debug public int[] violation;

  public void execute(double[] in)
  {
    try
    {
      input = in;

      // Condition the input for processing
      working = objective.conditionInput(input);

      // Compute the initial parameters
      double[] k = objective.computeInitial(working);

      // Set up the gradient and hessian
      int variables = k.length;
      Matrix gradient = new MatrixColumnArray(variables, 1);
      MatrixSymmetric hessian = new MatrixSymmetric(variables);
      int[] violation = new int[variables];

      // Standard Gauss-Newton gradiant descent
      double dk = 0;
      double err = 0;
      double previousErr = Double.MAX_VALUE;
      for (int i1 = 0; i1 < iterations; ++i1)
      {
        // Fill with zeros between computation
        MatrixOps.fill(gradient, 0);
        MatrixOps.fill(hessian, 0);

        // Update for the new location
        err = objective.updateGradiant(hessian, gradient, working, k);

        // Evaluate error to check for convergence
        if (err < previousErr && previousErr - err < 1e-5 * err)
          break;
        previousErr = err;

//      for (int i = 0; i < 5; ++i)
//      {
//        double lambda = limits[i][3];
//        double v1 = gradient.get(i, 0);
//        double v2 = hessian.get(i, i);
//        gradient.set(i, 0, v1 + lambda * (k[i] - limits[i][2]));
//        hessian.set(i, i, v2 + lambda);
//      }
        // Levenberg–Marquardt algorithm 
        Matrix v = MatrixViews.diagonal(hessian);
        MatrixOps.addAssignScaled(v, v, 0.01);

        // Solve for the gradiant
        Matrix update = MatrixOps.divideLeft(hessian, gradient);

        // Impose barrier conditions
        imposeLimits(update, hessian, gradient);
        // Clear the violation
        clearViolation();

        // Apply the update
        dk = applyUpdate(update);

        // Check for convergence
        if (dk < 1e-6)
          break;
      }

      // Project the results back to the space we are interested in
      objective.postOptimization(k);

    }
    catch (ArithmeticException ex)
    {
      System.out.println(DoubleArray.toString(k));
      MatrixOps.dump(System.out, hessian);
      MatrixOps.dump(System.out, gradient);
      try
      {
        Serializer serializer = new Serializer();
        serializer.setCompress(true);
        serializer.save(Paths.get(String.format("gf%d.ser.gz", this.hashCode())), this);
      }
      catch (IOException ioex)
      {
      }
      throw new ConvergenceException("NaN in operation");
    }
  }

  private void clearViolation()
  {
    IntegerArray.fill(violation, 0);
  }

  private void imposeLimits(Matrix update, MatrixSymmetric hessian, Matrix gradient)
  {
    //        boolean good = false;
//        int count = 0;
//
//        while (true)
//        {
//          good = true;
//
//        // Check the limits
//        for (int i = 0; i < 5; i++)
//        {
//          if (limits[i] == null)
//            continue;
//
//          double revised = k[i] - alpha * update.get(i, 0);
//          if (revised < limits[i][0] || revised > limits[i][1])
//          {
//            // Determine the appriate lambda
//            double lambda = limits[i][3];
//            if (lambda == 0)
//              lambda = 1e-4;
//            if (violation[i] != 0)
//              lambda *= 4;
//            limits[i][3] = lambda;
//            if (limits[i][3] > 100)
//              limits[i][3] = 100;
//
//            // Apply addition constraint to stay in the limits
//            good = false;
//            double v1 = gradient.get(i, 0);
//            double v2 = hessian.get(i, i);
//            gradient.set(i, 0, v1 + lambda * (k[i] - limits[i][2]));
//            hessian.set(i, i, v2 + lambda);
//            violation[i] = 1;
//          } else
//          {
//            violation[i] = 0;
//          }
//        }
//
//        // If all values in limit range
//        if (good)
//          break;
//
//        // Otherwise revise
//        update = MatrixOps.divideLeft(hessian, gradient);
//
//        count++;
//        if (count > 40)
//          throw new ConvergenceException("Failed in updating limits");
//      }
  }

  private double applyUpdate(Matrix update)
  {
    double dk = 0;
    for (int i3 = 0; i3 < k.length; ++i3)
    {
      double delta = update.get(i3, 0);

      // Handle NaN
      if (delta != delta)
      {
        throw new ArithmeticException();
      }

      dk += delta * delta;
      k[i3] -= alpha * delta;
    }
    return dk;
  }

//<editor-fold desc="gaussian">
  public static class GaussianFitterObjective implements ObjectiveFunction
  {
    // Parameters
    int begin;
    int end;
    // State variables
    @Debug public double mu;
    @Debug public double sigma;
    @Debug public double intensity;
    @Debug public double linear1;
    @Debug public double linear2;
    double norm;

    @Override
    public double[] conditionInput(double[] input)
    {
      // Auto scale problem to the point we have tested
      double[] working = DoubleArray.copyOfRange(input, begin, end);
      norm = Math.sqrt(DoubleArray.sumSqr(working));
      DoubleArray.divideAssign(working, norm);
      return working;
    }

    @Override
    public double[] computeInitial(double[] working)
    {
      // Compute the minimum k4
      double mink4 = (1.0 / (end - begin) / (end - begin));

      int n = working.length;

      // Set up the computeInitial guess.
      LinearRegression lr = new LinearRegression();
      for (int i = 0; i < n; ++i)
        lr.add(i + begin, working[i]);

      Function pf = lr.compute();
      for (int j = 0; j < 3; ++j)
      {
        lr.clear();
        for (int i = 0; i < n; ++i)
        {
          lr.add(i, working[i],
                  SpecialFunctions.logistic(pf.applyAsDouble(i), working[i], 100));
        }
        pf = lr.compute();
      }

      double s0 = 0;
      double s1 = 0;
      double s2 = 0;
      intensity = 0;
      for (int i = 0; i < n; ++i)
      {
        double d = working[i] - pf.applyAsDouble(i + begin);
        if (d > intensity)
          intensity = d;
        if (d > 0)
        {
          s0 += d;
          s1 += d * i;
          s2 += d * i * i;
        }
      }
      linear1 = pf.applyAsDouble(begin);
      linear2 = pf.applyAsDouble(end - 1);
      mu = s1 / s0;
      sigma = Math.sqrt(s2 / s0 - mu * mu) / 2;

      double k[] = new double[]
      {
        linear1, linear2, intensity, mu, 0.5 / sigma / sigma
      };
      if (k[4] < 4 * mink4)
        k[4] = 4 * mink4;
      return k;
    }

    @Override
    public double updateGradiant(
            Matrix.SymmetricAccess hessian, Matrix gradient,
            double[] working, double[] k)
    {
      double err = 0;
      double[] dfv = new double[k.length];
      Matrix df = MatrixFactory.wrapColumnVector(dfv);

      for (int i2 = begin; i2 < end; ++i2)
      {
        // Compute the fit error 
        double g = Math.exp(-k[4] * (i2 - k[3]) * (i2 - k[3]));
        double h = (i2 - begin) / (end - 1.0 - begin);
        double f = ((k[0] * (1 - h) + k[1] * h + k[2] * g) - working[i2 - begin]);
        err += f * f;

        // Compute the derivative
        dfv[0] = (1 - h);
        dfv[1] = h;
        dfv[2] = g;
        dfv[3] = 2 * g * k[2] * k[4] * (i2 - k[3]);
        dfv[4] = -g * k[2] * (i2 - k[3]) * (i2 - k[3]);

        // Update the (df'*df) and (df'*f) matrix
        MatrixOps.addAssignScaled(gradient, df, f);
        hessian.addAssign(MatrixSymmetric.multiplySelfOuter(df));
      }
      double v = MatrixOps.findMaximumElement(MatrixViews.diagonal(hessian)).value;

      // Handle errors near 0 where the hessian is singular
      if (Math.abs(k[4]) < v * 1e-8)
      {
        hessian.set(3, 3, hessian.get(3, 3) + 1);
        hessian.set(4, 4, hessian.get(4, 4) + 1);
      }

      return err;
    }

    @Override
    public void postOptimization(double[] k)
    {
      linear1 = norm * k[0];
      linear2 = norm * k[1];
      intensity = norm * k[2];
      mu = k[3];
      sigma = Math.sqrt(0.5 / k[4]);
    }
    //    // Set up limits
//    double[][] limits = new double[][]
//    {
//      DoubleArray.newArray(-0.1, 5.0, 0.5, 0),
//      DoubleArray.newArray(-0.1, 5.0, 0.5, 0),
//      DoubleArray.newArray(1e-8, 2.0, 1e-3, 0),
//      DoubleArray.newArray(start - 10, end + 10, (start + end) / 2, 0),
//      DoubleArray.newArray(mink4, 0.1, (4 * mink4 + 0.1) / 5, 0)
//    };
  }
//</editor-fold>
}
