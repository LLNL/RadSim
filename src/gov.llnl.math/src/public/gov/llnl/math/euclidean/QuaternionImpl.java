/*
 * Copyright 2016, Lawrence Livermore National Security, LLC. 
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.math.euclidean;

public final class QuaternionImpl implements Quaternion
{
  double u_, i_, j_, k_;

  QuaternionImpl(double u, double i, double j, double k)
  {
    this.u_ = u;
    this.i_ = i;
    this.j_ = j;
    this.k_ = k;
  }

  @Override
  public double getI()
  {
    return i_;
  }

  @Override
  public double getJ()
  {
    return j_;
  }

  @Override
  public double getK()
  {
    return k_;
  }

  @Override
  public double getU()
  {
    return u_;
  }

  public String toString()
  {
    return String.format("Quaternion(%f,%f,%f,%f)", u_, i_, j_, k_);
  }
}
