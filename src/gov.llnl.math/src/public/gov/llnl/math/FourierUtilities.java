/*
 * Copyright 2016, Lawrence Livermore National Security, LLC. 
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.math;

/**
 * Implementation class for Fourier transform functions.
 *
 * @author nelson85
 */
public class FourierUtilities
{
  /**
   * Function to compute log2 for an integer.
   *
   * @param bits
   * @return the log of the bits.
   */
  public static int binlog(int bits)
  {
    int log = 0;
    if ((bits & -65536) != 0)
    {
      bits >>>= 16;
      log = 16;
    }
    if (bits >= 256)
    {
      bits >>>= 8;
      log += 8;
    }
    if (bits >= 16)
    {
      bits >>>= 4;
      log += 4;
    }
    if (bits >= 4)
    {
      bits >>>= 2;
      log += 2;
    }
    return log + (bits >>> 1);
  }

  /**
   * Support for reordering partial Discrete Fourier Transform.
   *
   * @param in
   * @param bits
   * @return
   */
  public static int bitswap(int in, int bits)
  {
    int out = 0;
    if ((bits & 1) == 1)
    {
      out = in & (1 << (bits / 2));
    }
    bits--;
    for (int i = 0; i < (bits + 1) / 2; i++)
    {
      int b1 = (in & (1 << i)) >> i;
      int b2 = (in & (1 << (bits - i))) >> (bits - i);
      out |= (b1 << (bits - i)) | (b2 << i);
    }
    return out;
  }

  /**
   * Compute the Fast Fourier Transform of a real vector.
   *
   * Works only for length 2^N vectors.
   *
   * @param in
   * @return
   */
  public static ComplexVector fftRadix2(ComplexVector in)
  {
    int n = in.size();
    int u = binlog(n);
    if (n != (1 << u))
    {
      throw new UnsupportedOperationException("Must be radix 2, size=" + n);
    }
    double w_R = Math.cos(2 * Math.PI / n);
    double w_I = -Math.sin(2 * Math.PI / n);
    // Preallocate memory buffers
    double[] y1_R = in.getReal().clone();
    double[] y1_I = in.getImag().clone();
    double[] y2_R = new double[n];
    double[] y2_I = new double[n];
    // Counters for each dft stage
    int c1 = 1;
    int c2 = n;
    // Perform u stages of partial dfts
    for (int i = 0; i < u; ++i)
    {
      dftStage(y2_R, y2_I, y1_R, y1_I, c1, c2, w_R, w_I);
      // W=W^2
      double a = w_R;
      double b = w_I;
      w_R = a * a - b * b;
      w_I = 2 * a * b;
      // Update counters for next stage
      c1 = c1 * 2;
      c2 = c2 / 2;
      // swap y2 <=> y1
      double[] y3_R = y1_R;
      double[] y3_I = y1_I;
      y1_R = y2_R;
      y1_I = y2_I;
      y2_R = y3_R;
      y2_I = y3_I;
    }

    double[] syr = shuffle(y1_R);
    double[] syi = shuffle(y1_I);
    // After partial dfts the data is in wrong order
    // in memory. Thus we need to swap it.
    return ComplexVector.create(syr, syi);
  }

