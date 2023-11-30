/* 
 * Copyright 2016, Lawrence Livermore National Security, LLC.
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.math;

/**
 * Fourier transform functions.
 *
 * This is the front end for support of Fourier transform on one dimensional
 * arrays. As the Fourier transform works on complex vectors all front end
 * functions use the ComplexVector as inputs.
 *
 * @author nelson85
 */
public class Fourier
{

  /**
   * Compute the discrete Fourier transform.
   *
   * This uses either the fast Fourier transform or the chirped Z-transform
   * depending the the length of the input.
   *
   * @param x is the complex vector input.
   * @return is the fft for the input.
   */
  static public ComplexVector fft(ComplexVector x)
  {
    return fft(x, x.size());
  }

  /**
   * Compute the discrete Fourier transform.
   *
   * This uses either the fast Fourier transform or the chirped Z-transform
   * depending the the length of the input.
   *
   * If m is less than the length of x, then x is truncated. If m is greater
   * than x is zero padded.
   *
   * @param x is the complex vector input.
   * @param m is the requested length.
   * @return is the fft for the input.
   */
  static public ComplexVector fft(ComplexVector x, int m)
  {
    int n = x.size();
    if (m < n)
    {
      double[] rx = DoubleArray.copyOfRange(x.getReal(), 0, m);
      double[] ix = DoubleArray.copyOfRange(x.getImag(), 0, m);
      x = new ComplexVectorImpl(rx, ix);
    }
    else if (m > n)
    {
      double[] rx = new double[m];
      double[] ix = new double[m];
      DoubleArray.assign(rx, 0, x.getReal(), 0, n);
      DoubleArray.assign(ix, 0, x.getImag(), 0, n);
      x = new ComplexVectorImpl(rx, ix);
    }

    // If we are radix 2 then we can compute it directly.
    if ((1 << FourierUtilities.binlog(m)) == m)
      return FourierUtilities.fftRadix2(x);

    // Otherwise, use the chirped method.
    return FourierUtilities.czt(x, m);
  }

  /**
   * Compute the invert Fourier transform.
   *
   * This implementation uses the flip method to compute the inverse.
   *
   * @param x
   * @return
   */
  static public ComplexVector ifft(ComplexVector x)
  {
    return ifft(x, x.size());
  }

  /**
   * Compute the invert Fourier transform.
   *
   * This implementation uses the flip method to compute the inverse. If m is
   * less than the length of x, then x is truncated. If m is greater than x is
   * zero padded.
   *
   * @param x
   * @param m
   * @return
   */
  static public ComplexVector ifft(ComplexVector x, int m)
  {
    ComplexVector out = fft(new ComplexVectorImpl(x.getImag(), x.getReal()), m);
    double[] v1 = out.getReal();
    double[] v2 = out.getImag();
    DoubleArray.divideAssign(v1, m);
    DoubleArray.divideAssign(v2, m);
    return new ComplexVectorImpl(v2, v1);
  }

}
