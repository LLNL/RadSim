/*
 * Copyright 2019, Lawrence Livermore National Security, LLC. 
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package deadcode;

import deadcode.ShaperModel;
import gov.llnl.math.ComplexVector;
import gov.llnl.math.ComplexVectorOps;
import gov.llnl.math.DoubleArray;
import gov.llnl.math.Fourier;
import gov.llnl.math.FourierUtilities;
import gov.llnl.rtk.data.Spectrum;

/**
 * Algorithm that computes the effects of random pileup on an expected spectrum.
 *
 * @author nelson85
 */
public class PileupCalculator
{
  public Spectrum compute(Spectrum in, ShaperModel shaper)
  {
    // linearize the spectrum
    double[] f = null;

    // normalize f to sum to 1
    DoubleArray.normColumns1(f);

    // Apply the shaper model to distort the secondaries
    double[] g = shaper.computePileupDistribution(f);

    // Compute the length of the required 
    int len = (1 << FourierUtilities.binlog(f.length));
    len *= 4;

    // Compute the probability for each pileup fraction
    double[] prob = null; //shaper.trigger.getFractions(in.getRate());

    // Compute the closest radix 2 point
    ComplexVector F = Fourier.fft(ComplexVector.create(f, null), len);
    ComplexVector G = Fourier.fft(ComplexVector.create(g, null), len);
    ComplexVector FG = ComplexVectorOps.multiply(F, G);
    ComplexVector FG2 = ComplexVectorOps.multiply(FG, G);
    ComplexVector FG3 = ComplexVectorOps.multiply(FG2, G);

    // Accumulate based on the fraction 
    ComplexVectorOps.multiplyAssign(FG, prob[1]);
    ComplexVectorOps.addAssignScaled(FG, FG2, prob[2]);
    ComplexVectorOps.addAssignScaled(FG, FG3, prob[3]);
    double[] h = Fourier.ifft(FG).getReal();

    // Count the overrange
    double overrange = DoubleArray.sumRange(h, f.length, h.length);

    // Truncate
    h = DoubleArray.copyOfRange(h, 0, f.length);

    // Add the portion that is not piled up
    DoubleArray.addAssignScaled(h, f, prob[0]);

    throw new UnsupportedOperationException("This code appears to be incomplete.");
//    // convert back to energy scale
//    return null;
  }
}