  /**
   * Chirped Z transform.
   *
   * Formulated with Bluestein's algorithm. This is just the portion of the
   * algorithm required to generalize the FFT for arbitrary radix length arrays.
   * It would need to be generalized if we require a full czt.
   *
   * @param in
   * @param m
   * @return
   */
  public static ComplexVectorImpl czt(ComplexVector in, int m)
  {
    int n = in.size();
    double v = -Math.PI / m;

    // Find next largest FFT
    int bl = binlog(m + n - 1);
    if ((1 << bl) < m + n - 1)
      bl += 1;
    int N2 = 1 << bl;

    // Create the chirp signal
    int u = Math.max(m, n);
    double[] rchirp = new double[u];
    double[] ichirp = new double[u];
    for (int i = 0; i < u; ++i)
    {
      rchirp[i] = Math.cos(v * i * i);
      ichirp[i] = Math.sin(v * i * i);
    }
    // Multiply by chirp and zero pad to the required FFT length.
    double[] ra = new double[N2];
    double[] ia = new double[N2];
    double[] rin = in.getReal();
    double[] iin = in.getImag();
    for (int i = 0; i < n; ++i)
    {
      ra[i] = rin[i] * rchirp[i] - iin[i] * ichirp[i];
      ia[i] = rin[i] * ichirp[i] + iin[i] * rchirp[i];
    }
    // Compute the convolvution chirp signal
    //   This array is shifted as we need both positive and negative time.
    //   Thus the result will be shifted by the same ammount.
    double[] rb = new double[N2];
    double[] ib = new double[N2];
    for (int i = 1; i < m + n; ++i)
    {
      rb[i - 1] = Math.cos(-v * i * i);
      ib[i - 1] = Math.sin(-v * i * i);
    }
    // Convolve (X exp(-pi n^2/N)) with exp(pi n^2/N)
    //   Compute the FFT of each sequence, take the product and then multiply
    //   to compute the convolution.
    ComplexVector A = fftRadix2(new ComplexVectorImpl(ra, ia));
    ComplexVector B = fftRadix2(new ComplexVectorImpl(rb, ib));
    ComplexVector AB = ComplexVectorOps.multiply(A, B);

    // Take the IFFT to recover our covolution.
    //   Swap for ifft, result is swapped and times N2
    ComplexVector ab = fftRadix2(new ComplexVectorImpl(AB.getImag(), AB.getReal()));
    
    // Compute final product
    double[] rout = new double[m];
    double[] iout = new double[n];
    double[] rab = ab.getImag();
    double[] iab = ab.getReal();

    for (int i = 0; i < m; ++i)
    {
      // reverse R<->I and divide by N2, remove shift, and multiply by the chirp
      rout[i] = (rab[i + n - 1] * rchirp[i] - iab[i + n - 1] * ichirp[i]) / N2;
      iout[i] = (rab[i + n - 1] * ichirp[i] + iab[i + n - 1] * rchirp[i]) / N2;
    }
    return new ComplexVectorImpl(rout, iout);
  }

  /**
   * Support function for reordering an array for the Discrete Fourier
   * Transform.
   *
   * @param in
   * @return a new array with the order permuted.
   */
  public static double[] shuffle(double[] in)
  {
    int n = in.length;
    int u = binlog(n);
    double[] out = new double[n];
    for (int i = 0; i < n; ++i)
    {
      out[i] = in[bitswap(i, u)];
    }
    return out;
  }

  // No optimized
  public static void dftStage(double[] out_R, double[] out_I, double[] in_R, double[] in_I, int S1, int S2, double w_R, double w_I)
  {
    int S3 = S2 / 2;
    for (int i0 = 0; i0 < S1; ++i0)
    {
      int i4 = i0 * S2;
      double t_R = 1;
      double t_I = 0;
      for (int i1 = 0; i1 < S3; ++i1)
      {
        int i2 = i4 + i1;
        int i3 = i4 + i1 + S3;
        // out[i2]=in[i2] + in[i3]
        out_R[i2] = in_R[i2] + in_R[i3];
        out_I[i2] = in_I[i2] + in_I[i3];
        // out[i3]=T*(in[i2]-in[i3];
        double a_R = in_R[i2] - in_R[i3];
        double a_I = in_I[i2] - in_I[i3];
        out_R[i3] = a_R * t_R - a_I * t_I;
        out_I[i3] = a_R * t_I + a_I * t_R;
        // T=T*2
        a_R = t_R;
        a_I = t_I;
        t_R = a_R * w_R - a_I * w_I;
        t_I = a_R * w_I + a_I * w_R;
      }
    }
  }

}
