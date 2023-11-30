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
    return Quaternion.of(
            q1.getU() * q2.getU() - q1.getI() * q2.getI() - q1.getJ() * q2.getJ() - q1.getK() * q2.getK(),
            q1.getU() * q2.getI() + q1.getI() * q2.getU() + q1.getJ() * q2.getK() - q1.getK() * q2.getJ(),
            q1.getU() * q2.getJ() + q1.getJ() * q2.getU() + q1.getK() * q2.getI() - q1.getI() * q2.getK(),
            q1.getU() * q2.getK() + q1.getK() * q2.getU() + q1.getI() * q2.getJ() - q1.getJ() * q2.getI()
    );
  }

  public static Quaternion multiply(Quaternion q1, Vector3 q2)
  {
    return Quaternion.of(
           -q1.getI() * q2.getX() - q1.getJ() * q2.getY() - q1.getK() * q2.getZ(),
            q1.getU() * q2.getX() + q1.getJ() * q2.getZ() - q1.getK() * q2.getY(),
            q1.getU() * q2.getY() + q1.getK() * q2.getX() - q1.getI() * q2.getZ(),
            q1.getU() * q2.getZ() + q1.getI() * q2.getY() - q1.getJ() * q2.getX()
    );
  }
}
