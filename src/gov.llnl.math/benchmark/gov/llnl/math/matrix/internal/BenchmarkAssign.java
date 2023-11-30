/*
 * Copyright 2016, Lawrence Livermore National Security, LLC. 
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.math.matrix.internal;

import gov.llnl.utility.Benchmarker;
import gov.llnl.math.matrix.MatrixColumnTable;
import gov.llnl.math.matrix.MatrixRowTable;
import gov.llnl.math.internal.matrix.MatrixOpAssign;
import static java.lang.Math.random;

/**
 *
 * @author nelson85
 */
public class BenchmarkAssign extends Benchmarker
{

  private class DefaultCopy implements Benchmarker.Task
  {
    @Override
    public void execute(int passes)
    {
      for (int i = 0; i < passes; ++i)
        outRow.assign(inRow);
    }
  }

  private class DefaultCopyTranspose implements Benchmarker.Task
  {
    @Override
    public void execute(int passes)
    {
      for (int i = 0; i < passes; ++i)
        outColumn.assign(inRow);
    }
  }

  private class BlockCopy implements Benchmarker.Task
  {
    @Override
    public void execute(int passes)
    {
      for (int i = 0; i < passes; ++i)
        MatrixOpAssign.assign(outRow, inRow);
    }
  }

  private class BlockCopyTranspose implements Benchmarker.Task
  {
    @Override
    public void execute(int passes)
    {
      for (int i = 0; i < passes; ++i)
        MatrixOpAssign.assign(outColumn, inRow);
    }
  }

  final static int ROWS = 503;
  final static int COLUMNS = 499;

  MatrixRowTable inRow;
  MatrixColumnTable inColumn;

  MatrixRowTable outRow;
  MatrixColumnTable outColumn;

  BenchmarkAssign()
  {
    inRow = new MatrixRowTable(ROWS, COLUMNS);
    for (int i = 0; i < inRow.rows(); ++i)
      for (int j = 0; j < inRow.columns(); ++j)
        inRow.set(i, j, random());
    inColumn = new MatrixColumnTable(inRow);

    outRow = new MatrixRowTable(inRow.rows(), inRow.columns());
    outColumn = new MatrixColumnTable(inRow.rows(), inRow.columns());

    this.addTask("default copy ", new DefaultCopy());
    this.addTask("default copy transpose", new DefaultCopyTranspose());
    this.addTask("block copy", new BlockCopy());
    this.addTask("block copy transpose", new BlockCopyTranspose());
  }

  static public void main(String[] args)
  {
    BenchmarkAssign benchmark = new BenchmarkAssign();
    benchmark.test();
  }
}
