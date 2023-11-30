/*
 * Copyright 2016, Lawrence Livermore National Security, LLC.
 * All rights reserved
 *
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.utility.xml.bind;

import gov.llnl.utility.PackageResource;
import gov.llnl.utility.UtilityPackage;
import gov.llnl.utility.annotation.Internal;
import static gov.llnl.utility.xml.bind.ElementHandlerImpl.wrapException;
import gov.llnl.utility.xml.bind.readers.BooleanContents;
import gov.llnl.utility.xml.bind.readers.DoubleContents;
import gov.llnl.utility.xml.bind.readers.FlagReader;
import gov.llnl.utility.xml.bind.readers.IntegerContents;
import gov.llnl.utility.xml.bind.readers.LongContents;
import gov.llnl.utility.xml.bind.readers.StringContents;
import gov.llnl.utility.io.ReaderException;
import gov.llnl.utility.xml.bind.Reader.ElementHandler;
import gov.llnl.utility.xml.bind.Reader.ElementHandlerMap;
import gov.llnl.utility.xml.bind.Reader.Option;
import gov.llnl.utility.xml.bind.Reader.Order;
import gov.llnl.utility.xml.bind.Reader.ReaderBuilder;
import gov.llnl.utility.xml.bind.Reader.ReaderBuilderCall;
import gov.llnl.utility.xml.bind.Reader.ReaderBuilderContents;
import gov.llnl.utility.xml.bind.Reader.ReaderBuilderOptions;
import gov.llnl.utility.xml.bind.Reader.SectionInterface;
import java.util.EnumSet;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import org.xml.sax.Attributes;
import gov.llnl.utility.xml.bind.Reader.ReaderContextConsumer;

/**
 * Implementation for all reader builder tags.
 *
 * The interfaces define what can be done at each stage. This class implements
 * all stages.
 *
 * @author nelson85
 * @param <T>
 * @param <T2>
 */
