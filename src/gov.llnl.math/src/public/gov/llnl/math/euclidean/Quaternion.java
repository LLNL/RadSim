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
public interface Quaternion
{
  double getU();
  double getI();
  double getJ();
  double getK();
  
  static Quaternion of(double u, double i, double j, double k)
  {
    return new QuaternionImpl(u,i,j,k);
  }


}
