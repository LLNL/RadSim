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
class FloatMarshaller implements Marshaller<Float>
{
  @Override
  public String marshall(Float o, WriterContext.MarshallerOptions options)
  {
    if (options == null)
      return o.toString();
    String format = options.get(ObjectWriter.DOUBLE_FORMAT, String.class, null);
    if (format == null && o == 0)
      return "0";
    if (format == null)
      return String.format("%.8e", o).replaceFirst("\\.?0+e", "e");
    return String.format(format, o);
  }

  @Override
  public Class<Float> getObjectClass()
  {
    return Float.class;
  }
  
}
