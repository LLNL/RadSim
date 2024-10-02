/* 
 * Copyright 2016, Lawrence Livermore National Security, LLC.
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.math.graph;

import gov.llnl.math.graph.primitives.GraphNode;

/**
 *
 * @author nelson85
 */
public class ProbabilityGraphNode extends GraphNode<ProbabilityGraphEdge>
        implements Comparable<ProbabilityGraphNode>
{

  public enum NodeType
  {
    UNDEFINED(0),
    INTERCONNECTION(1),
    VARIABLE(2);

    final String[] desc =
    {
      "UNDEFINED", "INTERCONNECTION", "VARIABLE"
    };
    final int type;

    NodeType(int type)
    {
      this.type = type;
    }

    public String toString()
    {
      return desc[type];
    }
  }

  NodeType type;
//  int type_;
  int queryId = GraphQuery.QUERY_ID_NONE;
  double[] factors;
  String name;

  public ProbabilityGraphNode()
  {
    type = NodeType.UNDEFINED;
    factors = null;
    queryId = GraphQuery.QUERY_ID_NONE;
  }

  @Override
  public void dispose()
  {
    factors = null;
    super.dispose();
  }

  /**
   * Get the queryId for this node. Will be QUERY_ID_NONE if this is a factor
   * node.
   *
   * @return the id associated with this node or QUERY_ID_NONE if this is a
   * factor node.
   */
  public int getQueryId()
  {
    return queryId;
  }

  public void setQueryId(int id)
  {
    queryId = id;
  }

  public NodeType getType()
  {
    return type;
  }

  public void setType(NodeType type)
  {
    this.type = type;
  }

  public boolean isVariableNode()
  {
    return type == NodeType.VARIABLE;
  }

  public boolean isFactorNode()
  {
    return type == NodeType.INTERCONNECTION;
  }

  public void setAsVariableNode()
  {
    type = NodeType.VARIABLE;
  }

  public void setAsFactorNode()
  {
    type = NodeType.INTERCONNECTION;
  }

  // Access to the probability table
  void allocateFactorTable()
  {
    factors = new double[getFactorTableSize()];
  }

  void setFactorTable(double[] factors)
  {
    if (factors.length != this.getFactorTableSize())
      throw new RuntimeException("Size matmatch");
    this.factors = factors;
  }

  public int getFactorTableSize()
  {
    return (this.isVariableNode()) ? 2 : (1 << (this.getNumEdges()));
  }

  double[] getVariableFactors(GraphQuery query)
  {
    if (conditions == null || !query.hasConditions())
      return this.factors;
    double f0 = this.factors[0];
    double f1 = this.factors[1];
    System.out.println("f0=" + f0 + " f1=" + f1);
    // handle conditions
    for (ProbabilityGraphNode.Condition cond : conditions)
    {
      double r = cond.ratio;
      double c = query.conditions[cond.conditionId];
      f1 = 2 * f1 / (1 + r) * (1 - c + c * r) / (f1 + f0);
      f0 = 1 - f1;
      System.out.println("f0=" + f0 + " f1=" + f1 + " c=" + c);
    }

    return new double[]
    {
      f0, f1
    };
  }

  public double getFactor(int i)
  {
    return factors[i];
  }

  public void setFactor(int i, double v)
  {
    factors[i] = v;
  }

  public boolean hasFactorTable()
  {
    return factors != null;
  }

  public String getName()
  {
    return name;
  }

  void setName(String name)
  {
    this.name = name;
  }

//<editor-fold desc="conditions">
  static public class Condition
  {
    int conditionId;
    double ratio;

    Condition(int id, double ratio)
    {
      this.conditionId = id;
      this.ratio = ratio;
    }
  }
  Condition[] conditions = null;

  @Override
  public int compareTo(ProbabilityGraphNode t)
  {
    return Integer.compare(this.queryId, t.queryId);
  }

  public void setConditions(Condition[] conditions)
  {
    this.conditions = conditions;
  }

  public boolean hasConditions()
  {
    return this.conditions != null;
  }

  public Condition[] getConditions()
  {
    return this.conditions;
  }

//</editor-fold>
}
