/*
 * Copyright 2016, Lawrence Livermore National Security, LLC. 
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.utility;

import gov.llnl.utility.annotation.Internal;
import gov.llnl.utility.xml.bind.ElementContextImpl;
import gov.llnl.utility.xml.bind.SectionImpl;
import gov.llnl.utility.io.PathLocation;
import gov.llnl.utility.io.ReaderException;
import gov.llnl.utility.xml.DomBuilder;
import gov.llnl.utility.xml.bind.DocumentReader;
import gov.llnl.utility.xml.bind.ElementGroup;
import gov.llnl.utility.xml.bind.ObjectReader;
import gov.llnl.utility.xml.bind.PolymorphicReader;
import gov.llnl.utility.xml.bind.PropertyMap;
import gov.llnl.utility.xml.bind.Reader;
import gov.llnl.utility.xml.bind.ReaderContext;
import gov.llnl.utility.xml.bind.Schema;
import gov.llnl.utility.xml.bind.SchemaBuilder;
import java.net.URI;
import java.net.URL;
import java.util.EnumSet;
import java.util.Map;
import java.util.function.BiConsumer;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.Attributes;

/**
 * Contain static classes needed by other test
 *
 * @author pham21
 */
public class TestSupport
{

  public static Element newElement(String name)
  {
    try
    {
      DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
      DocumentBuilder documentBuilder = dbf.newDocumentBuilder();
      DOMImplementation dom = documentBuilder.getDOMImplementation();
      Document document = dom.createDocument(null, null, null);
      return document.createElement(name);
    }
    catch (ParserConfigurationException ex)
    {
      throw new RuntimeException();
    }
  }

  // <editor-fold defaultstate="collapsed" desc="Class ElementHandlerHarness">
  public static class ElementHandlerHarness implements Reader.ElementHandler
  {
    public Reader.ElementHandler nextHandler;
    public String key;
    public BiConsumer<Object, Object> method;
    public ElementGroup parentElementGroup;
    public EnumSet<Reader.Option> options;

    @Override
    public Reader getReader()
    {
      throw new UnsupportedOperationException();
    }

    @Override
    public String getKey()
    {
      return key;
    }

    @Override
    public String getName()
    {
      throw new UnsupportedOperationException();
    }

    @Override
    public EnumSet<Reader.Option> getOptions()
    {
      return this.options;
    }

    @Override
    public Class getObjectClass()
    {
      throw new UnsupportedOperationException();
    }

    @Override
    public Reader.ElementHandler getNextHandler()
    {
      return nextHandler;
    }

    @Override
    public ElementGroup getParentGroup()
    {
      return parentElementGroup;
    }

    @Override
    public void setParentGroup(ElementGroup parentGroup)
    {
      this.parentElementGroup = parentGroup;
    }

    @Override
    public void setNextHandler(Reader.ElementHandler handler)
    {
      this.nextHandler = handler;
    }

    @Override
    public void addOption(Reader.Option option)
    {
      if (this.options == null)
        options = EnumSet.of(option);
      else
        options.add(option);
    }

    @Override
    public boolean hasOption(Reader.Option option)
    {
      if (this.options == null)
        return false;
      return options.contains(option);
    }
  }
  // </editor-fold>

  // <editor-fold defaultstate="collapsed" desc="Class TestAnyHandler">
//  public static class TestAnyHandler extends ElementHandlerHarness implements AnyHandler
//  {
//
//    @Override
//    public Reader.ElementHandler getReaderFor(String namespaceURI, String localName, String qualifiedName, Attributes attr) throws ReaderException
//    {
//      throw new UnsupportedOperationException();
//    }
//
//  }
  // </editor-fold>
  // <editor-fold defaultstate="collapsed" desc="Class ElementGroupHarness">
  public static class ElementGroupHarness implements ElementGroup
  {
    public ElementGroup parent;

    @Override
    public DomBuilder createSchemaGroup(DomBuilder type)
    {
      return type;
    }

    @Override
    public EnumSet<Reader.Option> getElementOptions()
    {
      throw new UnsupportedOperationException();
    }

    @Override
    public ElementGroup getParent()
    {
      return parent;
    }
  }
  // </editor-fold>

