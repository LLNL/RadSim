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
 * @param <EdgeType>
 */
public class GraphNode<EdgeType extends EdgeBase> extends NodeBase
{
//    typedef GraphNode<EdgeT> SelfType;
//    typedef EdgeT EdgeType;
//    typedef NodeEdgeList<EdgeType> EdgeList;
//    typedef typename EdgeList::SizeType SizeType;
  public GraphNode()
  {
  }

  public void dispose()
  {
    edges_ = null;
  }

  // Sizes of the edges
  public int getNumEdges()

  {
    return edges_.size();
  }

  // Interface to create node linkages (Should be private)
  public void appendEdge(EdgeType edge)
  {
    edges_.append(edge.getConnectionFrom(this));
  }

  // Access to edges
  public NodeEdgeList getEdges()
  {
    return edges_;
  }

  private NodeEdgeList edges_ = new NodeEdgeList();
};
