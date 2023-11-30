

/*
 * Copyright 2016, Lawrence Livermore National Security, LLC. 
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
/**
 *
 * @author nelson85
 */
public class EnumTest
{
  enum Test
  {
    A,
    B
  };

  static public void main(String[] args)
  {
    Test test = Test.A;
    Class<Test> u = Test.class;
    System.out.println(Enum.valueOf(u, "a".toUpperCase()));
    System.out.println(Enum.valueOf(u, "A"));
  }

  static public <Type extends Enum<Type>> Type foo(Class<Type> cls, String v)
  {
    return Enum.valueOf(cls, v.toUpperCase());
  }

}
