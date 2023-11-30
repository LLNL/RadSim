/*
 * Copyright 2019, Lawrence Livermore National Security, LLC. 
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package deadcode;

import gov.llnl.math.DoubleArray;
import gov.llnl.rtk.pileup.TriggerModel;
import java.util.List;

/**
 *
 * @author nelson85
 */
public class ShaperModel
{
  TriggerModel trigger;
  double probabilityFull;
  double probabilityNone;
  double probabilityReject;
  List<Partial> partials;

  /**
   * Internal calculator for pdf for pileup counts
   *
   * The energy scale for the input must be linear.
   *
   * @param in
   * @return
   */
  public double[] computePileupDistribution(double[] in)
  {
    if (this.partials.isEmpty())
      return computePileup0(in);
    return computePileup1(in);
  }

  private double[] computePileup0(double[] in)
  {
    int n = in.length;
    double[] out = new double[in.length];
    for (int i = n - 1; i >= 0; --i)
    {
      out[i] += this.probabilityFull * in[i];
      out[0] += this.probabilityNone * in[i];
    }
    double s = DoubleArray.sum(out);
    DoubleArray.multiplyAssign(out, (1 - this.probabilityReject) / s);
    return out;
  }

  private double[] computePileup1(double[] in)
  {
    int n = in.length;
    double[] out = new double[in.length];
    double[] slope = new double[in.length + 1];
    double[] step = new double[in.length + 1];
    double c = 0;
    double m = 0;
    for (int i = n - 1; i >= 0; --i)
    {
      for (Partial partial : this.partials)
      {
        double e0 = partial.fraction0 * i;
        double e1 = partial.fraction1 * i;
        double d0 = partial.probability0 * (e1 - e0);
        double d1 = partial.probability1 * (e1 - e0);
        double m0 = (d1 - d0) / (e1 - e0);
        if (e1 == i)
        {
          step[i] += d1;
          slope[i] += m0;
        }
        else
        {
          int j1 = (int) e1;
          double f1 = e1 - j1;
          step[j1] += (1 - f1) * d1;
          step[j1 + 1] += f1 * d1;
          slope[j1] += (1 - f1) * m0;
          slope[j1 + 1] += f1 * m0;
        }
        if (e0 > 0)
        {
          int j0 = (int) e0;
          double f0 = e0 - j0;
          step[j0] -= (1 - f0) * d1;
          step[j0 + 1] -= f0 * d1;
          slope[j0] -= (1 - f0) * m0;
          slope[j0 + 1] -= f0 * m0;
        }
      }
      c += step[i];
      m += slope[i];
      out[i] += this.probabilityFull * in[i] + c;
      out[0] += this.probabilityNone * in[i];
      c -= m;
    }
    double s = DoubleArray.sum(out);
    DoubleArray.multiplyAssign(out, (1 - this.probabilityReject) / s);
    return out;
  }

  public double normalize()
  {
    double s = this.probabilityFull + this.probabilityNone + this.probabilityReject;
    for (Partial p : this.partials)
    {
      double q = (p.probability0 + p.probability1) / 2;
      s += q * (p.fraction1 - p.fraction0);
    }
    this.probabilityFull /= s;
    this.probabilityNone /= s;
    this.probabilityReject /= s;
    for (Partial p : this.partials)
    {
      p.probability0 /= s;
      p.probability1 /= s;
    }
    return s;
  }

  private class Partial
  {
    double probability0;
    double probability1;
    double fraction0;
    double fraction1;
  }
}
