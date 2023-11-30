/*
 * Copyright 2016, Lawrence Livermore National Security, LLC.
 * All rights reserved
 *
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.math;

import gov.llnl.math.matrix.Matrix;
import gov.llnl.math.matrix.MatrixFactory;
import gov.llnl.math.matrix.MatrixOps;
import gov.llnl.math.matrix.special.MatrixTriDiagonal;
import java.util.function.DoubleUnaryOperator;

/**
 * This is an implementation of a simple first order smoothing filter.
 *
 * @author nelson85
 */
public class Smoothing
{
  /**
   * @param y column vector
   * @param constraint
   * @return 
   */
  static public double[] smooth(double[] y, double constraint)
  {
    return smooth(MatrixFactory.createColumnVector(y), constraint).flatten();
  }   
     
  static public Matrix smooth(Matrix m, double constraint)
  {
    // PENDING verify that this is the same as ranged version and remove it.

    // Need MatrixTriDiagonal to make this efficient
    try
    {
      int n = m.rows();
      MatrixTriDiagonal A = new MatrixTriDiagonal(n);
      for (int i = 1; i < n - 1; ++i)
      {
        A.set(i, i, 1 + 2 * constraint);
      }
      A.set(0, 0, 1 + constraint);
      A.set(n - 1, n - 1, 1 + constraint);
      for (int i = 0; i < n - 1; ++i)
      {
        A.set(i + 1, i, -constraint);
        A.set(i, i + 1, -constraint);
      }
      return MatrixOps.divideLeft(A, m);
    }
    catch (MathExceptions.SizeException ex)
    {
      throw new RuntimeException(ex); // does not occur
    }
  }

  /**
   * The result is a new matrix.
   *
   * @param m
   * @param begin
   * @param end
   * @param constraint
   * @return
   */
  static public Matrix smoothRange(Matrix m, int begin, int end, double constraint)
  {
    // Need MatrixTriDiagonal to make this efficient
    try
    {
      int n = m.rows();
      MatrixTriDiagonal A = new MatrixTriDiagonal(n);
      for (int i = 0; i < begin; ++i)
        A.set(i, i, 1);
      for (int i = end; i < n; ++i)
        A.set(i, i, 1);

      for (int i = begin + 1; i < end - 1; ++i)
      {
        A.set(i, i, 1 + 2 * constraint);
      }
      A.set(begin, begin, 1 + constraint);
      A.set(end - 1, end - 1, 1 + constraint);

      for (int i = begin; i < end - 1; ++i)
      {
        A.set(i + 1, i, -constraint);
        A.set(i, i + 1, -constraint);
      }
      return MatrixOps.divideLeft(A, m);
    }
    catch (MathExceptions.SizeException ex)
    {
      throw new RuntimeException(ex); // does not occur
    }
  }

  /**
   * Smooth a range with a varying constraint function.
   *
   * @param m is a matrix which will be smoothed operating on the columns.
   * @param begin is the row to start smoothing (inclusive).
   * @param end is the row to end smoothing (exclusive).
   * @param constraint is a function of the row number.
   * @return a new matrix with smoothed data.
   */
  static public Matrix smoothRange(Matrix m, int begin, int end, DoubleUnaryOperator constraint)
  {
    // Need MatrixTriDiagonal to make this efficient
    try
    {
      int n = m.rows();
      MatrixTriDiagonal A = new MatrixTriDiagonal(n);

      for (int i = 0; i < begin; ++i)
        A.set(i, i, 1);
      for (int i = end; i < n; ++i)
        A.set(i, i, 1);

      double c2 = 0;
      for (int i = begin; i < end - 1; ++i)
      {
        double c = constraint.applyAsDouble(i);
        A.set(i, i, 1 + c + c2);
        A.set(i + 1, i, -c);
        A.set(i, i + 1, -c);
        c2 = c;
      }
      A.set(end - 1, end - 1, 1 + c2);

      return MatrixOps.divideLeft(A, m);
    }
    catch (MathExceptions.SizeException ex)
    {
      throw new RuntimeException(ex); // does not occur
    }
  }
  
  public static DoubleUnaryOperator linear(double x)
  {
    return (double d)-> d*x;
  }
  
  public static DoubleUnaryOperator sqrt(double x)
  {
    return (double d)-> Math.sqrt(d)*x;
  }
  
  public static DoubleUnaryOperator sqr(double x)
  {
    return (double d)-> d*d*x;
  }

}
