/*
 * Copyright 2022, Lawrence Livermore National Security, LLC.
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.utility.xml.bind;

/**
 * Small cache to try to deal with handler list recycling in readers.
 *
 * This may be better with a standard implementation, but we have not
 * had time to verify this.
 */
class ElementHandlerCache
{
  Reader.ElementHandler[] keys = new Reader.ElementHandler[64];
  Reader.ElementHandlerMap[] values = new Reader.ElementHandlerMap[64];

  Reader.ElementHandlerMap get(Reader.ElementHandler h)
  {
    // Cech for an entry
    int key = getKey(h);
    // If the space is not occupied with the current entry then it is a cache miss.
    if (keys[key] != h)
      return null;
    // Cache hit.
    return values[key];
  }

  void put(Reader.ElementHandler h, Reader.ElementHandlerMap context)
  {
    // Replace an existing entry.
    int key = getKey(h);
    values[key] = context;
  }

  int getKey(Reader.ElementHandler h)
  {
    int k = h.hashCode();
    k += (k >> 8);
    return k & 63;
  }
  
}
