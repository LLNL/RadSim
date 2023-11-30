
import java.util.Arrays;
import java.util.Collections;

/*
 * Copyright 2016, Lawrence Livermore National Security, LLC. 
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
/**
 * Test to see if Collections.sort on Arrays.asList changes the original. It
 * does.
 *
 * @author nelson85
 */
public class AsListTest
{
  static public void main(String[] args)
  {
    Integer[] array = new Integer[10];
    for (int i = 0; i < array.length; ++i)
    {
      array[i] = (i * 14407) % 10;
    }

    for (int i = 0; i < 10; ++i)
    {
      System.out.println(array[i]);
    }
    Collections.sort(Arrays.asList(array));
    for (int i = 0; i < 10; ++i)
    {
      System.out.println(array[i]);
    }
  }
}
