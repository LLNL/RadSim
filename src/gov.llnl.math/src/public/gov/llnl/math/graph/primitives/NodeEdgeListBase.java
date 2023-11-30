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
public class NodeEdgeListBase
{
  public NodeEdgeListBase()
  {
    size_ = 0;
    head_ = null;
  }

  public int size()
  {
    return size_;
  }

  public void append(ConnectionBase e)
  {
    size_++;
    if (head_ == null)
    {
      head_ = e;
      return;
    }
    // Track to end of the list and append
    ConnectionBase e2 = head_;
    while (e2.getNext() != null)
    {
      e2 = e2.getNext();
    }
    e2.setNext(e);
  }

  int size_;
  ConnectionBase head_;
};
