/*
 * Copyright 2016, Lawrence Livermore National Security, LLC.
 * All rights reserved
 *
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.math.graph;

import gov.llnl.math.MathPackage;
import gov.llnl.math.graph.ProbabilityGraphNode.Condition;
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

/**
 *
 * @author nelson85
 */
@Reader.Declaration(pkg = MathPackage.class, name = "probabilityGraph",
        cls = ProbabilityGraph.class,
        document = true, referenceable = true)
@Reader.Attribute(name = "probability", type = Double.class)
public class ProbabilityGraphReader extends ObjectReader<ProbabilityGraph>
{
  public final static String DEFAULT_IDENTIFIER ="_probabilityGraphReader";
  final String identifier;

  public ProbabilityGraphReader()
  {
    this.identifier = DEFAULT_IDENTIFIER;
  }

  public ProbabilityGraphReader(String identifier)
  {
    this.identifier = identifier;
  }

  @Override
  public ProbabilityGraph start(ReaderContext context, Attributes attributes) throws ReaderException
  {
    GraphContext gc = new GraphContext();
    context.setState(context);

    Double defaultProbability = context.get(identifier + ".defaultProbability", Double.class);
    QueryLookupInterface<Integer> conditionIdMap = context.get(identifier + ".conditionIdMap", QueryLookupInterface.class);
    QueryLookupInterface<Integer> queryIdMap = context.get(identifier + ".queryIdMap", QueryLookupInterface.class);

    // Copy in settings
    if (defaultProbability
            != null)
      gc.defaultProbability = defaultProbability;
    if (conditionIdMap == null)
      throw new RuntimeException("Condition map not set");
    if (queryIdMap == null)
      throw new RuntimeException("Query map not set");

    gc.conditionIdMap = conditionIdMap;
    gc.queryIdMap = queryIdMap;

    // Override the default probability if set 
    String prob = attributes.getValue("probability");
    if (prob != null)
      gc.defaultProbability = Double.parseDouble(prob);

    return new ProbabilityGraph();
  }

  @Override
  public ProbabilityGraph end(ReaderContext context) throws ReaderException
  {
    ProbabilityGraph pg = getObject(context);
    GraphContext gc = (GraphContext) context.getState();

    // Pass 1: Allocate all of the nodes
    gc.processPass1(pg, gc.nodeSpecifications);

    // Pass 2: Create connections
    gc.processPass2(pg, gc.nodeSpecifications);

    pg.setConditionCount(gc.maxCondition + 1);
    pg.setVariableCount(gc.maxVariable + 1);

    // Pass 3: Normalize the graph
    ProbabilityGraphNormalizer norm = new ProbabilityGraphNormalizer();
    norm.process(pg);
    return null;
  }

  @Override
  public ElementHandlerMap getHandlers(ReaderContext context) throws ReaderException
  {
    ReaderBuilder<ProbabilityGraph> builder = this.newBuilder();
    builder.element("node")
            .reader(new NodeSpecificationReader())
            .callContext(ProbabilityGraphReader::add);
    builder.element("define_condition").contents(String.class)
            .callContext(ProbabilityGraphReader::defineCondition).noid();
    builder.section(new NodeGroupSection());
    return builder.getHandlers();
  }

  static void add(ReaderContext context, ProbabilityGraph obj, NodeSpecification ns) throws ReaderException
  {
    GraphContext gc = (GraphContext) context.getState();
    gc.nodeSpecifications.add(ns);
  }

  static void defineCondition(ReaderContext context, ProbabilityGraph obj, String condition) throws ReaderException
  {
    GraphContext gc = (GraphContext) context.getState();
    if (gc.conditionIdMap == null)
      throw new RuntimeException("Condition map is not set.");
    int id = gc.getConditionId(condition);
    if (id > gc.maxCondition)
      gc.maxCondition = id;
  }

//<editor-fold desc="conditions">
//</editor-fold>
//<editor-fold desc="contents">
  public static class NodeSpecification
  {
    String idName;
    int id;
    List<Integer> depends = new LinkedList<>();
    double[] probability = null;
    List<Condition> conditions = new ArrayList<>();

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

    public void addCondition(Condition condition)
    {
      this.conditions.add(condition);
    }

    private Condition[] getConditions()
    {
      if (conditions.isEmpty())
        return null;
      return conditions.toArray(new Condition[conditions.size()]);
    }
  }

  @Reader.Declaration(pkg = MathPackage.class, name = "probabiblityNode",
          order = Reader.Order.SEQUENCE, referenceable = true, cls = NodeSpecification.class)
  @Reader.Attribute(name = "name", type = String.class)
  @Reader.Attribute(name = "probability", type = Double.class)
  static public class NodeSpecificationReader extends ObjectReader<NodeSpecification>
  {

    @Override
    public NodeSpecification start(ReaderContext context, Attributes attributes) throws ReaderException
    {
      GraphContext gc = (GraphContext) context.getContext(ProbabilityGraph.class).getState();
      context.setState(gc);
      String idName = attributes.getValue("name");
      if (idName == null)
        throw new ReaderException("fail to get id");
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
    public NodeSpecification end(ReaderContext context) throws ReaderException
    {
      GraphContext gc = (GraphContext) context.getState();
      NodeSpecification ns = getObject(context);
      if (ns.probability == null)
        ns.setProbability(new double[]
        {
          1 - gc.defaultProbability, gc.defaultProbability
        });
      return null;
    }

    @Override
    public ElementHandlerMap getHandlers(ReaderContext context) throws ReaderException
    {
      ReaderBuilder<NodeSpecification> builder = this.newBuilder();
      builder.reader(new ConditionReader())
              .call(NodeSpecification::addCondition).optional();
      builder.element("depends").contents(String.class)
              .callContext(NodeSpecificationReader::addDependency).optional().unbounded();
      builder.element("probability")
              .call(NodeSpecification::setProbability, double[].class);
      return builder.getHandlers();
    }

    static void addDependency(ReaderContext context, NodeSpecification ns, String name)
    {
      try
      {
        GraphContext gc = (GraphContext) context.getState();
        ns.addDepends(gc.getQueryId(name));
      }
      catch (ReaderException ex)
      {
        throw new RuntimeException(ex);
      }
    }
  }

