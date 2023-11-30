/*
 * Copyright 2016, Lawrence Livermore National Security, LLC. 
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.utility;

/**
 *
 * @author monterial1
 * @param <T>
 */
public interface Attribute<T>
{
  String getName();

  T getDefaultValue();

  Class<T> getAttributeClass();

  @SuppressWarnings("unchecked")
  public static <T2> Attribute<T2> of(String name, Class<T2> cls)
  {
    return new Attribute()
    {
      @Override
      public String getName()
      {
        return name;
      }

      @Override
      public Object getDefaultValue()
      {
        return null;
      }

      @Override
      public Class getAttributeClass()
      {
        return cls;
      }
    };
  }

}
