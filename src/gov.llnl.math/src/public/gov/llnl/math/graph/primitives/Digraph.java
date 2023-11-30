/* 
 * Copyright 2016, Lawrence Livermore National Security, LLC.
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.math.graph.primitives;

/**
 *
 * @author nelson85
 */
public abstract class Digraph<NodeType extends DigraphNode, EdgeType extends EdgeBase> extends GraphBase<NodeType, EdgeType>
{
  public Digraph()
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
    EdgeType e = this.allocateEdge();
    parent.appendChild(e);
    child.appendParent(e);
    return e;
  }
};
