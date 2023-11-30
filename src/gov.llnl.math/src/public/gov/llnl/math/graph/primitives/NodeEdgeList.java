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
public class NodeEdgeList<NodeType extends NodeBase, EdgeType extends EdgeBase> extends NodeEdgeListBase
        implements java.lang.Iterable<Connection<NodeType, EdgeType>>
{
  public class Iterator implements java.util.Iterator<Connection<NodeType, EdgeType>>
  {
    ConnectionBase current;

    @Override
    public boolean hasNext()
    {
      return current != null;
    }

    @Override
    public Connection<NodeType, EdgeType> next()
    {
      ConnectionBase next = current;
      current = next.getNext();
      return (Connection<NodeType, EdgeType>) next;
    }

    public Iterator(ConnectionBase connection)
    {
      current = connection;
    }

    @Override
    public void remove()
    {
      throw new UnsupportedOperationException("Not supported yet.");
    }
  }

  public Iterator iterator()
  {
    return new Iterator(this.head_);
  }
}
