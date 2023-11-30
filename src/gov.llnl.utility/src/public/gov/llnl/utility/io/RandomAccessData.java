/* 
 * Copyright 2016, Lawrence Livermore National Security, LLC.
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.utility.io;

import java.io.IOException;

/**
 *
 * @author nelson85
 */
public interface RandomAccessData
{
  /**
   * Reposition the writer or reader pointer for the spectrum file. Not
   * supported for all data types
   *
   * @param record to seek (0 counting)
   * @throws java.io.IOException
   */
  void seek(int record) throws IOException;
}
