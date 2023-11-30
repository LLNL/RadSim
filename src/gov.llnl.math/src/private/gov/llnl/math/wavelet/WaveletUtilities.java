/**
 * Copyright 2018, Lawrence Livermore National Security, LLC.
 * All rights reserved
 *
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.math.wavelet;

import gov.llnl.math.Fourier;
import gov.llnl.math.ComplexVector;
import gov.llnl.math.ComplexVectorOps;

/**
 *
 * @author munteanu1, chandrasekar1
 */
class WaveletUtilities
{
  /**
   * Replicates the behavior of the 1D 'circshift' function in Matlab for a 1D
   * array A positive shift (n) moves to the right
   */
  static public double[] circShift1D(double d[], int n)
  {
    n *= -1;
    int L = d.length;

    if (L > 0 && n != 0)
    {
      double[] shifted = new double[L];
      for (int k = 0; k < L; k++)
      {
        int old = (k + n);
        while (old < 0)
        {
          old += L;
        }
        old = old % L;
        shifted[k] = d[old];
      }

      return shifted;
    }
    else
    {
      return d;
    }
  }

  /**
   * Reverses the order of elements in a double array.
   *
   * @param in
   * @return a new array with the order reversed.
   */
  static public double[] reverse(double[] in)
  {
    int n = in.length;
    double[] out = new double[n];
    int j = n - 1;
    for (int i = 0; i < n; ++i, j--)
      out[j] = in[i];
    return out;
  }

  /**
   * Build the Quadrature Mirror Filter.
   *
   * @param LP are the wavelet filter coefficients for the low pass filter.
   * @return a set of wavelet filter coefficients for the high pass filter.
   */
  static public double[] createQuadratureMirror(double LP[])
  {
    if (LP.length < 2)
      return LP.clone();

    double[] HP = reverse(LP);
    for (int i = 1; i < HP.length; i += 2)
    {
      HP[i] *= -1;
    }
    return HP;
  }

  /**
   * Compress and replicate the 1D array by a factor of 2.
   *
   * Equivalent with the DFT change when the starting signal is up-sampled by a
   * factor of 2.
   *
   * @return a new complex vector with the revised scale.
   */
  static ComplexVector subsampleVector(ComplexVector x)
  {
    int szX = x.size();

    double[] Re_x = x.getReal();
    double[] Im_x = x.getImag();

    double[] real = new double[szX];
    double[] imag = new double[szX];

    int half = (int) Math.floor(0.5 * szX);

    for (int i = 0; i < half; i++)
    {
      real[i] = Re_x[2 * i];
      real[i + half] = real[i];

      imag[i] = Im_x[2 * i];
      imag[i + half] = imag[i];
    }

    return (ComplexVector.create(real, imag));
  }

  /**
   * Upsample data by inserting zeros between ever other sample.
   *
   * @param x is a vector of data.
   * @return a new vector with twice the length padded with zeros between each
   * sample starting with a zero.
   */
  static double[] insertZeros(double[] x)
  {
    int N = x.length;
    double[] y = new double[2 * N];
    for (int i = 0; i < N; i++)
    {
      y[2 * i] = x[i];
    }

    return y;
  }

  static public double[] circularConv(double[] t, double[] filter, int iscale)
  {
    int nlength = t.length;

    ComplexVector T = Fourier.fft(ComplexVector.create(t, null), nlength);
    ComplexVector H = Fourier.fft(ComplexVector.create(filter, null), nlength);

    // the next line does extra work as only the real portion is required.
    double[] ct = Fourier.ifft(ComplexVectorOps.multiply(T, H)).getReal();
    int nshift = filter.length - (int) Math.pow(2, (iscale - 1));
    ct = circShift1D(ct, -nshift);

    return ct;
  }

  /**
   * Pad the length of a vector to be even.
   *
   * Wavelet transform require even length. The data should be truncated when
   * the process is complete.
   *
   * @param inData
   * @return
   */
  public static double[] padEven(double[] inData)
  {
    int sigLength = inData.length;
    double[] noisyData;
    // Force data to be even length
    if ((sigLength % 2) != 0)
    {
      // make length of data an even  number by repeating the last value
      noisyData = new double[sigLength + 1];
      System.arraycopy(inData, 0, noisyData, 0, sigLength);
      noisyData[sigLength] = noisyData[sigLength - 1];
    }
    else
    {
      return inData.clone();
    }
    return noisyData;
  }
}
