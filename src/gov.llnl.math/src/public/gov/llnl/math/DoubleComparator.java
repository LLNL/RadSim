/*
 * Copyright 2016, Lawrence Livermore National Security, LLC. 
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.math;

/**
 *
 * @author nelson85
 */
public interface DoubleComparator
{

  /**
   * Compares two doubles.
   *
   * @param t1
   * @param t2
   * @return a negative integer, zero, or a positive integer as the first
   * argument is less than, equal to, or greater than the second.
   */
  int compare(double t1, double t2);

  class Absolute implements DoubleComparator
  {
    @Override
    final public int compare(double t1, double t2)
    {
      t1 = (t1 > 0) ? t1 : -t1;
      t2 = (t2 > 0) ? t2 : -t2;
      return Double.compare(t1, t2);
    }
  }

  class Positive implements DoubleComparator
  {
    @Override
    final public int compare(double t1, double t2)
    {
      return Double.compare(t1, t2);
    }
  }

  class Negative implements DoubleComparator
  {
    @Override
    public final int compare(double t1, double t2)
    {
      return -Double.compare(t1, t2);
    }
  }
}
