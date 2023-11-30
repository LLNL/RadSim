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
class BooleanMarshaller implements Marshaller<Boolean>
{
  @Override
  public String marshall(Boolean o, WriterContext.MarshallerOptions options)
  {
    return o.toString();
  }

  @Override
  public Class<Boolean> getObjectClass()
  {
    return Boolean.class;
  }
 
}
