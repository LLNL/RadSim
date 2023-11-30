/*
 * Copyright 2016, Lawrence Livermore National Security, LLC.
 * All rights reserved
 *
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.math.matrix;

/**
 * Abstraction for Gauss elimination code.
 */
public interface MatrixRowOperations
{

  static MatrixRowOperations of(Matrix matrix)
  {
    return MatrixFactory.createRowOperations(matrix);
  }

  static MatrixRowOperations of(double[] d)
  {
    return MatrixFactory.createRowOperations(d);
  }

  /**
   * Add two rows together with a scaling factor.
   *
   * @param r1 is the target row.
   * @param r2 is the source row to add.
   * @param scalar is the scaling factor to apply when adding.
   */
  void addScaledRows(int r1, int r2, double scalar);

  /**
   * Multiply a row by a scalar.
   *
   * @param row is the target row.
   * @param scalar is the factor to multiply by.
   */
  void multiplyAssignRow(int row, double scalar);

  /**
   * Divide a row by a scalar.
   *
   * @param row is the target row.
   * @param scalar is the factor to divide by.
   */
  void divideAssignRow(int row, double scalar);

  /**
   * Swap two rows in the matrix.
   *
   * @param r1
   * @param r2
   */
  void swapRows(int r1, int r2);

  /**
   * Apply the operations to the matrix. This must be called once the row
   * operations are complete.
   */
  void apply();

}
