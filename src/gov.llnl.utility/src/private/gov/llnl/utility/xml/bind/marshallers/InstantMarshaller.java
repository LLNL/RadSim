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
import java.text.SimpleDateFormat;
import java.time.Instant;

/**
 *
 * @author nelson85
 */
class InstantMarshaller implements Marshaller<Instant>
{
  @Override
  public Class<Instant> getObjectClass()
  {
    return Instant.class;
  }

  @Override
  public String marshall(Instant o, WriterContext.MarshallerOptions options)
  {
    if (options == null)
      return o.toString();
    String format = options.get(ObjectWriter.DATE_FORMAT, String.class, null);
    if (format == null)
      return o.toString();
    SimpleDateFormat sdf = new SimpleDateFormat(format);
    return sdf.format(o);
  }

  }
