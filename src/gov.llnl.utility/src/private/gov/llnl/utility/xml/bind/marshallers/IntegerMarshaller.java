/*
 * Copyright 2016, Lawrence Livermore National Security, LLC. 
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.utility.xml.bind.marshallers;

import gov.llnl.utility.xml.bind.Marshaller;
import gov.llnl.utility.xml.bind.ObjectWriter;
import gov.llnl.utility.xml.bind.WriterContext;

/**
 *
 * @author nelson85
 */
class IntegerMarshaller implements Marshaller<Integer>
{
  @Override
  public String marshall(Integer o, WriterContext.MarshallerOptions options)
  {
    if (options == null)
      return o.toString();
    String format = options.get(ObjectWriter.INTEGER_FORMAT, String.class, null);
    if (format == null)
      return o.toString();
    return String.format(format, o);
  }

  @Override
  public Class<Integer> getObjectClass()
  {
    return Integer.class;
  }
 
}
