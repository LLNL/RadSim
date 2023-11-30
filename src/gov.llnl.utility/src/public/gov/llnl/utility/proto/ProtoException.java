/*
 * Copyright 2022, Lawrence Livermore National Security, LLC.
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.utility.proto;

import java.io.IOException;

/**
 *
 * @author nelson85
 */
public class ProtoException extends Exception
{
  int where;

  ProtoException(String message, int where)
  {
    super(message);
    this.where = where;
  }

  ProtoException(String message, int where, IOException ex)
  {
    super(message, ex);
    this.where = where;
  }
}
