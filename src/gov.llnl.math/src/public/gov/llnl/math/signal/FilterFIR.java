/*
 * Copyright 2016, Lawrence Livermore National Security, LLC. 
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.math.signal;

import gov.llnl.math.DoubleArray;
import gov.llnl.math.signal.FilterUtilities.IterateDoublesForward;

/**
 *
 * @author nelson85
 */
public class FilterFIR implements Filter
{
  double[] b;

  public FilterFIR(double[] a)
  {
    this.b = DoubleArray.copyOf(a);
  }

  @Override
  public double[] apply(double[] in)
  {
    return apply(new IterateDoublesForward(in));
  }

  @Override
  public double[] apply(IterateDoubles iter)
  {
    double[] out = new double[iter.size()];
    RollingBuffer in = new RollingBuffer(b.length);
    int i = 0;
    while (iter.hasNext())
    {
      in.add(iter.next());
      double sum = 0;
      for (int j = 0; j < b.length; ++j)
        sum += b[j] * in.get(j);
      out[i++] = sum;
    }
    return out;
  }

}
