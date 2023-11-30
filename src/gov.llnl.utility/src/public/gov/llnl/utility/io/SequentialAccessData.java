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
 * @author seilhan3
 */
public interface SequentialAccessData
{
  /**
   * Skips over a specified number of records.
   *
   * @param records is the number of records to skip.
   * @throws IOException if the seek fails
   */
  void skip(int records) throws IOException;
}
