/*
 * Copyright 2016, Lawrence Livermore National Security, LLC.
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.utility.xml.bind;

import gov.llnl.utility.PackageResource;
import gov.llnl.utility.io.WriterException;
import java.util.List;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * ObjectWriters serve as templates to place XML contents in a document.
 *
 * @author nelson85
 * @param <Type>
 */
public abstract class ObjectWriter<Type>
{
  public static final String DOUBLE_FORMAT = "java.lang.Double#format";
  public static final String INTEGER_FORMAT = "java.lang.Integer#format";
  public static final String DATE_FORMAT = "java.util.Date#format";

  final private String elementName;
  final private PackageResource pkg;
  final private int options;
  WriterContext context;

  public ObjectWriter(int options, String name, PackageResource pkg)
  {
    this.options = options;
    this.elementName = name;
    this.pkg = pkg;
    if(this.elementName == null || this.elementName.isBlank())
    {
      throw new UnsupportedOperationException("name cannot be none");
    }
  }

  /**
   * Create the object writer for this class. Searches in order: Primitives,
   * annotations, and contents map.
   *
   * @param cls
   * @return does not return null.
   * @throws WriterException if the contents cannot be found.
   */
  public static <T> ObjectWriter<T> create(Class<T> cls) throws WriterException
  {
    return SchemaManager.getInstance().findObjectWriter(null, cls);
  }

  /**
   * Place any attributes that are required for this element. This operation is
   * only called if the element is contained in the body of the document. Thus
   * do not place anything other than attributes.
   *
   * @param attributes
   * @param object
   * @throws WriterException
   */
  public abstract void attributes(WriterAttributes attributes, Type object) throws WriterException;

  /**
   * Place document contents in the element. Elements should contain element
   * contents or texts contents, but not both.
   *
   * @param object
   * @throws WriterException
   */
  public abstract void contents(Type object) throws WriterException;

  final public void addContents(Object object) throws WriterException
  {
    getContext().addContents(object);
  }

  /**
   * Create a new WriterBuilder for pushing contents.
   *
   * @return a new builder for this object.
   */
  protected WriterBuilder newBuilder()
  {
    return context.newBuilder(this);
  }
//<editor-fold desc="output">

  /**
   * Convert an object to an XML representation.
   *
   * @param t
   * @return
   * @throws WriterException
   */
  public String toXML(Type t) throws WriterException
  {
    return GenericDocumentWriter.dumpXML(this, t);
  }

  public Element convertToElement(Document document, Type object) throws WriterException
  {
    // Convert the writer into a document
    DocumentWriter<Type> writer = new DocumentWriterImpl<>(this);
    Document document2 = writer.toDocument(object);
    Element element = document2.getDocumentElement();

    // Transfer the element from doc2 to the target document
    element = (Element) document.adoptNode(element);
    return element;
  }
//</editor-fold>
//<editor-fold desc="getters">

  public final String getElementName()
  {
    return elementName;
  }

  public final int getOptions()
  {
    return options;
  }

  public final PackageResource getPackage()
  {
    return pkg;
  }

  public WriterContext getContext() throws WriterException
  {
    return context;
  }

  public final void setContext(WriterContext context)
  {
    this.context = context;
  }

  public abstract class Section extends ObjectWriter<Type>
  {
    public Section(String name)
    {
      super(Options.NONE, name, pkg);
    }

    @Override
    final public void attributes(WriterAttributes attributes, Type object) throws WriterException
    {
    }
  }

//</editor-fold>
//<editor-fold desc="attributes">
  /**
   * Used to add attributes to an element. Currently this only supports
   * attributes in the same namespace as the element being written.
   */
  public interface WriterAttributes
  {
    /**
     * Add a string to attributes.
     *
     * @param value
     * @param object
     */
    void add(String value, String object);

    /**
     * Set an attribute from an object.
     *
     * The object will be converted to a string using the marshaller found.
     *
     * @param value is the attribute local name.
     * @param object is object to be marshalled.
     * @return a handle to the attribute so that parameters can be passed to the
     * marshaller.
     * @throws WriterException
     */
    WriterAttributesOptions add(String value, Object object) throws WriterException;

    /**
     * Set the id for the current element.
     * 
     * This will also register the item for other references to the same
     * object to use.
     *
     * @param id is the id to set, or null if not id to be assigned.
     */
    void setId(String id);
  }

  public interface WriterAttributesOptions
  {
    /**
     * Set properties used by the marshaller.
     *
     * @param key is the marshaller parameter id.
     * @param obj is the value to be passed to the marshaller.
     * @return
     */
    WriterAttributesOptions set(String key, Object obj);
  }

//</editor-fold>
//<editor-fold desc="builder">
  /**
   * WriterBuilder pushes element contents into the document.
   */
  public interface WriterBuilder
  {
    /**
     * Name the element to be placed in the document. This is optional.
     *
     * @param name
     * @return a writer contents for pushing contents.
     */
    WriterContents element(String name);

    /**
     * Apply a writer to the next set of contents.
     *
     * @param <Type>
     * @param writer
     * @return the writer for this object type.
     * @throws WriterException
     */
    <Type> WriteObject<Type> writer(ObjectWriter<Type> writer) throws WriterException;

    /**
     * Write a comment to the output.
     *
     * @param categories
     * @throws WriterException
     */
    void comment(String categories) throws WriterException;

