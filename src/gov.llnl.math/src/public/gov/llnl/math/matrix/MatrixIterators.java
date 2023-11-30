/*
 * Copyright 2016, Lawrence Livermore National Security, LLC. 
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.math.matrix;

import gov.llnl.math.MathExceptions.WriteAccessException;
import gov.llnl.math.matrix.Matrix.ColumnAccess;
import gov.llnl.math.matrix.Matrix.ColumnReadAccess;
import gov.llnl.math.matrix.Matrix.RowAccess;
import gov.llnl.math.matrix.Matrix.RowReadAccess;

/**
 * Specialized iterators for processing matrices by row or column operations. If
 * the matrix does not provide the requested access, the iterator will back the
 * operations with a memory.
 *
 * <pre>
 * {@code
 *
 * void work(Matrix m)
 * {
 * VectorIterator iter = MatrixIterators.newColumnReadIterator(m);
 * while (iter.hasNext())
 * {
 * double[] data = iter.access();
 * int begin = cri.begin();
 * // Use the column
 * }
 * }
 *
 * }
 * </pre>
 *
 * @author nelson85
 */
public class MatrixIterators
{

  @SuppressWarnings("unchecked")
  static public VectorIterator newColumnReadIterator(Matrix m)
  {
    if (m instanceof ColumnReadAccess)
      return new ColumnIterator((ColumnReadAccess) m);
    return new ColumnReadCacheIterator(m);
  }

  @SuppressWarnings("unchecked")
  static public VectorIterator newColumnWriteIterator(Matrix m) throws WriteAccessException
  {
    m.mutable();
    if (m instanceof ColumnAccess)
      return new ColumnIterator((ColumnAccess) m);
    if (!(m instanceof Matrix.WriteAccess))
      throw new UnsupportedOperationException();
    return new ColumnWriteCacheIterator((Matrix.WriteAccess) m);
  }

  @SuppressWarnings("unchecked")
  static public VectorIterator newColumnDualIterator(Matrix m) throws WriteAccessException
  {
    m.mutable();
    if (m instanceof ColumnAccess)
      return new ColumnIterator((ColumnAccess) m);
    if (!(m instanceof Matrix.WriteAccess))
      throw new UnsupportedOperationException();
    return new ColumnDualCacheIterator((Matrix.WriteAccess) m);
  }

  @SuppressWarnings("unchecked")
  static public VectorIterator newRowReadIterator(Matrix m)
  {
    if (m instanceof RowReadAccess)
      return new RowIterator((RowReadAccess) m);
    return new RowReadCacheIterator(m);
  }

  @SuppressWarnings("unchecked")
  static public VectorIterator newRowWriteIterator(Matrix m) throws WriteAccessException
  {
    m.mutable();
    if (m instanceof RowAccess)
      return new RowIterator((RowAccess) m);
    if (!(m instanceof Matrix.WriteAccess))
      throw new UnsupportedOperationException();
    return new RowWriteCacheIterator((Matrix.WriteAccess) m);
  }

  @SuppressWarnings("unchecked")
  static public VectorIterator newRowDualIterator(Matrix m) throws WriteAccessException
  {
    m.mutable();
    if (m instanceof RowAccess)
      return new RowIterator((RowAccess) m);
    if (!(m instanceof Matrix.WriteAccess))
      throw new UnsupportedOperationException();
    return new RowDualCacheIterator((Matrix.WriteAccess) m);
  }

  /**
   * Accessor for processing a matrix as a series of vectors.
   *
   */
  public static abstract class VectorIterator
  {
    final protected int memoryLength;
    final protected int steps;

    // State variables
    protected double[] memory = null;
    protected int memoryOffset;
    protected int iterationIndex = -1;

    protected VectorIterator(int length, int steps)
    {
      this.memoryLength = length;
      this.memoryOffset = 0;
      this.steps = steps;
    }

    /**
     * Get the memory block to access the hasNext iteration.
     *
     * @return the memory block to be iterated or null if the iterator is no
     * longer valid.
     */
    public final double[] access()
    {
      return memory;
    }

    /**
     * Get the begin of the memory block in the iteration.
     *
     * @return the begin in the current memory.
     */
    public final int begin()
    {
      return memoryOffset;
    }

    /**
     * Get the end of the memory block in the iteration.
     *
     * @return the begin in the current memory.
     */
    public final int end()
    {
      return memoryOffset + memoryLength;
    }

    /**
     * Length of the current memory block to work on.
     *
     * @return the length of valid items in the current memory block.
     */
    public final int length()
    {
      return memoryLength;
    }

    /**
     * Index of the current access. Will be either a row or column number
     * depending on type of iterator.
     *
     * @return the index of the current iteration in the matrix.
     */
    public final int index()
    {
      return iterationIndex;
    }

    public final int steps()
    {
      return steps;
    }

    /**
     * Advances the iterator to the hasNext item. This will copy back all write
     * caches and fetch the hasNext read memory. All data is written when then
     * function returns hasNext. Calling hasNext after the last element will not
     * cause any issues. When multiple matrix are being written it is important
     * to call hasNext on each after the loop to ensure all caches are written
     * back.
     *
     * @return true if there are additional columns to process.
     */
    public abstract boolean advance();

