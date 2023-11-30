/* 
 * Copyright 2019, Lawrence Livermore National Security, LLC.
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.math.stream;

/**
 *
 * @author nelson85
 */
public interface DoubleIndex
{
  /**
   * Get the value at the index.
   * 
   * FIXME this should likely throw NoSuchElementException to be consistent with 
   * OptionalInt interface in Java if the value does not exist, but this requires
   * examining all existing code.  Of course anything that uses the value without
   * checking existing code is likely buggy.
   * 
   * @return the value.
   */
  double getValue();

  /**
   * Get the index of where the condition was met.  
   * 
   * @return the index or -1 if not found. 
   */
  int getIndex();

  default boolean exists()
  {
    return getIndex() >= 0;
  }
  
}
