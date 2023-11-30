/*
 * Copyright 2016, Lawrence Livermore National Security, LLC. 
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.math.matrix;

import gov.llnl.math.DoubleArray;
import gov.llnl.math.MathExceptions.ResizeException;
import gov.llnl.math.MathExceptions.SizeException;
import gov.llnl.math.MathExceptions.WriteAccessException;
import gov.llnl.math.matrix.MatrixIterators.VectorIterator;
import gov.llnl.utility.annotation.Internal;

/**
 * Support class with different matrix multiply implementations. Called by
 * MatrixOps.multiply. Implementations lack certain safety checks so must be
 * used with caution.
 *
 */
@Internal
public class MatrixOpMultiply
{
  public enum MultiplyPolicy
  {
    COLUMN_ACCUMULATE,
    COLUMN_INNER,
    ROW_ACCUMULATE,
    ROW_INNER,
  }

  static private int getMajorAxis(Matrix m)
  {
    if (m instanceof Matrix.ColumnAccess)
      return m.rows();
    if (m instanceof Matrix.RowAccess)
      return m.columns();
    return 1;
  }

  public static MultiplyPolicy determinePolicy(Matrix a, Matrix b)
  {
    int d1 = a.rows();
    int d2 = a.columns();
    int d3 = b.columns();

    // Find the largest working axis
    int major1 = getMajorAxis(a);
    int major2 = getMajorAxis(b);

    if (major1 == 1 && major2 == 1)
    {
      if (d2 > d1 && d2 > d3)
      {
        if (d1 >= d3)
          return MultiplyPolicy.ROW_INNER;
        return MultiplyPolicy.COLUMN_INNER;
      }
      if (d1 >= d3)
        return MultiplyPolicy.COLUMN_ACCUMULATE;
      return MultiplyPolicy.ROW_ACCUMULATE;
    }

    // Operate on the largest axis
    if (major1 >= major2)
    {
      // A has the largest axis
      if (a instanceof Matrix.ColumnAccess)
        return MultiplyPolicy.COLUMN_ACCUMULATE;

      if (a instanceof Matrix.RowAccess)
        return MultiplyPolicy.ROW_INNER;

      throw new UnsupportedOperationException();
    }
    else
    {
      // B has the largest axis
      if (b instanceof Matrix.RowAccess)
        return MultiplyPolicy.ROW_ACCUMULATE;

      if (b instanceof Matrix.ColumnAccess)
        return MultiplyPolicy.COLUMN_INNER;

      throw new UnsupportedOperationException();
    }
  }

  /**
   * Multiple column accumulator form. Prefers to write by column. Does not
   * verify if the inputs and the result are the same objects.
   *
   * @param r is the resulting matrix product. (prefer ColumnAccess)
   * @param a is the first operand.
   * @param b is the second operand. (prefer ColumnAccess)
   * @return the matrix product.
   * @throws ResizeException if the result matrix cannot be resized.
   * @throws SizeException if the inner dimension of the operands does not
   * match.
   * @throws WriteAccessException when the result matrix is read only.
   */
  public static Matrix multiplyColumnAccumulate(Matrix r,
          Matrix.ColumnAccess a, Matrix b)
          throws ResizeException, SizeException, WriteAccessException
  {
    MatrixAssert.assertColumnsEqualsRows(a, b);
    int d1 = a.rows();
    int d2 = a.columns();
    int d3 = b.columns();
    r.resize(d1, d3);
    VectorIterator iterR = MatrixIterators.newColumnWriteIterator(r);
    VectorIterator iterB = MatrixIterators.newColumnReadIterator(b);
    while (iterR.advance() & iterB.advance())
    {
      double[] rv = iterR.access();
      double[] bv = iterB.access();
      int ri = iterR.begin();
      int bi = iterB.begin();
      DoubleArray.fillRange(rv, ri, ri + d1, 0);
      for (int i = 0; i < d2; ++i)
        DoubleArray.addAssignScaled(rv, ri, a.accessColumn(i), a.addressColumn(i), d1, bv[bi + i]);
    }
    return r;
  }

