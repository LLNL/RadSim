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
public class EdgeBase
{
  public EdgeBase()
  {
    id_ = 0;
    node0_ = null;
    node1_ = null;
    connection0_.parent_ = this;
    connection1_.parent_ = this;
  }

  public int getId()
  {
    return id_;
  }

  public void setId(int id)
  {
    id_ = id;
  }

  public void setNode(NodeBase node, boolean side)
  {
    if (side)
      node1_ = node;
    else
      node0_ = node;
  }

  public ConnectionBase getConnection(boolean side)
  {
    if (side)
      return connection1_;
    return connection0_;
  }

  public ConnectionBase getConnectionFrom(NodeBase node)
  {
    if (node0_ == node)
      return connection1_;
    if (node1_ == node)
      return connection0_;
    throw new RuntimeException("connection not found");
  }

  // Methods for connection to autoreference the other side
  public NodeBase getNodeFrom(ConnectionBase connection)
  {
    if (connection == connection0_)
      return node0_;
    if (connection == connection1_)
      return node1_;
    throw new RuntimeException("Bad connection request");
  }
  protected int id_;
  protected NodeBase node0_;
  protected NodeBase node1_;
  protected ConnectionBase connection0_ = new Connection();
  protected ConnectionBase connection1_ = new Connection();
};
