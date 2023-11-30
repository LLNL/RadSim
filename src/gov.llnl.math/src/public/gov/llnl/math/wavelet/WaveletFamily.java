/*
 * Copyright 2016, Lawrence Livermore National Security, LLC. 
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.math.wavelet;

/**
 *
 * @author guensche1
 */
public interface WaveletFamily
{

  /**
   * Returns the coefficients for the wavelets given the length.
   *
   * @param length
   * @return
   * @throws WaveletNotFoundException if the size exceeds available for the
   * family.
   */
  double[] get(int length) throws WaveletNotFoundException;

  /**
   * Return a list of wavelet names available from this family.
   *
   * @return
   */
  String[] getAvailableWavelets();
}
