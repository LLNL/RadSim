/*
 * Copyright 2022, Lawrence Livermore National Security, LLC.
 * All rights reserved
 *
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.math.euclidean;

public class MutableQuaternion implements Quaternion
{
  public double u;
  public double x;
  public double y;
  public double z;

  public MutableQuaternion()
  {
    u = 1;
    x = y = z = 0;
  }

  public MutableQuaternion(double u, double i, double j, double k)
  {
    this.u = u;
    this.x = i;
    this.y = j;
    this.z = k;
  }

  @Override
  public double getU()
  {
    return this.u;
  }

  @Override
  public double getX()
  {
    return this.x;
  }

  @Override
  public double getY()
  {
    return this.y;
  }

  @Override
  public double getZ()
  {
    return this.z;
  }

  public void addAssign(Quaternion q)
  {
    u += q.getU();
    x += q.getX();
    y += q.getY();
    z += q.getZ();
  }

  public void multiplyAssign(double scalar)
  {
    u *= scalar;
    x *= scalar;
    y *= scalar;
    z *= scalar;
  }

  public void addAssignScaled(Quaternion q, double scalar)
  {
    u += q.getU() * scalar;
    x += q.getX() * scalar;
    y += q.getY() * scalar;
    z += q.getZ() * scalar;
  }

  public void normalize()
  {
    double s = Math.sqrt(u * u + x * x + y * y + z * z);
    if (s <= 0)
      return;
    u /= s;
    x /= s;
    y /= s;
    z /= s;
  }

  public void inverseAssign()
  {
    double s = u * u + x * x + y * y + z * z;
    u = u / s;
    x = -x / s;
    y = -y / s;
    z = -z / s;
  }

  /**
   * Multiply this quaternion by another quaternion.
   *
   * @param q
   * @return
   */
  public MutableQuaternion multiplyAssign(Quaternion q)
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
    return this;
  }

  /**
   * Multiply this quaternion by the inverse of another.
   *
   * @param q
   * @return
   */
  public MutableQuaternion multiplyAssignInv(Quaternion q)
  {
    double q1i = x;
    double q1j = y;
    double q1k = z;
    double q1u = u;
    double q2i = -q.getX();
    double q2j = -q.getY();
    double q2k = -q.getZ();
    double q2u = q.getU();
    double s = q2i * q2i + q2j * q2j + q2k * q2k + q2u * q2u;
    u = (q1u * q2u - q1i * q2i - q1j * q2j - q1k * q2k) / s;
    x = (q1u * q2i + q1i * q2u + q1j * q2k - q1k * q2j) / s;
    y = (q1u * q2j + q1j * q2u + q1k * q2i - q1i * q2k) / s;
    z = (q1u * q2k + q1k * q2u + q1i * q2j - q1j * q2i) / s;
    return this;
  }

  public void assign(Quaternion q)
  {
    this.u = q.getU();
    this.x = q.getX();
    this.y = q.getY();
    this.z = q.getZ();
  }

  public void assign(double u, double i, double j, double k)
  {
    this.u = u;
    this.x = i;
    this.y = j;
    this.z = k;
  }

  @Override
  public String toString()
  {
    return String.format("Quat(%.8e, %.8e, %.8e, %.8e)", u, x, y, z);
  }

}
