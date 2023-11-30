/*
 * Copyright 2016, Lawrence Livermore National Security, LLC. 
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.math.wavelet;

import gov.llnl.math.ComplexVector;
import gov.llnl.math.ComplexVectorOps;
import gov.llnl.math.Fourier;
import gov.llnl.math.matrix.Matrix;
import gov.llnl.math.matrix.MatrixFactory;

/**
 *
 * @author nelson85
 */
class WaveletOps
{

  /**
   * Returns the smoothed version of the signal using the 'waveletScale' level
   * decomposition and the m-tap Daubechies wavelet
   *
   * @param inData
   * @param waveletScale
   * @return
   */
  public static double[] smooth(double[] inData, double[] h0, double[] h1, int waveletScale)
  {

    int sigLength = inData.length;
    double[] noisyData = WaveletUtilities.padEven(inData);
    Matrix waveletCoeff = overCompleteWaveletTransform(noisyData, h0, h1, waveletScale);

    // 'overCompleteWaveletTransform' will reduce the 'waveletScale' if a value larger
    // than the one that justified by the size of the signal and the filter is being required!
    // Figure out the new value!
    waveletScale = waveletCoeff.columns() - 1;
    Matrix.ColumnAccess multiResSignal = reconstructMultiResolutionWave(waveletCoeff, h0, h1);
    double[] smoothedData = new double[sigLength];
    System.arraycopy(multiResSignal.accessColumn(waveletScale), multiResSignal.addressColumn(waveletScale),
            smoothedData, 0, sigLength);

    // If the length was not changed then it was not padded.
    if (smoothedData.length == inData.length)
    {
      return smoothedData;
    }

    // It was padded and must be truncated
    double[] temp = new double[sigLength];
    System.arraycopy(smoothedData, 0, temp, 0, sigLength);
    return temp;
  }

  /**
   * Calculates the coefficients for the over-complete wavelet transform
   * (MODWT).
   *
   * @author chandrasekar1@llnl.gov
   *
   * @param x is the signal to decompose.
   * @param h0 is the wavelet filter low pass coefficients.
   * @param h1 is the wavelet filter high pass coefficients.
   * @param maxScale is the largest scale to decompose the signal onto.
   * @return a new matrix holding the wavelet coefficients with each column
   * representing a scale.
   */
  public static Matrix overCompleteWaveletTransform(double[] x, double[] h0, double[] h1, int maxScale)
  {
    if (maxScale < 1)
      throw new IllegalArgumentException("Scale must be positive.");

    // input data vector length, forced to be a power of 2
    int nx = x.length;

    // scaling function (low pass filter impulse response) coefficients length
    int m = h0.length;

    // figure out the maximum possible scale
    int maxPossibleScale = 1 + (int) ((Math.log(nx) - Math.log(m)) / Math.log(2));
    if (maxScale > maxPossibleScale)
    {
      maxScale = maxPossibleScale;
    }

    // All filter coefficients
    double[][] waveletCoeff = new double[maxScale + 1][nx];

    // LPF (Filter bank terminology) Scaling coefficients at scale k /Wavelet terminology
    ComplexVector H0 = Fourier.fft(ComplexVector.create(h0, null), nx);

    // HPF (Filter bank terminology) Wavelet expansion coefficients at scale k /Wavelet terminology
    ComplexVector H1 = Fourier.fft(ComplexVector.create(h1, null), nx);

    // Signal DFT
    ComplexVector X = Fourier.fft(ComplexVector.create(x, null), nx);

    /*
    higher scale wavelet components can be considered as details on a lower scale signal
    find out how many stages of filtering to do
    for any signal that is band limited, there will be an upper scale j = J,
    above which the wavelet coefficients are negligibly small
    that is indicated by maxScale
     */
    // define some temporary storage for the computations
    ComplexVector temp_Complex;
    int nshift = 0;
    for (int j = 0; j < maxScale; j++)
    {
      // wavelet expansion of signal at scale k
      // signal filtered by wavelet coefficients at scale k (HPF at scale k)
      temp_Complex = Fourier.ifft(ComplexVectorOps.multiply(X, H1));
      nshift = (m - 1) * (int) Math.pow(2, j);
      double[] temp = WaveletUtilities.circShift1D(temp_Complex.getReal(), -nshift);
      System.arraycopy(temp, 0, waveletCoeff[j], 0, nx);
      
      // low pass filter the signal for the next iteration
      X = ComplexVectorOps.multiply(X, H0);
      /*
      MULTIRATE IDENTITIES:  Interchange of filtering and downsampling:
      downsampling by N followed by filtering with H(z) is equivalent to
      filtering with the upsampled filter(H(z^N)) before downsampling.
      (upsampling a filter impulse response is equivalent to introducing
      2^k zeros between nonzero coefficients at scale k filter
       */
      H0 = WaveletUtilities.subsampleVector(H0);
      H1 = WaveletUtilities.subsampleVector(H1);
    }
    // lowest resolution signal
    nshift = (m - 1) * (int) Math.pow(2, maxScale - 1);
    double[] temp = WaveletUtilities.circShift1D(Fourier.ifft(X).getReal(), -nshift);
    System.arraycopy(temp, 0, waveletCoeff[maxScale], 0, nx);

    // One column for each scale with each column length equals number of samples
    return MatrixFactory.newColumnMatrix(waveletCoeff);
  }

