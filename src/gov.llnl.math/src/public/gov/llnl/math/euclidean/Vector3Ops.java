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
public class Vector3Ops
{
  public static Vector3 add(Vector3 v1, Vector3 v2)
  {
    return Vector3.of(v1.getX() + v2.getX(), v1.getY() + v2.getY(), v1.getZ() + v2.getZ());
  }

  public static Vector3 subtract(Vector3 v1, Vector3 v2)
  {
    return Vector3.of(v1.getX() - v2.getX(), v1.getY() - v2.getY(), v1.getZ() - v2.getZ());
  }

  public static Vector3 multiply(Vector3 v1, double s)
  {
    return Vector3.of(v1.getX() * s, v1.getY() * s, v1.getZ() * s);
  }

  public static double multiplyDot(Vector3 v1, Vector3 v2)
  {
    return v1.getX() * v2.getX() + v1.getY() * v2.getY() + v1.getZ() * v2.getZ();
  }

  public static double length(Vector3 v1)
  {
    double x = v1.getX();
    double y = v1.getY();
    double z = v1.getZ();
    return Math.sqrt(x * x + y * y + z * z);
  }

  public static double correlation(Vector3 v1, Vector3 v2)
  {
    double x1 = v1.getX();
    double y1 = v1.getY();
    double z1 = v1.getZ();
    double x2 = v2.getX();
    double y2 = v2.getY();
    double z2 = v2.getZ();

    return (x1 * x2 + y1 * y2 + z1 * z2)
            / Math.sqrt((x1 * x1 + y1 * y1 + z1 * z1) * (x2 * x2 + y2 * y2 + z2 * z2));
  }

  public static Vector3 scale(Vector3 v1, double s)
  {
    return Vector3.of(v1.getX() * s, v1.getY() * s, v1.getZ() * s);
  }

  public static Vector3 interpolate(double f, Vector3 p0, Vector3 p1)
  {
    double x = (1 - f) * p0.getX() + f * p1.getX();
    double y = (1 - f) * p0.getY() + f * p1.getY();
    double z = (1 - f) * p0.getZ() + f * p1.getZ();
    return Vector3.of(x, y, z);
  }

  public static double sqrDistance(Vector3 c1, Vector3 c2)
  {
    double dx = (c1.getX() - c2.getX());
    double dy = (c1.getY() - c2.getY());
    double dz = (c1.getZ() - c2.getZ());
    return dx * dx + dy * dy + dz * dz;
  }

}
