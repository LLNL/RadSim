/*
 * Copyright 2016, Lawrence Livermore National Security, LLC. 
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.utility;

/**
 *
 * @author nelson85
 */
public interface Copyable
{
  /**
   * Produce a deep copy of an object such that all stateful information is
   * separated. Immutable objects may be shared between objects. In general this
   * should just call the copy constructor of the object assuming the copy
   * constructor is deep.
   *
   * @return
   */
  Object copyOf();
}
