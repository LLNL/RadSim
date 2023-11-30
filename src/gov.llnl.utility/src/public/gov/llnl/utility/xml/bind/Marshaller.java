/*
 * Copyright 2022, Lawrence Livermore National Security, LLC.
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.utility.xml.bind;

/**
 * Marshallers are used to convert object to strings.
 *
 * @param <Type>
 */
public interface Marshaller<Type>
{
  Class<Type> getObjectClass();

  String marshall(Type o, WriterContext.MarshallerOptions options);
  
}