  @Reader.Attribute(name = "probability", type = Double.class, required = true)
  class NodeGroupSection extends Section
  {
    double last;

    public NodeGroupSection()
    {
      super(Order.FREE, "subgroup");
    }

    @Override
    public ProbabilityGraph start(ReaderContext context, Attributes attributes) throws ReaderException
    {
      GraphContext gc = (GraphContext) context.getState();
      String value = attributes.getValue("probability");
      gc.pushProbability(Double.parseDouble(value));
      return null;
    }

    @Override
    public ProbabilityGraph end(ReaderContext context) throws ReaderException
    {
      GraphContext gc = (GraphContext) context.getState();
      gc.popProbability();
      return null;
    }

    @Override
    public ElementHandlerMap getHandlers(ReaderContext context) throws ReaderException
    {
      ReaderBuilder<ProbabilityGraph> builder = this.newBuilder();
      builder.element("node").reader(new NodeSpecificationReader())
              .callContext(ProbabilityGraphReader::add);
      return builder.getHandlers();
    }
  }

  @Reader.Declaration(pkg = MathPackage.class, name = "condition",
          order = Reader.Order.ALL,
          referenceable = true, cls = Condition.class)
  @Reader.Attribute(name = "on", type = String.class)
  @Reader.Attribute(name = "ratio", type = Double.class, required = true)
  static public class ConditionReader extends ObjectReader<Condition>
  {

    @Override
    public Condition start(ReaderContext context, Attributes attributes) throws ReaderException
    {
      GraphContext gc = (GraphContext) context.getContext(ProbabilityGraph.class).getState();
      String on = attributes.getValue("on");
      String ratio = attributes.getValue("ratio");
      return new Condition(gc.getConditionId(on), Double.parseDouble(ratio.trim()));
    }

    @Override
    public ElementHandlerMap getHandlers(ReaderContext context) throws ReaderException
    {
      return null;
    }
  }
//</editor-fold>
//<editor-fold desc="internal">

  static class GraphContext
  {
    double defaultProbability = 0.5;
    final LinkedList<NodeSpecification> nodeSpecifications = new LinkedList<>();
    final Map<Integer, ProbabilityGraphNode> nodeMap = new HashMap<>();
    int maxVariable = -1;
    int maxCondition = -1;
    QueryLookupInterface<Integer> queryIdMap;
    QueryLookupInterface<Integer> conditionIdMap;

    ProbabilityGraphNode lookupNode(int name)
    {
      return nodeMap.get(name);
    }

    void registerNode(String name, int id, ProbabilityGraphNode node)
    {
      if (nodeMap.get(id) != null)
        throw new RuntimeException("Duplicate node " + name);
      nodeMap.put(id, node);
    }

    void processPass1(ProbabilityGraph pg, List<NodeSpecification> root)
    {
      for (NodeSpecification ns : root)
      {
        String idName = ns.idName;
        int id = ns.id;
        ProbabilityGraphNode node = pg.allocateNode();
        node.setQueryId(id);
        registerNode(idName, id, node);
        node.setConditions(ns.getConditions());
        node.setName(ns.idName);
      }
    }

    void processPass2(ProbabilityGraph pg, List<NodeSpecification> root) throws ReaderException
    {
      for (NodeSpecification ns : root)
      {
        ProbabilityGraphNode node = lookupNode(ns.id);
        if (node == null)
          throw new ReaderException("internal consistency check failed");
        node.setAsVariableNode();

        // Add the dependencies
        if (!ns.depends.isEmpty())
        {
          ProbabilityGraphNode factorNode = pg.allocateNode();
          // First link is to the dependent node
          pg.link(node, factorNode);
          for (int parent : ns.depends)
            pg.link(this.lookupNode(parent), factorNode);
          factorNode.setAsFactorNode();
          node = factorNode;
        }

        // Set up factor table
        if (ns.probability.length != node.getFactorTableSize())
          throw new ReaderException("Probability table size mismatch on "
                  + ns.idName + ", got "
                  + ns.probability.length + " expected " + node.getFactorTableSize());
        for (int i = 0; i < ns.probability.length; i += 2)
        {
          double d1 = ns.probability[i];
          double d2 = ns.probability[i + 1];
          if (Math.abs(d1 + d2 - 1) > 1e-12 || d1 < 0 || d2 < 0)
            throw new ReaderException("Bad probability table");
        }
        node.setFactorTable(ns.probability);
      }
    }

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

    private int getConditionId(String name) throws ReaderException
    {
      if (name == null)
        throw new ReaderException("Node must have id attribute.");
      int id = conditionIdMap.lookup(name);
      if (id == GraphQuery.QUERY_ID_NONE)
        throw new ReaderException("Unable to find mapping for " + name);
      return id;
    }

    ArrayList<Double> stack = new ArrayList<>();

    private void pushProbability(double parseDouble)
    {
      stack.add(defaultProbability);
      this.defaultProbability = parseDouble;
    }

    private void popProbability()
    {
      defaultProbability = stack.remove(stack.size() - 1);
    }

  }

//</editor-fold>
}
