/* 
 * Copyright 2016, Lawrence Livermore National Security, LLC.
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.math.graph.primatives;

// There are two connections in each Edge
public abstract class ConnectionBase
{

  public ConnectionBase()
  {
    parent_ = null;
    next_ = null;
  }

  public ConnectionBase getNext()
  {
    return next_;
  }

  void setNext(ConnectionBase next)
  {
    next_ = next;
  }
  protected EdgeBase parent_;
  protected ConnectionBase next_;

}
