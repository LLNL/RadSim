/* 
 * Copyright 2016, Lawrence Livermore National Security, LLC.
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.math.graph.primatives;

/**
 *
 * @author nelson85
 * @param <NodeType>
 * @param <EdgeType>
 */
public abstract class Graph<NodeType extends GraphNode, EdgeType extends EdgeBase>
        extends GraphBase<NodeType, EdgeType>
{
  public Graph()
  {
  }

  @Override
  public void dispose()
  {
  }

  @Override
  public EdgeType link(NodeType parent, NodeType child)
  {
    if (parent == null || child == null)
      return null;
    EdgeType e = allocateEdge();
    e.setNode(parent, false);
    e.setNode(child, true);
    parent.appendEdge(e);
    child.appendEdge(e);
    return e;
  }
};
