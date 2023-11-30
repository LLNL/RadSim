/*
 * Copyright 2016, Lawrence Livermore National Security, LLC. 
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.math.graph;

//<editor-fold desc="query">
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public interface QueryLookupInterface<Type>
{
  Type lookup(String idName);

  /**
   * Get the list of keys and values in the query. Primarily for debugging.
   * (optional)
   *
   * @return an iterable for all the key value pairs in the query.
   */
  Iterable<Map.Entry<String, Type>> entrySet();

  /**
   *
   * @author nelson85
   */
  class DefaultQueryLookup implements QueryLookupInterface
  {
    @Override
    public Integer lookup(String idName)
    {
      return Integer.parseInt(idName);
    }

    @Override
    public Iterable<Map.Entry<String, Integer>> entrySet()
    {
      throw new UnsupportedOperationException("Not supported yet.");
    }
  }

  /**
   *
   * @author nelson85
   */
  class ListQueryLookup implements QueryLookupInterface
  {
    TreeMap<String, Integer> map = new TreeMap<>();

    public ListQueryLookup(String... names)
    {
      super();
      int i = 0;
      for (String n : names)
        map.put(n, i++);
    }

    @Override
    public Integer lookup(String idName)
    {
      Integer out = map.get(idName);
      if (out == null)
        return GraphQuery.QUERY_ID_NONE;
      return out;
    }

    @Override
    public Set<Map.Entry<String, Integer>> entrySet()
    {
      return map.entrySet();
    }
  }

  /**
   *
   * @author nelson85
   */
  public static class AutoQueryLookup implements QueryLookupInterface
  {
    TreeMap<String, Integer> map = new TreeMap<>();
    int autoId = 0;

    public AutoQueryLookup()
    {
    }

    @Override
    public Integer lookup(String idName)
    {
      Integer out = map.get(idName);
      if (out == null)
      {
        out = autoId;
        map.put(idName, autoId++);
      }
      return out;
    }

    @Override
    public Set<Map.Entry<String, Integer>> entrySet()
    {
      return map.entrySet();
    }
  }

}
