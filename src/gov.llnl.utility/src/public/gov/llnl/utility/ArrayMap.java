/*
 * Copyright 2016, Lawrence Livermore National Security, LLC. 
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.utility;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Lightweight map for utility functions.Unsorted set for small number of key
 values.
 *
 * @author nelson85
 * @param <K>
 * @param <V>
 */
public class ArrayMap<K, V> implements Serializable, Map<K, V>
{
  ArrayList<ArrayEntry> entries = new ArrayList<>();

  public static ArrayMap of(Object... obj) throws IllegalArgumentException
  {
    ArrayMap out = new ArrayMap();
    if ((obj.length & 2) == 1)
      throw new IllegalArgumentException("arguments must be even");
    for (int i = 0; i < obj.length; i += 2)
    {
      out.put(obj[i], obj[i + 1]);
    }
    return out;
  }

  @Override
  public int size()
  {
    return entries.size();
  }

  @Override
  public boolean isEmpty()
  {
    return entries.isEmpty();
  }

  @Override
  public boolean containsKey(Object o)
  {
    return getEntry(o) != null;
  }

  @Override
  public boolean containsValue(Object o)
  {
    return (this.entries.stream().anyMatch(entry -> (entry.value.equals(o))));
  }

  Entry getEntry(Object key)
  {
    // Make sure we can cast to the generic type
    try
    {
      K obj = (K) key;
    }
    catch (Exception ex)
    {
      return null;
    }

    for (ArrayEntry entry : this.entries)
    {
      if (entry.key.equals(key))
        return entry;
    }
    return null;
  }

  @Override
  @SuppressWarnings("unchecked")
  public V get(Object o)
  {
    Entry entry = getEntry(o);
    if (entry == null)
      return null;
    return (V) entry.getValue();
  }

  @Override
  @SuppressWarnings("unchecked")
  public V put(K k, V v)
  {
    Entry entry = getEntry(k);
    if (entry == null)
    {
      entries.add(new ArrayEntry(k, v));
      return null;
    }

    Object obj = entry.getValue();
    entry.setValue(v);
    return (V) obj;
  }

  @Override
  @SuppressWarnings("unchecked")
  public V remove(Object o)
  {
    Iterator<ArrayEntry> iter;
    for (iter = this.entries.iterator(); iter.hasNext();)
    {
      ArrayEntry next = iter.next();
      if (next.key.equals(o))
      {
        Object obj = next.value;
        iter.remove();
        return (V) obj;
      }
    }
    return null;
  }

  @Override
  @SuppressWarnings("unchecked")
  public void putAll(Map<? extends K, ? extends V> map)
  {
    this.entries.ensureCapacity(this.entries.size() + map.size());
    map.entrySet().forEach(entry ->
    {
      this.put(entry.getKey(), (V) entry.getValue());
    });
  }

  @Override
  public void clear()
  {
    entries.clear();
  }

  @Override
  public Set<K> keySet()
  {
    return new KeySet();
  }

  @Override
  @SuppressWarnings("unchecked")
  public Collection<V> values()
  {
    return CollectionUtilities.adapter(entries, (ArrayEntry entry) -> (V) entry.value);
  }

  @Override
  @SuppressWarnings("unchecked")
  public Set<Entry<K, V>> entrySet()
  {
    return new EntrySet();
  }

  class SetBase
  {
    public int size()
    {
      return ArrayMap.this.size();
    }

    public boolean isEmpty()
    {
      return ArrayMap.this.isEmpty();
    }

    public void clear()
    {
      ArrayMap.this.clear();
    }
  }

//<editor-fold desc="keySet">
  class KeySet extends SetBase implements Set<K>, Serializable
  {

    @Override
    @SuppressWarnings("unchecked")
    public boolean contains(Object o)
    {
      return ArrayMap.this.containsKey((K) o);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Iterator<K> iterator()
    {
      return Iterators.adapter(ArrayMap.this.entries.iterator(), (ArrayEntry entry) -> (K) entry.key);
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean remove(Object o)
    {
      V obj = ArrayMap.this.remove((K) o);
      return obj == null;
    }

    @Override
    public Object[] toArray()
    {
      return Arrays.asList(CollectionUtilities.toArray(this)).toArray();
    }

    @Override
    public <T> T[] toArray(T[] ts)
    {
      return (T[]) Arrays.asList(CollectionUtilities.toArray(this)).toArray(ts);
    }

//<editor-fold desc="unsupported" defaultstate="collapsed">
    @Override
    public boolean add(K e)
    {
      throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public boolean containsAll(Collection<?> clctn)
    {
      throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public boolean addAll(Collection<? extends K> clctn)
    {
      throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public boolean retainAll(Collection<?> clctn)
    {
      throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public boolean removeAll(Collection<?> clctn)
    {
      throw new UnsupportedOperationException("Not supported.");
    }
//</editor-fold>

  }
//</editor-fold>
//<editor-fold desc="entrySet">

  class EntrySet<K, V> extends SetBase implements Set<Entry<K, V>>, Serializable
  {

    @Override
    public boolean contains(Object o)
    {
      if (!(o instanceof Entry))
        return false;
      Entry entry1 = (Entry) o;
      Entry entry2 = ArrayMap.this.getEntry(entry1.getKey());
      return entry2 != null && entry1.getValue().equals(entry2.getValue());
    }

    @Override
    @SuppressWarnings("unchecked")
    public Iterator<Entry<K, V>> iterator()
    {
      Iterator<ArrayEntry> base = ArrayMap.this.entries.iterator();
      return Iterators.< Entry<K, V>, ArrayEntry>cast(base);
    }

    @Override
    public Object[] toArray()
    {
      return ArrayMap.this.entries.toArray();
    }

    @Override
    public <T> T[] toArray(T[] ts)
    {
      return ArrayMap.this.entries.toArray(ts);
    }

    @Override
    public void clear()
    {
      ArrayMap.this.clear();
    }

//<editor-fold desc="unsupported" defaultstate="collapsed">
    @Override
    public boolean add(Entry<K, V> e)
    {
      throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public boolean remove(Object o)
    {
      throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public boolean containsAll(Collection<?> clctn)
    {
      throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public boolean addAll(Collection<? extends Entry<K, V>> clctn)
    {
      throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public boolean retainAll(Collection<?> clctn)
    {
      throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public boolean removeAll(Collection<?> clctn)
    {
      throw new UnsupportedOperationException("Not supported.");
    }
//</editor-fold>
  }
//</editor-fold>

  static class ArrayEntry<K, V> implements Entry<K, V>, Serializable
  {
    K key;
    V value;

    private ArrayEntry(K k, V v)
    {
      this.key = k;
      this.value = v;
    }

    @Override
    public K getKey()
    {
      return key;
    }

    @Override
    public V getValue()
    {
      return value;
    }

    @Override
    public V setValue(V v)
    {
      V old = value;
      value = v;
      return old;
    }
  }

}
