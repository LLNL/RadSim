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
  public double i;
  public double j;
  public double k;

  public MutableQuaternion()
  {
    u = 1;
    i = j = k = 0;
  }

  public MutableQuaternion(double u, double i, double j, double k)
  {
    this.u = u;
    this.i = i;
    this.j = j;
    this.k = k;
  }

  @Override
  public double getU()
  {
    return this.u;
  }

  @Override
  public double getI()
  {
    return this.i;
  }

  @Override
  public double getJ()
  {
    return this.j;
  }

  @Override
  public double getK()
  {
    return this.k;
  }

  public void addAssign(Quaternion q)
  {
    u += q.getU();
    i += q.getI();
    j += q.getJ();
    k += q.getK();
  }

  public void multiplyAssign(double scalar)
  {
    u *= scalar;
    i *= scalar;
    j *= scalar;
    k *= scalar;
  }

  public void addAssignScaled(Quaternion q, double scalar)
  {
    u += q.getU() * scalar;
    i += q.getI() * scalar;
    j += q.getJ() * scalar;
    k += q.getK() * scalar;
  }

  public void normalize()
  {
    double s = Math.sqrt(u * u + i * i + j * j + k * k);
    if (s <= 0)
      return;
    u /= s;
    i /= s;
    j /= s;
    k /= s;
  }

  public void inverseAssign()
  {
    double s = u * u + i * i + j * j + k * k;
    u = u / s;
    i = -i / s;
    j = -j / s;
    k = -k / s;
  }

  /**
   * Multiply this quaternion by another quaternion.
   *
   * @param q
   * @return
   */
  public MutableQuaternion multiplyAssign(Quaternion q)
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
    double q1i = i;
    double q1j = j;
    double q1k = k;
    double q1u = u;
    double q2i = -q.getI();
    double q2j = -q.getJ();
    double q2k = -q.getK();
    double q2u = q.getU();
    double s = q2i * q2i + q2j * q2j + q2k * q2k + q2u * q2u;
    u = (q1u * q2u - q1i * q2i - q1j * q2j - q1k * q2k) / s;
    i = (q1u * q2i + q1i * q2u + q1j * q2k - q1k * q2j) / s;
    j = (q1u * q2j + q1j * q2u + q1k * q2i - q1i * q2k) / s;
    k = (q1u * q2k + q1k * q2u + q1i * q2j - q1j * q2i) / s;
    return this;
  }

  public void assign(Quaternion q)
  {
    this.u = q.getU();
    this.i = q.getI();
    this.j = q.getJ();
    this.k = q.getK();
  }

  public void assign(double u, double i, double j, double k)
  {
    this.u = u;
    this.i = i;
    this.j = j;
    this.k = k;
  }

  @Override
  public String toString()
  {
    return String.format("Quat(%.8e, %.8e, %.8e, %.8e)", u, i, j, k);
  }

}
