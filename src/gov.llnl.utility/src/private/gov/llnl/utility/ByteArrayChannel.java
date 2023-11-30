/*
 * Copyright 2016, Lawrence Livermore National Security, LLC. 
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.utility;

import gov.llnl.utility.annotation.Internal;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SeekableByteChannel;
import java.util.Arrays;

/**
 *
 * @author nelson85
 */
@Internal
class ByteArrayChannel implements SeekableByteChannel
{
  byte[] buffer;
  int location;

  public ByteArrayChannel(byte[] buffer)
  {
    this.buffer = buffer;
  }

  @Override
  public int read(ByteBuffer bb) throws IOException
  {
    ensureOpen();
    if (location > buffer.length)
      return 0;
    int end = location + bb.remaining();
    if (end > buffer.length)
      end = buffer.length;
    int copied = end - location;
    bb.put(buffer, location, copied);
    location = end;
    return copied;
  }

  @Override
  public int write(ByteBuffer bb) throws IOException
  {
    ensureOpen();
    int end = location + bb.remaining();
    if (end > buffer.length)
      end = buffer.length;
    int copied = end - location;
    if (copied < 0)
      return 0;
    System.arraycopy(bb.array(), bb.position(), buffer, location, copied);
    location = end;
    return copied;
  }

  @Override
  public long position() throws IOException
  {
    ensureOpen();
    return location;
  }

  @Override
  public SeekableByteChannel position(long l) throws IOException
  {
    ensureOpen();
    if (l < 0)
      throw new IllegalArgumentException();
    this.location = (int) l;
    return this;
  }

  @Override
  public long size() throws IOException
  {
    ensureOpen();
    return buffer.length;
  }

  @Override
  public SeekableByteChannel truncate(long l) throws IOException
  {
    throw new UnsupportedOperationException("Not supported");
  }

  @Override
  public boolean isOpen()
  {
    return buffer != null;
  }

  @Override
  public void close() throws IOException
  {
    buffer = null;
  }

  @Override
  public boolean equals(Object o)
  {
    if (!(o instanceof ByteArrayChannel))
      return false;
    ByteArrayChannel o2 = (ByteArrayChannel) o;
    return Arrays.equals(o2.buffer, this.buffer)
            && o2.location == this.location;
  }

  private void ensureOpen() throws ClosedChannelException
  {
    if (this.buffer == null)
      throw new ClosedChannelException();
  }
}
