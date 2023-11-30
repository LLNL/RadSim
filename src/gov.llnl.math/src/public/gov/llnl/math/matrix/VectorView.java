/*
 * Copyright 2016, Lawrence Livermore National Security, LLC. 
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.math.matrix;

import gov.llnl.math.MathExceptions.SizeException;

/**
 * Provides a readonly view of a matrix as a vector if the matrix is linear.
 * This is useful when the orientation of the matrix is not important to the
 * operation. It will be backed by a cache if the matrix does not provide
 * appropriate access.
 */
public class VectorView
{
  private Matrix matrix;
  private double[] cache;
  private int sz = 0;
  private int offset = 0;

  /**
   * Create a vector view for a matrix.
   *
   * @param matrix
   * @throws SizeException if the matrix is not viewable as vector.
   */
  public VectorView(Matrix matrix) throws SizeException
  {
    MatrixAssert.assertVector(matrix);
    this.matrix = matrix;
    sz = Math.max(matrix.rows(), matrix.columns());
    // Accelerated access of arrays
    if (matrix instanceof MatrixArrayBase)
      this.cache = ((MatrixArrayBase) matrix).toArray();
    else if (matrix.columns() == 1 && matrix instanceof Matrix.ColumnAccess)
    {
      this.cache = ((Matrix.ColumnAccess) matrix).accessColumn(0);
      this.offset = ((Matrix.ColumnAccess) matrix).addressColumn(0);
    }
    else if (matrix.rows() == 1 && matrix instanceof Matrix.RowAccess)
    {
      this.cache = ((Matrix.RowAccess) matrix).accessRow(0);
      this.offset = ((Matrix.RowAccess) matrix).addressRow(0);
    }
  } // Accelerated access of arrays

  public VectorView(double[] d)
  {
    cache = d;
    offset = 0;
    sz = d.length;
  }

  /**
   * Get the index in the memory that holds this vector.
   *
   * @return the offset of the start of values in the array.
   */
  public int offset()
  {
    return offset;
  }

  /**
   * Get the length of the vector.
   *
   * @return the length of the vector.
   */
  public int size()
  {
    return sz;
  }

  /**
   * Get the memory holding this vector. The start of the vector may be offset.
   *
   * @return the memory that backs this vector.
   */
  public double[] access()
  {
    if (cache != null)
      return cache;
    cache = new MatrixColumnArray(matrix).toArray();
    return cache;
  }
}
