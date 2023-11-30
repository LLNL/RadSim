/*
 * Copyright 2016, Lawrence Livermore National Security, LLC. 
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.utility.io;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Path;

/**
 *
 * @author nelson85
 * @param <T>
 */
public interface DataFileWriter<T>
{
  /**
   * Save an object to a file.
   *
   * @param path
   * @param object
   * @throws IOException
   * @throws WriterException
   */
  void saveFile(Path path, T object) throws IOException, WriterException;

  /**
   * Write an object to a stream. The stream will be closed on completion.
   *
   * @param stream
   * @param object
   * @throws IOException
   * @throws WriterException
   */
  void saveStream(OutputStream stream, T object) throws IOException, WriterException;
}
