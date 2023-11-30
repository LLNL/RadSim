/*
 * Copyright 2016, Lawrence Livermore National Security, LLC. 
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.utility.io;

import gov.llnl.utility.UtilityPackage;
import gov.llnl.utility.annotation.Internal;
import gov.llnl.utility.xml.bind.ObjectReader;
import gov.llnl.utility.xml.bind.Reader;
import gov.llnl.utility.xml.bind.ReaderContext;
import org.xml.sax.Attributes;

@Internal
@Reader.Declaration(pkg = UtilityPackage.class, name = "dataContextFactory",
        document = true, referenceable = true, cls = DataContentFactory.class)
public class DataContentFactoryReader extends ObjectReader<DataContentFactory>
{
  @Override
  public ElementHandlerMap getHandlers(ReaderContext context) throws ReaderException
  {
    ReaderBuilder<DataContentFactory> builder = this.newBuilder();
    builder.element("handler")
            .contents(DataContentFactoryImpl.ContentHandler.class)
            .call(DataContentFactoryReader::addHandler);
    return builder.getHandlers();
  }

  @Override
  public DataContentFactory start(ReaderContext context, Attributes attributes) throws ReaderException
  {
    return new DataContentFactoryImpl();
  }

  private static void addHandler(DataContentFactory obj, DataContentFactoryImpl.ContentHandler ch)
  {
    ((DataContentFactoryImpl) obj).addContentHandler(ch);
  }

}
