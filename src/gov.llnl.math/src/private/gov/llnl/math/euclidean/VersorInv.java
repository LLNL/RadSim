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
final class VersorInv implements Versor
{
  Versor versor_;

  public VersorInv(Versor v)
  {
    this.versor_ = v;
  }

  @Override
  public double getI()
  {
    return -versor_.getI();
  }

  @Override
  public double getJ()
  {
    return -versor_.getJ();
  }

  @Override
  public double getK()
  {
    return -versor_.getK();
  }

  @Override
  public double getU()
  {
    return versor_.getU();
  }

}
