/* 
 * Copyright 2016, Lawrence Livermore National Security, LLC.
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.math;

import gov.llnl.math.random.BinomialRandom;
import gov.llnl.math.random.RandomGenerator;
import gov.llnl.utility.UUIDUtilities;
import java.io.Serializable;
import java.util.Arrays;

/**
 * Utility function to rescale an integer histogram. This function assumes that
 * the underlying data is Poisson and thus can be split by a binomial
 * distribution.
 *
 * @author nelson85
 */
public class IntegerRebin implements Serializable
{
  private static final long serialVersionUID = UUIDUtilities.createLong("IntegerRebin");

  /**
   * The number of counts that were unassigned at the end of output.
   */
  public int overflow;

  /**
   * The number of counts that were unassigned at the beginning of output.
   */
  public int underflow;

  /**
   * The number of bins at the end of the output that were not populated with
   * data.
   */
  public int underflowBins;

  BinomialRandom brand = new BinomialRandom();

  public void setRandomGenerator(RandomGenerator rg)
  {
    this.brand.setGenerator(rg);
  }

  /**
   * Rebin a spectrum to stretch the scale while maintaining statistics.
   *
   * This is a simplified version of spectral rebinning in which the scale is
   * changed only be stretching or shrinking a scale linearly. As the simplified
   * form it does not need to deal arbitrary energy bins in either scale.
   *
   * @param x
   * @param s is the change in scale. (typically desired/actual)
   * @return
   */
  public int[] scale(int[] x, double s)
  {

    // Track the underflow and overflow bins.
    overflow = 0;
    if (s < 1.0)
      underflowBins = x.length - (int) Math.floor(s * x.length);
    else
      underflowBins = 0;

    // If the scale is 1 there is no need to rebin.
    if (s == 1.0)
      return x.clone();

    double b0;
    double b1 = 0;
    int[] out = new int[x.length];
    for (int i = 0; i < x.length; ++i)
    {
      // lower energy edge is copied from the last instance 
      b0 = b1;
      // upper energy edge is computed from the scale
      b1 = (i + 1) * s;

      // Compute the bin that the upper and lower appear in for the original scale
      int i0 = (int) Math.floor(b0);
      int i1 = (int) Math.floor(b1);

      // If we are over scale then go to overflow.
      if (i0 >= x.length)
      {
        overflow += x[i];
        continue;
      }

      // If the bin is entirely enclosed then then copy it into the new bin.
      if (i0 == i1)
        out[i0] = out[i0] + x[i];
      else
      {
        // If it is not entirely enclosed, then we must draw a random number 
        // to determine the splitting.

        // Find out how many photons we are splitting.
        int n = x[i];
        // Compute what fraction of the bin overlaps
        double split = (i0 + 1 - b0) / s;

        // Draw a binomial variable with n photons with the success probability 
        // equal to the fraction of overlap
        int x1 = (int) brand.newVariable(n, split).next();

        // Push random number of successes into the lower bin
        out[i0] += x1;

        // Reduce the remaining photons by the random number 
        n -= x1;

        // If it is over more than one bin then draw additional splits 
        for (int j = i0 + 1; j < Math.min(i1, x.length); j++)
        {
          // Draw for each whole bin that spills accross 
          x1 = (int) brand.newVariable(n, 1.0 / s).next();

          // Push that random into the bin
          out[j] += x1;

          // Reduce the number of remaining photons
          n = n - x1;
        }

        // Any remaining photons from the original bin go into the upper
        // bin
        if (i1 < x.length)
        {
          out[i1] += n;
        }
        else
        {
          // Otherwise, go into overflow.
          overflow = n;
        }
      }
    }
    return out;
  }

  /**
   * Rebin a histogram using integer splitting.
   *
   * @param input
   * @param inputEdges
   * @param outputEdges
   * @return
   */
  public int[] rebin(int[] input, double[] inputEdges, double[] outputEdges)
  {
    return rebinRange(input, 0, input.length, inputEdges, outputEdges);
  }

  public int[] rebinRange(
          int[] input, int inputStart, int inputEnd,
          double[] inputEdges, double[] outputEdges)
  {
    int[] output = new int[outputEdges.length - 1];
    return rebinRange(output,
            input, inputStart, inputEnd,
            inputEdges, outputEdges);
  }

  public int[] rebinRange(
          int[] output,
          int[] input, int inputStart, int inputEnd,
          double[] inputEdges, double[] outputEdges)
  {
    MathAssert.assertLengthEqual(input, inputEdges.length - 1);
    MathAssert.assertLengthEqual(output, outputEdges.length - 1);
    Arrays.fill(output, 0);

    // Clear the results
    underflow = IntegerArray.sumRange(input, 0, inputStart);
    overflow = IntegerArray.sumRange(input, inputEnd, input.length);

    // Capture start and end of the output
    double outputBinsBegin = outputEdges[0];
    double outputBinsEnd = outputEdges[outputEdges.length - 1];

    int i1 = 0;

    double inputBin0;
    double inputBin1 = inputEdges[inputStart];

    for (int i0 = inputStart; i0 < inputEnd; ++i0)
    {
      // Get the bin edges for the counts to split
      inputBin0 = inputBin1;
      inputBin1 = inputEdges[i0 + 1];

      // Determine how many counts to split
      int n = input[i0];
      if (n <= 0)
        continue;

      // watch for full under and overflow
      if (inputBin0 > outputBinsEnd)
      {
        overflow += n;
        continue;
      }
      if (inputBin1 < outputBinsBegin)
      {
        underflow += n;
        continue;
      }

      // find the largest edge in output bins less than the inputBin0
      for (; i1 < outputEdges.length; ++i1)
        if (outputEdges[i1] > inputBin0)
          break;
      i1--;

      // Special case - partial to underflow
      if (i1 < 0)
      {
        i1 = 0;
        double f = (outputEdges[i1] - inputBin0) / (inputBin1 - inputBin0);
        int c = (int) (brand.newVariable(n, f).next());
        n -= c;
        underflow += c;
        inputBin0 = outputEdges[i1];
      }

      int i2 = i1 + 1;
      while (i2 < outputEdges.length)
      {
        // Find the next edge
        double b4 = outputEdges[i2];
        if (b4 > inputBin1)
          b4 = inputBin1;

        // Split the bin
        double f = (b4 - inputBin0) / (inputBin1 - inputBin0);
        int c = 0;
        if (f == 1)
        {
          c = n;
        }
        else
        {
          c = (int) (brand.newVariable(n, f).next());
        }
        n -= c;
        output[i2 - 1] += c;

        if (n == 0)
          break;

        // update the end point
        inputBin0 = b4;
        if (inputBin0 >= inputBin1)
          break;

        i2 = i2 + 1;
      }

      // Partial overflow
      overflow += n;
    }
    return output;
  }

}
