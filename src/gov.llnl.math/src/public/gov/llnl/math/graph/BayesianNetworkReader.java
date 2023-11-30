/*
 * Copyright 2016, Lawrence Livermore National Security, LLC.
 * All rights reserved
 *
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.math.graph;

import gov.llnl.math.MathPackage;
import gov.llnl.utility.io.ReaderException;
import gov.llnl.utility.xml.bind.ObjectReader;
import gov.llnl.utility.xml.bind.Reader;
import gov.llnl.utility.xml.bind.ReaderContext;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.xml.sax.Attributes;

@Reader.Declaration(pkg = MathPackage.class, name = "bayesianNetwork",
        cls = BayesianNetwork.class,
        document = true, referenceable = true)
public class BayesianNetworkReader extends ObjectReader<BayesianNetwork>
{
  final String identifier;
//
//  // Settings
//  private QueryLookupInterface queryIdMap;

  public BayesianNetworkReader()
  {
    identifier = "_bayesianNetworkReader";;
  }

  public BayesianNetworkReader(String identifier)
  {
    this.identifier = identifier;
  }

//  public void setDefaultQueryLookup()
//  {
//    this.setQueryLookup(new QueryLookupInterface.DefaultQueryLookup());
//  }
//
//  // The QueryLookup must in scope throughout the apply or load calls
//  // does not assume ownership
//  public void setQueryLookup(QueryLookupInterface qli)
//  {
//    if (qli == null)
//      throw new RuntimeException("null query lookup function");
//    this.queryIdMap = qli;
//  }
  @Override
  public BayesianNetwork start(ReaderContext context, Attributes attributes) throws ReaderException
  {
    GraphContext gc = new GraphContext();
    context.setState(gc);
    QueryLookupInterface qli = context.get(identifier, QueryLookupInterface.class);
    gc.queryIdMap = qli;

    // Sanity check
    if (gc.queryIdMap == null)
      throw new ReaderException("QueryIdMap not set");
    return new BayesianNetwork();
  }

  @Override
  public BayesianNetwork end(ReaderContext context) throws ReaderException
  {
    BayesianNetwork pg = getObject(context);
    GraphContext gc = (GraphContext) context.getState();
    // Pass 1: Allocate all of the nodes
    gc.processPass1(pg, gc.nodeSpecifications);

    // Pass 2: Create connections
    gc.processPass2(pg, gc.nodeSpecifications);

//    pg.setVariableCount(maxVariable + 1);
    return null;
  }

  @Override
  public ElementHandlerMap getHandlers(ReaderContext context) throws ReaderException
  {
    ReaderBuilder<BayesianNetwork> builder = this.newBuilder();
    builder.element("node")
            .reader(new NodeSpecificationReader())
            .callContext(BayesianNetworkReader::add);
    return builder.getHandlers();
  }

  static void add(ReaderContext context, BayesianNetwork bn, NodeSpecification ns) throws ReaderException
  {
    GraphContext gc = (GraphContext) context.getState();
    gc.nodeSpecifications.add(ns);
  }

//<editor-fold desc="contents">
  static class NodeSpecification
  {
    String idName;
    int id;
    List<Integer> depends = new LinkedList<>();
    double[] probability = null;
    List<ProbabilityGraphNode.Condition> conditions = new ArrayList<>();

    private NodeSpecification(String idName, int id)
    {
      this.idName = idName;
      this.id = id;
    }

    public void setProbability(double[] probability)
    {
      this.probability = probability;
    }

    public void addDepends(int dep)
    {
      this.depends.add(dep);
    }

    public void addCondition(ProbabilityGraphNode.Condition condition)
    {
      this.conditions.add(condition);
    }

    private ProbabilityGraphNode.Condition[] getConditions()
    {
      if (conditions.isEmpty())
        return null;
      return conditions.toArray(new ProbabilityGraphNode.Condition[conditions.size()]);
    }
  }

  @Reader.Declaration(pkg = MathPackage.class, name = "bayesianNode",
          order = Reader.Order.SEQUENCE, referenceable = false,
          cls = NodeSpecification.class)
  @Reader.Attribute(name = "id", required = true)
  @Reader.Attribute(name = "probability")
  static public class NodeSpecificationReader extends ObjectReader<NodeSpecification>
  {

    @Override
    public NodeSpecification start(ReaderContext context, Attributes attributes) throws ReaderException
    {
      String idName = attributes.getValue("id");
      GraphContext gc = (GraphContext) context.getContext(BayesianNetworkReader.class).getState();
      context.setState(gc);
      NodeSpecification ns = new NodeSpecification(idName, gc.getQueryId(idName));
      String probability = attributes.getValue("probability");
      if (probability != null)
      {
        double pv = Double.parseDouble(probability);
        ns.setProbability(new double[]
        {
          1 - pv, pv
        });
      }
      return ns;
    }

    @Override
    public ElementHandlerMap getHandlers(ReaderContext context) throws ReaderException
    {
      ReaderBuilder<NodeSpecification> builder = this.newBuilder();
      builder.element("depends").contents(String.class)
              .callContext(NodeSpecificationReader::addDependency).optional().unbounded();
      builder.element("probability")
              .contents(double[].class)
              .call(NodeSpecification::setProbability);
      return builder.getHandlers();
    }

    static void addDependency(ReaderContext context, NodeSpecification ns, String name) throws ReaderException
    {
      GraphContext gc = (GraphContext) context.getState();
      ns.addDepends(gc.getQueryId(name));
    }
  }

//</editor-fold>
  static class GraphContext
  {
    int maxVariable = -1;
    LinkedList<NodeSpecification> nodeSpecifications = new LinkedList<>();
    Map<Integer, BayesianNetworkNode> nodeMap = new HashMap<>();
    QueryLookupInterface<Integer> queryIdMap;

    protected int getQueryId(String name) throws ReaderException
    {
      if (queryIdMap == null)
        throw new ReaderException("Query id map not set");
      if (name == null)
        throw new ReaderException("Node must have id attribute.");
      int id = queryIdMap.lookup(name);
      if (id == GraphQuery.QUERY_ID_NONE)
        throw new ReaderException("Unable to find mapping for " + name);
      if (id > this.maxVariable)
        maxVariable = id;
      return id;
    }

    private void processPass1(BayesianNetwork pg, LinkedList<NodeSpecification> nsl)
    {
      // Pass 1: Allocate all of the nodes
      for (NodeSpecification ns : nsl)
      {
        int id = ns.id;
        BayesianNetworkNode node = pg.allocateNode();
        node.setQueryId(id);
        registerNode(id, node);
      }
    }

    private void processPass2(BayesianNetwork pg, LinkedList<NodeSpecification> nsl)
    {
      for (NodeSpecification ns : nsl)
      {
        BayesianNetworkNode node = lookupNode(ns.id);
        if (node == null)
          throw new RuntimeException("BayesianNetworkReader::processNode: internal consistency check failed");

        if (ns.depends.isEmpty())
        {
          node.setProbabilityTable(ns.probability);
        }
        else
        {
          // Pass 1 set up all the dependencies
          for (Integer parent : ns.depends)
          {
            BayesianNetworkNode parentNode = lookupNode(parent);
            if (parentNode == null)
              throw new RuntimeException("Bad name");

            pg.link(parentNode, node);
          }

          node.setProbabilityTable(ns.probability);
        }
      }
    }

    private BayesianNetworkNode lookupNode(int name)
    {
      return nodeMap.get(name);
    }

    private void registerNode(int name, BayesianNetworkNode node)
    {
      nodeMap.put(name, node);
    }
  }

}
