/*
 * Copyright 2016, Lawrence Livermore National Security, LLC.
 * All rights reserved
 *
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.math.matrix;

import static gov.llnl.math.matrix.MatrixBlockOps.BLOCKSIZE;
import gov.llnl.math.matrix.MatrixBlockOps.BlockOperationFactory;
import gov.llnl.math.matrix.MatrixBlockOps.ColumnOperationFactory;
import static gov.llnl.math.matrix.MatrixBlockOps.MAXBLOCKSIZE;
import gov.llnl.math.matrix.MatrixBlockOps.RowOperationFactory;
import static gov.llnl.math.matrix.MatrixBlockOps.blockOperation;
import gov.llnl.math.parallel.ParallelProcessor2;
import static java.lang.Math.random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

/**
 * Implementation of block assignment operations. The most painful thing in
 * profiling destination matrix code is seeing that destination huge amount of
 * time is being eaten in simply copying of data. To try to improve conditions,
 * I am implementing block copy capabilities. These methods are internal and
 * have no verification of the target size nor any other safety feature.
 * <p>
 * It is still unclear if this is the best formulation. Once I can decide what
 * is best I can optimize it and then generalize it for use on the other
 * assignment operations. Accurate benchmarking in Java remains a problem as run
 * to run variablity is high, and the just in time compiler is very inconsistent
 * as to when and what it decides to optimize. Need to put together a more
 * formal benchmarking capability that runs multiple times and takes the median.
 *
 * @author nelson85
 */
public class MatrixOpAssign
{

  // FIXME NO USAGE!
  
  // Block copy to improve speed of copy operations
  public static void assignCache(Matrix.ColumnAccess a, Matrix.RowAccess b)
  {
    final int blocksize = 32;
    int[] ao = new int[blocksize];
    double[][] ac = new double[blocksize][];

    int n = a.rows();
    int m = b.columns();
    for (int i = 0; i < n; i += blocksize) // rows
    {
      int endk = Math.min(i + blocksize, n);
      for (int j = 0; j < m; j += blocksize) // columns
      {
        int endl = Math.min(j + blocksize, m);
        for (int l = j; l < endl; ++l) // columns
        {
          ac[l - j] = a.accessColumn(l);
          ao[l - j] = a.addressColumn(l);
        }

        for (int k = i; k < endk; ++k) // rows
        {
          double[] brk = b.accessRow(k);
          int bro = b.addressRow(k) + j;
          for (int l = j; l < endl; ++l, ++bro) // columns
          {
            ac[l - j][k + ao[l - j]] = brk[bro];
          }
        }
      }
    }
  }

  public static <MatrixType extends Matrix.ColumnAccess>
          MatrixType assign(MatrixType destination, Matrix source)
  {
    if (source instanceof Matrix.RowAccess)
      return (MatrixType) blockOperation(destination, (Matrix.RowAccess) source,
              new CopyBlockColumnRowFactory());
    return (MatrixType) blockOperation(destination, source, new CopyBlockColumnColumnFactory());
  }

  public static <MatrixType extends Matrix.RowAccess>
          MatrixType assign(MatrixType destination, Matrix source)
  {
    if (source instanceof Matrix.ColumnAccess)
      return (MatrixType) blockOperation(destination, (Matrix.ColumnAccess) source,
              new CopyBlockRowColumnFactory());
    return (MatrixType) blockOperation(destination, source, new CopyBlockRowRowFactory());
  }

  // Block copy to improve speed of copy operations
  public static <MatrixType extends Matrix.ColumnAccess>
          MatrixType assignColumnRow(MatrixType destination, Matrix.RowAccess source)
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
          context.delegate(new CopyBlockColumnRow(destination, source, row, rowEnd, column, columnEnd));
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

//<editor-fold desc="internal">
  public static class CopyBlockColumnColumnFactory implements ColumnOperationFactory<Matrix.ColumnAccess, Matrix>
  {
    @Override
    public Callable create(Matrix.ColumnAccess destination, Matrix source, int columnBegin, int columnEnd)
    {
      return new CopyBlockColumnColumn(destination, source, columnBegin, columnEnd);
    }
  }

  public static class CopyBlockColumnColumn implements Callable
  {
    Matrix.ColumnAccess destination;
    Matrix source;
    int columnBegin;
    int columnEnd;

    CopyBlockColumnColumn(Matrix.ColumnAccess destination,
            Matrix source,
            int columnBegin,
            int columnEnd
    )
    {
      this.destination = destination;
      this.source = source;
      this.columnBegin = columnBegin;
      this.columnEnd = columnEnd;
    }

