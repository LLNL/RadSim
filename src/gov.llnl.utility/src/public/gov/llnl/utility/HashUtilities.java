/*
 * Copyright 2016, Lawrence Livermore National Security, LLC. 
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.utility;

import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 *
 * @author nelson85
 */
public class HashUtilities
{

  public static byte[] hash(ByteBuffer buffer)
  {
    MessageDigest md = null;
    try
    {
      md = MessageDigest.getInstance("MD5");
    }
    catch (NoSuchAlgorithmException ex)
    {
      throw new RuntimeException("Unable to find MD5 MessageDigest", ex);
    }
    md.update(buffer);
    return md.digest();
  }

  public static byte[] hash(byte[] values)
  {
    if (values == null)
      return null;
    ByteBuffer buffer = ByteBuffer.wrap(values);
    return hash(buffer);
  }

  public static byte[] hash(int[] values)
  {
    if (values == null)
      return null;
    ByteBuffer buffer = ByteBuffer.allocate(values.length * Integer.BYTES);
    for (int i = 0; i < values.length; ++i)
    {
      buffer.putInt(values[i]);
    }
    buffer.rewind();
    return hash(buffer);
  }

  public static String byteArrayToHexString(byte[] digest)
  {
    StringBuilder sb = new StringBuilder(digest.length * 2);
    for (byte b : digest)
    {
      sb.append(String.format("%02x", b & 255));
    }
    return sb.toString();
  }

  public static long byteArrayToLong(byte[] digest)
  {
    if (digest == null)
      return 0;
    ByteBuffer out = ByteBuffer.wrap(digest);
    return out.getLong();
  }

}
