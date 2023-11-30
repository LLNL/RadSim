/*
 * Copyright 2016, Lawrence Livermore National Security, LLC. 
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.utility;

/**
 *
 * @author albin3
 */
public class MutableInteger extends Number
{
  
  private int value;
  
  public MutableInteger(int value)
  {
    this.value = value;
  }
  
  public MutableInteger()
  {
    value = 0;
  }
  
  @Override
  public int intValue()
  {
    return (int) value;
  }

  @Override
  public long longValue()
  {
    return (long) value;
  }

  @Override
  public float floatValue()
  {
    return (float) value;
  }

  @Override
  public double doubleValue()
  {
    return value;
  }
  
   public void setValue(int value)
  {
    this.value = value;
  }
   
}
