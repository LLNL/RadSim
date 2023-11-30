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
   * @param angle is the angle to rotate.
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

  /**
   * Create a versor.
   *
   * @param x is the x dimension of the versor to rotate about.
   * @param y is the y dimension of the versor to rotate about.
   * @param z is the z dimension of the versor to rotate about.
   * @param angle is the angle to rotate.
   * @return a new versor.
   */
  public static Versor of(double x, double y, double z, double angle)
  {
    double r = Math.sqrt(x * x + y * y + z * z);
    if (r == 0)
    {
      x = 1;
      angle = 0;
      r = 1;
    }
    double q = Math.sin(angle / 2);
    return new VersorImpl(Math.cos(angle / 2), q * x / r, q * y / r, q * z / r);
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
    return Versor.of(
            Math.signum(w) * aw / p,
            Math.signum(x) * ax / p,
            Math.signum(y) * ay / p,
            Math.signum(z) * az / p);
  }
}
