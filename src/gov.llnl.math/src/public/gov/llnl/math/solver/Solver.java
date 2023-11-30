/*
 * Copyright 2016, Lawrence Livermore National Security, LLC.
 * All rights reserved
 *
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.math.solver;

import java.util.function.DoubleUnaryOperator;

/**
 *
 * @author seilhan3
 */
public interface Solver
{
  public double solve(DoubleUnaryOperator function, double target);
}
