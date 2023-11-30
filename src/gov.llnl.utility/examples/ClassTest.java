
import java.lang.reflect.Modifier;

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
public class ClassTest
{
  public class A
  {
  }

  public static class B
  {
  }

  static public void main(String[] args)
  {
    System.out.println(isInnerClass(ClassTest.class));
    System.out.println(isInnerClass(A.class));
    System.out.println(isInnerClass(B.class));
  }

  public static boolean isInnerClass(Class<?> cls)
  {
    return cls.isMemberClass() && !Modifier.isStatic(cls.getModifiers());
  }
}
