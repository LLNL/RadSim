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
public class QuaternionOps
{
  public static Quaternion multiply(Quaternion q1, Quaternion q2)
  {
    double q1i = q1.getI();
    double q1j = q1.getJ();
    double q1k = q1.getK();
    double q1u = q1.getU();
    double q2i = q2.getI();
    double q2j = q2.getJ();
    double q2k = q2.getK();
    double q2u = q2.getU();
    return Quaternion.of(
            q1u * q2u - q1i * q2i - q1j * q2j - q1k * q2k,
            q1u * q2i + q1i * q2u + q1j * q2k - q1k * q2j,
            q1u * q2j + q1j * q2u + q1k * q2i - q1i * q2k,
            q1u * q2k + q1k * q2u + q1i * q2j - q1j * q2i
    );
  }

  public static Quaternion multiply(Quaternion q1, Vector3 q2)
  {
    double q1i = q1.getI();
    double q1j = q1.getJ();
    double q1k = q1.getK();
    double q1u = q1.getU();
    double x = q2.getX();
    double y = q2.getY();
    double z = q2.getZ();
    return Quaternion.of(
            -q1i * x - q1j * y - q1k * z,
            q1u * x + q1j * z - q1k * y,
            q1u * y + q1k * x - q1i * z,
            q1u * z + q1i * y - q1j * x
    );
  }

}