    @Override
    public Object call() throws Exception
    {
      for (int i = columnBegin; i < columnEnd; ++i)
      {
        source.copyColumnTo(destination.accessColumn(i), destination.addressColumn(i), i);
      }
      return null;
    }
  }

  public static class CopyBlockRowRowFactory
          implements RowOperationFactory<Matrix.RowAccess, Matrix>
  {
    @Override
    public Callable create(Matrix.RowAccess destination, Matrix source, int begin, int end)
    {
      return new CopyBlockRowRow(destination, source, begin, end);
    }
  }

  public static class CopyBlockRowRow implements Callable
  {
    Matrix.RowAccess a;
    Matrix b;
    int rowStart;
    int rowEnd;

    CopyBlockRowRow(Matrix.RowAccess a,
            Matrix b,
            int rowStart,
            int rowEnd
    )
    {
      this.a = a;
      this.b = b;
      this.rowStart = rowStart;
      this.rowEnd = rowEnd;
    }

    @Override
    public Object call() throws Exception
    {
      for (int i = rowStart; i < rowEnd; ++i)
      {
        b.copyRowTo(a.accessRow(i), a.addressRow(i), i);
      }
      return null;
    }
  }

  public static class CopyBlockColumnRowFactory
          implements BlockOperationFactory<Matrix.ColumnAccess, Matrix.RowAccess>
  {
    @Override
    public Callable create(Matrix.ColumnAccess destination, Matrix.RowAccess source,
            int rowBegin, int rowEnd,
            int columnBegin, int columnEnd)
    {
      return new CopyBlockColumnRow(destination, source, rowBegin, rowEnd, columnBegin, columnEnd);
    }
  }

  public static class CopyBlockColumnRow implements Callable
  {
    Matrix.ColumnAccess a;
    Matrix.RowAccess b;
    int rowBegin;
    int rowEnd;
    int columnBegin;
    int columnEnd;

    CopyBlockColumnRow(Matrix.ColumnAccess a,
            Matrix.RowAccess b,
            int rowBegin,
            int rowEnd,
            int columnBegin,
            int columnEnd
    )
    {
      this.a = a;
      this.b = b;
      this.rowBegin = rowBegin;
      this.rowEnd = rowEnd;
      this.columnBegin = columnBegin;
      this.columnEnd = columnEnd;
    }

    @Override
    public Object call() throws Exception
    {
      int columnLength = columnEnd - columnBegin;
      int[] ao = new int[columnLength];
      double[][] ac = new double[columnLength][];

      for (int i0 = 0; i0 < columnLength; ++i0) // columns
      {
        ac[i0] = a.accessColumn(i0 + columnBegin);
        ao[i0] = a.addressColumn(i0 + columnBegin);
      }

      for (int i0 = rowBegin; i0 < rowEnd; ++i0) // rows
      {
        double[] brk = b.accessRow(i0);
        int i2 = b.addressRow(i0) + columnBegin;
        int i3 = 0;
        for (int i1 = columnBegin; i1 < columnEnd; ++i1, ++i2, ++i3) // columns
        {
          ac[i3][i0 + ao[i3]] = brk[i2];
        }
      }
      return null;
    }
  }

  public static class CopyBlockRowColumnFactory
          implements BlockOperationFactory<Matrix.RowAccess, Matrix.ColumnAccess>
  {
    @Override
    public Callable create(Matrix.RowAccess destination, Matrix.ColumnAccess source,
            int rowBegin, int rowEnd,
            int columnBegin, int columnEnd)
    {
      return new CopyBlockRowColumn(destination, source, rowBegin, rowEnd, columnBegin, columnEnd);
    }
  }

  public static class CopyBlockRowColumn implements Callable
  {
    Matrix.RowAccess a;
    Matrix.ColumnAccess b;
    int rowBegin;
    int rowEnd;
    int columnBegin;
    int columnEnd;

    CopyBlockRowColumn(Matrix.RowAccess a,
            Matrix.ColumnAccess b,
            int rowStart,
            int rowEnd,
            int columnBegin,
            int columnEnd
    )
    {
      this.a = a;
      this.b = b;
      this.rowBegin = rowStart;
      this.rowEnd = rowEnd;
      this.columnBegin = columnBegin;
      this.columnEnd = columnEnd;
    }

