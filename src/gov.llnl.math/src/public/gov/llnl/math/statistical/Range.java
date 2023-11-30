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
public interface Range
{
  boolean contains(double value);
  boolean intersects(Range range);
  
  static Range of(double begin, double end)
  {
    return new RangeImpl(begin, end);
  }
  
  double getBegin();
  double getEnd();
}
