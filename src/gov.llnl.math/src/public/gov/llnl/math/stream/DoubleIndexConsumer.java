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
@FunctionalInterface
public interface DoubleIndexConsumer
{
  void accept(int index, double value);
}
