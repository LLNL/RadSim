/*
 * Copyright 2016, Lawrence Livermore National Security, LLC.
 * All rights reserved
 *
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.math.solver;

import gov.llnl.math.MathExceptions;
import gov.llnl.math.MathExceptions.MathException;
import java.util.function.DoubleUnaryOperator;

public class BinarySearchSolver implements Solver
{

  private double start;
  private double end;
  private double tolerance = 1e-3;
  private int maxSteps = 50;

  @Override
  public double solve(DoubleUnaryOperator function, double target)
  {
    double fStart = function.applyAsDouble(start);
    double fEnd = function.applyAsDouble(end);

    if (fStart == target)
      return start;
    if (fEnd == target)
      return end;

    // check bounds
    double fMin = Math.min(fStart, fEnd);
    double fMax = Math.max(fStart, fEnd);

    if (fMin > target || fMax < target)
      throw new MathExceptions.RangeException("target outside range");

    // is the function increasing or decreasing across the interval
    boolean increasing = (fStart == fMin);
    double rangeL = start;
    double rangeR = end;

    double mean = (rangeL + rangeR) / 2.0;
    int steps = maxSteps;
    double fMean = 0;
    while (steps-- > 0)
    {
      fMean = function.applyAsDouble(mean);

      if (Math.abs(fMean - target) < tolerance)
        return mean;

      if (increasing)
      {
        if (fMean < target)
          rangeL = mean;
        else
          rangeR = mean;
      }
      else
      {
        if (fMean > target)
          rangeL = mean;
        else
          rangeR = mean;
      }
      mean = (rangeL + rangeR) / 2.0;
    }

    throw new MathExceptions.ConvergenceException("Did not converge after " + maxSteps + " iterations " + Math.abs(fMean - target) + " > " + tolerance);
  }

  public double getStart()
  {
    return start;
  }

  public void setStart(double start)
  {
    this.start = start;
  }

  public double getEnd()
  {
    return end;
  }

  public void setEnd(double end)
  {
    this.end = end;
  }

  public double getTolerance()
  {
    return tolerance;
  }

  public void setTolerance(double tolerance)
  {
    this.tolerance = tolerance;
  }

  public int getMaxSteps()
  {
    return maxSteps;
  }

  public void setMaxSteps(int maxSteps)
  {
    this.maxSteps = maxSteps;
  }

  static public void main(String[] args)
  {
    BinarySearchSolver bss = new BinarySearchSolver();
    bss.setStart(0);
    bss.setEnd(3);
    bss.setTolerance(1e-5);
    bss.setMaxSteps(25);

    DoubleUnaryOperator f = (double x) -> x*x;

    for (double i = 0; i < 9.1; i+=0.01)
    {
      try
      {
        double x = bss.solve(f, i);
        //System.out.println( x + " "+ f.evaluate(x));
      }catch (MathException e)
      {

        System.out.println(i + " " + e.getMessage());
      }
    }
    System.out.println("done");

  }

}
