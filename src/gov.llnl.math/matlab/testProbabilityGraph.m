import gov.llnl.math.graph.ProbabilityGraph
import gov.llnl.math.graph.ProbabilityGraphReader
import gov.llnl.math.graph.GraphQuery

pg=ProbabilityGraph();
pgr=ProbabilityGraphReader();

pgr.setDefaultQueryLookup();
pgr.load(pg, java.io.File('graph.xml'));

