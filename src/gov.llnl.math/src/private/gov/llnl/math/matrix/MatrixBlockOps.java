/*
 * Copyright 2016, Lawrence Livermore National Security, LLC. 
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.math.matrix;

import gov.llnl.math.parallel.ParallelProcessor2;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

/**
 *
 * @author nelson85
 */
class MatrixBlockOps
{
  public static final int BLOCKSIZE = 32;
  public static final int MAXBLOCKSIZE = 48;

  /**
   * Interface for creating block operations that delegate actions by blocks.
   *
   * @param <Matrix1>
   * @param <Matrix2>
   */
  public interface BlockOperationFactory<Matrix1 extends Matrix, Matrix2 extends Matrix>
  {
    Callable create(Matrix1 destination, Matrix2 source, int beginRow, int rowEnd, int columnBegin, int columnEnd);
  }

  /**
   * Interface for creating block operations that delegate actions by columns.
   *
   * @param <Matrix1>
   * @param <Matrix2>
   */
  public interface ColumnOperationFactory<Matrix1 extends Matrix, Matrix2 extends Matrix>
  {
    Callable create(Matrix1 destination, Matrix2 source, int columnBegin, int columnEnd);
  }

  /**
   * Interface for creating block operations that delegate actions by columns.
   *
   * @param <Matrix1>
   * @param <Matrix2>
   */
  public interface RowOperationFactory<Matrix1 extends Matrix, Matrix2 extends Matrix>
  {
    Callable create(Matrix1 destination, Matrix2 source, int beginRow, int rowEnd);
  }

  public static <MatrixType1 extends Matrix, MatrixType2 extends Matrix>
          MatrixType1 blockOperation(MatrixType1 destination, MatrixType2 source,
                  BlockOperationFactory<MatrixType1, MatrixType2> opf)
  {
    try
    {
      ParallelProcessor2.ParallelContext context = ParallelProcessor2.newContext();

      int n = destination.rows();
      int m = source.columns();
      int row = 0;
      int column;
      while (row < n)
      {
        int rowEnd = n;
        if (row + MAXBLOCKSIZE < n)
          rowEnd = row + BLOCKSIZE;
        column = 0;
        while (column < m)
        {
          int columnEnd = m;
          if (column + MAXBLOCKSIZE < m)
            columnEnd = column + BLOCKSIZE;
          context.delegate(opf.create(destination, source, row, rowEnd, column, columnEnd));
          column = columnEnd;
        }
        row = rowEnd;
      }
      context.waitForAll();
    }
    catch (ExecutionException ex)
    {
      throw new RuntimeException(ex);
    }
    return destination;
  }

  public static <MatrixType1 extends Matrix, MatrixType2 extends Matrix>
          MatrixType1 blockOperation(MatrixType1 destination, MatrixType2 source,
                  ColumnOperationFactory<MatrixType1, MatrixType2> opf)
  {
    try
    {
      ParallelProcessor2.ParallelContext context = ParallelProcessor2.newContext();

      int m = context.size();
      int n = destination.columns();
      int step = (n / m);
      int excess = (n % m);

      int column = 0;
      while (column < n)
      {
        int columnEnd = column + step;
        if (excess > 0)
        {
          columnEnd++;
          excess--;
        }
        context.delegate(opf.create(destination, source, column, columnEnd));
        column = columnEnd;
      }
      context.waitForAll();
    }
    catch (ExecutionException ex)
    {
      throw new RuntimeException(ex);
    }
    return destination;
  }

  public static <MatrixType1 extends Matrix, MatrixType2 extends Matrix>
          MatrixType1 blockOperation(MatrixType1 destination, MatrixType2 source,
                  RowOperationFactory<MatrixType1, MatrixType2> opf)
  {
    try
    {
      ParallelProcessor2.ParallelContext context = ParallelProcessor2.newContext();

      int m = context.size();
      int n = destination.rows();
      int step = (n / m);
      int excess = (n % m);

      int row = 0;
      while (row < n)
      {
        int rowEnd = row + step;
        if (excess > 0)
        {
          rowEnd++;
          excess--;
        }
        context.delegate(opf.create(destination, source, row, rowEnd));
        row = rowEnd;
      }
      context.waitForAll();
    }
    catch (ExecutionException ex)
    {
      throw new RuntimeException(ex);
    }
    return destination;
  }

}
