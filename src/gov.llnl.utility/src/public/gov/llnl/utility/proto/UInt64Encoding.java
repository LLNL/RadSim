/*
 * Copyright 2022, Lawrence Livermore National Security, LLC.
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.utility.proto;

/**
 * Encoding as a unsigned long as variable encoding..
 *
 * This does nothing as Java does not distinguish between signed and unsigned.
 *
 * @author nelson85
 */
class UInt64Encoding extends Int64Encoding
{
  final static UInt64Encoding INSTANCE = new UInt64Encoding();

  @Override
  public String getSchemaName()
  {
    return "uint64";
  }

}
