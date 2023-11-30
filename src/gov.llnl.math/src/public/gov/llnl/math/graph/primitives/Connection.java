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
 * @param <NodeType>
 * @param <EdgeType>
 */
public final class Connection<NodeType extends NodeBase, EdgeType extends EdgeBase> extends ConnectionBase
{
  public Connection()
  {
  }

  public int getId()
  {
    return parent_.getId();
  }

  public NodeType getNode()
  {
    return (NodeType) parent_.getNodeFrom(this);
  }

  public EdgeType getEdge()
  {
    return (EdgeType) parent_;
  }

  public Connection<NodeType, EdgeType> getNext()
  {
    return (Connection<NodeType, EdgeType>) next_;
  }
};
