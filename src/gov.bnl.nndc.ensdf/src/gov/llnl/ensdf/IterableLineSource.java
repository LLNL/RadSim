/*
 * Copyright 2022, Lawrence Livermore National Security, LLC.
 * All rights reserved.
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.ensdf;

import java.util.Iterator;

/**
 *
 * @author nelson85
 */
class IterableLineSource implements LineSource
{

  Iterator<String> source;
  String buffer;

  IterableLineSource(Iterable lines)
  {
    source = lines.iterator();
  }

  @Override
  public boolean isEmpty()
  {
    if (buffer != null)
      return false;
    return !source.hasNext();
  }

  @Override
  public String pop()
  {
    if (buffer != null)
    {
      String out = buffer;
      buffer = null;
      return out;
    }
    String line = source.next();
    while (line.startsWith("#"))
      line = source.next();
    return line;
  }

  @Override
  public void push(String str)
  {
    if (buffer != null)
    {
      throw new UnsupportedOperationException();
    }
    buffer = str;
  }

}
