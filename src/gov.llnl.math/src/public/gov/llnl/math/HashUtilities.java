/* 
 * Copyright 2016, Lawrence Livermore National Security, LLC.
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.math;

/**
 *
 * @author nelson85
 */
public class HashUtilities
{
  static public int HASH_FUNCTION = 0xdeece66d;
  static public int HASH_OFFSET = 0x3301342;

  static public int hash(int seed, byte bytes)
  {
    return seed * HASH_FUNCTION + HASH_OFFSET + bytes;
  }

  static public int hash(int seed, byte[] bytes)
  {
    for (int i = 0; i < bytes.length; i++)
    {
      seed = seed * HASH_FUNCTION + HASH_OFFSET + bytes[i];
    }
    return seed;
  }

  static public int hash(int seed, int d)
  {
    return HashUtilities.hash(HashUtilities.hash(HashUtilities.hash(HashUtilities.hash(seed,
            (byte) (d & 0xff)),
            (byte) ((d >> 8) & 0xff)),
            (byte) ((d >> 16) & 0xff)),
            (byte) ((d >> 24) & 0xff));
  }

  static public int hash(int seed, long d)
  {
    for (int i = 0; i < 8; i++)
      seed = HashUtilities.hash(seed, (byte) ((d >> (i << 3)) & 0xff));
    return seed;
  }

  static public int hash(int seed, double d)
  {
    return HashUtilities.hash(seed, Double.doubleToLongBits(d));
  }

  static public int hash(int seed, double d[], int length)
  {
    for (int i = 0; i < length; i++)
      seed = hash(seed, d[i]);
    return seed;
  }

  static public <T> int hash(int seed, T[] in)
  {
    for (T i : in)
      seed = hash(seed, i.hashCode());
    return seed;
  }
}
