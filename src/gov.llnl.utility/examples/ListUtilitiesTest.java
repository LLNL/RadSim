/*
 * Copyright 2021, Lawrence Livermore National Security, LLC. 
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
import gov.llnl.utility.ListUtilities.ConditionMatcher;
import static gov.llnl.utility.ListUtilities.findFirst;
import static gov.llnl.utility.ListUtilities.replaceFirst;
import static gov.llnl.utility.ListUtilities.reverse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author nelson85
 */
public class ListUtilitiesTest
{

  static public void main(String[] args)
  {
    List<String> foo = Arrays.asList("Larry", "Moe", "Curly");
    for (String s : reverse(foo))
    {
      System.out.println(s);
    }
    {
      LinkedList<String> list = new LinkedList<>();
      list.addAll(Arrays.asList("Sally", "Fred", "George", "Judy"));
      test(list);
    }
    {
      ArrayList<String> list = new ArrayList<>();
      list.addAll(Arrays.asList("Sally", "Fred", "George", "Judy"));
      test(list);
    }
  }

  static void test(List<String> list)
  {
    System.out.println("Has Fred " + findFirst(list, new ConditionMatcher<String, String>("Fred")
    {
      @Override
      public boolean matches(String item)
      {
        return condition.equals(item);
      }
    }).hasNext());

    System.out.println("Has Hugo " + findFirst(list, new ConditionMatcher<String, String>("Hugo")
    {
      @Override
      public boolean matches(String item)
      {
        return condition.equals(item);
      }
    }).hasNext());

    System.out.println(replaceFirst(list, "Bob", new ConditionMatcher<String, String>("George")
    {
      @Override
      public boolean matches(String item)
      {
        return condition.equals(item);
      }
    }));

    System.out.println(replaceFirst(list, "Alice", new ConditionMatcher<String, String>("Gina")
    {
      @Override
      public boolean matches(String item)
      {
        return condition.equals(item);
      }
    }));
  }
}