    @Override
    public Object call() throws Exception
    {
      try
      {
        int rowLength = rowEnd - rowBegin;
        int[] ao = new int[rowLength];
        double[][] ac = new double[rowLength][];

        for (int i0 = 0; i0 < rowLength; ++i0) // rows
        {
          ac[i0] = a.accessRow(i0 + rowBegin);
          ao[i0] = a.addressRow(i0 + rowBegin);
        }

        for (int i0 = columnBegin; i0 < columnEnd; ++i0) // columns
        {
          double[] brk = b.accessColumn(i0);
          int i2 = b.addressColumn(i0) + rowBegin;
          int i3 = 0;
          for (int i1 = rowBegin; i1 < rowEnd; ++i1, ++i2, ++i3) // rows
          {
            ac[i3][i0 + ao[i3]] = brk[i2];
          }
        }
        return null;
      }
      catch (IndexOutOfBoundsException ex)
      {
        System.out.println("Fail in block " + rowBegin + " " + rowEnd + " " + columnBegin + " " + columnEnd);
        throw ex;
      }
    }
  }
//</editor-fold>

  static public void main(String[] args)
  {
    MatrixRowTable in1 = new MatrixRowTable(503, 503);
    for (int i = 0; i < in1.rows(); ++i)
      for (int j = 0; j < in1.columns(); ++j)
        in1.set(i, j, random());
    MatrixColumnTable in2 = new MatrixColumnTable(in1);

    MatrixRowTable out1 = new MatrixRowTable(in1.rows(), in1.columns());
    MatrixColumnTable out2 = new MatrixColumnTable(in1.rows(), in1.columns());

    System.out.println("Check");
    check(out1, out2, in1, in2);

    warmup(out1, out2, in1, in2);

    {
      long begin = System.currentTimeMillis();
      speed1(out2, in1, 10000);
      long end = System.currentTimeMillis();
      System.out.println("speed 1 - default " + (end - begin) / 1000.0);
    }
    {
      long begin = System.currentTimeMillis();
      speed3(out2, in1, 10000);
      long end = System.currentTimeMillis();
      System.out.println("speed 3 - assign dedicated " + (end - begin) / 1000.0);
    }

    {
      long begin = System.currentTimeMillis();
      speed2(out2, in1, 10000);
      long end = System.currentTimeMillis();
      System.out.println("speed 2 - assign " + (end - begin) / 1000.0);
    }

    System.out.println("done");
  }

  private static void speed1(MatrixColumnTable out1, MatrixRowTable in, int sz)
  {
    for (int i = 0; i < sz; ++i)
      out1.assign(in);
  }

  private static void speed2(MatrixColumnTable out2, MatrixRowTable in, int sz)
  {
    for (int i = 0; i < sz; ++i)
      assign(out2, in);
  }

  private static void speed3(MatrixColumnTable out2, MatrixRowTable in, int sz)
  {
    for (int i = 0; i < sz; ++i)
      assignColumnRow(out2, in);
  }

  private static void warmup(MatrixRowTable out1, MatrixColumnTable out2, MatrixRowTable in1, MatrixColumnTable in2)
  {
    for (int i = 0; i < 200; ++i)
    {
      out2.assign(in1);
      assign(out1, in1);
      assign(out2, in1);
      assign(out1, in2);
      assign(out2, in2);
      assignColumnRow(out2, in1);
    }
  }

  /**
   * Verify all the operations are functional.
   *
   * @param out1
   * @param out2
   * @param in1
   * @param in2
   */
  private static void check(MatrixRowTable out1, MatrixColumnTable out2, MatrixRowTable in1, MatrixColumnTable in2)
  {
    assignColumnRow(out2, in1);
    if (MatrixOps.equivalent(out2, in1))
      System.out.println("pass direct");
    else
      System.out.println("fail direct");

    MatrixOps.fill(out2, 0);
    assign(out2, in1);
    if (MatrixOps.equivalent(out2, in1))
      System.out.println("pass column row");
    else
      System.out.println("fail column row");

    MatrixOps.fill(out1, 0);
    assign(out1, in1);
    if (MatrixOps.equivalent(out1, in1))
      System.out.println("pass row row");
    else
      System.out.println("fail row row");

    MatrixOps.fill(out2, 0);
    assign(out2, in2);
    if (MatrixOps.equivalent(out2, in2))
      System.out.println("pass column column");
    else
      System.out.println("fail column column");

    MatrixOps.fill(out1, 0);
    assign(out1, in2);
    if (MatrixOps.equivalent(out1, in2))
      System.out.println("pass row column");
    else
      System.out.println("fail row column");
  }
}