  // <editor-fold defaultstate="collapsed" desc="Class TestReader">
  @Reader.Declaration(pkg = TestPackage.class,
          name = "#TestReader", order = Reader.Order.SEQUENCE,
          referenceable = true, contents = Reader.Contents.TEXT,
          autoAttributes = true, typeName = "TestReaderType")
  static public class TestReader<T> extends ObjectReader<T>
  {
    public Class<T> cls;
    public T obj;  // FIXME this code is not reentrant.  Replace.

    /**
     * Used to build the schema.
     */
    @Internal
    TestReader()
    {
      this.cls = null;
    }

    private TestReader(Class<T> cls)
    {
      this.cls = cls;
    }

    static public <T> TestReader<T> of(Class<T> cls)
    {
      return new TestReader<>(cls);
    }

    @Override
    public T start(ReaderContext context, Attributes attributes) throws ReaderException
    {
      context.setState(this);
      return obj;
    }

    @Override
    public T end(ReaderContext context) throws ReaderException
    {
      return obj;
    }

    @Override
    public Class getObjectClass()
    {
      return cls;
    }
//
//    private static void setObj(ReaderContext context, Object o, Object obj)
//    {
//      TestReader self = (TestReader) context.getState();
//      self.obj = obj;
//    }

    @Override
    public T contents(ReaderContext context, String textContents) throws ReaderException
    {
      return obj;
    }

    @Override
    public DomBuilder createSchemaElement(SchemaBuilder builder, String name, DomBuilder group, boolean topLevel) throws ReaderException
    {
      return group;
    }

  }
  // </editor-fold>

  // <editor-fold defaultstate="collapsed" desc="Class TestReaderMixed">
  @Reader.Declaration(pkg = TestPackage.class,
          name = "TestReader", order = Reader.Order.SEQUENCE,
          contents = Reader.Contents.MIXED, 
          options = Reader.Option.CONTENT_REQUIRED)
  static public class TestReaderMixed<T> extends ObjectReader<T>
  {
    Class<T> cls;

    /**
     * Used to build the schema.
     */
    @Internal
    TestReaderMixed()
    {
      this.cls = null;
    }

    private TestReaderMixed(Class<T> cls)
    {
      this.cls = cls;
    }

    static public <T> TestReaderMixed<T> of(Class<T> cls)
    {
      return new TestReaderMixed<>(cls);
    }

    @Override
    public T start(ReaderContext context, Attributes attributes) throws ReaderException
    {
      return null;
    }

    @Override
    public T end(ReaderContext context) throws ReaderException
    {
      return (T) context.getState();
    }

    @Override
    public ElementHandlerMap getHandlers(ReaderContext context) throws ReaderException
    {
      ReaderBuilder<T> builder = this.newBuilder();
      builder.any(cls)
              .callContext((c, o, v) -> c.setState(v));
      return builder.getHandlers();
    }

    @Override
    public Class getObjectClass()
    {
      return cls;
    }

  }
  // </editor-fold>

  // <editor-fold defaultstate="collapsed" desc="Class TestSection">
  @Reader.Declaration(pkg = UtilityPackage.class,
          name = "TestSection", contents = Reader.Contents.TEXT)
  static public class TestSection implements Reader.SectionInterface
  {
    public Attributes attributes;
    public ElementHandlerMap elementHandlerMap;

    @Override
    public Object start(ReaderContext context, Attributes attributes) throws ReaderException
    {
      this.attributes = attributes;
      return null;
    }

    @Override
    public Object contents(ReaderContext context, String textContents) throws ReaderException
    {
      return null;
    }

    @Override
    public Object end(ReaderContext context) throws ReaderException
    {
      return null;
    }

    @Override
    public ElementHandlerMap getHandlers(ReaderContext context) throws ReaderException
    {
      return elementHandlerMap;
    }

    @Override
    public String getHandlerKey()
    {
      return "TestSection";
    }

    @Override
    public String getSchemaType()
    {
      throw new UnsupportedOperationException();
    }

    @Override
    public DomBuilder createSchemaElement(SchemaBuilder builder, String name, DomBuilder group, boolean topLevel) throws ReaderException
    {
      return group;
    }

  }
  // </editor-fold>

  // <editor-fold defaultstate="collapsed" desc="Class TestReaderContext">
  static public class TestReaderContext implements ReaderContext
  {
    public ElementContext handlerContext;

    public static ReaderContext create()
    {
      TestReaderContext out = new TestReaderContext();
      out.handlerContext = new ElementContextImpl(null, null, null, null, null);
      return out;
    }

