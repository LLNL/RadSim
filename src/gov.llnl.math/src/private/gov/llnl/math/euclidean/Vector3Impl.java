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
class Vector3Impl implements Vector3, Serializable
{
  private static final long serialVersionUID = 235981610L;

  final double x_, y_, z_;

  public Vector3Impl(double x, double y, double z)
  {
    this.x_ = x;
    this.y_ = y;
    this.z_ = z;
  }

  @Override
  public double getX()
  {
    return x_;
  }

  @Override
  public double getY()
  {
    return y_;
  }

  @Override
  public double getZ()
  {
    return z_;
  }

  @Override
  public String toString()
  {
    return String.format("Vector3(%.20f,%.20f,%.20f)", x_, y_, z_);
  }

  @Override
  public boolean equals(Object other)
  {
    if (other == this)
      return true;

    if (!(other instanceof Vector3))
      return false;

    Vector3 otherVec3 = (Vector3) other;
    return this.getX() == otherVec3.getX()
            && this.getY() == otherVec3.getY()
            && this.getZ() == otherVec3.getZ();
  }

  @Override
  public int hashCode()
  {
    int hash = 7;
    hash = 37 * hash + (int) (Double.doubleToLongBits(this.x_) ^ (Double.doubleToLongBits(this.x_) >>> 32));
    hash = 37 * hash + (int) (Double.doubleToLongBits(this.y_) ^ (Double.doubleToLongBits(this.y_) >>> 32));
    hash = 37 * hash + (int) (Double.doubleToLongBits(this.z_) ^ (Double.doubleToLongBits(this.z_) >>> 32));
    return hash;
  }

}
