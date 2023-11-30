/*
 * Copyright 2016, Lawrence Livermore National Security, LLC. 
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.utility.xml.bind.marshallers;

import gov.llnl.utility.ArrayEncoding;
import gov.llnl.utility.xml.bind.Marshaller;
import gov.llnl.utility.xml.bind.WriterContext;

/**
 *
 * @author nelson85
 */
class LongArrayMarshaller implements Marshaller<long[]>
{
  @Override
  public String marshall(long[] o, WriterContext.MarshallerOptions options)
  {
    return ArrayEncoding.encodeLongs(o);
  }

  @Override
  public Class<long[]> getObjectClass()
  {
    return long[].class;
  }

}
