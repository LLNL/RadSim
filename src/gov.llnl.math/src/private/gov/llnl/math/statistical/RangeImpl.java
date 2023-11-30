/*
 * Copyright 2016, Lawrence Livermore National Security, LLC. 
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.math.statistical;

/**
 * @author nelson85
 */
class RangeImpl implements Range
{
  double begin;
  double end;

  public RangeImpl(double begin, double end)
  {
    this.begin = begin;
    this.end = end;
  }

  @Override
  public boolean contains(double value)
  {
    return (value >= begin) && (value < end);
  }

  @Override
  public boolean intersects(Range range)
  {
    return !(range.getEnd() <= begin || range.getBegin() >= end);
  }

  @Override
  public double getBegin()
  {
    return begin;
  }

  @Override
  public double getEnd()
  {
    return end;
  }
  
  @Override
  public String toString()
  {
    return String.format("Range(%f,%f)", begin, end);
  }
}
