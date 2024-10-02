/*
 * Copyright 2016, Lawrence Livermore National Security, LLC.
 * All rights reserved
 *
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.math.graph;

import gov.llnl.math.graph.primitives.Connection;
import gov.llnl.math.graph.primitives.Digraph;
import gov.llnl.utility.xml.bind.ReaderInfo;
import java.io.PrintStream;

@ReaderInfo(BayesianNetworkReader.class)
public class BayesianNetwork extends Digraph<BayesianNetworkNode, BayesianNetworkEdge>
{
  @Override
  public BayesianNetworkNode allocateNode()
  {
    return addNode(new BayesianNetworkNode());
  }

  @Override
  public BayesianNetworkEdge allocateEdge()
  {
    return addEdge(new BayesianNetworkEdge());
  }

  public BayesianNetwork()
  {
  }

  @Override
  public void dispose()
  {
    super.dispose();
  }

  public double getProbability(GraphQuery query)
  {
    double probability = 1;
    for (BayesianNetworkNode node : nodes)
    {
      probability *= node.getProbability(query);
    }
    return probability;
  }

  public void initializeQuery(GraphQuery query)
  {
    query.resize(nodes.size());
  }

  public void dump(PrintStream os)
  {
    os.println("BayesianNetwork:  " + nodes.size());
    for (BayesianNetworkNode node : this.nodes())
    {
      os.println("  " + node.getId() + ": " + node.getNumParents() + " " + node.getNumChildren());
      if (node.getNumParents() > 0)
      {
        os.print("    parents ");
        for (Connection<BayesianNetworkNode, BayesianNetworkEdge> iter : node.getParents())
        {
          os.println(iter.getNode().getId() + " ");
        }
        os.println();
        os.print("    children ");
        for (Connection<BayesianNetworkNode, BayesianNetworkEdge> iter : node.getChildren())
        {
          os.println(iter.getNode().getId() + " ");
        }
        os.println();
        os.print("    probability ");
        for (int i = 0; i < node.getProbabilityTableSize(); ++i)
          os.println(node.getProbabilityTable(i) + " ");
        os.println();
      }
    }
  }
}

//// Data ordering for the probability table is
////   [Pn .. P1 Self]
////
////  Thus if D has parents ABC
////    ___   __  _ _    _  __
//// _  ABC  ABC  ABC  ABC  ABC ...
//// D   0    2    4    6    8
//// D   1    3    5    7    9
////
