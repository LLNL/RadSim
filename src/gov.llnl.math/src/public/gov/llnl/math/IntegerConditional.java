/*
 * Copyright 2016, Lawrence Livermore National Security, LLC. 
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.math;

import java.util.function.IntPredicate;

/**
 *
 * @author nelson85
 */
public interface IntegerConditional extends IntPredicate 
{
//  /**
//   * Returns true if the condition is met.
//   *
//   * @param value
//   * @return true if condition met.
//   */
//  boolean test(int value);

  public static class GreaterThan implements IntegerConditional
  {
    private final int cond;

    public GreaterThan(int cond)
    {
      this.cond = cond;
    }

    @Override
    public boolean test(int value)
    {
      return value > cond;
    }
  }

  public static class GreaterThanEqual implements IntegerConditional
  {
    private final int cond;

    public GreaterThanEqual(int cond)
    {
      this.cond = cond;
    }

    @Override
    public boolean test(int value)
    {
      return value >= cond;
    }
  }

  public static class LessThan implements IntegerConditional
  {
    private final int cond;

    public LessThan(int cond)
    {
      this.cond = cond;
    }

    @Override
    public boolean test(int value)
    {
      return value < cond;
    }
  }

  public static class LessThanEqual implements IntegerConditional
  {
    private final int cond;

    public LessThanEqual(int cond)
    {
      this.cond = cond;
    }

    @Override
    public boolean test(int value)
    {
      return value <= cond;
    }
  }

}