    @Override
    public DocumentReader getDocumentReader()
    {
      throw new UnsupportedOperationException();
    }

    @Override
    public <T> T get(String name, Class<T> cls)
    {
      throw new UnsupportedOperationException();
    }

    @Override
    public Iterable<Map.Entry<String, Object>> getReferences()
    {
      throw new UnsupportedOperationException();
    }

    @Override
    public <Obj> Obj put(String name, Obj object) throws ReaderException
    {
      throw new UnsupportedOperationException();
    }

    @Override
    public String getElementPath()
    {
      throw new UnsupportedOperationException();
    }

    @Override
    public URL getExternal(String extern) throws ReaderException
    {
      throw new UnsupportedOperationException();
    }

    @Override
    public URI getFile()
    {
      throw new UnsupportedOperationException();
    }

    @Override
    public PathLocation getLocation()
    {
      throw new UnsupportedOperationException();
    }

    @Override
    public ElementContext getCurrentContext()
    {
      return handlerContext;
    }

    @Override
    public ElementContext getChildContext()
    {
      throw new UnsupportedOperationException();
    }

    @Override
    public void setErrorHandler(ExceptionHandler handler)
    {
      throw new UnsupportedOperationException();
    }

    @Override
    public void handleException(Throwable ex) throws ReaderException
    {
      throw new UnsupportedOperationException();
    }

    @Override
    public void setPropertyHandler(PropertyMap handler)
    {
      throw new UnsupportedOperationException();
    }

    @Override
    public <T, T2> void addDeferred(T target, BiConsumer<T, T2> method, String refId, Class<T2> cls) throws ReaderException
    {
      throw new UnsupportedOperationException();
    }

    @Override
    public void pushTemporaryContext(Object object, Object previous)
    {
    }

    @Override
    public void popTemporaryContext()
    {
    }

    public Object state;

    @Override
    public Object getState()
    {
      return state;
    }

    @Override
    public void setState(Object state)
    {
      this.state = state;
    }

    @Override
    public ElementContext getParentContext()
    {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ElementContext getContext(Class cls)
    {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

  }
  // </editor-fold>

  // <editor-fold defaultstate="collapsed" desc="Class TestPolyReader">
  @Reader.Declaration(pkg = TestPackage.class,
          name = "TestPolyReader", contents = Reader.Contents.TEXT,
          cls = String.class)
  static public class TestPolyReader extends PolymorphicReader<String>
  {
    @Override
    public ObjectReader<? extends String>[] getReaders() throws ReaderException
    {
      return group(new TestReader(String.class));
    }

  }
  // </editor-fold>

  // <editor-fold defaultstate="collapsed" desc="Class TestPackage">
  @Schema(namespace = "TestPackage",
          schema = "http://utility.llnl.gov/schema/utility.xsd",
          prefix = "util")
  static public class TestPackage extends PackageResource
  {
    public static final TestPackage SELF = new TestPackage();
    public static final String namespace = "TestPackage";

    private TestPackage()
    {
    }

    public static TestPackage getInstance()
    {
      return SELF;
    }

    @Override
    public String getNamespaceURI()
    {
      return namespace;
    }
  }
  // </editor-fold>

  // <editor-fold defaultstate="collapsed" desc="Class TestSectionImpl">
  @Reader.Declaration(pkg = TestSectionImplPackage.class,
          name = "TestSectionImpl", order = Reader.Order.CHOICE,
          cls = Double.class)
  static public class TestSectionImpl extends SectionImpl
  {

    @Override
    public Reader.ElementHandlerMap getHandlers(ReaderContext context) throws ReaderException
    {
      throw new UnsupportedOperationException();
    }
  }
  // </editor-fold>

  // <editor-fold defaultstate="collapsed" desc="Class TestSectionImplPackage">
  @Schema(namespace = "http://utility.llnl.gov",
          schema = "http://utility.llnl.gov/schema/utility.xsd",
          prefix = "TestSectionPrefix")
  public static class TestSectionImplPackage extends PackageResource
  {
    public static final TestSectionImplPackage SELF = new TestSectionImplPackage();

    private TestSectionImplPackage()
    {
    }

    public static TestSectionImplPackage getInstance()
    {
      return SELF;
    }
  }
  // </editor-fold>

}
