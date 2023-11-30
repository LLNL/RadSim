/* 
 * Copyright 2019, Lawrence Livermore National Security, LLC.
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.math.stream;

/**
 *
 * Mutable implementation of DoubleIndex.
 * 
 * @author nelson85
 */
public class DoubleIndexImpl implements DoubleIndex
{
  public int index_;
  public double value_;

  public DoubleIndexImpl(int index, double value)
  {
    this.index_ = index;
    this.value_ = value;
  }

  @Override
  public int getIndex()
  {
    return this.index_;
  }

  @Override
  public double getValue()
  {
    return this.value_;
  }
  
}
