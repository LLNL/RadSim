/*
 * Copyright 2016, Lawrence Livermore National Security, LLC. 
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.utility.xml.bind.marshallers;

import gov.llnl.utility.xml.bind.Marshaller;
import gov.llnl.utility.xml.bind.WriterContext;

/**
 *
 * @author nelson85
 */
class StringMarshaller implements Marshaller<String>
{
  @Override
  public String marshall(String o, WriterContext.MarshallerOptions options)
  {
    return o;
  }

  @Override
  public Class<String> getObjectClass()
  {
    return String.class;
  }
  
}
