/*
 * Copyright 2016, Lawrence Livermore National Security, LLC. 
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.utility;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Objects;
import java.util.function.Function;

/**
 *
 * @author nelson85
 */
public class IteratorInputStream<T> extends InputStream
{
  private final Iterator<T> src;
  private final Function<T, byte[]> mapper;
  private InputStream backer;

  /**
   * Create an input stream from a String iterator.
   *
   * @param src
   * @return
   */
  public static IteratorInputStream create(Iterator<String> src)
  {
    return new IteratorInputStream<>(src, (String s) -> s.getBytes());
  }

  /**
   * Create an input stream from a String iterable.
   *
   * @param src
   * @return
   */
  public static IteratorInputStream create(Iterable<String> src)
  {
    return new IteratorInputStream<>(src.iterator(), (String s) -> s.getBytes());
  }

  /**
   * Create an input Stream from an arbitrary source of bytes.
   *
   * @param src
   * @param mapper
   */
  public IteratorInputStream(Iterator<T> src, Function<T, byte[]> mapper)
  {
    this.src = src;
    this.mapper = mapper;
  }

  @Override
  public int read() throws IOException
  {
    // Fetch a backer from the source if we do not have one.
    if (backer == null)
      backer = next();

    // While we have yet to find bytes to use.
    while (backer != null)
    {
      // Get the next byte from the buffer
      int c = backer.read();

      // If it is eof, then switch to the next source.
      if (c == -1)
        backer = next();
      else
        return c;
    }

    // Otherwise we are at eof.
    return -1;
  }

  @Override
  public int read(byte b[], int off, int len) throws IOException
  {
    // Fetch a backer from the source if we do not have one.
    if (backer == null)
      backer = next();

    if (backer == null)
      return -1;

    int read = 0;
    while (backer != null && len>0)
    {
      // Get the next byte from the buffer
      int c = backer.read(b, off, len);

      // If it is eof, then switch to the next source.
      if (c == -1)
      {
        backer = next();
        continue;
      }
      read += c;
      len -= c;
      off += c;
    }
    return read;
  }

  /**
   * Internal method to get the next source in the iterator.
   *
   * @return
   */
  private InputStream next()
  {
    while (this.src.hasNext())
    {
      byte[] contents = mapper.apply(this.src.next());
      if (contents == null)
        continue;
      return new ByteArrayInputStream(contents);
    }
    return null;
  }

  @Override
  public boolean equals(Object o)
  {
    if (!(o instanceof IteratorInputStream))
      return false;
    IteratorInputStream o2 = (IteratorInputStream) o;
    return Objects.equals(o2.backer, this.backer)
            && Objects.equals(o2.mapper, this.mapper)
            && Objects.equals(o2.src, this.src);
  }
}
