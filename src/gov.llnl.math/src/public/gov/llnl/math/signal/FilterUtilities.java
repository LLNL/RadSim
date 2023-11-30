/*
 * Copyright 2016, Lawrence Livermore National Security, LLC. 
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.math.signal;

import gov.llnl.math.DoubleArray;

/**
 *
 * @author nelson85
 */
public class FilterUtilities
{

  public static double[] applyReverse(Filter filter, double[] in)
  {
    return DoubleArray.reverseAssign(filter.apply(new IterateDoublesReverse(in)));
  }

  public static double[] applySymmetric(Filter filter, double[] in)
  {
    double[] out = filter.apply(new IterateDoublesForward(in));
    return DoubleArray.reverseAssign(filter.apply(new IterateDoublesReverse(out)));
  }

  public static class IterateDoublesForward implements Filter.IterateDoubles
  {
    double[] memory;
    int begin;
    int end;
    int n;

    public IterateDoublesForward(double[] in)
    {
      memory = in;
      begin = 0;
      end = in.length;
      n = 0;
    }

    @Override
    public boolean hasNext()
    {
      return n != end;
    }

    @Override
    public double next()
    {
      int current = n;
      ++n;
      return memory[current];
    }

    @Override
    public int size()
    {
      return end - begin;
    }
  }

  public static class IterateDoublesReverse implements Filter.IterateDoubles
  {
    double[] memory;
    int begin;
    int end;
    int n;

    public IterateDoublesReverse(double[] in)
    {
      memory = in;
      begin = 0;
      end = in.length;
      n = end - 1;
    }

    @Override
    public boolean hasNext()
    {
      return n >= begin;
    }

    @Override
    public double next()
    {
      int current = n;
      --n;
      return memory[current];
    }

    @Override
    public int size()
    {
      return end - begin;
    }
  }

}
