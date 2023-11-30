/*
 * Copyright 2016, Lawrence Livermore National Security, LLC. 
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.utility;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author nelson85
 */
public class Benchmarker
{
  public interface Task
  {
    void execute(int passes);
  }

  int targetPasses = 100;
  double targetTime = 20.0;
  int trials = 20;
  int defaultWarmupPasses = 20;

  public class Working
  {
    public String label;
    public Task task;
    public long[] results;

    /**
     * This holds a rough guess of the number of passes required to achieve the
     * target time.
     */
    public int initial;
    public int warmupPasses;

    public void dump(PrintStream out)
    {
      double[] resultTime = new double[results.length];
      double totalTime = 0;
      for (int i = 0; i < results.length; ++i)
      {
        resultTime[i] = 1000.0 * results[i] / initial;  // us/pass
        totalTime += resultTime[i];
      }

      double[] sortedTime = Arrays.copyOf(resultTime, resultTime.length);
      Arrays.sort(sortedTime);
      double median = sortedTime[trials / 2];
      double mean = totalTime / trials;
      out.print(label + ": ");
      out.print(String.format("mean=%.2f μs  ", mean));
      out.print(String.format("median=%.2f μs  ", median));

      out.print("raw=");
      for (int i = 0; i < resultTime.length; ++i)
      {
        out.print(String.format("%.2f ", resultTime[i]));
      }
      out.println();
    }
  }
  ArrayList<Working> workingList = new ArrayList<>();

  /**
   * Add a test to assess in the benchmarking.
   *
   * @param name
   * @param task
   * @return a new working object for setting options.
   */
  public Working addTask(String name, Task task)
  {
    Working working = new Working();
    working.label = name;
    working.task = task;
    working.warmupPasses = this.defaultWarmupPasses;
    workingList.add(working);
    return working;
  }

  /**
   * Warm up each of the tasks. It order to get the jvm to benchmark a task, we
   * need to warm it up enough to get it recognized as a high usage code.
   */
  public void warmupAll()
  {
    for (Working working : workingList)
    {
      working.task.execute(working.warmupPasses);
    }
  }

  /**
   * Determine how many passes will be required to get good statistics. Computes
   * the time required to get the required test length.
   *
   * @param working
   */
  public void assessTarget(Working working)
  {
    long start = System.currentTimeMillis();
    long end = start;
    int passes = 0;
    int factor = 1;
    while ((end - start) / 1000.0 < targetTime / trials)
    {
      working.task.execute(factor);
      passes += factor;
      factor = factor * 2;
      end = System.currentTimeMillis();
    }
    double total = (double) (end - start) / 1000.0 / passes;  // time per pass
    int needed = (int) (this.targetTime / total / trials);  // time per trial

    System.out.println(String.format("   %s: %d=>%f  %d", working.label, passes, total, needed));
    working.initial = needed;
  }

  public void test()
  {
    // Run the warmupAll
    System.out.println("Warmup");
    this.warmupAll();

    // Compute the initial passes to reach our target load
    System.out.println("Initial assessment");
    for (Working working : workingList)
    {
      working.results = new long[this.trials];
      this.assessTarget(working);
    }

    // Benchmark each item
    System.out.println("Load test");
    for (int i = 0; i < trials; ++i)
    {
      for (Working working : workingList)
      {
        long start = System.currentTimeMillis();
        working.task.execute(working.initial);
        long end = System.currentTimeMillis();
        working.results[i] = end - start;
      }
    }

    // Dump the results
    for (Working working : workingList)
    {
      working.dump(System.out);
    }
  }
}
