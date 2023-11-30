/* 
 * Copyright 2016, Lawrence Livermore National Security, LLC.
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.math.graph.primitives;

import java.util.LinkedList;

/**
 *
 * @author nelson85
 * @param <NodeType>
 * @param <EdgeType>
 */
public abstract class GraphBase<NodeType extends NodeBase, EdgeType extends EdgeBase>
{
  public GraphBase()
  {
  }

  public void dispose()
  {
  }

  // Methods for loader
  public int getNumNodes()
  {
    return nodes.size();
  }

  public int getNumEdges()
  {
    return edges_.size();
  }

//    public int getNode(int id)
//    {
//      return nodes_.get(id);
//    }
//    public EdgeType getEdge(int id)
//    {
//      return edges_.get(id);
//    }
  public abstract NodeType allocateNode();

  public abstract EdgeType allocateEdge();

  protected NodeType addNode(NodeType node)
  {
    node.setId(nodes.size());
    nodes.addLast(node);
    return node;
  }

  protected EdgeType addEdge(EdgeType edge)
  {
    edge.setId(edges_.size());
    edges_.addLast(edge);
    return edge;
  }

  public void clear()
  {
    nodes.clear();
    edges_.clear();
  }

  public LinkedList<NodeType> nodes()
  {
    return nodes;
  }

  public LinkedList<EdgeType> edges()
  {
    return edges_;
  }

  abstract EdgeType link(NodeType parent, NodeType child);
  protected LinkedList<NodeType> nodes = new LinkedList<>();
  protected LinkedList<EdgeType> edges_ = new LinkedList<>();
};
