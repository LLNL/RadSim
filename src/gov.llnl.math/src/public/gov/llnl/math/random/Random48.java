/* 
 * Copyright 2016, Lawrence Livermore National Security, LLC.
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.math.random;

import gov.llnl.utility.UUIDUtilities;
import java.io.Serializable;

/**
 * This is the standard random algorithm used in Unix
 *
 * @author nelson85
 */
public final class Random48 implements RandomGenerator, Serializable
{
  private static final long serialVersionUID = UUIDUtilities.createLong("Random48");
  static final long A_SEED = 0x0005deece66dL;
  static final long C_SEED = 0x00000000000bL;
  long r_ = 0x330e;
  long a_ = 0x0005deece66dL;
  long c_ = 0x00000000000bL;

  public Random48()
  {
    setSeed(System.currentTimeMillis());
  }

  public Random48(long s)
  {
    setSeed(s);
  }

  public Random48(long s, long a, long c)
  {
    this.setCongruential(s, a, c);
  }

  public void setCongruential(long s, long a, long c)
  {
    synchronized (this)
    {
      a_ = a & 0x0000FFFFFFFFFFFFL;
      c_ = c & 0xFFFF;
      r_ = s & 0x0000FFFFFFFFFFFFL;
    }
  }

  // srand48
  @Override
  public void setSeed(long i)
  {
    synchronized (this)
    {
      a_ = A_SEED;
      c_ = C_SEED;
      r_ = ((i * 0xffffffff) << 16) + 0x330e;
    }
  }

  // mrand48
  @Override
  public int nextInt()
  {
    synchronized (this)
    {
      r_ = (a_ * r_ + c_);
      r_ &= 0xffffffffffffL;
      return (int) (r_ >> 16);
    }
  }

  // lrand48
  public int nextUnsignedInt()
  {
    synchronized (this)
    {
      r_ = (a_ * r_ + c_);
      r_ &= 0xffffffffffffL;
      return (int) (r_ >> 17);
    }
  }

  // drand48
  @Override
  public double nextDouble()
  {
    nextInt();
    double out = r_;
    out /= 281474976710656.0;
    return out;
  }

//  public static void main(String[] args)
//  {
//    Random48 r = new Random48();
//    System.out.println("a=" + r.a_);
//    System.out.println("c=" + r.c_);
//    System.out.println("r=" + r.r_);
//    for (int i = 0; i < 10; ++i)
//    {
//      System.out.println(r.randomInt());
//    }
//
//    r.setSeed(0);
//    for (int i = 0; i < 10; ++i)
//    {
//      System.out.println(r.nextDouble());
//    }
//  }
}