  /**
   * Multiple row accumulator form. Prefers to write by row. Does not verify if
   * the inputs and the result are the same objects.
   *
   * @param r is the resulting matrix product. (prefer RowAccess)
   * @param a is the first operand.
   * @param b is the second operand. (prefer RowAccess)
   * @return the matrix product.
   * @throws ResizeException if the result matrix cannot be resized.
   * @throws SizeException if the inner dimension of the operands does not
   * match.
   * @throws WriteAccessException when the result matrix is read only.
   */
  public static Matrix multiplyRowAccumulate(Matrix r, Matrix a, Matrix.RowAccess b)
          throws ResizeException, SizeException, WriteAccessException
  {
    MatrixAssert.assertColumnsEqualsRows(a, b);
    int d1 = a.rows();
    int d2 = a.columns();
    int d3 = b.columns();
    r.resize(d1, d3);
    VectorIterator iterR = MatrixIterators.newRowWriteIterator(r);
    VectorIterator iterA = MatrixIterators.newRowReadIterator(a);
    while (iterR.advance() & iterA.advance())
    {
      double[] rv = iterR.access();
      double[] av = iterA.access();
      int ri = iterR.begin();
      int ai = iterA.begin();
      DoubleArray.fillRange(rv, ri, ri + d3, 0);
      for (int i = 0; i < d2; ++i)
        DoubleArray.addAssignScaled(rv, ri, b.accessRow(i), b.addressRow(i), d3, av[ai + i]);
    }
    return r;
  }

  /**
   * Multiple row inner form. Prefers to write by column. Does not verify if the
   * inputs and the result are the same objects.
   *
   * @param r is the resulting matrix product. (prefer ColumnAccess)
   * @param a is the first operand.
   * @param b is the second operand. (prefer ColumnAccess)
   * @return the matrix product.
   * @throws ResizeException if the result matrix cannot be resized.
   * @throws SizeException if the inner dimension of the operands does not
   * match.
   * @throws WriteAccessException when the result matrix is read only.
   */
  public static Matrix multiplyRowInner(Matrix r, Matrix.RowAccess a, Matrix b)
          throws ResizeException, SizeException, WriteAccessException
  {
    MatrixAssert.assertColumnsEqualsRows(a, b);
    int d1 = a.rows();
    int d2 = a.columns();
    int d3 = b.columns();
    r.resize(d1, d3);
    VectorIterator iterR = MatrixIterators.newColumnWriteIterator(r);
    VectorIterator iterB = MatrixIterators.newColumnReadIterator(b);
    while (iterR.advance() & iterB.advance())
    {
      @SuppressWarnings("MismatchedReadAndWriteOfArray")
      double[] rv = iterR.access();
      double[] bv = iterB.access();
      int ri = iterR.begin();
      int bi = iterB.begin();
      for (int i = 0; i < d1; ++i)
        rv[i + ri] = DoubleArray.multiplyInner(a.accessRow(i), a.addressRow(i), bv, bi, d2);
    }
    return r;
  }

  /**
   * Multiple column inner form. Prefers to write by column. Does not verify if
   * the inputs and the result are the same objects.
   *
   * @param r is the resulting matrix product. (prefer RowAccess)
   * @param a is the first operand.
   * @param b is the second operand. (prefer RowAccess)
   * @return the matrix product.
   * @throws ResizeException if the result matrix cannot be resized.
   * @throws SizeException if the inner dimension of the operands does not
   * match.
   * @throws WriteAccessException when the result matrix is read only.
   */
  public static Matrix multiplyColumnInner(Matrix r, Matrix a, Matrix.ColumnAccess b)
          throws ResizeException, SizeException, WriteAccessException
  {
    MatrixAssert.assertColumnsEqualsRows(a, b);
    int d1 = a.rows();
    int d2 = a.columns();
    int d3 = b.columns();
    if (d2 != b.rows())
      throw new SizeException("Incorrect inner size");
    r.resize(d1, d3);
    VectorIterator iterR = MatrixIterators.newRowWriteIterator(r);
    VectorIterator iterA = MatrixIterators.newRowReadIterator(a);
    while (iterR.advance() & iterA.advance())
    {
      @SuppressWarnings("MismatchedReadAndWriteOfArray")
      double[] rv = iterR.access();
      double[] av = iterA.access();
      int ri = iterR.begin();
      int ai = iterA.begin();
      for (int i = 0; i < d3; ++i)
        rv[i + ri] = DoubleArray.multiplyInner(av, ai, b.accessColumn(i), b.addressColumn(i), d2);
    }
    return r;
  }
}
