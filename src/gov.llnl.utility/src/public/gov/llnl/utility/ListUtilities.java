/*
 * Copyright 2016, Lawrence Livermore National Security, LLC.
 * All rights reserved
 *
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.utility;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.RandomAccess;
import java.util.function.Function;
import java.util.function.ToDoubleFunction;

/**
 * Collection of utilities for dealing with Lists.
 *
 * @author nelson85
 */
public class ListUtilities
{
  public static <T> List<T> newList(Iterable<T> iterable)
  {
    LinkedList<T> out = new LinkedList<>();
    for (T item : iterable)
    {
      out.add(item);
    }
    return out;
  }

  public static <T> void permute(List<T> list, int range)
  {
    int n = list.size();
    if (range > n)
      range = n;
    for (int i = 0; i < range; ++i)
    {
      int j = (int) (Math.random() * (n - i)) + i;
      // swap i<=>j
      T o = list.get(i);
      list.set(i, list.get(j));
      list.set(j, o);
    }
  }

  /**
   * Create a copy of a list with all members unique.
   *
   * @param <T>
   * @param list
   * @return a new list containing only unique items in sorted order.
   */
  public static <T extends Comparable<? super T>>
          List<T> unique(List<T> list)
  {
    LinkedList<T> out = new LinkedList<>();
    out.addAll(list);
    Collections.sort(out);
    T last = null;
    Iterator<T> iter = out.iterator();
    while (iter.hasNext())
    {
      T next = iter.next();
      if (last == next || last != null && next != null && next.compareTo(last) == 0)
        iter.remove();
      last = next;
    }
    return out;
  }

  /**
   * Calls a method to extract a double field from a list.
   *
   * @param <T> is the type of the element to extract.
   * @param list is the list to traverse through.
   * @param mapper is the function to call.
   * @return
   */
  public static <T> double[] getDoubleField(List<T> list, ToDoubleFunction<T> mapper)
  {
    return list.stream().mapToDouble(mapper).toArray();
  }

  /**
   * Produced an view that iterates in reverse.
   *
   * @param <T>
   * @param <T2>
   * @param list
   * @return a view with order reversed.
   */
  static final public <T, T2 extends List<T>> ReverseCollection<T> reverse(T2 list)
  {
    return new ReverseCollection<>((List<T>) list);
  }

  static final public <T> ReverseCollection<T> reverse(T[] list)
  {
    return new ReverseCollection<>(Arrays.asList(list));
  }

  /**
   * Interface used to search a list.
   *
   * @param <T>
   */
  public interface FindMatcher<T>
  {
    boolean matches(T item);
  }

  /**
   * Implementation of matcher that takes one argument.
   *
   * @param <T>
   * @param <T2>
   */
  static abstract public class ConditionMatcher<T, T2> implements FindMatcher<T>
  {
    protected T condition;

    public ConditionMatcher(T condition)
    {
      this.condition = condition;
    }
  }

  /**
   * Searches a list for first item in the array that matches a specified
   * condition.
   *
   * @param <T> is the item type on the list
   * @param list is the list to be searched
   * @param matcher is the condition for a match
   * @return an iterator whose next item matches, or the end of the array if
   * there is no match.
   */
  static public <T> ListIterator<T> findFirst(List<T> list, FindMatcher<T> matcher)
  {
    ListIterator<T> iter = list.listIterator();
    while (iter.hasNext())
    {
      T obj = iter.next();
      if (matcher.matches(obj))
      {
        iter.previous();
        return iter;
      }
    }
    return iter;
  }

  /**
   * Searches a list to find the first item that matches a criteria. It will
   * return the item that was replaced or null if the item is not found.
   *
   * @param <T> is the item type on the list
   * @param list is the list to be processed
   * @param replacement is the item to use as a replacement
   * @param matcher is the condition for the match
   * @return the item that was replaced or null if no item was found.
   */
  static public <T> T replaceFirst(List<T> list, T replacement, FindMatcher<T> matcher)
  {
    ListIterator<T> iter = findFirst(list, matcher);
    if (iter.hasNext() == false)
      return null;

    // Random access has high efficiency calls
    if (list instanceof RandomAccess)
    {
      int i = iter.nextIndex();
      T out = list.get(i);
      list.set(i, replacement);
      return out;
    }

    // Sequential requires a different patter
    iter.add(replacement);
    iter.next();
    T out = iter.previous();
    iter.remove();
    return out;
  }

  /**
   * Shrink the size of a list to a specified size. List type must support
   * subList with clear.
   *
   * @param <T> is a type derived from list.
   * @param list is the list to be shrunk.
   * @param size is the desired size.
   * @return the reduced size list.
   * @throws IllegalArgumentException if the list is is smaller than the
   * requested size.
   */
  static public <T extends List<?>> T shrinkTo(T list, int size)
  {
    list.subList(size, list.size()).clear();
    return list;
  }

  public static <T> void ensureSize(ArrayList<T> list, int requiredSize)
  {
    list.ensureCapacity(requiredSize);
    int tailLength = requiredSize - list.size();
    for (int i = 0; i < tailLength; i++)
    {
      list.add(null);
    }
  }

  public static <T> void ensureSize(List<T> list, int requiredSize)
  {
    int tailLength = requiredSize - list.size();
    for (int i = 0; i < tailLength; i++)
    {
      list.add(null);
    }
  }
}
