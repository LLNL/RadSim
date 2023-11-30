/*
 * Copyright 2022, Lawrence Livermore National Security, LLC.
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.utility.proto;

/**
 * Encoding as a unsigned int as variable encoding.
 *
 * This does nothing as Java does not distinguish between signed and unsigned.
 * 
 * @author nelson85
 */
class UInt32Encoding extends Int32Encoding
{
  final static UInt32Encoding INSTANCE = new UInt32Encoding();

  @Override
  public String getSchemaName()
  {
    return "uint32";
  }
}
