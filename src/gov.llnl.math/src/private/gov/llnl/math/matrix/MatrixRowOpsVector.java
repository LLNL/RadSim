/*
 * Copyright 2016, Lawrence Livermore National Security, LLC. 
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.math.matrix;

import gov.llnl.math.DoubleArray;
import gov.llnl.utility.annotation.Debug;
import gov.llnl.utility.annotation.Internal;

/**
 *
 * @author nelson85
 */
@Internal
class MatrixRowOpsVector implements MatrixRowOperations
{
  double[] data;
  int offset;

  @Debug
  public Matrix getObj()
  {
    return MatrixFactory.wrapColumnVector(DoubleArray.copyOfRange(data, offset, data.length));
  }

  public MatrixRowOpsVector(double[] values, int offset)
  {
    this.data = values;
    this.offset = offset;
  }

  @Override
  public void addScaledRows(int r1, int r2, double scalar)
  {
    data[r1 + offset] += scalar * data[r2 + offset];
  }

  @Override
  public void swapRows(int r1, int r2)
  {
    double tmp = data[r1 + offset];
    data[r1 + offset] = data[r2 + offset];
    data[r2 + offset] = tmp;
  }

  @Override
  public void multiplyAssignRow(int row, double scalar)
  {
    data[row + offset] *= scalar;
  }

  @Override
  public void divideAssignRow(int row, double scalar)
  {
    data[row + offset] /= scalar;
  }

  @Override
  public void apply()
  {
    // Not used.  Applied as called.
  }

}
