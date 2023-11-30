/*
 * Copyright 2022, Lawrence Livermore National Security, LLC.
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.utility.proto;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 *
 * @author nelson85
 */
public class ByteStreamSource implements ByteSource
{
  final ByteStreamSource origin;
  final InputStream is;
  ByteBuffer working;
  int offset;
  int end;  // total number of bytes in this subsection

  ByteStreamSource(ByteStreamSource origin, InputStream is, int offset, int length)
  {
    if (origin == null)
      origin = this;
    this.origin = origin;
    this.is = is;
    this.offset = offset;
    this.end = length + this.offset;
  }

  @Override
  public int position()
  {
    return offset;
  }

  @Override
  public int remaining()
  {
    return end - offset;
  }

  @Override
  public int get()
  {
    try
    {
      int out = is.read();
      if (out != -1)
        offset++;
      return out;
    }
    catch (IOException ex)
    {
      return -1;
    }
  }

  @Override
  public ByteSource slice(int length)
  {
    ByteStreamSource out = new ByteStreamSource(origin, is, offset, length);
    offset += length;
    return out;
  }

  @Override
  public boolean hasRemaining()
  {
    // if the end is unknown then always report remaining
    if (end == -1)
      return true;
    return end>offset;
  }

  @Override
  public ByteBuffer request(int bytes)
  {
    try
    {
      // if the size requested is negative then give what we can
      if (bytes < 0)
        bytes = end - offset;

      // Make sure we have space in working
      if (origin.working == null || origin.working.capacity() < bytes)
        origin.working = ByteBuffer.allocate(bytes);

      // Transfer to the working buffer
      ByteBuffer working = origin.working;
      working.rewind();
      working.order(ByteOrder.BIG_ENDIAN);
      int got = is.readNBytes(working.array(), working.arrayOffset(), bytes);
      working.limit(got);
      offset += got;

      return working;
    }
    catch (IOException ex)
    {
      throw new RuntimeException(ex);
    }
  }

}
