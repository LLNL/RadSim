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
 */
public class Edge<NodeType extends NodeBase> extends EdgeBase
{
  public Edge()
  {
  }

//  public void setNode(NodeType node, boolean side)
//  {
//    if (side)
//      node1_ = node;
//    else
//      node0_ = node;
//  }
  public NodeType getNode(boolean side)
  {
    if (side)
      return (NodeType) node1_;
    return (NodeType) node0_;
  }

  @Override
  public Connection<NodeType, Edge<NodeType>> getConnection(boolean side)
  {
    return (Connection<NodeType, Edge<NodeType>>) super.getConnection(side);
  }

  @Override
  public NodeType getNodeFrom(ConnectionBase c)
  {
    return (NodeType) super.getNodeFrom(c);
  }

  @Override
  public Connection<NodeType, Edge<NodeType>> getConnectionFrom(NodeBase node)
  {
    return (Connection<NodeType, Edge<NodeType>>) super.getConnectionFrom(node);
  }
};
