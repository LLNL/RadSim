/*
 * Copyright 2016, Lawrence Livermore National Security, LLC. 
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.utility.io;

/**
 *
 * @author nelson85
 */
public interface PropertyInterface
{
  /**
   * Set a property in the Class.Properties should only be set prior to start of
   * accessing the resource.
   *
   * @param key
   * @param property
   * @throws gov.llnl.utility.io.PropertyException
   */
  void setProperty(String key, Object property) throws PropertyException;

  /**
   * Get a property in the Class.Properties should only be set prior to start of
   * accessing the resource.
   *
   * @param key
   * @return
   * @throws gov.llnl.utility.io.PropertyException
   */
  Object getProperty(String key) throws PropertyException;

}
