/*
 * Copyright 2016, Lawrence Livermore National Security, LLC. 
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.utility.xml.bind;

import gov.llnl.utility.annotation.Internal;

/**
 *
 * @author nelson85
 */
@Internal
class MarshallerOptionsProxy implements WriterContext.MarshallerOptions
{
  WriterContext.MarshallerOptions parent;
  String key;
  Object obj;

  public MarshallerOptionsProxy(WriterContext.MarshallerOptions options, String key, Object obj)
  {
    this.parent = options;
    this.key = key;
    this.obj = obj;
  }

  @Override
  public <Type> Type get(String key, Class<Type> cls, Type defaultValue)
  {
    if (key.equals(this.key))
      return cls.cast(obj);
    return parent.get(key, cls, defaultValue);
  }

}
