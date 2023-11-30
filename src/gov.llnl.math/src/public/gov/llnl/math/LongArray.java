/*
 * Copyright 2016, Lawrence Livermore National Security, LLC. 
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.math;

import java.util.Collection;

/**
 *
 * @author nelson85
 */
public class LongArray
{

  public static long[] toPrimitives(Collection<Long> in)
  {
    int i = 0;
    long out[] = new long[in.size()];
    for (Long f : in)
      out[i++] = f;
    return out;
  }

}
