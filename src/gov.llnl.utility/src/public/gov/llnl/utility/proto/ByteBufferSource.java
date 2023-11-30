/*
 * Copyright 2022, Lawrence Livermore National Security, LLC.
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.utility.proto;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Backer for ByteBuffer based ByteSource.
 *
 * @author nelson85
 */
class ByteBufferSource implements ByteSource
{
  final ByteBufferSource origin;
  final ByteBuffer data;
  ByteBuffer working;
  int offset;
  int limit;

  ByteBufferSource(ByteBufferSource origin, ByteBuffer data, int offset, int limit)
  {
    if (origin == null)
      origin = this;
    this.origin = origin;
    this.data = data;
    this.offset = offset;
    this.limit = limit;
  }

  @Override
  public int position()
  {
    return offset + data.position();
  }
  
  @Override
  public int remaining()
  {
    return limit;
  }

  @Override
  public boolean hasRemaining()
  {
    return data.position() < limit;
  }

  @Override
  public int get()
  {
    if (!data.hasRemaining())
      return -1;
    offset++;
    return data.get();
  }

  @Override
  public ByteSource slice(int bytes)
  {
    int remaining = data.remaining();
    if (bytes > remaining)
      bytes = remaining;
    ByteBuffer bb2 = data.slice();
    bb2.limit(bytes);
    ByteBufferSource out = new ByteBufferSource(origin, bb2, position(), bytes);
    data.position(data.position() + bytes);
    offset += bytes;
    return out;
  }

  @Override
  public ByteBuffer request(int bytes)
  {
    int remaining = data.remaining();
    if (bytes < 0)
      bytes = remaining;
    if (bytes > remaining)
      bytes = remaining;

    // Make sure we have space in working
    if (origin.working == null || origin.working.capacity() < bytes)
      origin.working = ByteBuffer.allocate(bytes);

    ByteBuffer working = origin.working;
    working.rewind();
    working.order(ByteOrder.BIG_ENDIAN);
    data.get(working.array(), working.arrayOffset(), bytes);
    working.limit(bytes);
    offset += bytes;

    return working;
  }

}
