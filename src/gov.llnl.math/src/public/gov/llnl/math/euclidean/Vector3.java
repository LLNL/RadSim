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
 *
 * @author nelson85
 */
@ReaderInfo(Vector3Reader.class)
@WriterInfo(Vector3Writer.class)
public interface Vector3
{
  final static Vector3 ZERO = Vector3.of(0, 0, 0);
  final static Vector3 AXIS_X = Vector3.of(1, 0, 0);
  final static Vector3 AXIS_Y = Vector3.of(0, 1, 0);
  final static Vector3 AXIS_Z = Vector3.of(0, 0, 1);

  /**
   * Create a vector3.
   *
   * @param x is the x dimension of the vector.
   * @param y is the y dimension of the vector.
   * @param z is the z dimension of the vector.
   * @return a new vector.
   */
  static Vector3 of(double x, double y, double z)
  {
    return new Vector3Impl(x, y, z);
  }

  double getX();

  double getY();

  double getZ();

  /**
   * Length of the vector.
   *
   * @return
   */
  default double norm()
  {
    return Math.sqrt(this.norm2());
  }

  /**
   * Dot-product
   *
   * @param vec2
   * @return
   */
  default double dot(Vector3 vec2)
  {
    double vx1 = this.getX();
    double vx2 = vec2.getX();

    double vy1 = this.getY();
    double vy2 = vec2.getY();

    double vz1 = this.getZ();
    double vz2 = vec2.getZ();

    return vx1 * vx2 + vy1 * vy2 + vz1 * vz2;
  }

  /**
   * Norm squared
   *
   * @return
   */
  default double norm2()
  {
    return this.dot(this);
  }

  default Vector3 roundTo(int digits)
  {
    long p = POWERS_OF_10[digits];
    double x = this.getX();
    double y = this.getY();
    double z = this.getZ();
    double ax = (long) (Math.abs(x) * p + 0.5);
    double ay = (long) (Math.abs(y) * p + 0.5);
    double az = (long) (Math.abs(z) * p + 0.5);
    Vector3 out = Vector3.of(
            Math.signum(x) * ax / p,
            Math.signum(y) * ay / p,
            Math.signum(z) * az / p);
    return out;
  }

  default double[] toArray()
  {
    return new double[]
    {
      this.getX(), this.getY(), this.getZ()
    };
  }
}
