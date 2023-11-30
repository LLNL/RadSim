/*
 * Copyright 2016, Lawrence Livermore National Security, LLC. 
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.utility.io;

import java.io.Closeable;
import java.io.IOException;
import java.nio.file.Path;

/**
 *
 * @author nelson85
 * @param <Type> is the type of object to be stored.
 */
public interface DataStreamWriter<Type> extends Closeable, PropertyInterface
{
  public void openFile(Path path) throws IOException;

  /**
   * Get the next input in the stream.
   *
   * @param object is the object to be written.
   * @throws java.io.IOException if there is a problem writing the file.
   * @throws WriterException if there data cannot be formated for this file.
   */
  public void put(Type object) throws IOException, WriterException;

}
