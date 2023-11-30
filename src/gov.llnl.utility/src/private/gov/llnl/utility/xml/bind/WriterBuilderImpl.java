/*
 * Copyright 2016, Lawrence Livermore National Security, LLC.
 * All rights reserved
 *
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.utility.xml.bind;

import gov.llnl.utility.PackageResource;
import gov.llnl.utility.annotation.Internal;
import gov.llnl.utility.io.WriterException;
import gov.llnl.utility.xml.DomBuilder;

/**
 *
 * @author nelson85
 */
@Internal
@SuppressWarnings("unchecked")
class WriterBuilderImpl implements
        ObjectWriter.WriterBuilder,
        ObjectWriter.WriteObject,
        ObjectWriter.WriteOptions,
        ObjectWriter.WriterContents
{
  ObjectWriter parentWriter;
  String elementName = null;
  Producer producer = null;
  WriterContextImpl context = null;

  WriterBuilderImpl(WriterContextImpl context, ObjectWriter writer)
  {
    this.context = context;
    this.parentWriter = writer;
  }

  @Override
  public WriterBuilderImpl element(String name)
  {
    elementName = name;
    producer = null;
    return this;
  }

  @Override
  public <Type> WriterBuilderImpl writer(ObjectWriter<Type> writer)
  {
    producer = new WriterProducer(elementName, writer);
    elementName = null;
    return this;
  }

  @Override
  public <Type> WriterBuilderImpl contents(Class<? extends Type> cls) throws WriterException
  {
    return writer(SchemaManager.getInstance().findObjectWriter(context, cls));
  }

  @Override
  public void comment(String str) throws WriterException
  {
    context.current().domBuilder.comment(str);
  }

  @Override
  public WriterBuilderImpl put(Object object) throws WriterException
  {
    producer.handle(object);
    return this;
  }

  @Override
  public void section(ObjectWriter.Section writer) throws WriterException
  {
    if (elementName == null)
      elementName = writer.getElementName();
    producer = new WriterProducer(elementName, writer);
    producer.handle(context.current().object);
    elementName = null;
  }

  @Override
  public WriterBuilderImpl id(String id) throws WriterException
  {
    context.last().setId(id);
    return this;
  }

  @Override
  public <T> WriterBuilderImpl attr(String key, T value) throws WriterException
  {
    if (value == null)
      return this;
    Marshaller marshaller = context.getMarshaller(value.getClass());
    if (marshaller == null)
      throw new WriterException("No marshaller for " + value.getClass().getName());
    context.last().domBuilder.attr(key,
            marshaller.marshall(value, context.getMarshallerOptions()));
    return this;
  }

//<editor-fold desc="internal">
  @Override
  public <Type> WriterBuilderImpl putContents(Type value) throws WriterException
  {
    try
    {
      context.writeContent(elementName, value);
      elementName = null;
      return this;
    }
    catch (WriterException ex)
    {
      throw new WriterException(String.format("Fail to write element %s", elementName), ex);
    }
  }

  @Override
  public <Type> WriterBuilderImpl putAny(Type value) throws WriterException
  {
    if (elementName == null)
      throw new WriterException("any element not named");
    PackageResource pkg = this.parentWriter.getPackage();
    DomBuilder element = context.newElement(pkg, elementName);
    context.pushContext(element, null, elementName, pkg);
    Class cls = value.getClass();
    ObjectWriter writer = ObjectWriter.create(cls);
    producer = new WriterProducer(null, writer);
    producer.handle(value);
    elementName = null;
    context.popContext();
    return this;
  }

  @Override
  public <Type> WriterBuilderImpl putList(Iterable<Type> value) throws WriterException
  {
    if (elementName == null)
      throw new WriterException("list element not named");
    PackageResource pkg = this.parentWriter.getPackage();
    DomBuilder element = context.newElement(pkg, elementName);
    context.pushContext(element, null, elementName, pkg);
    Class cls = null;
    ObjectWriter writer = null;
    for (Type v : value)
    {
      if (v == null)
        throw new WriterException("null element");
      if (cls != v.getClass())
      {
        cls = v.getClass();
        writer = ObjectWriter.create(cls);
        producer = new WriterProducer(null, writer);
      }
      producer.handle(v);
    }
    elementName = null;
    context.popContext();
    return this;
  }

  @Override
  public <Type> WriterBuilderImpl putList(Iterable<Type> value, ObjectWriter<Type> writer) throws WriterException
  {
    if (elementName == null)
      throw new WriterException("list element not named");
    PackageResource pkg = this.parentWriter.getPackage();
    DomBuilder element = context.newElement(pkg, elementName);
    context.pushContext(element, null, elementName, pkg);
    producer = new WriterProducer(writer.getElementName(), writer);
    elementName = null;
    for (Type v : value)
    {
      producer.handle(v);
    }
    context.popContext();
    return this;
  }

  @Override
  public void reference(String id) throws WriterException
  {
    context.current().domBuilder.element(elementName).attr("ref_id", id);
    elementName = null;
  }

  @Override
  public WriterBuilderImpl put()
          throws WriterException
  {
    return putContents(null);
  }

  @Override
  public WriterBuilderImpl putString(String value)
          throws WriterException
  {
    return putContents(value);
  }

  @Override
  public WriterBuilderImpl putInteger(int value)
          throws WriterException
  {
    return putContents(value);
  }

  @Override
  public WriterBuilderImpl putLong(long value)
          throws WriterException
  {
    return putContents(value);
  }

  @Override
  public WriterBuilderImpl putDouble(double value)
          throws WriterException
  {
    return putContents(value);
  }

  @Override
  public WriterBuilderImpl putDouble(double value, String format)
          throws WriterException
  {
    return putContents(String.format(format, value));
  }

  @Override
  public WriterBuilderImpl putBoolean(boolean value)
          throws WriterException
  {
    return putContents(value);
  }

  @Override
  public WriterBuilderImpl putFlag(boolean value)
          throws WriterException
  {
    if (value == true)
      putContents(null);
    elementName = null;
    return this;
  }

  interface Producer
  {
    void handle(Object object) throws WriterException;
  }

  class WriterProducer implements Producer
  {
    String elementName;
    ObjectWriter writer;

    WriterProducer(String elementName, ObjectWriter writer)
    {
      this.elementName = elementName;
      this.writer = writer;
    }

    @Override
    public void handle(Object object) throws WriterException
    {
      context.write(writer, elementName, object);
    }
  }
//</editor-fold>
}
