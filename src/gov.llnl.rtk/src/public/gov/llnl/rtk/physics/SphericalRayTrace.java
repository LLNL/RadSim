/*
 * Copyright 2025, Lawrence Livermore National Security, LLC. 
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.rtk.physics;

import gov.llnl.rtk.physics.SphericalRayTrace.Segment;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.ListIterator;

/**
 * Extract a ray trace for a model.
 *
 * Segments and quantities from the ray trace are reused between trace calls.
 * Don't reuse the results between traces.
 *
 * @author nelson85
 */
public class SphericalRayTrace implements Iterable<Segment>
{

  private final ArrayList<Segment> segments = new ArrayList<>();
  private final ArrayList<Segment> pool = new ArrayList<>();

  /**
   * Update a trace for a specific radius and angle.
   *
   * @param model
   * @param radius
   * @param theta
   */
  public void trace(SourceModel model, Quantity radius, double theta)
  {
    double r = radius.get();
    double outer;
    double inner;

    this.clear();

    // Fint the starting point 
    //   we need both the layer data and the iterator so we can walk through 
    //   the layers.
    Layer current = null;
    ListIterator<? extends Layer> iter = model.getLayers().listIterator();
    while (iter.hasNext())
    {
      current = iter.next();
      if (r < current.getOuter().get())
        break;
    }

    // Check to make sure it has a layer.
    if (current == null)
      return;

    // We started outside the object, no matter check to see if we hit the outer layer
    outer = current.getOuter().get();
    if (r >= outer)
    {
      double u = -r * Math.cos(theta);
      double sT = Math.sin(theta);
      double q = outer * outer - r * r * sT * sT;
      if (u > 0 && q > 0)
      {
        double d0 = u - Math.sqrt(q);
        add(null, d0, theta);
        theta = Math.asin(r / outer * Math.sin(theta));
        r = -outer;
      }
      else
        return;
    }

    // We are inside the model, work our way out
    for (int j = 0; j < model.size() * 2; ++j)
    {
      // Get the layer 
      inner = current.getInner().get();
      outer = current.getOuter().get();

      // Test the inner direction
      double u = -r * Math.cos(theta);
      double sT = Math.sin(theta);
      double q = inner * inner - r * r * sT * sT;

      if (u > 0 && q > 0) // and r!=layer.inner:
      {
        // Move inside
        double d0 = u - Math.sqrt(q);
        add(current, d0, theta);
        theta = Math.asin(r / inner * sT);
        r = -inner;
        current = iter.previous();
        continue;
      }

      // Test outer direction
      q = outer * outer - r * r * sT * sT;

      // No additional layers
      if (q <= 0)
        break;

      // Move out
      double d0 = u + Math.sqrt(q);
      add(current, d0, theta);
      theta = Math.asin(r / outer * sT);
      r = outer;
      if (!iter.hasNext())
        break;
      current = iter.next();
    }
  }

  /**
   * Get the result.
   *
   * @return
   */
  @Override
  public Iterator<Segment> iterator()
  {
    return this.segments.iterator();
  }

  /**
   * Result of the ray trace.
   */
  public static class Segment
  {
    /** 
     * The layer that was hit.
     */
    public Layer layer;
    
    /**
     * The length of the chord.
     */
    final public Quantity length = new QuantityImpl(0, PhysicalProperty.LENGTH, 0, true);
    
    /**
     * The angle relative to layer.  
     * 
     * 0 would denote straight out of the layer.
     * PI would denote straight into the layer.
     */
    public double angle;
        
    @Override
    public String toString()
    {
      return String.format("Seg(%s,%s,%.2f)", layer, length, angle);
    }
  }

//<editor-fold desc="internal" defaultstate="collapsed">
  /**
   * Add a segment to the result.
   *
   * @param layer
   * @param length
   * @param theta
   */
  private void add(Layer layer, double length, double theta)
  {
    Segment segment;
    if (!pool.isEmpty())
      segment = pool.remove(pool.size() - 1);
    else
      segment = new Segment();
    QuantityImpl q = (QuantityImpl) segment.length;
    segment.layer = layer;
    q.value = length;
    segment.angle = theta;
    this.segments.add(segment);
  }

  private void clear()
  {
    // Clear the ray trace object of the previous result.
    this.pool.addAll(this.segments);
    this.segments.clear();
  }
//</editor-fold>
}
