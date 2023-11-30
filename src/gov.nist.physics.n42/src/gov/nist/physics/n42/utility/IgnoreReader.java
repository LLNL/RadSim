/*
 * Copyright 2022, Lawrence Livermore National Security, LLC.
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.nist.physics.n42.utility;

import gov.llnl.utility.io.ReaderException;
import gov.llnl.utility.xml.bind.AnyReader;
import gov.llnl.utility.xml.bind.Reader;
import gov.llnl.utility.xml.bind.ReaderContext;
import gov.nist.physics.n42.N42Package;
import org.xml.sax.Attributes;

/**
 *
 * @author nelson85
 */
@Reader.Declaration(pkg=N42Package.class, name="ignore", options=Reader.Option.NO_SCHEMA)
public class IgnoreReader extends AnyReader<Object>
{

  public IgnoreReader()
  {
    super(Object.class);
  }
  
  @Override
  public Reader.ElementHandlerMap getHandlers(ReaderContext context)
          throws ReaderException
  {
    Reader.ReaderBuilder<Object> builder = this.newBuilder();
    builder.reader(this).nop().unbounded();
    return builder.getHandlers();
  }

  @Override
  public Reader findReader(String namespaceURI, String localName, String qualifiedName, Attributes attributes)
  {
    return this;
  }

}
