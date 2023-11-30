/*
 * Copyright 2022, Lawrence Livermore National Security, LLC.
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.nist.physics.n42.utility;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author her1
 */
public class SpectrumUtilities
{
  /**
   * Unpacks compressed array with CountedZeros algorithm.
   *
   * When a “0” value appears in the ChannelData contents, the next value is the
   * number of consecutive zero-value channels beginning with the first
   * zero-value in the sequence.
   *
   * @return Uncompressed array
   */
  public static double[] unpackCountedZeros(double[] compressed)
  {
    List<Double> unpacked = new ArrayList<>();
    for (int i = 0; i < compressed.length; i++)
    {
      if (compressed[i] != 0)
      {
        unpacked.add(compressed[i]);
      }
      else
      {
        for (int j = 0; j < compressed[i + 1]; j++)
        {
          unpacked.add(Double.valueOf(0));
        }
        i++;
      }
    }
    return unpacked.stream().mapToDouble(Double::doubleValue).toArray();
  }
  
  public static double[] packCountedZeros(double[] uncompressed)
  {
    List<Double> packed = new ArrayList<>();
    int zeros = 0;
    for (int i = 0; i < uncompressed.length; i++)
    {
      if (uncompressed[i] == 0)
      {
        zeros++;
      }
      else
      {
        if(zeros > 0)
        {
          packed.add(Double.valueOf(0));
          packed.add(Double.valueOf(zeros));
          zeros = 0;
        }
        
        packed.add(uncompressed[i]); 
      }
    }
    
    if(zeros > 0)
    {
      packed.add(Double.valueOf(0));
      packed.add(Double.valueOf(zeros));
    }
    
    return packed.stream().mapToDouble(Double::doubleValue).toArray();
  }
}
