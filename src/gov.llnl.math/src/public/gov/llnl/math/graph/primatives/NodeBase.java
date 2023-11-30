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
 */
public class NodeBase
{

  public NodeBase()
  {
    id_ = 0;
  }

  public int getId()
  {
    return id_;
  }

  public void setId(int id)
  {
    id_ = id;
  }
  private int id_;

}
