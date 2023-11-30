/*
 * Copyright 2022, Lawrence Livermore National Security, LLC.
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.utility.proto;

import java.io.InputStream;
import java.nio.ByteBuffer;

/**
 *
 * Wrapper that may be backed by different implementation.
 * 
 * Backings include ByteBuffer or InputStream.
 * 
 * @author nelson85
 */
public interface ByteSource
{
  /** 
   * Create a ByteSource from an InputStream.
   * 
   * @param is
   * @return 
   */
  static ByteSource wrap(InputStream is)
  {
    return new ByteStreamSource(null, is, 0, Integer.MAX_VALUE);
  }
  
  /** 
   * Create a ByteSource from a byte array.
   * 
   * @param contents
   * @return 
   */
  static ByteSource wrap(byte[] contents)
  {
    return new ByteBufferSource(null, ByteBuffer.wrap(contents), 0, contents.length);
  }
  
  /** 
   * Create a ByteSource from a ByteBuffer.
   * 
   * @param contents
   * @return 
   */
  static ByteSource wrap(ByteBuffer contents)
  {
    return new ByteBufferSource(null, contents, contents.position(), contents.limit());
  }
  
  /** 
   * Get the position relative to the start of the stream.
   * 
   * @return 
   */
  int position();
  
  /**
   * Get the first byte in the stream.
   * 
   * @return the byte or -1 if nothing further is available. 
   */
  int get();
  
  /** Create a byte source with a new limit.
   * 
   * It is assumed the original will be advanced by the request slice.
   * Though in the case of an InputStream backed source this won't occur
   * until the slice has been fully consumed.
   * 
   * @param length
   * @return 
   */
  ByteSource slice(int length);
  
  /** 
   * Check if the limit has been reached.
   * 
   * For some implementations the exact end cannot be established.
   * Thus one should check the return for get always.
   * 
   * @return true if there may be additional bytes.
   */
  boolean hasRemaining();

  /** 
   * Get a byte buffer holding the next n bytes from the source.
   * 
   * The returned buffer is shared with all consumers of the byte stream and
   * is only valid until the next call to request.  Byte order for the ByteBuffer
   * is network (bigendian).
   * 
   * @param bytes is the requested bytes or -1 for the rest of the available bytes.
   * @return the working byte buffer with up to bytes requested.
   */
  public ByteBuffer request(int bytes);

  int remaining();
}
