/*
 * Copyright 2016, Lawrence Livermore National Security, LLC.
 * All rights reserved
 *
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.utility.xml.bind;

import gov.llnl.utility.xml.bind.marshallers.ContentMarshallers;
import gov.llnl.utility.PackageResource;
import gov.llnl.utility.annotation.Internal;
import gov.llnl.utility.io.WriterException;
import gov.llnl.utility.xml.DomBuilder;
import gov.llnl.utility.xml.bind.ObjectWriter.WriterAttributes;
import gov.llnl.utility.xml.bind.ObjectWriter.WriterAttributesOptions;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;

/**
 *
 * @author nelson85
 */
@Internal
@SuppressWarnings("unchecked")
public class WriterContextImpl implements WriterContext
{
  final static String ROOT = "##root";
  DocumentWriter documentWriter;
  Document document;
  HashMap<Integer, ReferenceEntry> references = new HashMap<>();
  HashSet<PackageResource> schema = new HashSet<>();
  private ContextEntry currentContext = null;
  private ContextEntry lastContext;
  private final WriteAttributesImpl attributes = new WriteAttributesImpl();
  private final Marshallers marshallers = new Marshallers();
  private final WriterProperties properties = new WriterProperties();

  public WriterContextImpl(DocumentWriter documentWriter, ObjectWriter objectWriter) throws WriterException
  {
    this.documentWriter = documentWriter;
    this.marshallers.addAll(ContentMarshallers.getDefault());
    this.schema.add(objectWriter.getPackage());
  }

  @Override
  public DomBuilder getDomBuilder()
  {
    return current().domBuilder;
  }

  @Override
  public WriterAttributes getAttributes()
  {
    return attributes;
  }

  @Override
  public void setMarshaller(Marshaller marshall)
  {
    this.marshallers.add(marshall);
  }

  @Override
  public Marshaller getMarshaller(Class cls) throws WriterException
  {
    return marshallers.get(cls);
  }

  @Override
  public MarshallerOptions getMarshallerOptions()
  {
    return this.properties;
  }

//<editor-fold desc="properties">
  @Override
  public void setProperty(String key, Object value) throws UnsupportedOperationException
  {
    // Remove the marshaller property check for now as it was not documented.
    this.properties.put(key, value);
  }

  @Override
  public Object getProperty(String key)
  {
    return properties.get(key);
  }

  @Override
  public <T> T getProperty(String key, Class<T> cls, T defaultValue)
  {
    return properties.get(key, cls, defaultValue);
  }
//</editor-fold>

  @Override
  public <Type> void addContents(Type object) throws WriterException
  {
    if (object == null)
      return;
    if (object instanceof String)
      currentContext.domBuilder.text((String) object);
    else
    {
      Marshaller marshaller = marshallers.get(object.getClass());
      if (marshaller == null)
        throw new WriterException("Unable to find marshaller for " + object.getClass().getCanonicalName());
      currentContext.domBuilder.text(marshaller.marshall(object, properties));
    }
  }

  /**
   * Add a new reference to a previously defined object.
   *
   * If key is null it automatically creates with prefix.
   *
   * @param obj is the object to register.
   * @param prefix is the prefix to use for automatic key generation (null will
   * use class name).
   * @param key is the key to use (null is automatic)
   * @return
   */
  public String register(Object obj, String prefix, String key)
  {
    ReferenceEntry ref;

    // The object is already registered if we have the same hashcode
    int hash = obj.hashCode();

    // Compute an automatic reference id if not supplied.
    if (key == null)
    {
      if (prefix == null)
        prefix = obj.getClass().toString();
      key = String.format("%s.%08x", prefix, hash);
    }

    // and object types are the same
    references.put(hash, ref = new ReferenceEntry(key, obj));
    return ref.key;
  }

  /**
   * Get the reference to a previously written object.
   *
   * @param obj
   * @return
   */
  @Override
  public String getReference(Object obj)
  {
    int i = obj.hashCode();
    ReferenceEntry ref = references.get(i);
    if (ref == null || ref.object == null)
      return null;
    if (ref.object.getClass() == obj.getClass())
      return ref.key;
    return null;
  }

