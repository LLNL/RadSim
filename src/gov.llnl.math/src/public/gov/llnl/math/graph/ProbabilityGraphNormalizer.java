/* 
 * Copyright 2016, Lawrence Livermore National Security, LLC.
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.math.graph;

import gov.llnl.math.graph.primitives.Connection;
import gov.llnl.math.graph.primitives.NodeEdgeList;
import gov.llnl.math.graph.primitives.NodeEdgeList.Iterator;
import java.util.LinkedList;
import java.util.TreeMap;

/**
 * Helper class for loader
 *
 * @author nelson85
 */
public class ProbabilityGraphNormalizer
{
  public static class Datum
  {
    ProbabilityGraphNode node;
    int neighborCount;
    boolean visited;

    public Datum()
    {
      node = null;
      neighborCount = 0;
      visited = false;
    }
  };

  TreeMap<ProbabilityGraphNode, Datum> data;
  LinkedList<Datum> queue;

  public ProbabilityGraphNormalizer()
  {
  }

  public void dispose()
  {
    data = null;
    queue = null;
  }

// Assumptions: 
//   1) The first edge of an interconnection node points the 
//      dependent node (child in the Bayesian graph).
//   2) The remaining edges point to parents in the original Bayesian graph
  public void process(ProbabilityGraph pg)
  {
    // Step 1 associate data with the probability graph

    LinkedList<ProbabilityGraphNode> nodes = pg.nodes();
    data = new TreeMap<>();
    queue = new LinkedList<>();
    {
      for (ProbabilityGraphNode node : nodes)
      {
        Datum datum = new Datum();
        data.put(node, datum);
        datum.node = node;
        datum.neighborCount = node.getNumEdges();
      }
    }

    // Step 2 find all nodes that are ready to proceed and push them the queue
    for (Datum datum : data.values())
    {
      ProbabilityGraphNode node = datum.node;

      // Skip factor nodes
      if (!node.isVariableNode())
        continue;

      // Must have a factor table defined
      if (!node.hasFactorTable())
        continue;

      datum.neighborCount = 1;
      queue.addLast(datum);
    }

    // Step 3 process the graph
    while (!queue.isEmpty())
    {
      // Dequeue the first node
      Datum datum = queue.getFirst();
      queue.removeFirst();
      if (datum.visited)
        throw new RuntimeException("internal consistency check failed (visited).");
      ProbabilityGraphNode node = datum.node;
      NodeEdgeList<ProbabilityGraphNode, ProbabilityGraphEdge> edges = node.getEdges();

      if (node.isVariableNode())
      {
        // Find any unvisited neighbor, decrement the neighbor count 
        // and then push them on the queue if ready
        for (Connection<ProbabilityGraphNode, ProbabilityGraphEdge> iter : edges)
        {
          ProbabilityGraphNode node2 = iter.getNode();
          Datum datum2 = data.get(node2);

          if (datum2.visited == true)
            continue;
          datum2.neighborCount--;

          if (datum2.neighborCount < 2)
            queue.addLast(datum2);
        }
      }
      else if (node.isFactorNode())
      {
        // Compute the marginals using the table and the parents marginals
        double f0 = 0;
        double f1 = 0;
        for (int i = 0; i < node.getFactorTableSize(); i += 2)
        {
          int j = 2;
          Iterator iter = edges.iterator();
          iter.next(); // skip the first
          double q = 1;
          while (iter.hasNext())
          {
            Connection<ProbabilityGraphNode, ProbabilityGraphEdge> f = iter.next();
            ProbabilityGraphNode node2 = f.getNode();
            q *= node2.getFactor((i & j) != 0 ? 1 : 0);
            j <<= 1;
          }
          f0 += node.getFactor(i) * q;
          f1 += node.getFactor(i + 1) * q;
        }
        // Allocate the table for the dependent node and set the marginals
        ProbabilityGraphNode node_front = edges.iterator().next().getNode();
        if (node_front.hasFactorTable())
          throw new RuntimeException("table is already allocated");
        node_front.allocateFactorTable();
        node_front.setFactor(0, f0);
        node_front.setFactor(1, f1);

        // queue the dependent node
        Datum datum2 = data.get(node_front);
        queue.addLast(datum2);

        // Watch for divide by 0
        if (f0 == 0)
          f0 = 1;
        if (f1 == 0)
          f1 = 1;

        // Next we need to divide out the marginal from the table
        for (int i = 0; i < node.getFactorTableSize(); i += 2)
        {
          node.setFactor(i, node.getFactor(i) / f0);
          node.setFactor(i + 1, node.getFactor(i + 1) / f1);
        }
      }
      else
      {
        throw new RuntimeException("Unknown node type");
      }

      datum.visited = true;
    }

    // Step 4 check to see if the graph has been visited properly
    for (Datum datum : data.values())
    {
      if (!datum.visited)
        throw new RuntimeException("Unvisited node");
    }
  }
}
