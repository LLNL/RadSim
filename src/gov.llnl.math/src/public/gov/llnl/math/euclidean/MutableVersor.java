/*
 * Copyright 2022, Lawrence Livermore National Security, LLC.
 * All rights reserved
 *
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.math.euclidean;

import java.io.Serializable;

public class MutableVersor implements Versor, Serializable
{
  private static final long serialVersionUID = -1798111508634035503L;
  public double u;
  public double x, y, z;

  public MutableVersor(Versor v)
  {
    u = v.getU();
    x = v.getX();
    y = v.getY();
    z = v.getZ();
  }

  public MutableVersor()
  {
    u = 1;
    x = 0;
    y = 0;
    z = 0;
  }

  @Override
  public double getU()
  {
    return u;
  }

  @Override
  public double getX()
  {
    return x;
  }

  @Override
  public double getY()
  {
    return y;
  }

  @Override
  public double getZ()
  {
    return z;
  }

  /**
   * Convert Euler angles into a versor.
   *
   * @param roll in radians
   * @param pitch in radians
   * @param yaw in radians
   */
  public void assignEuler(double roll, double pitch, double yaw)
  {
    // https://en.wikipedia.org/wiki/Conversion_between_quaternions_and_Euler_angles
    double c1 = Math.cos(roll / 2);
    double c2 = Math.cos(pitch / 2);
    double c3 = Math.cos(yaw / 2);
    double s1 = Math.sin(roll / 2);
    double s2 = Math.sin(pitch / 2);
    double s3 = Math.sin(yaw / 2);

    // Store in the rotation
    u = c1 * c2 * c3 + s1 * s2 * s3;
    x = s1 * c2 * c3 - c1 * s2 * s3;
    y = c1 * s2 * c3 + s1 * c2 * s3;
    z = c1 * c2 * s3 - s1 * s2 * c3;
    double s = Math.sqrt(u * u + x * x + y * y + z * z);
    u /= s;
    x /= s;
    y /= s;
    z /= s;
  }

//  public void toEuler(MutableVector3 v)
//  {
//    v.x = Math.atan2(
//            2 * (u * i + j * k),
//            1 - 2 * (i * i + j * j)
//    );
//    v.y = Math.asin(2 * (u * j - i * k));
//    v.z = Math.atan2(
//            2 * (u * k + i * j),
//            1 - 2 * (j * j + k * k)
//    );
//  }

  public void assign(Versor v)
  {
    this.u = v.getU();
    this.x = v.getX();
    this.y = v.getY();
    this.z = v.getZ();
  }

  public void assign(double w, double i, double j, double k)
  {
    this.u = w;
    this.x = i;
    this.y = j;
    this.z = k;
    this.normalize();
  }

  @Override
  public String toString()
  {
    return String.format("Versor(%.8f, %.8f, %.8f, %.8f)", u, x, y, z);
  }

  /**
   * Add a versor.
   *
   * This will renormalize to maintain the normalization.
   *
   * @param q is the quaternion to add
   */
  public void addAssign(Quaternion q)
  {
    this.u += q.getU();
    this.x += q.getX();
    this.y += q.getY();
    this.z += q.getZ();
    normalize();
  }

  /**
   * Ensure the versor is proper.
   */
  public void normalize()
  {
    double s = Math.sqrt(u * u + x * x + y * y + z * z);
    if (s == 0)
      return;
    if (u<0) 
      s = -s;
    u /= s;
    x /= s;
    y /= s;
    z /= s;
  }

  public void multiplyAssign(Quaternion q)
  {
    double q1i = x;
    double q1j = y;
    double q1k = z;
    double q1u = u;
    double q2i = q.getX();
    double q2j = q.getY();
    double q2k = q.getZ();
    double q2u = q.getU();
    u = q1u * q2u - q1i * q2i - q1j * q2j - q1k * q2k;
    x = q1u * q2i + q1i * q2u + q1j * q2k - q1k * q2j;
    y = q1u * q2j + q1j * q2u + q1k * q2i - q1i * q2k;
    z = q1u * q2k + q1k * q2u + q1i * q2j - q1j * q2i;
    normalize();
  }

  public void multiplyInvAssign(Quaternion q)
  {
    double q1i = x;
    double q1j = y;
    double q1k = z;
    double q1u = u;
    double q2i = -q.getX();
    double q2j = -q.getY();
    double q2k = -q.getZ();
    double q2u = q.getU();
    u = q1u * q2u - q1i * q2i - q1j * q2j - q1k * q2k;
    x = q1u * q2i + q1i * q2u + q1j * q2k - q1k * q2j;
    y = q1u * q2j + q1j * q2u + q1k * q2i - q1i * q2k;
    z = q1u * q2k + q1k * q2u + q1i * q2j - q1j * q2i;
    normalize();
  }

  public double[] toArray()
  {
    return new double[]
    {
      u, x, y, z
    };
  }

  public void negateAssign()
  {
    this.x *= -1;
    this.y *= -1;
    this.z *= -1;
  }
}
