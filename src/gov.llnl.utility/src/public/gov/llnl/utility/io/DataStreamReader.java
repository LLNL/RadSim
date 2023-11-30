/*
 * Copyright 2016, Lawrence Livermore National Security, LLC. 
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.utility.io;

import java.io.Closeable;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;

/**
 * Base class for data storage that holds many records as a serial stream. For
 * example, DataProcessor stream, pcf files, asc files.
 *
 * @author nelson85
 * @param <T>
 */
public interface DataStreamReader<T> extends Closeable, PropertyInterface
{
  void openFile(Path path) throws FileNotFoundException, IOException, ReaderException;

  void openStream(InputStream stream) throws IOException, ReaderException;

  /**
   * Get the next input in the stream.
   *
   * @return null if nothing is available
   * @throws java.io.IOException
   */
  T getNext() throws IOException, ReaderException;

}
