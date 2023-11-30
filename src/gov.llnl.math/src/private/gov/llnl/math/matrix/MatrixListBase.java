/*
 * Copyright 2016, Lawrence Livermore National Security, LLC. 
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.math.matrix;

import gov.llnl.math.MathExceptions;
import gov.llnl.utility.annotation.Internal;
import java.util.List;

/**
 * Base class for all MatrixList classes.
 */
@Internal
abstract class MatrixListBase implements Matrix
{
  final transient Object origin;
  protected List<Vector> data;
  protected int dim;

  public static class Vector
  {
    public double[] values;
    public int offset;

    public Vector(double[] values, int offset)
    {
      this.values = values;
      this.offset = offset;
    }
  }

  protected MatrixListBase(List<Vector> data, int dim)
  {
    this.origin = this;
    this.dim = dim;
    this.data = data;
  }

  protected MatrixListBase(Object origin, List<Vector> data, int dim)
  {
    this.origin = origin;
    this.dim = dim;
    this.data = data;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void mutable() throws MathExceptions.WriteAccessException
  {
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Object sync()
  {
    return origin;
  }

}
