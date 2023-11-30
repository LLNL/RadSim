/*
 * Copyright 2016, Lawrence Livermore National Security, LLC. 
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.utility.xml.bind;

import gov.llnl.utility.io.WriterException;
import java.util.Collection;

/**
 * Convenience class for writing out lists of items.
 *
 * @author nelson85
 */
public class CollectionWriter<Entry> extends ObjectWriter<Collection<Entry>>
{
  private ObjectWriter<Entry> writer;

  public CollectionWriter(ObjectWriter<Entry> writer)
  {
    super(Options.NONE, "collection", writer.getPackage());
    this.writer = writer;
  }

  public static <Type> CollectionWriter<Type> from(Class<Type> cls) throws WriterException
  {
    return new CollectionWriter<>(ObjectWriter.create(cls));
  }

  @Override
  public void attributes(WriterAttributes attributes, Collection object) throws WriterException
  {
  }

  @Override
  public void contents(Collection<Entry> object) throws WriterException
  {
    WriterBuilder wb = newBuilder();
    WriteObject<Entry> wo = wb.writer(writer);
    for (Entry obj : object)
    {
      wo.put(obj);
    }
  }

}
