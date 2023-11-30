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
import java.util.UUID;

/**
 * Utilities to generate UUIDs from strings.
 *
 * These are used for storage and to make serialization ids for the java
 * Serialization method.
 *
 * @author seilhan3
 */
public class UUIDUtilities
{
  /**
   * Create a UUID for a string using the MD5 method.
   *
   * @param input is the string to be hashed.
   * @return a 128 bit UUID.
   */
  public static UUID createUUID(String input)
  {
    // Convert the string to byts so we can make a hash
    byte[] bytes = input.getBytes();

    // Use MessageDigest to convert bytes into a hash number
    MessageDigest md = null;
    try
    {
      md = MessageDigest.getInstance("MD5");
    }
    catch (NoSuchAlgorithmException ex)
    {
      throw new RuntimeException("Unable to find MD5 MessageDigest", ex);
    }
    md.update(bytes, 0, bytes.length);
    byte[] digest = md.digest();

    // UUID needs 2 longs but we have a byte stream.  
    // So use a bytebuffer convert bytes to 2 longs.
    ByteBuffer bb = ByteBuffer.wrap(digest);
    return new UUID(bb.getLong(), bb.getLong());
  }

  /**
   * Create a hash for use in serialization. This is used when we want a
   * serialization of an object to be persistent. The string used for the hash
   * should change if data elements with the class are added or removed.
   * <p>
   * Example:
   * <pre>{@code
   * public class Event implements Serializable
   * {
   *   private static final long serialVersionUID = UUIDUtilities.createLong("Event-v1");
   *   ...
   * }
   * }</pre>
   *
   * @param input is the string to hash.
   * @return the long hash for this string.
   */
  public static long createLong(String input)
  {
    UUID out = createUUID(input);
    return out.getLeastSignificantBits() ^ out.getMostSignificantBits();
  }

}
