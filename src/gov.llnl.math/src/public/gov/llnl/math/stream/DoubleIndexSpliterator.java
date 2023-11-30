/* 
 * Copyright 2019, Lawrence Livermore National Security, LLC.
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.math.stream;

/**
 *
 * @author nelson85
 */
public interface DoubleIndexSpliterator
{
  int characteristics();

  long estimateSize();

  DoubleIndexSpliterator trySplit();

  boolean tryAdvance(DoubleIndexConsumer action);

  boolean tryAdvanceReverse(DoubleIndexConsumer action);

  void range(int begin, int end);

  @SuppressWarnings("empty-statement")
  default void forEachRemaining(DoubleIndexConsumer action)
  {
    while (tryAdvance(action));
  }

  @SuppressWarnings("empty-statement")
  default void forEachRemainingReverse(DoubleIndexConsumer action)
  {
    while (tryAdvanceReverse(action));
  }
}
