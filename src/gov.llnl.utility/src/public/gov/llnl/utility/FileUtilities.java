/*
 * Copyright 2016, Lawrence Livermore National Security, LLC.
 * All rights reserved
 *
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.utility;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 *
 * @author nelson85
 */
public class FileUtilities
{
  public static InputStream newCompressedInputStream(Path file, OpenOption... options) throws IOException
  {
    if (PathUtilities.isGzip(file))
    {
      return new GZIPInputStream(Files.newInputStream(file, options));
    }
    else
    {
      return Files.newInputStream(file, options);
    }
  }

  public static OutputStream newCompressedOutputStream(Path file, OpenOption... options) throws IOException
  {
    return new GZIPOutputStream(Files.newOutputStream(file, options));
  }

  public static String getCompressionExtension(Path file)
  {
    String extension = getFileExtension(file);
    if ("".equals(extension))
    {
      return "";
    }
    int i = extension.lastIndexOf(".");
    // There is no double extension like .tar.gz
    if (i != -1)
    {
      extension = extension.substring(i + 1);
    }
    if (isCompressionExtension(extension))
    {
      return extension;
    }
    return "";
  }

  /**
   * Create an input stream to a file with optional compression.
   *
   * @param file
   * @return
   * @throws FileNotFoundException if the file does not exist or cannot be read.
   * @throws IOException if the file is incorrectly formatted.
   */
  public static InputStream createInputStream(Path file) throws FileNotFoundException, IOException
  {
    String compression = getCompressionExtension(file);
    if (compression.equals(""))
    {
      return Files.newInputStream(file);
    }
    if (compression.equals("gz"))
    {
      return new GZIPInputStream(Files.newInputStream(file));
    }
    throw new UnsupportedOperationException("File compression " + compression + " not supported yet.");
  }

  public static String getFileExtension(Path file)
  {
    return PathUtilities.getFileExtension(file);
  }

  private static boolean isCompressionExtension(String string)
  {
    if ("zip".equals(string))
    {
      return true;
    }
    if ("bz".equals(string))
    {
      return true;
    }
    if ("gz".equals(string))
    {
      return true;
    }
    return false;
  }

  /**
   * Create an output stream with optional compression.
   *
   * @param file
   * @return
   * @throws FileNotFoundException if the file cannot be written to.
   * @throws IOException if an I/O error occurs.
   */
  public static OutputStream createOutputStream(Path file) throws FileNotFoundException, IOException
  {
    String compression = getCompressionExtension(file);
    if (compression.equals(""))
    {
      return Files.newOutputStream(file);
    }
    if (compression.equals("gz"))
    {
      return new GZIPOutputStream(Files.newOutputStream(file));
    }
    throw new UnsupportedOperationException("File compression " + compression + " not supported yet.");
  }
}