    /** Access the memory. 
     * 
     * Index is relative to the beginning of the memory.
     * 
     * @param i
     * @return 
     */
    public double get(int i)
    {
      return this.access()[this.begin() + i];
    }
  }

//<editor-fold desc="internal" defaultstate="collapsed">
  final private static class ColumnIterator<Type extends ColumnReadAccess> extends VectorIterator
  {
    ColumnReadAccess matrix;

    public ColumnIterator(Type matrix)
    {
      super(matrix.rows(), matrix.columns());
      this.matrix = (ColumnReadAccess) matrix;
    }

    @Override
    public boolean advance()
    {
      ++iterationIndex;
      if (iterationIndex < steps)
      {
        memory = matrix.accessColumn(iterationIndex);
        memoryOffset = matrix.addressColumn(iterationIndex);
        return true;
      }
      memory = null;
      return false;
    }
  }

  final private static class ColumnReadCacheIterator extends VectorIterator
  {
    Matrix matrix;

    public ColumnReadCacheIterator(Matrix matrix)
    {
      super(matrix.rows(), matrix.columns());
      this.matrix = matrix;
      memory = new double[memoryLength];
    }

    @Override
    public boolean advance()
    {
      ++iterationIndex;
      if (iterationIndex < steps)
      {
        matrix.copyColumnTo(memory, 0, iterationIndex);
        return true;
      }
      memory = null;
      return false;
    }
  }

  final private static class ColumnWriteCacheIterator extends VectorIterator
  {
    Matrix.WriteAccess matrix;

    public ColumnWriteCacheIterator(Matrix.WriteAccess matrix)
    {
      super(matrix.rows(), matrix.columns());
      this.matrix = matrix;
      memory = new double[memoryLength];
    }

    @Override
    public boolean advance()
    {
      try
      {
        if (memory == null)
          return false;
        if (iterationIndex >= 0)
          matrix.assignColumn(memory, iterationIndex);
        ++iterationIndex;
        if (iterationIndex < steps)
          return true;
        memory = null;
        return false;
      }
      catch (WriteAccessException ex)
      {
        throw new RuntimeException(ex); // Checked at creation
      }
    }
  }

  final private static class ColumnDualCacheIterator extends VectorIterator
  {
    Matrix.WriteAccess matrix;

    public ColumnDualCacheIterator(Matrix.WriteAccess matrix)
    {
      super(matrix.rows(), matrix.columns());
      this.matrix = matrix;
      memory = new double[memoryLength];
    }

    @Override
    public boolean advance()
    {
      try
      {
        if (memory == null)
          return false;
        if (iterationIndex >= 0)
          matrix.assignColumn(memory, iterationIndex);
        ++iterationIndex;
        if (iterationIndex < steps)
        {
          matrix.copyColumnTo(memory, 0, iterationIndex);
          return true;
        }
        memory = null;
        return false;
      }
      catch (WriteAccessException ex)
      {
        throw new RuntimeException(ex); // Checked at creation
      }
    }
  }

  final private static class RowIterator<Type extends RowReadAccess> extends VectorIterator
  {
    Type matrix;

    public RowIterator(Type matrix)
    {
      super(matrix.columns(), matrix.rows());
      this.matrix = matrix;
    }

    @Override
    public boolean advance()
    {
      ++iterationIndex;
      if (iterationIndex < steps)
      {
        memory = matrix.accessRow(iterationIndex);
        memoryOffset = matrix.addressRow(iterationIndex);
        return true;
      }
      memory = null;
      return false;
    }
  }

  final private static class RowReadCacheIterator extends VectorIterator
  {
    Matrix matrix;

    public RowReadCacheIterator(Matrix matrix)
    {
      super(matrix.columns(), matrix.rows());
      this.matrix = matrix;
      memory = new double[memoryLength];
    }

    @Override
    public boolean advance()
    {
      ++iterationIndex;
      if (iterationIndex < steps)
      {
        matrix.copyRowTo(memory, 0, iterationIndex);
        return true;
      }
      memory = null;
      return false;
    }
  }

  final private static class RowWriteCacheIterator extends VectorIterator
  {
    Matrix.WriteAccess matrix;

    public RowWriteCacheIterator(Matrix.WriteAccess matrix)
    {
      super(matrix.columns(), matrix.rows());
      this.matrix = matrix;
      memory = new double[memoryLength];
    }

    @Override
    public boolean advance()
    {
      try
      {
        if (memory == null)
          return false;
        if (iterationIndex >= 0)
          matrix.assignRow(memory, iterationIndex);
        ++iterationIndex;
        if (iterationIndex < steps)
          return true;
        memory = null;
        return false;
      }
      catch (WriteAccessException ex) // Checked at creation
      {
        throw new RuntimeException(ex);
      }
    }
  }

  final private static class RowDualCacheIterator extends VectorIterator
  {
    Matrix.WriteAccess matrix;

    public RowDualCacheIterator(Matrix.WriteAccess matrix)
    {
      super(matrix.columns(), matrix.rows());
      this.matrix = matrix;
      memory = new double[memoryLength];
    }

    @Override
    public boolean advance()
    {
      try
      {
        if (memory == null)
          return false;
        if (iterationIndex >= 0)
          matrix.assignRow(memory, iterationIndex);
        ++iterationIndex;
        if (iterationIndex < steps)
        {
          matrix.copyRowTo(memory, 0, iterationIndex);
          return true;
        }
        memory = null;
        return false;
      }
      catch (WriteAccessException ex)
      {
        throw new RuntimeException(ex);  // Checked at creation
      }
    }
  }
//</editor-fold>
}
