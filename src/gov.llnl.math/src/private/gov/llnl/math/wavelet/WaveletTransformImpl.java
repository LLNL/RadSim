/*
 * Copyright 2016, Lawrence Livermore National Security, LLC. 
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.math.wavelet;

import gov.llnl.math.matrix.Matrix;

/**
 * Implementation for a generic 1D wavelet transform.
 * 
 * @author guensche1
 */
class WaveletTransformImpl implements WaveletTransform
{
  private final double[] h0;
  private final double[] h1;

  public WaveletTransformImpl(double[] filterCoef)
  {
    this.h0 = filterCoef;

    /*coefficients of the wavelet (high pass filter) obtained as
    (+/-)*((-1)^n)*h0(N-n) where N = length of h0
    (under orthogonality conditions for scaling and wavelet functions)*/
    this.h1 = WaveletUtilities.createQuadratureMirror(h0);
  }

  /**
   * Compute the wavelet transform.
   *
   * @param signal
   * @return wavelet coefficients 
   */
  @Override
  public Matrix forward(double[] signal, int scale)
  {
    return WaveletOps.overCompleteWaveletTransform(signal, h0, h1, scale);
  }

  /**
   * Reconstructs the signal from the wavelet coefficients.
   *
   * @param waveletCoef
   * @return
   */
  @Override
  public Matrix reconstruct(Matrix waveletCoef)
  {
    return WaveletOps.reconstructMultiResolutionWave(waveletCoef, h0, h1);
  }

  /**
   * Smooth a signal using wavelets with a specified scale.
   * 
   * The user is responsible for determined the scale of the filtering
   * based on the statistics.
   *
   * @param inData is a 1d vector holding 
   * @param waveletScale is the requested scale for filtering.
   * @return the smoothed copy of the data.
   */
  @Override
  public double[] smooth(double[] inData, int waveletScale)
  {
    return WaveletOps.smooth(inData, this.h0, this.h1, waveletScale);
  }

}
