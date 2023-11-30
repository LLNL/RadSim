/*
 * Copyright 2016, Lawrence Livermore National Security, LLC. 
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.utility.xml.bind;

import gov.llnl.utility.PackageResource;
import gov.llnl.utility.annotation.Internal;
import gov.llnl.utility.io.ReaderException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import org.xml.sax.Attributes;

/**
 *
 * @author nelson85
 */
@Internal
public class ObjectListReader<T> extends ObjectReader<List<T>>
{
  private final ObjectReader reader;
  private final String name;
  private final PackageResource schema;

  public ObjectListReader(ObjectReader reader, String name, PackageResource schema)
  {
    this.name = name;
    this.schema = schema;
    this.reader = reader;
    if (reader == null)
      throw new NullPointerException();
  }

  @Override
  public Reader.Declaration getDeclaration()
  {
    return new ReaderDeclarationImpl()
    {
      @Override
      public Class<? extends PackageResource> pkg()
      {
        return schema.getClass();
      }

      @Override
      public String name()
      {
        return name;
      }
    };
  }

  @Override
  public List<T> start(ReaderContext context, Attributes attributes) throws ReaderException
  {
    return new ArrayList<>();
  }

  @Override
  @SuppressWarnings("unchecked")
  public ElementHandlerMap getHandlers(ReaderContext context) throws ReaderException
  {
    ReaderBuilder<List<T>> builder = this.newBuilder();

    ReaderBuilderCall source;
    if (reader instanceof PolymorphicReader)
    {
      PolymorphicReader poly = (PolymorphicReader) reader;
      source = builder.readers(poly.getObjectClass(), poly.getReaders());
    }
    else if (reader instanceof AnyReader)
    {
      source = builder.anyReader(reader);
    }
    else
    {
      source = builder.element(reader.getXmlName()).reader(reader);
    }

    BiConsumer<List<T>, T> method = (l, t) ->
    {
      l.add(t);
    };
    source.call(method).optional();
    return builder.getHandlers();
  }

  @Override
  @SuppressWarnings("unchecked")
  public Class getObjectClass()
  {
    return List.class;
  }

  @Override
  public String getSchemaType()
  {
    return "List-" + reader.getSchemaType();
  }

}