  @Override
  public Document newDocument(ObjectWriter writer)
  {
    try
    {
      DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
      DocumentBuilder documentBuilder = dbf.newDocumentBuilder();
      DOMImplementation dom = documentBuilder.getDOMImplementation();
      String ns = "";
      if (writer.getPackage() != null)
        ns = writer.getPackage().getNamespaceURI();
      document = dom.createDocument(
              ns,
              writer.getElementName(), null);
    }
    catch (ParserConfigurationException ex)
    {
      throw new RuntimeException(ex);
    }
    currentContext = new ContextEntry(
            new DomBuilder(document.getDocumentElement()),
            writer, "##root", writer.getPackage(), null);
    writer.setContext(this);
    return document;
  }

  @Override
  public <Type> DomBuilder write(
          ObjectWriter<Type> writer,
          String elementName,
          Type object)
          throws WriterException
  {
    if (currentContext == null)
      throw new RuntimeException("writer called on without a document");

    int options = writer.getOptions();
    ContextEntry entry = pushContext(writer, object, elementName);
    String ref = checkReference(object, options);
    if (ref != null)
    {
      // If the reference was already defined, we don't need to save
      // an identical object.  Instead, mark it as a reference to
      // a previously defined object.
      entry.referenceTo(ref);
      popContext();
      return entry.domBuilder;
    }

    // Copy over the attributes.
    writer.attributes(attributes, object);
    attributes.flush();

    // Fill out the contents
    writer.contents(object);
    entry.addReference(options);
    addReference(entry, object, options);
    popContext();
    return entry.domBuilder;
  }

  <Type> void writeContent(String elementName, Type value) throws WriterException
  {
    PackageResource pkg = current().pkg;
    DomBuilder element = newElement(pkg, elementName);
    pushContext(element, null, elementName, pkg);
    addContents(value);
    popContext();
  }

  @Override
  public WriterBuilderImpl newBuilder(ObjectWriter writer)
  {
    return new WriterBuilderImpl(this, writer);
  }

//<editor-fold desc="context stack">
  ContextEntry pushContext(ObjectWriter writer, Object object, String elementName) throws WriterException
  {
    writer.setContext(this);

    // Get the element name and package
    PackageResource pkg = this.currentContext.pkg;
    if (elementName == null)
    {
      elementName = writer.getElementName();
      pkg = writer.getPackage();
    }

    // We use a magic string for checking if we are at root element
    boolean objectRoot = (elementName.equals(ROOT));

    // Create a new element
    DomBuilder element;
    int options = writer.getOptions();
    if (objectRoot || (options & ObjectWriter.Options.COMMENT) == ObjectWriter.Options.COMMENT)
    {
      element = currentContext.domBuilder;
    }
    else
    {
      element = newElement(pkg, elementName);
    }

    // Pack the opject
    return pushContext(element, object, writer.getElementName(), writer.getPackage());
  }

  ContextEntry pushContext(DomBuilder element, Object object, String elementName, PackageResource pkg)
  {
    return currentContext = new ContextEntry(element, object, elementName, pkg, currentContext);
  }

  void popContext()
  {
    this.lastContext = currentContext;
    this.currentContext = currentContext.previous;
  }

  ContextEntry current()
  {
    return currentContext;
  }

  ContextEntry last()
  {
    return this.lastContext;
  }

//</editor-fold>
//<editor-fold desc="internal">
  DomBuilder newElement(PackageResource pkg, String elementName) throws WriterException
  {
    if (elementName == null)
      throw new WriterException("null element name");

    DomBuilder parent = currentContext.domBuilder;

    PackageResource rootPackage = this.documentWriter.getObjectWriter().getPackage();
    if (pkg == rootPackage || pkg == null)
      return parent.element(elementName);
    else
    {
      this.schema.add(pkg);
      return parent.elementNS(pkg, elementName);
    }
  }

  @Override
  public void clearReferences()
  {
    this.references.clear();
  }

