/* 
 * Copyright 2016, Lawrence Livermore National Security, LLC.
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.math.graph;

//#ifndef RNAK_GRAPHS_GRAPH_QUERY_H__
import gov.llnl.utility.UUIDUtilities;
import java.io.PrintStream;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * A GraphQuery is represents the state to apply to a probability or Bayesian
 * Network graph to compute the probability.
 *
 * @author nelson85
 */
public class GraphQuery implements Serializable
{
  private static final long serialVersionUID = UUIDUtilities.createLong("GraphQuery");
  static final int QUERY_ID_NONE = -1;

  public enum State
  {
    STATE_FALSE(0),
    STATE_TRUE(1),
    STATE_UNKNOWN(2),
    STATE_ASSUMED_FALSE(4),
    STATE_ASSUMED_TRUE(5),
    STATE_PARTIAL(8),
    STATE_INVALID(9);

    byte value;

    State(int value)
    {
      this.value = (byte) value;
    }

    private static State valueOf(byte num)
    {
      if (num == State.STATE_FALSE.value)
        return State.STATE_FALSE;
      if (num == State.STATE_TRUE.value)
        return State.STATE_TRUE;
      if (num == State.STATE_UNKNOWN.value)
        return State.STATE_UNKNOWN;
      if (num == State.STATE_PARTIAL.value)
        return State.STATE_PARTIAL;
      return State.STATE_INVALID;
    }
  }
//  static final byte STATE_FALSE = 0;
//  static final byte STATE_TRUE = 1;
//  static final byte STATE_UNKNOWN = 2;
//  static final byte STATE_ASSUMED_FALSE = 4;
//  static final byte STATE_ASSUMED_TRUE = 5;
//  static final byte STATE_PARTIAL = 8;

  byte[] state;
  double[] conditions;

  //  public:
//    typedef unsigned int size_type;
  public GraphQuery()
  {
  }

  public GraphQuery(int querySize)
  {
    state = new byte[querySize];
  }

  public GraphQuery(int querySize, int conditionSize)
  {
    state = new byte[querySize];
    conditions = new double[conditionSize];
    for (int i = 0; i < conditionSize; i++)
      conditions[i] = 0.5;
  }

  public void dispose()
  {
    state = null;
  }

  public void resize(int sz)
  {
    state = new byte[sz];
  }

  public int size()
  {
    return state.length;
  }

  public void setAllFalse()
  {
    setAll(State.STATE_FALSE);
  }

  public void setAllTrue()
  {
    setAll(State.STATE_TRUE);
  }

  public void setAllUnknown()
  {
    setAll(State.STATE_UNKNOWN);
  }

  public boolean isTrue(int queryId)
  {
    return state[queryId] == State.STATE_TRUE.value;
  }

  public boolean isFalse(int queryId)
  {
    return state[queryId] == State.STATE_FALSE.value;
  }

  public boolean isUnknown(int queryId)
  {
    return state[queryId] == State.STATE_UNKNOWN.value;
  }

  public void next()
  {
    for (int i = 0; i < size(); i++)
    {
      if (isFalse(i))
      {
        setTrue(i);
        break;
      }
      else
      {
        setFalse(i);
      }
    }
  }

  public void set(int queryId, State state)
  {
    if (queryId == -1)
      return;
    if (queryId < size())
      this.state[queryId] = state.value;
    else
      throw new RuntimeException("QueryId out of range " + queryId + " " + size());
  }

  public void setTrue(int queryId)
  {
    set(queryId, State.STATE_TRUE);
  }

  public void setFalse(int queryId)
  {
    set(queryId, State.STATE_FALSE);
  }

  public void setUnknown(int queryId)
  {
    set(queryId, State.STATE_UNKNOWN);
  }

  public void dump(PrintStream os)
  {
    for (int i = 0; i < size(); i++)
    {
      if (isTrue(i))
        os.print("1");
      else if (isFalse(i))
        os.print("0");
      else if (isUnknown(i))
        os.print("X");
      else
        os.print("x" + state[i]);
    }
  }

  /**
   * Get the state of a query variable. This is primarily for debugging purposes
   * and will not be used in general code.
   *
   * @param qeueryId
   * @return
   */
  public State get(int qeueryId)
  {
    return State.valueOf(state[qeueryId]);
  }

  public void setAll(State state)
  {
    for (int i = 0; i < this.state.length; i++)
    {
      this.state[i] = state.value;
    }
  }

  public boolean hasPartials()
  {
    return partials != null;
  }

  public void clear()
  {
    this.setAllFalse();
    this.partials = null;
  }

  void assignState(GraphQuery query)
  {
    this.state = query.state.clone();
  }

//<editor-fold desc="partial">
  static public class PartialDatum
  {
    public int id;
    public double value;

    public PartialDatum(int queryId, double value)
    {
      this.id = queryId;
      this.value = value;
    }
  }
  ArrayList<PartialDatum> partials = null;

  public boolean isPartial(int queryId)
  {
    return state[queryId] == State.STATE_PARTIAL.value;
  }

  public double getPartial(int queryId)
  {
    for (PartialDatum p : partials)
    {
      if (p.id == queryId)
      {
        return p.value;
      }
    }
    return 0;
  }

  public void setPartial(int queryId, double value)
  {
    if (queryId == QUERY_ID_NONE)
      return;

    this.set(queryId, State.STATE_PARTIAL);
    // If no partials have been used we allocate a new one
    if (partials == null)
    {
      partials = new ArrayList<>();
    }
    else
    {
      // otherwise search for an old partial to update
      for (PartialDatum p : partials)
      {
        if (p.id == queryId)
        {
          p.value = value;
          return;
        }
      }
    }
    // if not found, add a ne partial
    partials.add(new PartialDatum(queryId, value));
  }
//</editor-fold>

  @Override
  public GraphQuery clone() throws CloneNotSupportedException
  {
    return (GraphQuery) super.clone();
  }

  boolean hasConditions()
  {
    return conditions != null;
  }

  public void setCondition(int conditionId, double d)
  {
    this.conditions[conditionId] = d;
  }

  public void clearConditions()
  {
    if (conditions == null)
      return;
    for (int i = 0; i < conditions.length; ++i)
      conditions[i] = 0.5;
  }

}
