/*
 * Copyright 2020, Lawrence Livermore National Security, LLC.
 * All rights reserved
 *
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.utility;

import java.util.Arrays;
import java.util.Comparator;
import java.util.stream.IntStream;

/**
 * Java implementation of Python's <a href="https://docs.python.org/3/library/bisect.html">bisect module</a>.
 * 
 * @author albin3
 */
public class Bisect
{
  private Bisect()
  {
    
  }
  
  /**
   * Locate the insertion point for {@code key} in {@code a} to maintain sorted 
   * order. The parameters {@code fromIndex} and {@code toIndex} may be used to 
   * specify a subset of the list which should be considered; by default the 
   * entire list is used. If {@code key} is already present in {@code a}, the 
   * insertion point will be before (to the left of) any existing entries. The 
   * return value is suitable for use as the first parameter to 
   * java.util.ArrayList.add() assuming that {@code a} is already sorted.
   * 
   * The returned insertion point {@code i} partitions the array {@code a} into 
   * two halves such that all values in {@code a} that are less-than {@code key} 
   * in {@code a[fromIndex], ..., a[i]} form the "left half", and all remaining 
   * values in {@code a} that are therefore greater-than or equal to {@code key}
   * in {@code a[i], ..., a[toIndex-1]} form the "right half."
   * @param <T> the type of the array
   * @param a the array
   * @param fromIndex the index of the first element (inclusive)
   * @param toIndex the index of the last element (exclusive)
   * @param key the value to be inserted
   * @param c an optional comparator for custom ordering, null defaults to natural ordering
   * @return {@code i} - the insertion point index
   */
  public static <T> int bisectLeft(T[] a, int fromIndex, int toIndex, T key, Comparator<? super T> c)
  {
      int i = bisect(a, fromIndex, toIndex, key, c);
      for (; i > fromIndex && a[i - 1] == key; i--);
      return i;
  }  

  private static <T> int bisectLeft(T[] a, T key)
  {
    return bisectLeft(a, 0, a.length, key, null);
  }
  
  private static <T> int bisectLeft(T[] a, int fromIndex, int toIndex, T key)
  {
    return bisectLeft(a, fromIndex, toIndex, key, null);
  }
  
  private static <T> int bisectLeft(T[] a, T key, Comparator<? super T> c)
  {
    return bisectLeft(a, 0, a.length, key, c);
  }
  
  /**
   * Similar to {@link #bisectLeft bisectLeft}, 
   * but returns an insertion point which comes after (to the right of) any 
   * existing entries of {@code key} in {@code a}.
   * 
   * The returned insertion point {@code i} partitions the array a into two halves
   * such that all values in {@code a} that are less-than or equal to {@code key}
   * in {@code a[fromIndex], ..., a[i]} form the "left half," and all remaining 
   * values in {@code a} that are therefore greater-than {@code key} in 
   * {@code a[i], ..., a[toIndex-1]} form the "right half."
   * @param <T> 
   * @param a
   * @param fromIndex
   * @param toIndex
   * @param key
   * @param c
   * @return
   */  
  public static <T> int bisectRight(T[] a, int fromIndex, int toIndex, T key, Comparator<? super T> c)
  {
      int i = bisect(a, fromIndex, toIndex, key, c);
      for (; i < toIndex && a[i] == key; i++);
      return i;
  }
  
  private static <T> int bisectRight(T[] a, T key)
  {
    return bisectRight(a, 0, a.length, key, null);
  }
  
  private static <T> int bisectRight(T[] a, int fromIndex, int toIndex, T key)
  {
    return bisectRight(a, fromIndex, toIndex, key, null);
  }
  
  private static <T> int bisectRight(T[] a, T key, Comparator<? super T> c)
  {
    return bisectRight(a, 0, a.length, key, c);
  }  
  
  ////////////////////////////////////////////////////////////////////////////////
  
  private static <T> int bisect(T[] a, Integer fromIndex, Integer toIndex, T key, Comparator<? super T> c)
  {
    if (fromIndex == null)
      fromIndex = 0;
    if (toIndex == null)
      toIndex = a.length;
    
    int i = Arrays.binarySearch(a, fromIndex, toIndex, key, c);
    if (i < 0)
      i = -i - 2;
    if (i < fromIndex)
      i = fromIndex;
    if (i >= toIndex - 2)
      i = toIndex - 2;
    return i + 1;    
  }
  
  private static <T> int bisect(T[] a, T key)
  {
    return bisect(a, null, null, key, null);
  }
  
  private static <T> int bisect(T[] a, int fromIndex, int toIndex, T key)
  {
    return bisect(a, fromIndex, toIndex, key, null);
  }
  
  private static <T> int bisect(T[] a, T key, Comparator<? super T> c)
  {
    return bisect(a, null, null, key, c);
  } 
    
  ////////////////////////////////////////////////////////////////////////////////
  
  public static int bisectLeft(byte[] a, int fromIndex, int toIndex, byte key)
  {
    var A = IntStream.range(0, a.length).map(i -> a[i]).boxed().toArray();
    return bisectLeft(A, fromIndex, toIndex, key);
  }
  
  public static int bisectLeft(byte[] a, byte key)
  {
    var A = IntStream.range(0, a.length).map(i -> a[i]).boxed().toArray();
    return bisectLeft(A, key);
  }  

  public static int bisectLeft(char[] a, int fromIndex, int toIndex, char key)
  {
    var A = IntStream.range(0, a.length).map(i -> a[i]).boxed().toArray();
    return bisectLeft(A, fromIndex, toIndex, key);
  }
  
