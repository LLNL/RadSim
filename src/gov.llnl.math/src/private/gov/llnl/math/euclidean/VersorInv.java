/*
 * Copyright 2016, Lawrence Livermore National Security, LLC. 
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.math.euclidean;

import java.io.Serializable;

/**
 *
 * @author nelson85
 */
final class VersorInv implements Versor, Serializable
{
  Versor versor_;

  public VersorInv(Versor v)
  {
    this.versor_ = v;
  }

  @Override
  public double getX()
  {
    return -versor_.getX();
  }

  @Override
  public double getY()
  {
    return -versor_.getY();
  }

  @Override
  public double getZ()
  {
    return -versor_.getZ();
  }

  @Override
  public double getU()
  {
    return versor_.getU();
  }

}