  /**
   * Description: This function reconstructs the multiresolution signal from the
   * overcomplete wavelet series expansion of the signal. This is practically
   * the inverse overcomplete transform
   *
   * Input: (1) waveletCoeff a matrix of size signal_length x_scale containing
   * the overcomplete wavelet series coefficients (2) ho - low pass analysis
   * bank filter for the lowest scale
   *
   * Output: multiResSignal - multi resolution signal in matrix form of size
   * signal length x_scale
   *
   * chandrasekar1@llnl.gov Comments: Idea from 'Ripples in Mathematics - The
   * Discrete Wavelet Transform' by A. Jensen and A.la Cour-Harbo,
   * Springer-Veralg, 2001
   *
   * @param waveletCoeff
   * @param h0 is the wavelet filter low pass coefficients.
   * @param h1 is the wavelet filter high pass coefficients.
   * @return a new matrix holding the reconstructed signal by scale.
   */
  public static Matrix.ColumnAccess reconstructMultiResolutionWave(
          Matrix waveletCoeff, double[] h0, double[] h1)
  {
    int ns = waveletCoeff.columns();
    int nx = waveletCoeff.rows();

    int m = h0.length;
    int scale = ns - 1;
    double[][] rhlow = new double[ns][nx];
    double[][] rghigh = new double[ns][nx];
    double[][] multiResSignal = new double[ns][nx];

    int[] filtlen = new int[ns - 1];
    filtlen[0] = m;

    /**
     * filters with holes or zeros we can create these filters from the basic h0
     * = daubh0(12) by filling 2^(j-1) zeros in between samples for each scale
     * j. Here these filters were obtained from OWT
     */
    {
      // This portion is a preprocessing step which could be cached if we 
      // are performing multiple transforms on the same scale.  Consider adding a
      // cache for it.
      double[] g0 = WaveletUtilities.reverse(h0);
      double[] g1 = WaveletUtilities.reverse(h1);
      
      // We don't use g0 or g1 again so why do we need to clone here?
      double[] filter0 = g0.clone();
      double[] filter1 = g1.clone();
      for (int i = 0; i < scale; i++)
      {
        filtlen[i] = filtlen[0] * (int) Math.pow(2, i);
        if (i > 0)
        {
          filter0 = WaveletUtilities.insertZeros(filter0);
          filter1 = WaveletUtilities.insertZeros(filter1);
        }
        int n = filter0.length;
        if (n <= nx)
        {
          System.arraycopy(filter0, 0, rhlow[i], 0, n);
          System.arraycopy(filter1, 0, rghigh[i], 0, n);
        }
      }
    }

    // treat scale+1 differently
    // uses only low pass filters
    double[] t = new double[nx];
    waveletCoeff.copyColumnTo(t, 0, scale);

    /*
     * this was the shift introduced in overCompleteWaveletTransform.m to align
     * the wavelet coefficients in time. Still can't understand why this has to
     * be undone for perfect reconstruction!!!
     */
    int nshift = filtlen[scale - 1] - (int) Math.pow(2, scale - 1);
    t = WaveletUtilities.circShift1D(t, nshift);

    // for the last scale use the same filter length as the (last-1) scale
    for (int i = scale - 1; i >= 0; i -= 1)
    {
      int L = filtlen[i];
      // filters with holes or zeros
      // we can create these filters from the basic h0 = daubh0(12) by filling
      // 2^(j-1) zeros in between samples for each scale j. Here these filters
      // were obtained from OWT
      double[] filter = new double[L];
      System.arraycopy(rhlow[i], 0, filter, 0, L);

      // this is circular convolution followed by a shift
      t = WaveletUtilities.circularConv(t, filter, i + 1);
    }

    double coeff = Math.pow(2, -scale); // for OWT scale each signal
    for (int r = 0; r < nx; r++)
    {
      multiResSignal[ns - 1][r] = coeff * t[r];
    }

    for (int jj = scale - 1; jj >= 0; jj -= 1)
    {
      for (int ii = jj; ii >= 0; ii -= 1)
      {
        int L = filtlen[ii];
        double[] filter = new double[L];
        if (ii == jj)
        {
          // retrieve a particular scale of coefficients
          waveletCoeff.copyColumnTo(t, 0, ii);
          nshift = filtlen[ii] - (int) Math.pow(2, ii);

          // This was the shift introduced in overCompleteWaveletTransform.m to
          // align the wavelet coefficients in time. Still can't understand why
          // this has to be undone for perfect reconstruction!!!
          t = WaveletUtilities.circShift1D(t, nshift);
          System.arraycopy(rghigh[ii], 0, filter, 0, L);
        }
        else
        {
          System.arraycopy(rhlow[ii], 0, filter, 0, L);
        }
        // this is circular convolution followed by a shift
        t = WaveletUtilities.circularConv(t, filter, ii + 1);
      }
      coeff = Math.pow(2, -(jj + 1));
      for (int r = 0; r < nx; r++)
      {
        multiResSignal[jj][r] = coeff * t[r];
      }
    }
    return MatrixFactory.newColumnMatrix(multiResSignal);
  }
}
