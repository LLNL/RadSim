/*
 * Copyright 2016, Lawrence Livermore National Security, LLC. 
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.math.statistical;

/**
 *
 * @author nelson85
 */
public class ExtremeImpl implements Histogram.Extreme
{
  private final boolean unique;
  private int bin;
  private final int counts;

  public ExtremeImpl(int bin, int max, boolean unique)
  {
    this.counts = max;
    this.bin = bin;
    this.unique = unique;
  }

  @Override
  public boolean isUnique()
  {
    return unique;
  }

  @Override
  public int getIndex()
  {
    return this.bin;
  }

  @Override
  public int getCounts()
  {
    return this.counts;
  }

}
