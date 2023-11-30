/*
 * Copyright 2016, Lawrence Livermore National Security, LLC. 
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.utility.io;

import gov.llnl.utility.HashUtilities;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Support class for computing the md5sum as we process a file.
 *
 * @author nelson85
 */
public class MD5FilterInputStream extends FilterInputStream
{
  MessageDigest md = null;

  public MD5FilterInputStream(InputStream is)
  {
    super(is);

    try
    {
      md = MessageDigest.getInstance("MD5");
    }
    catch (NoSuchAlgorithmException ex)
    {
      throw new RuntimeException("Unable to find MD5 MessageDigest", ex);
    }
  }

  public int read() throws IOException
  {
    int i = super.read();
    if (i == -1)
      return i;
    md.update((byte) i);
    return i;
  }

  public int read(byte[] b) throws IOException
  {
    int rc = super.read(b);
    if (rc == -1)
      return -1;
    md.update(b, 0, 0 + rc);
    return rc;
  }

  public int read(byte[] b, int off, int len) throws IOException
  {
    int rc = super.read(b, off, len);
    if (rc == -1)
      return -1;
    md.update(b, off, off + rc);
    return rc;
  }

  public String getChecksum()
  {
    byte[] digest = md.digest();
    return HashUtilities.byteArrayToHexString(digest);
  }
}
