/*
 * Copyright 2022, Lawrence Livermore National Security, LLC.
 * All rights reserved.
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.ensdf;

public interface LineSource
{

  /**
   * Check if there are further lines in the buffer.
   *
   * @return
   */
  boolean isEmpty();

  /**
   * Get the next line.
   *
   * @return
   */
  String pop();

  /**
   * Push a new line on the stack.
   *
   * @param str
   * @return
   */
  void push(String str);

}
