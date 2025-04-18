/*
 * Copyright 2016, Lawrence Livermore National Security, LLC. 
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.math.euclidean;

import java.io.Serializable;

/**
 *
 * @author nelson85
 */
final class VersorImpl implements Versor, Serializable
{
  double u_, i_, j_, k_;

  public VersorImpl(double u, double i, double j, double k)
  {
    double s = u * u + i * i + j * j + k * k;
    if (s == 0)
      u = 1;
    if (s != 1)
    {
      s = Math.sqrt(s);
      u /= s;
      i /= s;
      j /= s;
      k /= s;
    }
    this.u_ = u;
    this.i_ = i;
    this.j_ = j;
    this.k_ = k;
  }

  @Override
  public double getX()
  {
    return i_;
  }

  @Override
  public double getY()
  {
    return j_;
  }

  @Override
  public double getZ()
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
    return u_ == v.getU() && i_ == v.getX() && j_ == v.getY() && k_ == v.getZ();
  }

}