  String checkReference(Object object, int options)
  {
    if ((options & ObjectWriter.Options.REFERENCEABLE) != ObjectWriter.Options.REFERENCEABLE)
      return null;
    String ref = this.getReference(object);
    return ref;
  }

  void addReference(ContextEntry entry, Object object, int options)
  {
    if ((options & ObjectWriter.Options.REFERENCEABLE) != ObjectWriter.Options.REFERENCEABLE)
      return;
    // If it is referenceable then it needs to be registered
    String ref = this.getReference(object);
    if (ref == null)
      entry.setId(null);
  }

//</editor-fold>
//<editor-fold desc="inner classes">
  private class WriteAttributesImpl implements WriterAttributes, WriterAttributesOptions
  {
    String key = null;
    Object object = null;
    Marshaller marshaller = null;
    MarshallerOptions options = properties;

    @Override
    public void add(String key, String value)
    {
      currentContext.domBuilder.attr(key, value);
    }

    @Override
    public WriterAttributesOptions add(String key, Object object) throws WriterException
    {
      flush();

      // If the value is null then we simply add an empty attribute
      // there is no way currently to differential between an empty attribute and 
      // null.
      if (object == null)
      {
        currentContext.domBuilder.attr(key, "");
        return this;
      }

      // Find the appropriate marshaller.
      marshaller = marshallers.get(object.getClass());

      // No marshaller avaialable.
      if (marshaller == null)
        throw new WriterException("Cannot find marshaller for " + object.getClass());

      // Add the marshaller to the buffer.  We will not actually write the 
      // value to the element until the next call to flush.  This is because
      // writer options may be applied so we can't complete until all options
      // have been registered.
      this.key = key;
      this.object = object;
      return this;
    }

    @Override
    public void setId(String id)
    {
      if (id == null)
        return;
      currentContext.setId(id);
    }

    @Override
    public ObjectWriter.WriterAttributesOptions set(final String key, final Object obj)
    {
      this.options = new MarshallerOptionsProxy(this.options, key, obj);
      return this;
    }

    public void flush()
    {
      if (key == null)
        return;
      if (marshaller == null)
      {
        throw new NullPointerException("null marshaller");
      }

      // Add the attribute to the currentContext
      currentContext.domBuilder.attr(key, marshaller.marshall(object, options));

      // Clear the key.
      key = null;
      object = null;
      options = properties;
    }
  }

  public class Marshallers extends HashMap<Integer, Marshaller>
  {
    public void addAll(List<Marshaller> marshall)
    {
      for (Marshaller m : marshall)
      {
        setMarshaller(m);
      }
    }

    public void add(Marshaller marshall)
    {
      int hash = System.identityHashCode(marshall.getObjectClass());
      this.put(hash, marshall);
    }

    public Marshaller get(Class cls)
    {
      int hash = System.identityHashCode(cls);
      return this.get(hash);
    }
  }

  @Internal
  static class ReferenceEntry
  {
    Object object;
    String key;

    ReferenceEntry(String key, Object obj)
    {
      this.key = key;
      this.object = obj;
    }
  }

  @Internal
  class ContextEntry
  {
    DomBuilder domBuilder;
    final Object object;
    final String elementName;
    final PackageResource pkg;
    final ContextEntry previous;
    boolean defined = true;

    ContextEntry(DomBuilder element, Object object, String elementName, PackageResource pkg, ContextEntry previous)
    {
      this.domBuilder = element;
      this.object = object;
      this.elementName = elementName;
      this.pkg = pkg;
      this.previous = previous;
    }

    void referenceTo(String ref)
    {
      defined = false;
      domBuilder.attr("ref_id", ref);
    }

    void setId(String id)
    {
      if (!defined)
        return;
      String ref = register(object, elementName, id);
      domBuilder.attr("id", ref);
    }

    private void addReference(int options)
    {
      if ((options & ObjectWriter.Options.REFERENCEABLE) != ObjectWriter.Options.REFERENCEABLE)
        return;
      // If it is referenceable then it needs to be registered
      String ref = getReference(object);
      if (ref == null)
      {
        ref = this.domBuilder.toElement().getAttribute("id");
        setId(ref);
      }
    }
  }

//</editor-fold>
}
