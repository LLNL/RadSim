/*
 * Copyright 2016, Lawrence Livermore National Security, LLC. 
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.math.euclidean;

/**
 *
 * @author nelson85
 */
final class VersorImpl implements Versor
{
  double u_, i_, j_, k_;

  public VersorImpl(double u, double i, double j, double k)
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

  @Override
  public String toString()
  {
    return String.format("Versor(%f,%f,%f,%f)", u_, i_, j_, k_);
  }

  @Override
  public boolean equals(Object obj)
  {
    if (!(obj instanceof Versor))
      return false;
    Versor v = (Versor) obj;
    return u_ == v.getU() && i_ == v.getI() && j_ == v.getJ() && k_ == v.getK();
  }

}
