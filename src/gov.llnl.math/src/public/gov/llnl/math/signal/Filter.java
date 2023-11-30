/*
 * Copyright 2016, Lawrence Livermore National Security, LLC. 
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.math.signal;

/**
 * Interface for functions that act as filters.
 *
 * @author nelson85
 */
public interface Filter
{
  double[] apply(double[] in);

  double[] apply(IterateDoubles iter);

  public interface IterateDoubles
  {
    boolean hasNext();

    double next();

    int size();
  }

  public class RollingBuffer
  {
    double[] memory;
    int index;

    public RollingBuffer(int size)
    {
      memory = new double[size];
    }

    public void add(double value)
    {
      index--;
      if (index < 0)
        index += memory.length;
      memory[index] = value;
    }

    public double get(int i)
    {
      int n = (index + i) % memory.length;
      return memory[n];
    }
  }

}
