/*
 * Copyright 2016, Lawrence Livermore National Security, LLC. 
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.utility.io;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;

/**
 *
 * @author nelson85
 * @param <T>
 */
public interface DataFileReader<T>
{
  /**
   * Load a file into a class.
   *
   * @param path is the file to be read
   * @return the object that was read
   * @throws FileNotFoundException if the file cannot be found
   * @throws IOException if any io errors occur while reading
   * @throws ReaderException if the file format is incorrect
   */
  T loadFile(Path path) throws FileNotFoundException, IOException, ReaderException;

  /**
   * Load a stream into a class. Must close the stream when complete.
   *
   * @param stream is the file to be read
   * @return the object that was read
   * @throws FileNotFoundException if the file cannot be found
   * @throws IOException if any io errors occur while reading
   * @throws ReaderException if the file format is incorrect
   */
  T loadStream(InputStream stream) throws IOException, ReaderException;

}
