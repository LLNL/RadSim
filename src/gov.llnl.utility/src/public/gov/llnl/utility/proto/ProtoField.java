/*
 * Copyright 2022, Lawrence Livermore National Security, LLC.
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.utility.proto;

/**
 * Opaque data structure holding the features for the encoder.
 * 
 * @author nelson85
 */
public class ProtoField
{
  int id;
  String name;
  ProtoEncoding encoding;
  Object setter;
  Object getter;
  boolean repeated;
}
