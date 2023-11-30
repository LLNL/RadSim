/*
 * Copyright 2016, Lawrence Livermore National Security, LLC.
 * All rights reserved
 *
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.math.spline;

import gov.llnl.math.MathPackage;
import gov.llnl.math.spline.CubicHermiteSpline.ControlPoint;
import gov.llnl.utility.io.ReaderException;
import gov.llnl.utility.xml.bind.ObjectReader;
import gov.llnl.utility.xml.bind.Reader;
import gov.llnl.utility.xml.bind.ReaderContext;
import java.util.ArrayList;
import org.xml.sax.Attributes;

/**
 *
 * @author nelson85
 */
@Reader.Declaration(pkg = MathPackage.class, name = "cubicHermiteSpline",
        cls = CubicHermiteSpline.class,
        order = Reader.Order.SEQUENCE,
        referenceable = true)
@Reader.Attribute(name = "type", type = String.class)
@Reader.Attribute(name = "endBehavior", type = String.class)
public class CubicHermiteSplineReader extends ObjectReader<CubicHermiteSpline>
{
  static class State
  {
    ArrayList<CubicHermiteSpline.ControlPoint> cp = new ArrayList<>();
    double[] x;
    double[] y;
    double[] m;
    private String type;
    private EndBehavior end = EndBehavior.LINEAR;
  }

  static State getState(ReaderContext context)
  {
    return (State) context.getState();
  }

  @Override
  public CubicHermiteSpline start(ReaderContext context, Attributes attributes) throws ReaderException
  {
    State state = new State();
    context.setState(state);
    state.type = attributes.getValue("type");
    String end = attributes.getValue("endBehavior");
    if (end != null)
      state.end = EndBehavior.valueOf(end.toUpperCase().trim());
    return null;
  }

  @Override
  public CubicHermiteSpline end(ReaderContext context) throws ReaderException
  {
    State state=getState(context);
    CubicHermiteSpline out = null;
    ArrayList<ControlPoint> cp = state.cp;
    double[] x = state.x;
    double[] y = state.y;
    double[] m = state.y;
    String type = state.type;
    EndBehavior end = state.end;
    if (cp != null)
    {
      //     ControlPoint[] control = cp.toArray(new ControlPoint[0]);
      SplineUtilities.sort(cp);
      x = SplineUtilities.extractControlX(cp);
      y = SplineUtilities.extractControlY(cp);
      if (type != null)
      {
        m = new double[cp.size()];
        for (int i = 0; i < cp.size(); ++i)
        {
          m[i] = cp.get(i).m;
        }
      }
    }

    if (x != null && y != null)
    {
      if (x.length != y.length)
        throw new ReaderException("Mismatched control vector lengths");
    }

    if (type == null && x != null && y != null && m != null)
    {
      out = CubicHermiteSplineFactory.create(x, y, m);
    }
    if ("natural".equals(type) && x != null && y != null)
    {
      out = CubicHermiteSplineFactory.createNatural(x, y);
    }
    if ("monotonic".equals(type) && x != null && y != null)
    {
      out = CubicHermiteSplineFactory.createMonotonic(x, y);
    }
    if (out == null)
      throw new ReaderException("Unable to load spline");
    out.setEndBehavior(end);
    return out;
  }

  @Override
  public ElementHandlerMap getHandlers(ReaderContext context) throws ReaderException
  {
    ReaderBuilder<CubicHermiteSpline> rb = newBuilder();
    rb.element("x")
            .callContext((c,o,v)->getState(c).x = v, double[].class)
            .optional();
    rb.element("y")
            .callContext((c,o,v)->getState(c).y = v, double[].class)
            .optional();
    rb.element("m")
            .callContext((c,o,v)->getState(c).m = v, double[].class)
            .optional();
    rb.element("point")
            .callContext((c,o,v)->getState(c).cp.add(v), CubicHermiteSpline.ControlPoint.class)
            .unbounded();
    return rb.getHandlers();
  }

}