    /**
     * Specify the content type of to be written.
     *
     * The class will be examined for the WriterInfo annotation to see what is
     * the defined writer.
     *
     * @param <Type>
     * @param cls
     * @return
     * @throws WriterException
     */
    <Type> WriteObject<Type> contents(Class<? extends Type> cls) throws WriterException;

    void section(ObjectWriter.Section part) throws WriterException;
  }

  public interface WriterContents
  {

    /**
     * Put object contents into the document. The type must have defined
     * marshaller or have a defined writer.
     *
     * @param <Type> is the type of object to be written.
     * @param value is an object of the required type.
     * @return an object to apply options.
     * @throws WriterException
     */
    <Type> WriteOptions putContents(Type value)
            throws WriterException;
    
    /** 
     * Place an object of unspecified contents.
     * 
     * @param <Type>
     * @param value
     * @return
     * @throws WriterException 
     */
    <Type> WriteOptions putAny(Type value) throws WriterException;

    /**
     * Add a series of items from a collection.
     *
     * @param <Type>
     * @param value
     * @return
     * @throws WriterException
     *
     */
    <Type> WriteOptions putList(Iterable<Type> value) throws WriterException;

    /**
     * Add a series of items from a collection using a writer.
     *
     * @param <Type>
     * @param value
     * @param writer
     * @return
     * @throws WriterException
     */
    <Type> WriteOptions putList(Iterable<Type> value, ObjectWriter<Type> writer) throws WriterException;

    /**
     * Specify a writer for object to this stream.
     *
     * @param <Type>
     * @param writer
     * @return an object to apply options.
     * @throws WriterException
     */
    <Type> WriteObject<Type> writer(ObjectWriter<Type> writer)
            throws WriterException;

    /**
     * Specify a writer for object to this stream.
     *
     * @param <Type>
     * @param cls
     * @return an object to apply options.
     * @throws WriterException
     */
    public <Type> WriteObject<Type> contents(Class<? extends Type> cls)
            throws WriterException;

    /**
     * Write an empty element.
     *
     * @param <Type>
     * @return an object to apply options.
     * @throws WriterException if the context is invalid.
     */
    <Type> WriteOptions put()
            throws WriterException;

    /**
     * Marshall a string as the contents of the element.
     *
     * @param <Type>
     * @param value
     * @return an object to apply options.
     * @throws WriterException if the context is invalid.
     */
    <Type> WriteOptions putString(String value)
            throws WriterException;

    /**
     * Marshall an integer as the contents of the string.
     *
     * @param <Type>
     * @param value
     * @return an object to apply options.
     * @throws WriterException if the context is invalid.
     */
    <Type> WriteOptions putInteger(int value)
            throws WriterException;

    /**
     * Marshall an long as the contents of the string.
     *
     * @param <Type>
     * @param value
     * @return an object to apply options.
     * @throws WriterException if the context is invalid.
     */
    <Type> WriteOptions putLong(long value)
            throws WriterException;

    /**
     * Marshall a double as the contents of the string.
     *
     * @param <Type>
     * @param value
     * @return an object to apply options.
     * @throws WriterException if the context is invalid.
     */
    <Type> WriteOptions putDouble(double value)
            throws WriterException;

    /**
     * Marshall a double as the contents with a specified format. Calls
     * {@code String.format} to format the double.
     *
     * @param <Type>
     * @param value
     * @param format is the format of the double.
     * @return an object to apply options.
     * @throws WriterException if the context is invalid.
     */
    <Type> WriteOptions putDouble(double value, String format)
            throws WriterException;

    /**
     * Marshall a boolean as the contents.
     *
     * @param <Type>
     * @param value
     * @return an object to apply options.
     * @throws WriterException
     */
    <Type> WriteOptions putBoolean(boolean value)
            throws WriterException;

    /**
     * Marshall a boolean as the contents.
     *
     * Adds if true. Does not add it false.
     *
     * @param <Type>
     * @param value
     * @return an object to apply options.
     * @throws WriterException
     */
    <Type> WriteOptions putFlag(boolean value)
            throws WriterException;

    /**
     * Set the reference id for this object.
     *
     * @param id is the unique id for this item.
     * @throws WriterException if the context is invalid.
     */
    void reference(String id) throws WriterException;

  }

  public interface WriteObject<Type>
  {
    /**
     * Put an object into the contents of the current element.
     *
     * @param <Type>
     * @param o
     * @return the WriteObject for chaining.
     * @throws WriterException
     */
    <Type> WriteOptions put(Type o) throws WriterException;

    default void putAll(List<Type> o) throws WriterException
    {
      for (Type item : o)
      {
        this.put(item);
      }
    }
  }

  public interface WriteOptions
  {
    /**
     * Assign a referenceable id to the last placed element.
     *
     * @param format
     * @return a reference to apply options.
     */
    WriteOptions id(String format) throws WriterException;

    <T> WriteOptions attr(String key, T value) throws WriterException;
  }
//</editor-fold>
//<editor-fold desc="options">

  /**
   * Options control how this writer will be used to place contents.
   */
  public static class Options
  {
    /**
     * Default options for a writer element.
     */
    public final static int NONE = 0;

    /**
     * Marker that an element can be referenced to a previous instance.
     *
     * This requires that the element has the option of being empty and can
     * supports the ref_id attribute.
     */
    public final static int REFERENCEABLE = 1;

    public final static int COMMENT = 2;
  }
//</editor-fold>
}
