/*
 * Copyright 2019, Lawrence Livermore National Security, LLC. 
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.rtk.physics;

import java.util.List;

/**
 *
 * @author nelson85
 */
public interface XrayData
{
  Element getElement();

  /**
   * Get the list of edges associated with this electron transition.
   *
   * @return
   */
  List<XrayEdge> getEdges();

  default XrayEdge findEdge(String name)
  {
    for (XrayEdge edge : getEdges())
    {
      if (edge.getName().equals(name))
        return edge;
    }
    return null;
  }
}
