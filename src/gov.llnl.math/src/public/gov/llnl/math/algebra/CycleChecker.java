/* 
 * Copyright 2016, Lawrence Livermore National Security, LLC.
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.math.algebra;

import gov.llnl.math.IntegerArray;
import java.util.Random;

/**
 * Use to detect a cycle in the NNLSQ algorithm.
 *
 * @author nelson85
 */
public class CycleChecker
{
  int index = 0;
  int[] history;
  int[] lastUse;
  int cycleLength = -1;

  public CycleChecker(int regressors, int historySize)
  {
    history = new int[historySize];
    lastUse = new int[regressors];
    IntegerArray.fill(lastUse, -1);
  }

  public boolean add(int regressorId)
  {
    // Assume not in cycle
    cycleLength = -1;

    // Update the history
    int previous = lastUse[regressorId];
    int next = index;
    lastUse[regressorId] = index;
    history[index] = regressorId;
    index++;
    if (index == history.length)
      index = 0;

    // If this is the first add then we are not in a cycle
    if (previous == -1)
      return false;

    if (previous == next)
      return false;

    // Otherwise walk backwards to find the first cycle 
    int i0 = next;
    int i1 = previous;
    while (true)
    {
      if (i1 < 0)
        i1 += history.length;
      if (i0 < 0)
        i0 += history.length;
      if (i0 == previous)
      {
        return true;
      }
      if (history[i1] != history[i0])
        return false;
      cycleLength++;
      i1--;
      i0--;
    }
  }

  public void clear()
  {
    index = 0;
    IntegerArray.fill(history, 0);
    IntegerArray.fill(lastUse, -1);
  }

  static public void main(String[] args)
  {
    CycleChecker cc = new CycleChecker(200, 200);

    Random rand = new Random();
    rand.setSeed(0);
    int last = -1;
    int next;

    for (int i = 0; i < 10000; i++)
    {
      do
      {
        next = (int) (rand.nextDouble() * 100);
      }
      while (next == last);
      last = next;
      System.out.println("add " + next);
      if (cc.add(next) == true)
      {
        throw new RuntimeException("except" + cc.cycleLength);
      }
    }
    System.out.println("No cycles");

    // Test a cycle of different lengths
    for (int cycle = 1; cycle < 105; ++cycle)
    {
      // Clear the checker
      cc.clear();

      // Add in a bunch of randoms
      for (int j = 0; j < 100; j++)
      {
        do
        {
          next = (int) (rand.nextDouble() * 100);
        }
        while (next == last);
        last = next;
        cc.add(next);
      }

      loop:
      for (int j = 0; j < 5; j++)
      {
        for (int k = 0; k < cycle; k++)
        {
          if (cc.add(k) == true)
          {
            System.out.println("cycle caught " + cycle + " " + j + " " + k + " " + cc.cycleLength);
            break loop;
          }
        }

        if (j == 3)
          System.out.println("Cycle miss");
      }
    }

  }

}
