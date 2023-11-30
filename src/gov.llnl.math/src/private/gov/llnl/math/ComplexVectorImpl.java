/*
 * Copyright 2016, Lawrence Livermore National Security, LLC. 
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.math;

import gov.llnl.utility.annotation.Internal;

/**
 *
 * @author nelson85
 */
class ComplexVectorImpl implements ComplexVector
{
  double[] real;
  double[] imag;

  @Internal
  public ComplexVectorImpl(double[] real, double[] img)
  {
    this.real = real;
    this.imag = img;
  }

  @Override
  public double[] getReal()
  {
    return real;
  }

  @Override
  public double[] getImag()
  {
    return imag;
  }

  @Override
  public double[] getAbs()
  {
    int n = real.length;
    double[] out = new double[n];
    for (int i = 0; i < n; i++)
    {
      out[i] = Math.sqrt(this.real[i] * this.real[i] + this.imag[i] * this.imag[i]);
    }
    return out;
  }

  @Override
  public int size()
  {
    return real.length;
  }

//  public void resize(int n)
//  {
//    real = new double[n];
//    imag = new double[n];
//  }
}
