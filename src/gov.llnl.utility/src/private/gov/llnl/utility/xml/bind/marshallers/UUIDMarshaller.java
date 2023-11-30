/*
 * Copyright 2016, Lawrence Livermore National Security, LLC. 
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.utility.xml.bind.marshallers;

import gov.llnl.utility.xml.bind.Marshaller;
import gov.llnl.utility.xml.bind.WriterContext;
import java.util.UUID;

/**
 *
 * @author nelson85
 */
class UUIDMarshaller implements Marshaller<UUID>
{
  @Override
  public Class<UUID> getObjectClass()
  {
    return UUID.class;
  }

  @Override
  public String marshall(UUID o, WriterContext.MarshallerOptions options)
  {
    return o.toString();
  }

}
