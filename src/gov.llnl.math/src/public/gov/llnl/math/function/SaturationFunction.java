/*
 * Copyright 2016, Lawrence Livermore National Security, LLC. 
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.math.function;

/**
 *
 * @author nelson85
 */
public class SaturationFunction implements Function.Parameterized
{
  double gain;
  double saturation;
  double offset;

  public SaturationFunction(double offset, double gain, double saturation)
  {
    this.saturation = saturation;
    this.gain = gain;
    this.offset = offset;
  }

  @Override
  public double applyAsDouble(double x)
  {
    return (gain * x + offset) / (1 + saturation * x);
  }

  @Override
  public double[] toArray()
  {
    return new double[]
    {
      saturation, gain, offset
    };
  }

  @Override
  public int hashCode()
  {
    int hash = 3;
    hash = 67 * hash + (int) (Double.doubleToLongBits(this.gain) ^ (Double.doubleToLongBits(this.gain) >>> 32));
    hash = 67 * hash + (int) (Double.doubleToLongBits(this.saturation) ^ (Double.doubleToLongBits(this.saturation) >>> 32));
    hash = 67 * hash + (int) (Double.doubleToLongBits(this.offset) ^ (Double.doubleToLongBits(this.offset) >>> 32));
    return hash;
  }
  
  public boolean equals(Object o)
  {
    if (!(o instanceof SaturationFunction))
      return false;
    SaturationFunction o2 = (SaturationFunction) o;
    return o2.saturation == this.saturation
            && o2.offset == this.offset
            && o2.gain == this.gain;
  }

}
