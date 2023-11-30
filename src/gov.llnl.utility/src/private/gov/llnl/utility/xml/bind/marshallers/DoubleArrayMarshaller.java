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
class DoubleArrayMarshaller implements Marshaller<double[]>
{
  @Override
  public String marshall(double[] o, WriterContext.MarshallerOptions options)
  {
    return ArrayEncoding.encodeDoubles(o);
  }

  @Override
  public Class<double[]> getObjectClass()
  {
    return double[].class;
  }
  
}
