/*
 * Copyright 2016, Lawrence Livermore National Security, LLC. 
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.math;

import java.util.function.DoublePredicate;

/**
 * Support class for DoubleArray.find.
 */
@FunctionalInterface
public interface DoubleConditional extends DoublePredicate
{

  public class GreaterThan implements DoubleConditional
  {
    private final double cond;

    public GreaterThan(double cond)
    {
      this.cond = cond;
    }

    @Override
    public boolean test(double value)
    {
      return value > cond;
    }
  }

  public class GreaterThanEqual implements DoubleConditional
  {
    private final double cond;

    public GreaterThanEqual(double cond)
    {
      this.cond = cond;
    }

    @Override
    public boolean test(double value)
    {
      return value >= cond;
    }
  }

  public class LessThan implements DoubleConditional
  {
    private final double cond;

    public LessThan(double cond)
    {
      this.cond = cond;
    }

    @Override
    public boolean test(double value)
    {
      return value < cond;
    }
  }

  public class LessThanEqual implements DoubleConditional
  {
    private final double cond;

    public LessThanEqual(double cond)
    {
      this.cond = cond;
    }

    @Override
    public boolean test(double value)
    {
      return value <= cond;
    }
  }

  public class NotEqual implements DoubleConditional
  {
    double cond;

    public NotEqual(double cond)
    {
      this.cond = cond;
    }

    @Override
    public boolean test(double value)
    {
      return value != cond;
    }

  }

  public static class Equal implements DoubleConditional
  {
    double cond;

    public Equal(double cond)
    {
      this.cond = cond;
    }

    @Override
    public boolean test(double value)
    {
      return value == cond;
    }
  }
}
