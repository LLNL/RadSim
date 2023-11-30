/* 
 * Copyright 2023, Lawrence Livermore National Security, LLC.
 * All rights reserved.
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.rtk.physics;

/**
 *
 * @author nelson85
 */
public enum ActivityUnit
{
  Bq(1),
  kBq(1_000),
  MBq(1_000_000),
  uCi(3.7e4),
  mCi(3.7e7),
  Ci(3.7e10);

  double factor;

  ActivityUnit(double v)
  {
    factor = v;
  }

  public double getFactor()
  {
    return factor;
  }
}
