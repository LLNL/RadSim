/* 
 * Copyright 2016, Lawrence Livermore National Security, LLC.
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.math.graph;

import gov.llnl.math.graph.primatives.Connection;
import gov.llnl.math.graph.primatives.DigraphNode;

/**
 *
 * @author nelson85
 */
public class BayesianNetworkNode extends DigraphNode<BayesianNetworkNode, BayesianNetworkEdge>
{
  int queryId;
  double[] probability;

  public BayesianNetworkNode()
  {
  }

  @Override
  public void dispose()
  {
    probability = null;
    super.dispose();
  }

  public int getQueryId()
  {
    return queryId;
  }

  public void setQueryId(int id)
  {
    queryId = id;
  }

  public int getProbabilityTableSize()
  {
    return (1 << (this.getParents().size() + 1));
  }

  public double getProbabilityTable(int i)
  {
    return probability[i];
  }

  public void setProbabilityTable(double[] v)
  {
    if (this.getProbabilityTableSize() != v.length)
      throw new RuntimeException("Bad size");
    this.probability = v;
  }

  public void setProbability(int index, double prob)
  {
    if (probability == null)
      allocateProbabilityTable();
    probability[index] = prob;
  }

  public void allocateProbabilityTable()
  {
    int sz = getProbabilityTableSize();
    probability = new double[sz];
  }

  public double getProbability(GraphQuery query)
  {
    // first parent is in lowest bit
    int index = query.isTrue(queryId) ? 1 : 0;
    int i0 = 1;
    for (Connection<BayesianNetworkNode, BayesianNetworkEdge> iter : this.getParents())
    {
      index |= (query.isTrue(iter.getNode().getQueryId()) ? 1 : 0) << i0;
      i0++;
    }
    return probability[index];
  }
}
