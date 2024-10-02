/*
 * Copyright 2016, Lawrence Livermore National Security, LLC.
 * All rights reserved
 *
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.math.graph;

import gov.llnl.math.graph.ProbabilityGraphNode.NodeType;
import gov.llnl.math.graph.primitives.Connection;
import gov.llnl.math.graph.primitives.Graph;
import gov.llnl.math.graph.primitives.NodeEdgeList;
import gov.llnl.utility.UUIDUtilities;
import gov.llnl.utility.xml.bind.ReaderInfo;
import java.io.PrintStream;
import java.io.Serializable;

@ReaderInfo(ProbabilityGraphReader.class)
public class ProbabilityGraph extends Graph<ProbabilityGraphNode, ProbabilityGraphEdge>
        implements Serializable
{
  private static final long serialVersionUID = UUIDUtilities.createLong("ProbabilityGraph");
  int variableCount = 0;
  int conditions;

  /**
   * Creates a query of the appropriate size to use with this probability graph.
   *
   * @return a newly allocated GraphQuery.
   */
  public GraphQuery allocateQuery()
  {
    int max = 0;
    for (ProbabilityGraphNode node : this.nodes)
    {
      max = Math.max(max, node.queryId);
    }
    return new GraphQuery(this.getVariableCount(), this.getConditionCount());
  }

  @Override
  public ProbabilityGraphNode allocateNode()
  {
    return addNode(new ProbabilityGraphNode());
  }

  @Override
  public ProbabilityGraphEdge allocateEdge()
  {
    return addEdge(new ProbabilityGraphEdge());
  }

  /**
   * Evaluate the probability of a condition occurring for the this graph. Note
   * this does not compute the probability of incomplete queries.
   *
   * @param query is a set containing true, false or partial specifications for
   * each graph node.
   * @return the probability for this query.
   * @see ViterbiProbabilityGraph
   */
  public double computeProbability(GraphQuery query)
  {
    if (query.hasPartials())
      return computeProbabilityPartials(query);

    double probability = 1;
    for (ProbabilityGraphNode node : nodes())
    {
      if (node.isVariableNode())
      {
        int id = node.getQueryId();
        double[] factors = node.getVariableFactors(query);
        if (query.isTrue(id))
          probability *= factors[1];
        else if (query.isFalse(id))
          probability *= factors[0];
        else
        {
          query.dump(System.out);
          System.out.println();
          dump(System.out);
          throw new RuntimeException("unspecified state (QueryId=" + id + ")");
        }
      }
      else if (node.isFactorNode())
      {
        probability *= computeVariableNodeProbability(node, query);
      }
    }
    return probability;
  }

  private double computeProbabilityPartials(GraphQuery query)
  {
    double out = 0;
    GraphQuery query2 = new GraphQuery();
    query2.assignState(query);
    GraphQuery.PartialDatum[] partials = query.partials.toArray(new GraphQuery.PartialDatum[0]);
    partials = query.partials.toArray(partials);
    for (int i0 = 0; i0 < (1 << partials.length); ++i0)
    {
      double weight = 1;
      for (int i1 = 0; i1 < partials.length; ++i1)
      {
        GraphQuery.PartialDatum datum = partials[i1];
        if (((i0 >> i1) & 1) == 1)
        {
          query2.setTrue(datum.id);
          weight *= datum.value;
        }
        else
        {
          query2.setFalse(datum.id);
          weight *= (1 - datum.value);
        }
      }
      if (weight != 0)
        out += computeProbability(query2) * weight;
    }
    return out;
  }

  private double computeVariableNodeProbability(ProbabilityGraphNode node, GraphQuery query)
  {
    int index = 0;
    int c = 1;
    NodeEdgeList<ProbabilityGraphNode, ProbabilityGraphEdge> el = node.getEdges();
    for (Connection<ProbabilityGraphNode, ProbabilityGraphEdge> iter2 : el)
    {
      int qid = iter2.getNode().getQueryId();
      if (query.isTrue(qid))
        index |= c;
      c <<= 1;
    }
    return node.getFactor(index);
  }

  /**
   * Prints the content of a Probability graph to a PrintStream.
   *
   * @param os is the stream to dump to.
   */
  public void dump(PrintStream os)
  {
    os.println("ProbabilityGraph: " + nodes.size());
    for (ProbabilityGraphNode iter : nodes)
    {
      os.println("  " + iter.getId() + ": " + iter.getNumEdges() + " " + iter.getName());
      os.println("    query_id " + iter.getQueryId());
      if (iter.getNumEdges() > 0)
      {
        os.print("    neighbors ");
        NodeEdgeList<ProbabilityGraphNode, ProbabilityGraphEdge> el = iter.getEdges();
        os.print("(" + el.size() + ") ");
        for (Connection<ProbabilityGraphNode, ProbabilityGraphEdge> i2 : el)
        {
          os.print(i2.getNode().getId() + " ");
        }
        os.println();
      }
      os.println("    type " + iter.getType());
      if (iter.hasFactorTable())
      {
        os.print("    factors ");
        int size = iter.getFactorTableSize();
        os.print("(" + size + ") ");
        for (int i = 0; i < size; ++i)
        {
          os.print(iter.getFactor(i) + " ");
        }
        os.println();
      }
    }
  }

  void setVariableCount(int variableCount)
  {
    this.variableCount = variableCount;
  }

  public int getVariableCount()
  {
    return variableCount;
  }

  public int getConditionCount()
  {
    return conditions;
  }

  public void setConditionCount(int conditions)
  {
    this.conditions = conditions;
  }

  /**
   * Checks to see if the graph depends on a variable.
   *
   * @param id
   * @return true if the graph depends on this variable.
   */
  public boolean hasVariable(int id)
  {
    if (id < 0)
      return true;
    for (ProbabilityGraphNode node : this.nodes)
    {
      if (node.type != NodeType.VARIABLE)
        continue;
      if (node.queryId == id)
        return true;
    }
    return false;
  }
}

//// Note that the factors do not appear to be conditional probabilities.  If for example
////   B and C are children of A then our factor is
////     F=P(A) P(B|A)/(P(A)P(B|A)+P(/A)P(B|/A)) P(C|A)/(P(A)P(C|A)+P(/A)P(C|/A))
////   checking this F*P(B)*P(C) should equal P(C|A)P(B|A)P(A)
////    P(B)=P(A)*P(B|A)+P(/A)P(B|/A)
////
////   if P(ABC) > P(B)P(C) then F will be greater than 1

