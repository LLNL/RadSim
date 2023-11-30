/*
 * Copyright 2016, Lawrence Livermore National Security, LLC. 
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.math.wavelet;

import java.util.Arrays;

/**
 * Small statistical utility for 1D array
 */
public class Statistics1D
{
  double[] data;
  int size;
  double minValue;
  double maxValue;

  public Statistics1D(double[] data)
  {
    this.data = data;
    size = data.length;
    maxValue = data[0];
    minValue = data[0];
    for (int it = 0; it < size; it++)
    {
      if (maxValue < data[it])
      {
        maxValue = data[it];
      }
      if (minValue > data[it])
      {
        minValue = data[it];
      }
    }
  }

  double getMean()
  {
    double sum = 0.0;
    for (double a : data)
      sum += a;
    return sum / size;
  }

  double getVariance()
  {
    double mean = getMean();
    double temp = 0;
    for (double a : data)
      temp += (a - mean) * (a - mean);
    return temp / (size - 1);
  }

  double getStdDev()
  {
    return Math.sqrt(getVariance());
  }

  public double median()
  {
    Arrays.sort(data);
    if (data.length % 2 == 0)
      return (data[(data.length / 2) - 1] + data[data.length / 2]) / 2.0;
    return data[data.length / 2];
  }

  public double getMin()
  {
    return minValue;
  }

  public double getMax()
  {
    return maxValue;
  }
}