@Internal
@SuppressWarnings("unchecked")
final class ReaderBuilderImpl<T, T2> implements ReaderBuilder<T>,
        ReaderBuilderContents<T>, ReaderBuilderCall<T, T2>, ReaderBuilderOptions
{
  final String uriName;
  final Reader.Declaration decl;
  final PackageResource schema;

  // Auto-chain
  ElementGroupImpl parentGroup;
  final HandlerList handlerList;

  // Transient settings
  ElementHandler lastHandler = null;
  Class resultClass = null;
  String elementName = null;
  Producer producer;

  public ReaderBuilderImpl(
          PackageResource schema,
          Reader.Declaration decl
  )
  {
    this.decl = decl;
    this.schema = schema;

    String namespaceURI = "";
    if (schema != null)
      namespaceURI = schema.getNamespaceURI();
    this.uriName = "#" + namespaceURI;
    EnumSet<Option> options = null;
    if (decl.referenceable())
      options = EnumSet.of(Option.OPTIONAL);
    else if (Option.check(decl.options(), Option.CONTENT_REQUIRED))
      options = EnumSet.of(Option.REQUIRED);

    this.parentGroup = ElementGroupImpl.newInstance(null, decl.order(), options);
    this.handlerList = new HandlerList();
  }

  protected ReaderBuilderImpl(ReaderBuilderImpl rb)
  {
    this.uriName = rb.uriName;
    this.schema = rb.schema;
    this.decl = rb.decl;

    // Transient settings
    this.resultClass = rb.resultClass;
    this.elementName = rb.elementName;
    this.handlerList = rb.handlerList;
    this.parentGroup = rb.parentGroup;
  }

  @Override
  public ElementHandlerMap getHandlers() throws ReaderException
  {
    if (this.producer != null)
      throw new ReaderException("Incomplete reader on getHandlers " + producer);
    return ElementHandlerMapImpl.newInstance(uriName, handlerList);
  }

  @Override
  public ReaderBuilderImpl element(String name)
  {
    this.lastHandler = null;
    this.elementName = name + this.uriName;
    return this;
  }

  @Override
  public ReaderBuilderImpl contents(Class readerClass) throws ReaderException
  {
    return reader(ObjectReader.create(readerClass));
  }

  @Override
  public ReaderBuilderImpl any(Class readerClass) throws ReaderException
  {
    return anyReader(AnyReader.of(readerClass));
  }

  @Override
  public ReaderBuilderImpl anyReader(Reader reader) throws ReaderException
  {
    this.lastHandler = null;
    if (this.producer != null)
      throw new ReaderException("element contents redefined,\n    previous="
              + this.producer.toString() + "\n    new=" + reader.toString());
    if (reader == null)
      throw new ReaderException("reader is null");
    this.resultClass = reader.getObjectClass();
    this.producer = new AnyHandlerProducer(reader);
    return this;
  }

  @Override
  public <Obj> ReaderBuilderImpl reader(Reader<Obj> reader)
          throws ReaderException
  {
    this.lastHandler = null;
    if (this.producer != null)
      throw new ReaderException("element contents redefined,\n    previous="
              + this.producer.toString() + "\n    new=" + reader.toString());
    if (reader == null)
      throw new ReaderException("reader is null");
    this.resultClass = reader.getObjectClass();
    if (reader instanceof AnyReader)
      this.producer = new AnyHandlerProducer(reader);
    else
      this.producer = new ReaderHandlerProducer(reader);
    return this;
  }

  @Override
  public <Obj> ReaderBuilderCall readers(Class<Obj> cls, ObjectReader<? extends Obj>... readers) throws ReaderException
  {
    this.lastHandler = null;
    if (this.producer != null)
      throw new ReaderException("element contents redefined");
    if (readers == null)
      throw new ReaderException("reader is null");

    this.resultClass = cls;
    this.producer = new ReadersProducer(readers);
    return this;
  }

//<editor-fold desc="subs">
  @Override
  public ReaderBuilderImpl group(Order order, Option... options)
  {
    EnumSet<Option> optionSet = null;
    if (options != null && options.length > 0)
      optionSet = EnumSet.of(options[0], options);
    ReaderBuilderImpl out = new ReaderBuilderImpl(this);
    switch (order)
    {
      case OPTIONS:
        out.parentGroup = new ElementGroupImpl.OptionsGroup(parentGroup, optionSet);
        break;
      case ALL:
        out.parentGroup = new ElementGroupImpl.AllGroup(parentGroup, optionSet);
        break;
      case CHOICE:
        out.parentGroup = new ElementGroupImpl.ChoiceGroup(parentGroup, optionSet);
        break;
      case FREE:
        out.parentGroup = new ElementGroupImpl.FreeGroup(parentGroup, optionSet);
        break;
      case SEQUENCE:
        out.parentGroup = new ElementGroupImpl.SequenceGroup(parentGroup, optionSet);
        break;
    }
    return out;
  }

//</editor-fold>
//<editor-fold desc="contents">
  @Override
  public ReaderBuilderImpl reference(Class resultClass) throws ReaderException
  {
    if (producer != null)
      throw new ReaderException("element contents redefined");
    this.resultClass = resultClass;
    this.producer = new ReferenceHandlerProducer();
    return this;
  }

  @Override
  public ReaderBuilderOptions section(SectionInterface section) throws ReaderException
  {
    if (this.producer != null)
      throw new ReaderException("element contents redefined");
    if (section == null)
      throw new ReaderException("section is null");
    this.resultClass = section.getObjectClass();
    this.producer = new SectionHandlerProducer(section);
    executeCall(null);
    define();
    return this;
  }
//</editor-fold>
//<editor-fold desc="setOptions">

  @Override
  public ReaderBuilderImpl options(Option first, Option... rest)
  {
    setOption(first);
    for (Option option : rest)
    {
      setOption(option);
    }
    return this;
  }

  /**
   * Internal call to set an option.
   *
   * @param option
   */
  public void setOption(Option option)
  {
    if (lastHandler == null)
      throw new RuntimeException("null handler");

    // Option apply to all the elements in the last group.
    //  FIXME it is not entirely clear what this is used for
    //  currently.   It may be part of groups of elements, but
    //  further testing is required.
    ElementHandler iter = lastHandler;
    while (iter != null)
    {
      iter.addOption(option);
      iter = iter.getNextHandler();
    }
  }

  @Override
  public ReaderBuilderImpl deferrable()
  {
    setOption(Option.DEFERRABLE);
    return this;
  }

  @Override
  public ReaderBuilderImpl optional()
  {
    setOption(Option.OPTIONAL);
    return this;
  }

  @Override
  public ReaderBuilderImpl required()
  {
    setOption(Option.REQUIRED);
    return this;
  }

  @Override
  public ReaderBuilderImpl unbounded()
  {
    setOption(Option.UNBOUNDED);
    return this;
  }

  @Override
  public ReaderBuilderImpl define()
  {
    setOption(Option.NO_REFERENCE);
    return this;
  }

  @Override
  public ReaderBuilderImpl noid()
  {
    setOption(Option.NO_ID);
    return this;
  }

//</editor-fold>
//<editor-fold desc="call">
  protected <T2> ReaderBuilderImpl executeCall(BiConsumer<T, T2> method) throws ReaderException
  {
    this.lastHandler = producer.newInstance(this, elementName,
            this.parentGroup.getElementOptions(), resultClass, method);
    producer = null;
    elementName = null;
    resultClass = null;
    return this;
  }

  protected void updateResultClass(Class resultClass) throws ReaderException
  {
    if (this.resultClass == null)
      this.contents(resultClass);
    else
    {
      if (!resultClass.isAssignableFrom(this.resultClass))
        throw new ReaderException("Can't assign " + this.resultClass + " to " + resultClass);
      this.resultClass = resultClass;
    }
  }

  @Override
  public ReaderBuilderImpl call(BiConsumer<T, T2> method) throws ReaderException
  {
    return executeCall(method);
  }

  @Override
  public <T3> ReaderBuilderImpl call(BiConsumer<T, T3> method, Class<T3> resultClass) throws ReaderException
  {
    updateResultClass(resultClass);
    ReaderBuilderImpl<T, T3> impl = (ReaderBuilderImpl<T, T3>) this;
    return impl.call(method);
  }

  @Override
  public ReaderBuilderImpl call(Consumer<T> method) throws ReaderException
  {
    // FIXME this seems a bit like a kludge.  I can add a full path for this case,
    reader(new NopReader());
    BiConsumer<T, Integer> proxy = (T t, Integer i) -> method.accept(t);
    return call(proxy, Integer.class);
  }

  @Override
  public <T3> ReaderBuilderImpl callContext(Reader.ReaderContextConsumer<T, T3> method, Class<T3> resultClass) throws ReaderException
  {
    updateResultClass(resultClass);
    ReaderBuilderImpl<T, T3> impl = (ReaderBuilderImpl<T, T3>) this;
    return impl.callContext(method);
  }

  @Override
  public ReaderBuilderImpl callContext(ReaderContextConsumer<T, T2> method) throws ReaderException
  {
    if (method == null)
      throw new ReaderException("Method is null");

    // This is a bit complicated.  If we have a Readers group then we will
    // get more than one item added to the handler list.  In this case we need
    // update all of them.   
    // Record the position at the end of the lsit
    ElementHandler tail = this.handlerList.lastHandler;

    // Add all the handers without a method to call
    executeCall(null);

    // Find the insertion point
    ElementHandler next;
    if (tail == null)
      next = this.handlerList.firstHandler;
    else
      next = tail.getNextHandler();

    // Reset the tail of the list
    this.handlerList.lastHandler = tail;
    if (tail == null)
      this.handlerList.firstHandler = null;

    // Replace all of the elements past the insertion point.
    while (next != null)
    {
      this.add(new ProxyHandler(next)
      {
        @Override
        public void onCall(ReaderContext context, Object parent, Object child) throws ReaderException
        {
          try
          {
            method.call(context, (T) parent, (T2) child);
          }
          catch (RuntimeException ex)
          {
            wrapException(ex, parent);
          }
        }
      });
      next = next.getNextHandler();
    }

    // Proxy the handler with a new method
    if (tail == null)
      this.lastHandler = this.handlerList.firstHandler;
    else
      this.lastHandler = tail.getNextHandler();
    return this;
  }

  @Override
  public ReaderBuilderImpl callString(BiConsumer<T, String> methodName) throws ReaderException
  {
    return this.reader(new StringContents()).call(methodName);
  }

  @Override
  public ReaderBuilderImpl callDouble(BiConsumer<T, Double> methodName) throws ReaderException
  {
    return this.reader(new DoubleContents()).call(methodName);
  }

  @Override
  public ReaderBuilderImpl callInteger(BiConsumer<T, Integer> methodName) throws ReaderException
  {
    return this.reader(new IntegerContents()).call(methodName);
  }

  @Override
  public ReaderBuilderImpl callLong(BiConsumer<T, Long> methodName) throws ReaderException
  {
    return this.reader(new LongContents()).call(methodName);
  }

  @Override
  public ReaderBuilderImpl callBoolean(BiConsumer<T, Boolean> methodName) throws ReaderException
  {
    return this.reader(new BooleanContents()).call(methodName);
  }

  @Override
  public ReaderBuilderImpl nop() throws ReaderException
  {
    return executeCall(null);
  }
//</editor-fold>
//<editor-fold desc="producer">

  String getKey(String value)
  {
    return value + this.uriName;
  }

  @Override
  public ReaderBuilderCall<T, Boolean> flag() throws ReaderException
  {
    return this.reader(new FlagReader());
  }

  @Override
  public <T2> ReaderBuilderCall<T, List<T2>> list(ObjectReader<T2> reader) throws ReaderException
  {
    return this.reader(new ObjectListReader(reader,
            this.elementName,
            this.schema));
  }

  protected interface Producer<T, T2>
  {
    ElementHandler newInstance(ReaderBuilderImpl builder, String elementName, EnumSet<Option> flags, Class<T2> resultClass, BiConsumer<T, T2> method)
            throws ReaderException;
  }

  private class ReaderHandlerProducer<T, T2> implements Producer<T, T2>
  {
    Reader<T2> reader;

    ReaderHandlerProducer(Reader<T2> reader)
    {
      this.reader = reader;
    }

    @Override
    public ElementHandler newInstance(ReaderBuilderImpl builder, String key, EnumSet<Option> flags, Class<T2> resultCls, BiConsumer<T, T2> method) throws ReaderException
    {
      if (key == null)
      {
        // FIXME this check may be necessary but need to examine the use cases.
//        if (reader.getPackage() != parentReader.getPackage())
//          throw new ReaderException("Schema mismatch on element. element name must be specifier in handler.");
        key = reader.getHandlerKey();
      }
      if (key == null)
        throw new ReaderException("No element name for " + reader.getClass());
      ElementHandler out = new ReaderHandler(key, flags, method, reader);
      builder.add(out);
      return out;
    }

    @Override
    public String toString()
    {
      return "reader for " + reader.getObjectClass().toString();
    }
  }

  static private class ReadersProducer<T, T2> implements Producer<T, T2>
  {
    ObjectReader[] obj;

    ReadersProducer(ObjectReader[] obj)
    {
      this.obj = obj;
    }

    @Override
    public ElementHandler newInstance(ReaderBuilderImpl builder, String elementName, EnumSet<Option> flags, Class<T2> resultClass, BiConsumer<T, T2> method)
    {
      ElementHandler out = null;
      for (ObjectReader reader : obj)
      {
        EnumSet<Option> nextFlags = flags;
        ElementHandler next = new ReaderHandler(reader.getHandlerKey(), nextFlags, method, reader);
        if (next.getKey() == null)
          throw new RuntimeException("Missing key for " + reader);
        builder.add(next);
        if (out == null)
          out = next;
      }
      return out;
    }
  }

  static private class SectionHandlerProducer<T, T2> implements Producer<T, T2>
  {
    SectionInterface obj;

    SectionHandlerProducer(SectionInterface obj)
    {
      this.obj = obj;
    }

    @Override
    public ElementHandler newInstance(
            ReaderBuilderImpl builder, String elementName, EnumSet<Option> flags, Class<T2> resultClass, BiConsumer<T, T2> method)
    {
      SectionHandler handler = new SectionHandler(flags, obj);
      builder.add(handler);
      return handler;
    }

    @Override
    public String toString()
    {
      return "section of " + obj.getObjectClass().getClass();
    }
  }

  static private class ReferenceHandlerProducer<T, T2> implements Producer<T, T2>
  {
    ReferenceHandlerProducer()
    {
    }

    @Override
    public ElementHandler newInstance(
            ReaderBuilderImpl builder, String elementName, EnumSet<Option> flags, Class<T2> resultClass, BiConsumer<T, T2> method)
    {
      ElementHandler out = new ReferenceHandler<>(elementName,
              flags, resultClass, method);
      builder.add(out);
      return out;
    }

    @Override
    public String toString()
    {
      return "reference";
    }
  }

  static private class AnyHandlerProducer<T, T2> implements Producer<T, T2>
  {
    Reader reader;

    AnyHandlerProducer(Reader obj)
    {
      this.reader = obj;
    }

    @Override
    public ElementHandler newInstance(ReaderBuilderImpl builder, String key, EnumSet<Option> flags, Class<T2> resultClass, BiConsumer<T, T2> method)
    {
      if (key == null)
      {
        key = "##any";
      }
      ElementHandler out = new ReaderHandler(key, flags, method, reader);
      out.addOption(Reader.Option.ANY);
      builder.add(out);
      return out;
    }

    @Override
    public String toString()
    {
      return "any " + reader.getClass().toString();
    }
  }

//</editor-fold>
//<editor-fold desc="list">
  @Internal
  public static class HandlerList
  {
    public ElementHandler firstHandler;
    public ElementHandler lastHandler;

    void add(ElementHandler handler)
    {
      if (this.firstHandler == null)
      {
        this.firstHandler = handler;
        this.lastHandler = handler;
      }
      else
      {
        lastHandler.setNextHandler(handler);
        this.lastHandler = handler;
      }
    }
  };

  void add(ElementHandler handler)
  {
    handler.setParentGroup(parentGroup);
    handlerList.add(handler);
  }

  @Reader.Declaration(pkg = UtilityPackage.class, name = "nop", cls = Integer.class)
  private class NopReader extends ObjectReader<Integer>
  {

    @Override
    public Integer start(ReaderContext context, Attributes attributes) throws ReaderException
    {
      return null;
    }

    @Override
    public Reader.ElementHandlerMap getHandlers(ReaderContext context) throws ReaderException
    {
      return null;
    }

    @Override
    public String getSchemaType()
    {
      return "";
    }
  }
//</editor-fold>
}
