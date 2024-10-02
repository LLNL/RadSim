/*
 * Copyright 2016, Lawrence Livermore National Security, LLC.
 * All rights reserved
 *
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.math.euclidean;

import static gov.llnl.math.euclidean.EuclideanUtilities.POWERS_OF_10;
import gov.llnl.utility.xml.bind.ReaderInfo;
import gov.llnl.utility.xml.bind.WriterInfo;

/**
 * Specialty representation of a unit length quaternion.
 *
 * Versors are used to represent a rotation in a Euclidian space.
 *
 * @author nelson85
 */
@ReaderInfo(VersorReader.class)
@WriterInfo(VersorWriter.class)
public interface Versor extends Quaternion
{
  public static final Versor ZERO = Versor.of(Vector3.AXIS_X, 0);

  /**
   * Create a versor.
   *
   * @param axis is the direction to rotate about.
   * @param angle is the angle to rotate in radians.
   * @return a new versor.
   */
  public static Versor of(Vector3 axis, double angle)
  {
    double x = axis.getX();
    double y = axis.getY();
    double z = axis.getZ();
    double n = axis.norm();
    if (n == 0)
      return ZERO;
    double k = Math.sin(angle / 2) / n;
    return new VersorImpl(Math.cos(angle / 2), x * k, y * k, z * k);
  }

  public static Versor fromQuaternion(double w, double i, double j, double k)
  {
    return new VersorImpl(w, i, j, k);
  }

  public static Versor fromAxis(MutableVector3 ax, MutableVector3 ay, MutableVector3 az)
  {
    double qx;
    double qy;
    double qz;
    double qw;
    double t;
    if (az.z < 0)
    {
      if (ax.x > ay.y)
      {
        t = 1 + ax.x - ay.y - az.z;
        qx = t;
        qy = ax.y + ay.x;
        qz = ax.z + az.x;
        qw = ay.z - az.y;
      }
      else
      {
        t = 1 - ax.x + ay.y - az.z;
        qx = ax.y + ay.x;
        qy = t;
        qz = ay.z + az.y;
        qw = az.x - ax.z;
      }
    }
    else
    {
      if (ax.x < -ay.y)
      {
        t = 1 - ax.x - ay.y + az.z;
        qx = az.x + ax.z;
        qy = ay.z + az.y;
        qz = t;
        qw = ax.y - ay.x;
      }
      else
      {
        t = 1 + ax.x + ay.y + az.z;
        qx = ay.z - az.y;
        qy = az.x - ax.z;
        qz = ax.y - ay.x;
        qw = t;
      }
    }
    t = Math.sqrt(qw*qw+qx*qx+qy*qy+qz*qz);
    return Versor.fromQuaternion(qw/t, qx/t, qy/t, qz/t);
  }

  /**
   * Produce a versor that rotates in the opposite direction.
   *
   * @return
   */
  default Versor inv()
  {
    return new VersorInv(this);
  }

  /**
   * Rotate a vector by a versor.
   *
   * @param v
   * @return
   */
  default Vector3 rotate(Vector3 v)
  {
    if (getU() == 0)
      return Vector3.of(v.getX(), v.getY(), v.getZ());
    Quaternion q = QuaternionOps.multiply(this, v);
    Quaternion q2 = QuaternionOps.multiply(q, this.inv());
    return Vector3.of(q2.getI(), q2.getJ(), q2.getK());
  }

  default Vector3 invRotate(Vector3 v)
  {
    if (getU() == 0)
      return Vector3.of(v.getX(), v.getY(), v.getZ());
    Quaternion q = QuaternionOps.multiply(this.inv(), v);
    Quaternion q2 = QuaternionOps.multiply(q, this);
    return Vector3.of(q2.getI(), q2.getJ(), q2.getK());
  }

  default Versor multiply(Versor v)
  {
    double q1i = this.getI();
    double q1j = this.getJ();
    double q1k = this.getK();
    double q1u = this.getU();
    double q2i = v.getI();
    double q2j = v.getJ();
    double q2k = v.getK();
    double q2u = v.getU();
    return Versor.fromQuaternion(
            q1u * q2u - q1i * q2i - q1j * q2j - q1k * q2k,
            q1u * q2i + q1i * q2u + q1j * q2k - q1k * q2j,
            q1u * q2j + q1j * q2u + q1k * q2i - q1i * q2k,
            q1u * q2k + q1k * q2u + q1i * q2j - q1j * q2i);
  }

  default Versor round(int dig)
  {
    long p = POWERS_OF_10[dig];
    double w = this.getU();
    double x = this.getI();
    double y = this.getJ();
    double z = this.getK();
    double aw = (long) (Math.abs(w) * p + 0.5);
    double ax = (long) (Math.abs(x) * p + 0.5);
    double ay = (long) (Math.abs(y) * p + 0.5);
    double az = (long) (Math.abs(z) * p + 0.5);
    return Versor.fromQuaternion(
            Math.signum(w) * aw / p,
            Math.signum(x) * ax / p,
            Math.signum(y) * ay / p,
            Math.signum(z) * az / p);
  }
}
