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
  public double i, j, k;

  public MutableVersor(Versor v)
  {
    u = v.getU();
    i = v.getI();
    j = v.getJ();
    k = v.getK();
  }

  public MutableVersor()
  {
    u = 1;
    i = 0;
    j = 0;
    k = 0;
  }

  @Override
  public double getU()
  {
    return u;
  }

  @Override
  public double getI()
  {
    return i;
  }

  @Override
  public double getJ()
  {
    return j;
  }

  @Override
  public double getK()
  {
    return k;
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
    i = s1 * c2 * c3 - c1 * s2 * s3;
    j = c1 * s2 * c3 + s1 * c2 * s3;
    k = c1 * c2 * s3 - s1 * s2 * c3;
    double s = Math.sqrt(u * u + i * i + j * j + k * k);
    u /= s;
    i /= s;
    j /= s;
    k /= s;
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
    this.i = v.getI();
    this.j = v.getJ();
    this.k = v.getK();
  }

  public void assign(double w, double i, double j, double k)
  {
    this.u = w;
    this.i = i;
    this.j = j;
    this.k = k;
    this.normalize();
  }

  @Override
  public String toString()
  {
    return String.format("Versor(%.8f, %.8f, %.8f, %.8f)", u, i, j, k);
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
    this.i += q.getI();
    this.j += q.getJ();
    this.k += q.getK();
    normalize();
  }

  /**
   * Ensure the versor is proper.
   */
  public void normalize()
  {
    double s = Math.sqrt(u * u + i * i + j * j + k * k);
    if (s == 0)
      return;
    u /= s;
    i /= s;
    j /= s;
    k /= s;
  }

  public void multiplyAssign(Quaternion q)
  {
    double q1i = i;
    double q1j = j;
    double q1k = k;
    double q1u = u;
    double q2i = q.getI();
    double q2j = q.getJ();
    double q2k = q.getK();
    double q2u = q.getU();
    u = q1u * q2u - q1i * q2i - q1j * q2j - q1k * q2k;
    i = q1u * q2i + q1i * q2u + q1j * q2k - q1k * q2j;
    j = q1u * q2j + q1j * q2u + q1k * q2i - q1i * q2k;
    k = q1u * q2k + q1k * q2u + q1i * q2j - q1j * q2i;
    normalize();
  }

  public void multiplyInvAssign(Quaternion q)
  {
    double q1i = i;
    double q1j = j;
    double q1k = k;
    double q1u = u;
    double q2i = -q.getI();
    double q2j = -q.getJ();
    double q2k = -q.getK();
    double q2u = q.getU();
    u = q1u * q2u - q1i * q2i - q1j * q2j - q1k * q2k;
    i = q1u * q2i + q1i * q2u + q1j * q2k - q1k * q2j;
    j = q1u * q2j + q1j * q2u + q1k * q2i - q1i * q2k;
    k = q1u * q2k + q1k * q2u + q1i * q2j - q1j * q2i;
    normalize();
  }

  public double[] toArray()
  {
    return new double[]
    {
      u, i, j, k
    };
  }

  public void negateAssign()
  {
    this.i *= -1;
    this.j *= -1;
    this.k *= -1;
  }
}
