/*
 * Copyright 2022, Lawrence Livermore National Security, LLC.
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.utility.xml.bind;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Support calls for handling deferred references.
 * @author nelson85
 */
class DeferredMap
{
  HashMap<String, List<DeferredAction>> map = new HashMap<>();

  public void add(String refId, DeferredAction handler)
  {
    List<DeferredAction> obj = map.get(refId);
    if (obj == null)
    {
      obj = new LinkedList<>();
      map.put(refId, obj);
    }
    obj.add(handler);
  }

  List<DeferredAction> get(String ref)
  {
    return map.getOrDefault(ref, Collections.EMPTY_LIST);
  }

  void clear(String ref)
  {
    map.remove(ref);
  }

   void clear()
  {
    map.clear();
  }
  
}
