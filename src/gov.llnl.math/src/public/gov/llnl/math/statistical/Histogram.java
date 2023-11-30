/* 
 * Copyright 2016, Lawrence Livermore National Security, LLC.
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.math.statistical;

import gov.llnl.math.DoubleArray;
import gov.llnl.math.IntegerArray;
import gov.llnl.math.IntegerConditional;
import java.util.Arrays;

/**
 *
 * @author mattoon1
 */
public class Histogram
{

  private int[] counts;
  private int underrange;
  private int overrange;

  private Edges binEdges;
  private double[] binCenters;

  /**
   * Create new Histogram with nbins (equally spaced, linear scale). Upper bin
   * boundary = max of data (but that count is included in final bin)
   *
   * @param data
   * @param nbins
   */
  public Histogram(double[] data, int nbins)
  {
    double[] binEdges = new double[nbins + 1];
    counts = new int[nbins];

    double min = data[0];
    double max = min;
    for (double current : data)
    {
      if (current < min)
      {
        min = current;
      }
      if (current > max)
      {
        max = current;
      }
    }

    double binwidth = (max - min) / nbins;

    for (int i = 0; i < nbins + 1; i++)
      binEdges[i] = min + binwidth * i;

    this.binEdges = new DiscreteEdges(binEdges);

    for (double current : data)
    {
      int bin = (int) ((current - min) / binwidth);
      bin = Math.min(bin, nbins - 1);
      counts[bin]++;
    }
  }

  // KEN I added this to better match functionality of MATLAB. 
  //  However both need to be present because the high cost of the generalized
  // function.
  public Histogram(double[] data, double[] edges)
  {
    counts = new int[edges.length - 1];
    this.binEdges = new DiscreteEdges(edges);

    this.incorperate(data);
  }

  public Histogram(double[] data, Edges edges)
  {
    counts = new int[edges.size() - 1];
    this.binEdges = edges;

    this.incorperate(data);
  }

  final public void incorperate(double... values)
  {
    Edges edges = this.binEdges;

// Copy and sort array
    double[] sorted = DoubleArray.copyOf(values);
    Arrays.sort(sorted);

    // Histogram the sorted array
    int end = values.length;
    int i = 0;
    int j = 0;
    while (j < end && sorted[j] < edges.get(0))
    {
      ++j;
    }
    underrange = j - i;
    for (int k = 1; k < edges.size(); ++k)
    {
      i = j;
      while (j < end && sorted[j] < edges.get(k))
      {
        ++j;
      }
      counts[k - 1] = j - i;
    }
    overrange = end - j;
  }

//  public int findIndexOfMaximum()
//  {
//    return IntegerArray.findIndexOfMaximum(counts);
//  }

  public Extreme getMaximum()
  {
    int max = -1;
    boolean unique = true;
    int bin = -1;

    for (int i = 0; i < this.counts.length; ++i)
    {
      if (counts[i] > max)
      {
        max = counts[i];
        unique = true;
        bin = i;
      }
      else if (counts[i] == max)
      {
        unique = false;
      }
    }
    return new ExtremeImpl(bin, max, unique);
  }

//  public int findIndexOfMinimum()
//  {
//    return IntegerArray.findIndexOfMinimum(counts);
//  }

//  public int sumRange(int start, int end)
//  {
//    return IntegerArray.sumRange(counts, start, end);
//  }

  /**
   * Find the bin getIndex where histogram first crosses threshold, starting at
   * 'startbin' and moving in given direction. If the number of getCounts in
   * startbin is greater than the threshold, this returns the first bin (in
   * given direction) where getCounts &lt; threshold. Otherwise, returns first
   * bin where getCounts &gt; threshold.
   *
   * @param threshold threshold to search for
   * @param startbin getIndex of starting bin
   * @param positive true if searching in positive direction, false otherwise
   * @return -1 if no crossing found
   */
  public int findNextThresholdCrossing(double threshold, int startbin, boolean positive)
  {
    // KEN not sure what should be the functionality once we go to non-uniform bins.
    boolean falling = (counts[startbin] > threshold);

    /* KEN I reworked the section to be more consistent with the design philosophy.
      I would consider moving the helpers to IntegerArray
     */
    int thresh = (int) Math.floor(threshold);
    if (positive)
    {
      if (falling)
        return IntegerArray.findIndexOfFirstRange(counts, startbin, counts.length,
                new IntegerConditional.LessThan(thresh));
      else
        return IntegerArray.findIndexOfFirstRange(counts, startbin, counts.length,
                new IntegerConditional.GreaterThan(thresh));
    }
    else
    {
      if (falling)
        return IntegerArray.findIndexOfLastRange(counts, 0, startbin + 1,
                new IntegerConditional.LessThan(thresh));
      else
        return IntegerArray.findIndexOfLastRange(counts, 0, startbin + 1,
                new IntegerConditional.GreaterThan(thresh));
    }
  }

  public Edges getBinEdges()
  {
    return binEdges;
  }

  public double[] getBinCenters()
  {
    // KEN lazy evaluation saves memory and means you only pay the cost
    // when you actually need it.
    if (binCenters == null)
    {
      binCenters = new double[binEdges.size() - 1];
      double last = binEdges.get(0);
      for (int i = 0; i < binCenters.length; i++)
      {
        double next = binEdges.get(i + 1);
        binCenters[i] = (last + next) / 2;
        last = next;
      }
    }
    return binCenters;
  }

  public int[] getCounts()
  {
    return counts;
  }

  // KEN added these for the general case
  public int getUnderRangeCounts()
  {
    return underrange;
  }

  public int getOverRangeCounts()
  {
    return overrange;
  }
  
  public interface Extreme
  {
    boolean isUnique();

    int getIndex();

    int getCounts();
  }
  

  public interface Edges
  {
    double get(int i);

    int size();
    
    default Range getRange(int i)
    {
      return new RangeImpl(get(i), get(i+1));
    }

    default double[] toArray()
    {
      double[] out = new double[size()];
      for (int i = 0; i < out.length; ++i)
        out[i] = get(i);
      return out;
    }

  }

  public static class DiscreteEdges implements Edges
  {
    double[] edges;

    private DiscreteEdges(double[] edges)
    {
      this.edges = edges;
    }

    @Override
    public double get(int i)
    {
      return edges[i];
    }

    @Override
    public int size()
    {
      return edges.length;
    }

    @Override
    public double[] toArray()
    {
      return edges;
    }
  }

  public static class FunctionEdges implements Edges
  {
    double start;
    double step;
    int length;

    private FunctionEdges(double min, double step, int length)
    {
      this.start = min;
      this.step = step;
      this.length = length;
    }

    @Override
    public double get(int i)
    {
      return start + i * step;
    }

    @Override
    public int size()
    {
      return length;
    }
  }

  public static Edges newEdges(double min, double max, double step)
  {
    return new FunctionEdges(min, step, (int) Math.ceil((max - min) / step));
  }
}
