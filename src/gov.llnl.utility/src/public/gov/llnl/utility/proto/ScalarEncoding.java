/*
 * Copyright 2022, Lawrence Livermore National Security, LLC.
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.utility.proto;

/**
 * Specialization for scalar values.
 * 
 * @author nelson85
 * @param <T>
 */
public abstract class ScalarEncoding<T> implements ProtoEncoding<T>
{
  public abstract int getWireType();
}
