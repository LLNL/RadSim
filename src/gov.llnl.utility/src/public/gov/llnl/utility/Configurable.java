/*
 * Copyright 2016, Lawrence Livermore National Security, LLC. 
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.utility;

import java.util.Map;

/**
 * Class for configuring options for factories, input streams, and readers.
 * 
 * This same concept comes up often. We have a factory or a reader
 * that has some optional behavior that can be set prior to using the 
 * method.  These properties after the behavior but exposing them as
 * a huge list would clutter up the interface.  Thus usually there is 
 * an enum or other array of strings that we are drawing from.  But this
 * list can grow.
 * 
 * This interface is present to tag interfaces that operate in this 
 * fashion.  We may try to to switch to a key enum but the lack of expansion
 * sometimes proves challenging to manage.  Requiring the key to be an interface
 * with a get string method and then having the interface be implemented by 
 * a enum would allow implementing both open and closed property lists.
 * 
 * @author nelson85
 */
public interface Configurable
{

  /**
   * Set an option for the configurable object.
   *
   * @param key is the option name to set.
   * @param value is the value to set.
   */
  default void setProperty(String key, Object value)
  {
    
  }

  /** Check if a property is set on this configurable object.
   * 
   * @param key
   * @return 
   */
  default boolean hasProperty(String key)
  {
    return getProperty(key) != null;
  }

  /**
   * Get the value of an option set for the configurable.
   *
   * @param key is the option to get.
   * @return the value of the option or null if no such option exists.
   * @throws NullPointerException if key is null.
   */
  default Object getProperty(String key)
          throws NullPointerException
  {
    return null;
  }

  /**
   * Get the value associated with this key, assuming the value is of class T
   *
   * @param <T> The type to return.
   * @param key is the name of the option.
   * @param klass is the requested class type.
   * @return the option, or null if not found.
   * @throws ClassCastException if the option exists but is the wrong type.
   * @throws NullPointerException if key or klass are null.
   */
  default <T> T getProperty(String key, Class<T> klass)
          throws ClassCastException, NullPointerException
  {
    return klass.cast(getProperty(key));
  }
  
    /**
   * Get the properties set on this document reader.
   * 
   * This is an optional operation.
   *
   * @return an unmodifiable collection of properties.
   * @throws UnsupportedOperationException if not implemented.
   */
  default Map<String, Object> getProperties()
  {
    throw new UnsupportedOperationException("Not supported");
  }

}
