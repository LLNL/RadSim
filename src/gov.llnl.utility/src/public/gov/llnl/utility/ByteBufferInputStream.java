/*
 * Copyright 2016, Lawrence Livermore National Security, LLC. 
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.utility;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

/**
 *
 * @author nelson85
 */
public class ByteBufferInputStream extends InputStream
{
  final ByteBuffer buffer;
  
  public ByteBufferInputStream(ByteBuffer buffer)
  {
    this.buffer = buffer;
  }
  
  @Override
  public int read() throws IOException
  {
    if (this.buffer.hasRemaining())
      return this.buffer.get();
    return -1;
  }
  
  @Override
  public int read(byte[] contents, int off, int len)
  {
    if (!this.buffer.hasRemaining())
      return -1;
    len = Math.min(len, this.buffer.remaining());
    buffer.get(contents, off, len);
    return len;
  }
}
