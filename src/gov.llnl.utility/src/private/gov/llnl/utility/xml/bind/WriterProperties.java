/*
 * Copyright 2022, Lawrence Livermore National Security, LLC.
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.utility.xml.bind;

import java.util.HashMap;

/**
 *
 * @author nelson85
 */
class WriterProperties extends HashMap<String, Object> implements WriterContext.MarshallerOptions
{
  private String cacheKey;
  private Object cacheValue;

  @Override
  public Object put(String value, Object object)
  {
    clearCache();
    return super.put(value, object);
  }

  @Override
  public <Type> Type get(String key, Class<Type> cls, Type defaultValue)
  {
    if (key.equals(cacheKey))
      return cls.cast(cacheValue);
    Object value = get(key);
    if (value == null)
    {
      if (containsKey(key) == false)
        return defaultValue;
      return null;
    }
    if (cls.isInstance(value))
      return cache(key, cls.cast(value));
    throw new ClassCastException("Property is not correct type " + key + " " + cls);
  }

  private <Type> Type cache(String key, Type value)
  {
    this.cacheKey = key;
    this.cacheValue = value;
    return value;
  }

  private void clearCache()
  {
    this.cacheKey = null;
  }
  
}