  public static int bisectLeft(char[] a, char key)
  {
    var A = IntStream.range(0, a.length).map(i -> a[i]).boxed().toArray();
    return bisectLeft(A, key);
  }

  public static int bisectLeft(double[] a, int fromIndex, int toIndex, double key)
  {
    var A = IntStream.range(0, a.length).mapToDouble(i -> a[i]).boxed().toArray();
    return bisectLeft(A, fromIndex, toIndex, key);
  }
  
  public static int bisectLeft(double[] a, double key)
  {
    var A = IntStream.range(0, a.length).mapToDouble(i -> a[i]).boxed().toArray();
    return bisectLeft(A, key);
  }
  
  public static int bisectLeft(float[] a, int fromIndex, int toIndex, float key)
  {
    var A = IntStream.range(0, a.length).mapToDouble(i -> a[i]).boxed().toArray();
    return bisectLeft(A, fromIndex, toIndex, key);
  }
  
  public static int bisectLeft(float[] a, float key)
  {
    var A = IntStream.range(0, a.length).mapToDouble(i -> a[i]).boxed().toArray();
    return bisectLeft(A, key);
  }
 
  public static int bisectLeft(int[] a, int fromIndex, int toIndex, int key)
  {
    var A = IntStream.range(0, a.length).map(i -> a[i]).boxed().toArray();
    return bisectLeft(A, fromIndex, toIndex, key);
  }
 
  public static int bisectLeft(int[] a, int key)
  {
    var A = IntStream.range(0, a.length).map(i -> a[i]).boxed().toArray();
    return bisectLeft(A, key);
  }

  public static int bisectLeft(long[] a, int fromIndex, int toIndex, long key)
  {
    var A = IntStream.range(0, a.length).mapToLong(i -> a[i]).boxed().toArray();
    return bisectLeft(A, fromIndex, toIndex, key);
  }
  
  public static int bisectLeft(long[] a, long key)
  {
    var A = IntStream.range(0, a.length).mapToLong(i -> a[i]).boxed().toArray();
    return bisectLeft(A, key);
  }

  public static int bisectLeft(short[] a, int fromIndex, int toIndex, short key)
  {
    var A = IntStream.range(0, a.length).map(i -> a[i]).boxed().toArray();
    return bisectLeft(A, fromIndex, toIndex, key);
  }
  
  public static int bisectLeft(short[] a, short key)
  {
    var A = IntStream.range(0, a.length).map(i -> a[i]).boxed().toArray();
    return bisectLeft(A, key);
  }
  
  ////////////////////////////////////////////////////////////////////////////////

  public static int bisectRight(byte[] a, int fromIndex, int toIndex, byte key)
  {
    var A = IntStream.range(0, a.length).map(i -> a[i]).boxed().toArray();
    return bisectRight(A, fromIndex, toIndex, key);
  }
  
  public static int bisectRight(byte[] a, byte key)
  {
    var A = IntStream.range(0, a.length).map(i -> a[i]).boxed().toArray();
    return bisectRight(A, key);
  }  

  public static int bisectRight(char[] a, int fromIndex, int toIndex, char key)
  {
    var A = IntStream.range(0, a.length).map(i -> a[i]).boxed().toArray();
    return bisectRight(A, fromIndex, toIndex, key);
  }
  
  public static int bisectRight(char[] a, char key)
  {
    var A = IntStream.range(0, a.length).map(i -> a[i]).boxed().toArray();
    return bisectRight(A, key);
  }

  public static int bisectRight(double[] a, int fromIndex, int toIndex, double key)
  {
    var A = IntStream.range(0, a.length).mapToDouble(i -> a[i]).boxed().toArray();
    return bisectRight(A, fromIndex, toIndex, key);
  }
  
  public static int bisectRight(double[] a, double key)
  {
    var A = IntStream.range(0, a.length).mapToDouble(i -> a[i]).boxed().toArray();
    return bisectRight(A, key);
  }
  
  public static int bisectRight(float[] a, int fromIndex, int toIndex, float key)
  {
    var A = IntStream.range(0, a.length).mapToDouble(i -> a[i]).boxed().toArray();
    return bisectRight(A, fromIndex, toIndex, key);
  }
  
  public static int bisectRight(float[] a, float key)
  {
    var A = IntStream.range(0, a.length).mapToDouble(i -> a[i]).boxed().toArray();
    return bisectRight(A, key);
  }
 
  public static int bisectRight(int[] a, int fromIndex, int toIndex, int key)
  {
    var A = IntStream.range(0, a.length).map(i -> a[i]).boxed().toArray();
    return bisectRight(A, fromIndex, toIndex, key);
  }
 
  public static int bisectRight(int[] a, int key)
  {
    var A = IntStream.range(0, a.length).map(i -> a[i]).boxed().toArray();
    return bisectRight(A, key);
  }

  public static int bisectRight(long[] a, int fromIndex, int toIndex, long key)
  {
    var A = IntStream.range(0, a.length).mapToLong(i -> a[i]).boxed().toArray();
    return bisectRight(A, fromIndex, toIndex, key);
  }
  
  public static int bisectRight(long[] a, long key)
  {
    var A = IntStream.range(0, a.length).mapToLong(i -> a[i]).boxed().toArray();
    return bisectRight(A, key);
  }

  public static int bisectRight(short[] a, int fromIndex, int toIndex, short key)
  {
    var A = IntStream.range(0, a.length).map(i -> a[i]).boxed().toArray();
    return bisectRight(A, fromIndex, toIndex, key);
  }
  
  public static int bisectRight(short[] a, short key)
  {
    var A = IntStream.range(0, a.length).map(i -> a[i]).boxed().toArray();
    return bisectRight(A, key);
  }
    
}
