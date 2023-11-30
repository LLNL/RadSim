/*
 * Copyright 2016, Lawrence Livermore National Security, LLC. 
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.math.stream;

import java.util.function.IntSupplier;

/**
 *
 * @author nelson85
 */
public class IntGenerator
{
  public static IntSupplier sequence(int initial)
  {
    int[] v = new int[]{initial};
    return ()->v[0]++;
  }
  
}
