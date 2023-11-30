/* 
 * Copyright 2016, Lawrence Livermore National Security, LLC.
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.math.algebra;

import gov.llnl.math.algebra.Constraint.ConstraintDatum;
import gov.llnl.utility.UUIDUtilities;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Constraints are augmentations to the Nnlsq algorithm to additional
 * dynamically computed quadratic constraints.
 *
 * @author yao2
 */
public class Constraint implements Iterable<ConstraintDatum>, Serializable
{

  private static final long serialVersionUID = UUIDUtilities.createLong("Constraint");
  
  public static class ConstraintDatum implements Serializable
  {

    public int regressorId;
    public double coefficent;

    private ConstraintDatum(int regressorId, double coefficient)
    {
      this.regressorId = regressorId;
      this.coefficent = coefficient;
    }

    public int getId()
    {
      return regressorId;
    }

    public double getCoef()
    {
      return coefficent;
    }
  }

  public ArrayList<ConstraintDatum> points = new ArrayList<>();
  public double rhs_;

  /**
   * Allocate a constraint. 
   *
   * @param rhs
   */
  public Constraint(double rhs)
  {
    this.rhs_ = rhs;
  }

  public void add(int regressorId, double coef)
  {
    points.add(new ConstraintDatum(regressorId, coef));
  }
  
  public void weight(double d)
  {
    this.rhs_*=d;
    for (ConstraintDatum p:this.points)
    {
      p.coefficent*=d;
    }
  }

  @Override
  public Iterator<ConstraintDatum> iterator()
  {
    return points.iterator();
  }

}
