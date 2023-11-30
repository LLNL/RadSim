/*
 * Copyright 2016, Lawrence Livermore National Security, LLC. 
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.utility;

import gov.llnl.utility.xml.bind.Reader;
import java.util.Map;

/**
 *
 * @author nelson85
 * @param <Key>
 * @param <Value>
 */
//@Reader.Declaration(pkg = UtilityPackage.class, name = "pair")
public class Pair<Key, Value> implements Map.Entry<Key, Value>
{
  Key key = null;
  Value value = null;

  protected Pair()
  {
  }

  public Pair(Key key, Value value)
  {
    this.key = key;
    this.value = value;
  }

  @Override
  public Key getKey()
  {
    return key;
  }

  @Override
  public Value getValue()
  {
    return value;
  }

  @Override
  //  @Reader.Attribute(name = "value", required = true, type = String.class)
  public Value setValue(Value v)
  {
    return this.value = v;
  }

//  @Reader.Attribute(name = "key", required = true, type = String.class)
  protected Key setKey(Key k)
  {
    return this.key = k;
  }
}
