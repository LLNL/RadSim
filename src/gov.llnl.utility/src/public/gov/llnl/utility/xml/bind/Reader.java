/*
 * Copyright 2016, Lawrence Livermore National Security, LLC.
 * All rights reserved
 *
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.utility.xml.bind;

import gov.llnl.utility.PackageResource;
import gov.llnl.utility.Singletons;
import gov.llnl.utility.annotation.Internal;
import gov.llnl.utility.io.ReaderException;
import gov.llnl.utility.xml.DomBuilder;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.EnumSet;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import org.xml.sax.Attributes;

/**
 * Interface used as the base class for all ObjectReaders.
 *
 * This contains many embedded interfaces. They are embedded so that they do not
 * need to imported when used.
 *
 * @author nelson85
 * @param <Component> is the type of object that will be produced by this
 * Reader.
 */
public interface Reader<Component>
{

  /**
   * Event called at the start of an element.{@code start} will produce the
   * loaded object if it can be created.
   *
   * The created object will be used as the parent for all handled methods.
   * Elements that contain child elements should always return an object on
   * {@code start}.
   * <p>
   * Readers that use attributes should set the options {@code Option.ANY_ATTR}
   * or {@code Option.ATTR_DEF} in the constructor. Or should use getAttributes
   * to define the supported attributes.
   *
   * FIXME replace getAttributes with annotations on the ObjectReader class.
   * This sort of meta data should not need to be implemented as code.
   *
   * @param context
   * @param attributes Holds the attributes to use in creating this object
   * @return the object produced by this element, or null if the object is to be
   * produced at a later stage
   * @throws ReaderException on anyReader failure in the start method. This
   * includes incorrect or missing attributes.
   */
  Component start(ReaderContext context, Attributes attributes) throws ReaderException;

  /**
   * Event called at the end of the element to process the text contents.
   *
   * @param context
   * @param textContents is the text held by this element
   * @return the object to be produced for this element, will override object if
   * not null
   * @throws ReaderException
   */
  default Component contents(ReaderContext context, String textContents) throws ReaderException
  {
    return null;
  }

  /**
   * Event called when the element end is encountered.For most elements, there
   * is no need to take action when the end is encountered. Deferred actions may
   * not have been complete until after {@code end}. References to this object
   * are set after {@code end}.
   *
   * @param context
   * @return the object for this element or null if the object is already
   * defined
   * @throws ReaderException on anyReader failure while processing this element
   */
  default Component end(ReaderContext context) throws ReaderException
  {
    return null;
  }

  /**
   * Handlers should be constructed using ReaderBuilder.* @param context
   *
   * @param context
   * @see ObjectReader.ReaderBuilder
   *
   * @return the ReaderBuilder to create the handlers for this element
   * @throws ReaderException
   */
  default ElementHandlerMap getHandlers(ReaderContext context)
          throws ReaderException
  {
    return null;
  }

  /**
   * Defines this reader to be a proxy for any element.
   *
   * In order for a reader to be used for any it must have the Reader.Option.ANY
   * set. This method must never fail but instead return null, because there may
   * be more than one wild card item on element list. XSD places certain
   * requirements on any such a no more than one any in a choice element.
   *
   * @param namespaceURI is the namespace for the requested tag.
   * @param localName is the local name of the element.
   * @param qualifiedName is the fully qualified name of the element.
   * @param attr are the attributed of the element.
   * @return a reader to handle this element or null if this element is not
   * handled by this reader.
   */
  default Reader findReader(String namespaceURI, String localName, String qualifiedName, Attributes attr)
  {
    return null;
  }

  /**
   * Get the type of the object produced. 
   * 
   * Used by the schema builder.
   *
   * @return the type if the object or null if the contents does not produce a
   * object.
   */
  default Class getObjectClass()
  {
    Class cls = this.getDeclaration().cls();
    if (cls.equals(void.class))
      throw new RuntimeException("object class not defined " + this.getClass().getCanonicalName());
    return cls;
  }

//<editor-fold desc="declaration" defaultstate="collapsed">
  /**
   * Order controls how elements in the Reader will be handled. The schema used
   * to validate the xml will enforce the order of the elements in the document.
   * All readers that contain elements as children must specify an order in the
   * constructor.
   */
  public static enum Order
  {
    /**
     * All elements should be present in any order at most once, unless marked
     * optional. Elements can be marked {@code optional} or {@code isRequired}.
     * {@code OPTIONS} does not support {@code unbounded} elements.
     *
     */
    ALL,
    /**
     * Any element can appear in any order at most once, unless marked
     * isRequired. Elements can be marked {@code optional} or
     * {@code isRequired}. {@code OPTIONS} does not support {@code unbounded}
     * elements.
     */
    OPTIONS,
    /**
     * Elements must appear in order. Elements can be optional, isRequired or
     * unbounded. The elements in the sequence must appear unless
     * {@code Option.OPTIONAL} is set.
     */
    SEQUENCE,
    /**
     * Elements must select from the list of choices. One of the elements must
     * appear unless {@code Option.OPTIONAL} is set.
     */
    CHOICE,
    /**
     * Elements can appear in any order multiple times. Contents can be empty
     * unless {@code Option.REQUIRED} is set.
     */
    FREE
  }

  public enum Contents
  {
    NONE,
    TEXT,
    ELEMENTS,
    MIXED
  }

  /**
   * Declares this class to be used as a reader. This can appear on object
   * readers or on classes for which an object reader should be created.
   */
  @Inherited
  @Target(ElementType.TYPE)
  @Retention(RetentionPolicy.RUNTIME)
  public @interface Declaration
  {
    final static String NULL = "";

    /**
     * The package that this class belongs to.
     *
     * @return the package class which will hold this schema.
     */
    Class<? extends PackageResource> pkg();

    /**
     * The default element name that will be used for this class. This name must
     * be unique for the package.
     *
     * @return a unique element name for this element.
     */
    String name();

    /**
     * The order of the elements will appear as enforced by the schema. SEQUENCE
     * is not currently supported java methods do no recognize declaration
     * order. FREE, CHOICE, OPTIONS, ALL are supported.
     *
     * @return the order of elements to be enforced in the schema, default is
     * FREE.
     */
    Order order() default Order.FREE;

    /**
     * Option to make this element referenceable in the scope of the xml
     * document. referenceable elements support the "id" attribute and can be
     * referenced using "ref_id" attribute.
     *
     * @return true if element can be referenced, default false.
     */
    boolean referenceable() default false;

    Contents contents() default Contents.ELEMENTS;

    boolean copyable() default false;

    /**
     * The typename for type to appear in the schema. This name must be unique
     * for the package.
     *
     * @return a string containing the typename.
     */
    String typeName() default NULL;

    /**
     * Determines this type be exported in the schema as a top level element.
     *
     * @return true if this is top level.
     */
    boolean document() default true;

    boolean autoAttributes() default false;

    Class cls() default void.class;

    /**
     * Class name to create while creating the schema. In order to instantiate
     * the schema, we must has an implementation of the class. For interfaces in
     * which an implementation is required, this declaration is required.
     *
     * @return the class of the implementation or void.class if no special
     * implementation is required.
     */
    Class impl() default void.class;

    String schema() default NULL;

    /**
     * Special handling for low frequency options.
     *
     * These should be limited to an Option declared in the declaration fold.
     *
     * Currently supported: Option.AUTO_ATTRIBUTES, Option.NO_SCHEMA,
     * Option.CONTENT_REQUIRED,
     *
     * @return
     */
    Option[] options() default 
    {
    };

    String substitutionGroup() default NULL;
  }

  /**
   * Declares a simple element type.
   *
   */
  @Inherited
  @Target(ElementType.FIELD)
  @Retention(RetentionPolicy.RUNTIME)
  public @interface ElementDeclaration
  {
    /**
     * The package that this class belongs to.
     *
     * @return the package class which will hold this schema.
     */
    Class<? extends PackageResource> pkg();

    /**
     * The default element name that will be used for this class. This name must
     * be unique for the package.
     *
     * @return a unique element name for this element.
     */
    String name();

    Class type() default String.class;
  }

  /**
   * Get the declaration for the reader.
   *
   * The declaration contains a set of information required to create the
   * schema. All concrete classes should have a declaration.
   *
   * @return
   */
  default Reader.Declaration getDeclaration()
  {
    Reader.Declaration decl = this.getClass().getDeclaredAnnotation(Reader.Declaration.class);
    if (decl == null)
      throw new NullPointerException("Declaration not found for " + this.getClass());
    return decl;
  }

  /**
   * Get the default element name for this object. This name can be overridden
   * by {@link ObjectReader.ReaderBuilder#element(java.lang.String) element()}
   *
   * @return name of element to be handled, must not be null
   */
  default String getXmlName()
  {
    return getDeclaration().name();
  }

  /**
   * Get the schema resource that will be used to validate this object.
   *
   * @return null if no schema defined for this object.
   */
  default PackageResource getPackage()
  {
    return Singletons.getSingleton(getDeclaration().pkg());
  }

  /**
   * Get the handler key that will be used to look up the element in the table.
   * Keys are currently of the form urn#tag.
   *
   * @return a unique string for handling elements.
   */
  String getHandlerKey();

  /**
   * Get the type name that will be used for the schema file.
   *
   * This should be set when using {@code Option.DEF}.
   *
   * @return the type name that will be used for this object in the schema file
   */
  String getSchemaType();

  /**
   * Create a schema type definition for this element. If the schema is defined
   * externally or this is a simple element that does not need a schema
   * definition, then this will have no contents.
   *
   * @param builder
   * @throws ReaderException if declaration was unable to resolve a isRequired
   * element.
   */
  default void createSchemaType(SchemaBuilder builder)
          throws ReaderException
  {
    SchemaBuilderUtilities.createSchemaTypeDefault(this, builder);
  }

  /**
   * Add the schema element declaration into the group.
   *
   * @param builder is the schema builder creating this schema.
   * @param name is the name of the element that we are declaring.
   * @param group is the position in the dom to insert the declaration.
   * @param topLevel is true if the schema element is at the toplevel of the
   * schema.
   *
   * @return the root of the created element in which attributes can be applied.
   * @throws ReaderException if declaration was unable to resolve a isRequired
   * element.
   */
  default DomBuilder createSchemaElement(SchemaBuilder builder, String name, DomBuilder group, boolean topLevel)
          throws ReaderException
  {
    return SchemaBuilderUtilities.createSchemaElementDefault(this, builder, name, group, topLevel);
  }

//</editor-fold>
//<editor-fold desc="annotations" defaultstate="collapsed">
  /**
   * Declares a set of Attributes for an ObjectReader.
   *
   * This will likely be replaced when we convert to Java 8. Prior to Java8 it
   * was not possible to place more than one annotation of the same type on a
   * class. Thus we need to place a container. After Java 8 multiple annotations
   * are supported.
   */
  @Retention(RetentionPolicy.RUNTIME)
  public @interface AttributesDecl
  {
    Attribute[] value();
  }

  /**
   * Hook to get the attributes for a reader class when defining a schema.
   *
   * This method can be overridden to define manual attributes. By default it
   * refers to the annotations on the ObjectReader.
   *
   * @return the attributes for this ObjectReader, or null if no attributes.
   */
  default Attribute[] getAttributesDecl()
  {
    AttributesDecl attrib = this.getClass().getAnnotation(AttributesDecl.class);
    if (attrib != null)
      return attrib.value();
    Attribute attrib2 = this.getClass().getAnnotation(Attribute.class);
    if (attrib2 != null)
      return new Attribute[]
      {
        attrib2
      };
    return null;
  }

  default AnyAttribute getAnyAttributeDecl()
  {
    AnyAttribute attrib = this.getClass().getAnnotation(AnyAttribute.class);
    return attrib;
  }

  /**
   * Define an attribute of a reader for a class.
   *
   * This may either appear on the set method for a method of an automatically
   * an object with an automatically generated reader, or on the ObjectReader
   * implementation.
   */
  @Repeatable(AttributesDecl.class)
  @Retention(RetentionPolicy.RUNTIME)
  public @interface Attribute
  {
    String name() default NULL;

    Class<?> type() default String.class;

    boolean required() default false;

    static final String NULL = "##null";
  }

  public enum ProcessContents
  {
    Strict, Lax, Skip;
  }

  @Retention(RetentionPolicy.RUNTIME)
  public @interface AnyAttribute
  {
    String id() default NULL;

    String namespace() default "##any";

    ProcessContents processContents() default ProcessContents.Strict;

    static final String NULL = "##null";
  }

  /**
   * Define an element as contents of a reader for a class.
   *
   * This appears on set methods for objects with automatically generated
   * readers.
   */
  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.RUNTIME)
  public @interface Element
  {
    String name();

    Class type() default NULL.class;

    boolean any() default false;

    boolean required() default false;

    boolean unbounded() default false;

    boolean deferrable() default false;

    static public class NULL
    {
    }
  }

  /**
   * Define method as the starting contents of a reader for a class.
   *
   * This appears on set methods for objects with automatically generated
   * readers. Automatically generated readers may have element or text contents.
   * Mixed contents are not supported.
   */
  @Target(value =
  {
    ElementType.METHOD, ElementType.TYPE
  })
  @Retention(RetentionPolicy.RUNTIME)
  public @interface TextContents
  {
    String base() default "xs:string";
  }

  default TextContents getTextContents()
  {
    return this.getClass().getDeclaredAnnotation(Reader.TextContents.class);
  }

//</editor-fold>
//<editor-fold desc="builder" defaultstate="collapsed">
  /**
   * Interface to support receiving attributes.Used by ReaderBuilder.call.
   *
   *
   * @param <T>
   * @param <T2>
   */
  @FunctionalInterface
  public interface ReaderContextConsumer<T, T2>
  {
    void call(ReaderContext context, T t, T2 contents) throws ReaderException;
  }

  /**
   * ReaderBuilder constructs the HandlerMap used to read files and create the
   * schema. HandlerMap can be defined in the constructor or in the
   * {@link #getHandlers getHandlers} method. HandlerMap must not be static.
   *
   * @param <T>
   */
  public static interface ReaderBuilder<T>
  {

    /**
     * Create a group with a specified order.
     *
     * Options supported are Options.OPTIONAL, Options.REQUIRED, and
     * Options.UNBOUNDED. XSD restricts what types can appear inside a
     * particular existing Order.
     *
     * @param order
     * @param options
     * @return
     */
    ReaderBuilder<T> group(Order order, Option... options);

    /**
     * Gets the handlers for the contents. This should always be the method
     * called to pass back the handlers in the ObjectReader {@code getHandlers}
     * method.
     * <p>
     * Example:
     * <pre>
     * {@code
     *    class FooReader extends ObjectReader<Foo>
     *    {
     *      ...
     *      public ElementHandlerMap getHandlers() throws ReaderException
     *      {
     *        ReaderBuilder<Foo> builder=this.newBuilder();
     *        ...
     *        return builder.getHandlers();
     *     }
     *   }
     * }
     * </pre>
     *
     * @return the handlers constructed for this contents.
     * @throws ReaderException
     */
    ElementHandlerMap getHandlers() throws ReaderException;

    /**
     * Defines the name of the element to appear. The elements must be defined
     * in order. If {@code element} is not called, then name of the element will
     * be taken as the default in the contents.
     *
     * @param name
     * @return the builder for chaining
     */
    ReaderBuilderContents<T> element(String name);

    /**
     * Defines the contents that will be called to handle this element. The
     * reader will be looked up from available contents types.
     *
     * @param <Obj>
     * @param resultClass the class for which the contents will be created.
     * @return the builder for chaining.
     * @throws ReaderException if there is no contents defined for this class or
     * the contents encounters an error.
     */
    <Obj> ReaderBuilderCall<T, Obj> contents(Class<Obj> resultClass) throws ReaderException;

    /**
     * Defines the reader that will be called to handle this element. If the
     * element() has not already been defined, the element name will be taken
     * from the reader. The type of object produced for this element is taken
     * from {@link ObjectReader#getObjectClass() getObjectClass}.
     *
     * @param <Obj>
     * @param reader
     * @return the builder for chaining
     * @throws ReaderException
     */
    <Obj> ReaderBuilderCall<T, Obj> reader(Reader<Obj> reader) throws ReaderException;

    <Obj> ReaderBuilderCall<T, Obj> any(Class<Obj> cls) throws ReaderException;

    <Obj> ReaderBuilderCall<T, Obj> anyReader(Reader<Obj> reader) throws ReaderException;

    /**
     * Defines the set readers that will be called to handle this element. The
     * element name will be taken from the readers.
     *
     * @param <Obj>
     * @param cls the class of objects produced by this contents
     * @param reader
     * @return the builder for chaining
     * @throws ReaderException
     */
    @SuppressWarnings("unchecked")
    <Obj> ReaderBuilderCall<T, Obj> readers(Class<Obj> cls, ObjectReader<? extends Obj>... reader) throws ReaderException;

    /**
     * Terminator that delegates the elements to a contents section.
     *
     * @param section
     * @return chain for options
     * @throws ReaderException
     */
    ReaderBuilderOptions section(SectionInterface section) throws ReaderException;
  }

  /**
   *
   * @author nelson85
   * @param <T>
   * @param <T2>
   */
  public static interface ReaderBuilderCall<T, T2>
  {
    /**
     * Terminator that calls a method on the specified object defined in the
     * element. The object call be either an object created by referenceName,
     * text content, a contents, a factory, or fixed contents.
     * <p>
     * Example:
     * <pre>{@code
     * class FooReader extends ObjectReader<Foo>
     * {
     * ...
     * public ElementHandlerMap getHandlers() throws ReaderException
     * {
     * ReaderBuilder builder=this.newBuilder();
     * builder.element("bar").contents(new BarReader()).call(Foo::setBar);
     * return builder.create();
     * }
     * }
     * }</pre>
     *
     * @param method is the method to call on the end of the element.
     * @return the builder for chaining for options.
     * @throws ReaderException if the result type cannot be determined
     *
     * @see ReaderBuilderContents#reference
     * @see ReaderBuilderContents#contents
     * @see ReaderBuilderContents#reader see ReaderBuilderContents#fixed (FIXME
     * reference not found)
     *
     */
    ReaderBuilderOptions call(BiConsumer<T, T2> method) throws ReaderException;

    ReaderBuilderOptions callContext(ReaderContextConsumer<T, T2> method) throws ReaderException;

    /**
     *
     * Terminator for a currentHandler which calls no actions.
     *
     * @return the builder for chaining options.
     * @throws ReaderException
     */
    ReaderBuilderOptions nop() throws ReaderException;
  }

  // The stuff that follows element()
  public static interface ReaderBuilderContents<T>
  {

    /**
     * Calls the method assuming the string contents. It is a terminator and is
     * a short cut for {@code .contents(String.class).call(method)}.
     *
     * @param method
     * @return an object to apply options.
     * @throws ReaderException if the result type can not be determined
     */
    ReaderBuilderOptions callString(BiConsumer<T, String> method) throws ReaderException;

    /**
     * Calls the method assuming the double contents. It is a terminator and is
     * a short cut for {@code .contents(double.class).call(method)}.
     *
     * @param methodName
     * @return an object to apply options.
     * @throws ReaderException if the result type can not be determined
     */
    ReaderBuilderOptions callDouble(BiConsumer<T, Double> methodName) throws ReaderException;

    /**
     * Calls the method assuming the integer contents. It is a terminator and is
     * a short cut for {@code .contents(int.class).call(method)}.
     *
     * @param methodName
     * @return an object to apply options.
     * @throws ReaderException if the result type can not be determined
     */
    ReaderBuilderOptions callInteger(BiConsumer<T, Integer> methodName) throws ReaderException;

    /**
     * Calls the method assuming the long contents. It is a terminator and is a
     * short cut for {@code .contents(long.class).call(method)}.
     *
     * @param method
     * @return an object to apply options.
     * @throws ReaderException if the result type can not be determined
     */
    ReaderBuilderOptions callLong(BiConsumer<T, Long> method) throws ReaderException;

    /**
     * Calls the method assuming the boolean contents. It is a terminator and is
     * a short cut for {@code .contents(boolean.class).call(method)}.
     *
     * @param method
     * @return the builder for chaining options.
     * @throws ReaderException if the result type can not be determined
     */
    ReaderBuilderOptions callBoolean(BiConsumer<T, Boolean> method) throws ReaderException;

    <T2> ReaderBuilderOptions call(BiConsumer<T, T2> method, Class<T2> resultClass) throws ReaderException;

    <T2> ReaderBuilderOptions callContext(ReaderContextConsumer<T, T2> method, Class<T2> resultClass) throws ReaderException;

    /**
     * Call a method for a tag requiring no contents. This is the replacement
     * for "fixed(value)".
     *
     * @param method
     * @return
     * @throws ReaderException
     */
    ReaderBuilderOptions call(Consumer<T> method) throws ReaderException;

    /**
     * Defines the contents of the element to accept anyReader element that fits
     * the required type.
     *
     * @param <Obj>
     * @param resultClass
     * @return the builder for chaining to call.
     * @throws ReaderException
     */
    <Obj> ReaderBuilderCall<T, Obj> any(Class<Obj> resultClass) throws ReaderException;

    /**
     * Defines the contents of the object in terms of a type. The contents will
     * be taken as the default contents for this type.
     *
     * @param <T2>
     * @param resultClass
     * @return the builder for chaining to call.
     * @throws ReaderException if the contents for this type is not found.
     */
    <T2> ReaderBuilderCall<T, T2> contents(Class<T2> resultClass) throws ReaderException;

    /**
     * Defines the reader that will be called to handle this element. If the
     * element() has not already been defined, the element name will be taken
     * from the reader. The type of object produced for this element is taken
     * from {@link ObjectReader#getObjectClass() getObjectClass}.
     *
     * @param <Obj>
     * @param reader
     * @return the builder for chaining
     * @throws ReaderException
     */
    <Obj> ReaderBuilderCall<T, Obj> reader(Reader<Obj> reader) throws ReaderException;

    /**
     * Defines the contents of this tag to be a referenceName to an object
     * defined elsewhere. Use the {link ReaderBuilderOptions#resolve resolve
     * (FIXME reference not found)} flag if the action cannot be deferred.
     *
     * @param <T2>
     * @param resultClass
     * @return the builder for chaining
     * @throws ReaderException
     */
    <T2> ReaderBuilderCall<T, T2> reference(Class<T2> resultClass) throws ReaderException;

    /**
     * Defines the contents to be a boolean flag.
     *
     * @return the builder for chaining
     * @throws ReaderException
     */
    ReaderBuilderCall<T, Boolean> flag() throws ReaderException;

    /**
     * Defines the contents to be a list of items.
     *
     * The name of each element will be based on the default element name in the
     * reader. If the object reader is polymorphic then the elements will accept
     * polymorphic types.
     *
     * @param <T2>
     * @param reader
     * @return the builder for chaining
     * @throws ReaderException
     */
    <T2> ReaderBuilderCall<T, List<T2>> list(ObjectReader<T2> reader) throws ReaderException;

    /**
     * Defines the contents to be a list of items.
     *
     * The name of each element will be based on the default element name in the
     * reader. If the object reader is polymorphic then the elements will accept
     * polymorphic types.
     *
     * @param <T2>
     * @param cls is the class for the reader.
     * @return the builder for chaining
     * @throws ReaderException
     */
    default <T2> ReaderBuilderCall<T, List<T2>> list(Class<T2> cls) throws ReaderException
    {
      ObjectReader<T2> reader = ObjectReader.create(cls);
      if (reader == null)
        throw new ReaderException("Unable to find reader for " + cls.getName());
      return list(reader);
    }
  }

  /**
   *
   * @author nelson85
   */
  public static interface ReaderBuilderOptions
  {

    /**
     * Option for element specifying that the referenceName can be deferred
     * until later. Use with
     * {@link ReaderBuilderContents#reference referenceName} and
     * {@link ReaderBuilder#contents(java.lang.Class) contents}.
     *
     * @return the builder options for chaining
     */
    ReaderBuilderOptions deferrable();

    /**
     * Flags this element as being optional. Optional elements don't need to
     * appear in the document. Useful if the element order is set to Order.ALL
     * or {@code Order.SEQUENCE}.
     *
     * @return the builder options for chaining
     */
    ReaderBuilderOptions optional();

    /**
     * Flags this element as being optional. Optional elements don't need to
     * appear in the document. Useful if the element order is set to
     * {@code Order.OPTIONS} or {@code Order.SEQUENCE}.
     *
     * @return the builder options for chaining
     */
    ReaderBuilderOptions required();

    /**
     * Flags this element as appearing multiple times. Must not be use with
     * {@code Option.ALL} or {@code Option.OPTIONS}.
     *
     * @return the builder options for chaining
     */
    ReaderBuilderOptions unbounded();

    /**
     * Flags this element as unable to be a referenceName.
     *
     * This is for use with the {@link ReaderBuilder#using using} directive.
     *
     * @return the builder options for chaining
     */
    ReaderBuilderOptions define();

    /**
     * Flags this element does not create a referenceable object.
     *
     * This is used when the xml element will be using the id attribute for its
     * purposes.
     *
     * @return the builder options for chaining
     */
    ReaderBuilderOptions noid();

    /**
     * Add options to this element.
     *
     * @param first
     * @param rest
     * @return
     */
    ReaderBuilderOptions options(Option first, Option... rest);

  }

  /**
   * Factory to create a reader when supporting the any tag.
   *
   * If a tag can support many different types of objects rather than one
   * specific set from a list.
   *
   * @param <Type>
   */
  public static interface AnyFactory<Type>
  {

    /**
     * Create a new reader given a specified tag.
     *
     * @param namespaceURI is the xml namespace that this tag belongs to.
     * @param localName is the local name for the tag.
     * @param qualifiedName is the fully qualified name for this tag including
     * the prefix.
     * @param attributes are the attributes specified for this xml tag.
     * @return
     * @throws ReaderException if there is no reader specified for this xml tag.
     */
    ObjectReader getReader(String namespaceURI, String localName, String qualifiedName,
            Attributes attributes)
            throws ReaderException;

    /**
     * Get a set of reader options.
     *
     * This supports all the same options that are allowed for any reader.
     *
     * @return
     */
    EnumSet<Option> getOptions();

    /**
     * Get the type of object to be produced by this interface.
     *
     * It can be a generic Object if all types of objects are supported.
     *
     * @return
     */
    Class<Type> getObjectClass();
  }

  /**
   * Interface for all sections. Sections can only appear inside of an
   * ObjectReader.
   *
   * @param <Component>
   */
  public static interface SectionInterface<Component> extends Reader<Component>
  {
    /**
     * Event called at the start of an element. The object is inherited from the
     * parent contents.
     * <p>
     * Readers that use attributes should set the setOptions {@code Option.ANY}
     * or {@code Option.DEF} in the constructor.
     *
     * @param attributes Holds the attributes to use in creating this object
     * @throws ReaderException on anyReader failure in the start method. This
     * includes incorrect or missing attributes.
     */
    @Override
    Component start(ReaderContext context, Attributes attributes) throws ReaderException;

    /**
     * Event called at the end of the element to process the text contents.
     * {@code contents} will only be called if {link #hasTextContent
     * hasTextContent (FIXME reference not found)} returns true.
     *
     * @param textContents is the text held by this element
     * @throws ReaderException
     */
    @Override
    Component contents(ReaderContext context, String textContents) throws ReaderException;

    /**
     * Event called when the element end is encountered. For most elements,
     * there is no need to take action when the end is encountered. Deferred
     * actions may not have been complete until after {@code end}. References to
     * this object are set after {@code end}.
     *
     * @throws ReaderException on anyReader failure while processing this
     * element
     */
    @Override
    Component end(ReaderContext context) throws ReaderException;

  }
//</editor-fold>
//<editor-fold desc="handlers" defaultstate="collapsed">

  /**
   * Representation for a set of handlers created by the builder.
   */
  public static interface ElementHandlerMap
  {
    /**
     * Find the handler for this element.
     *
     * @param namespaceURI is the namespace of the element.
     * @param localName is the name of element.
     * @return the element handler or null if no handler found.
     * @throws gov.llnl.utility.io.ReaderException
     */
    ElementHandler get(String namespaceURI, String localName) throws ReaderException;

    /**
     * Create a schema for this handler list.
     *
     * This has the job of adding the element handlers to populate the schema
     * type for the reader which declared the handlers.
     *
     * @param sb
     * @param type
     * @throws ReaderException
     */
    void createSchemaType(SchemaBuilder sb, DomBuilder type) throws ReaderException;

    /**
     * Get a list of handlers.
     *
     * Used for debugging.
     *
     * @return
     */
    List<ElementHandler> toList();

    /**
     * Get the handler for unknown element types.
     *
     * @param previousHandler
     * @param namespaceURI
     * @param localName
     * @param qualifiedName
     * @param attr
     * @return a pair of handlers with containing the handler than matched and
     * the handler for this element.
     */
    default ElementHandler[] searchHandlers(ElementHandler previousHandler,
            String namespaceURI,
            String localName,
            String qualifiedName,
            Attributes attr)
    {
      return null;
    }

  }

  /**
   * An element handler is created for each element in the document to marshal
   * the contents through the reader.
   */
  @Internal
  public static interface ElementHandler
  {
//<editor-fold desc="xml">
    /**
     * Action to perform when the start of an element is encounter.
     *
     * @param context is the contextual information parsed from the document.
     * @param attr
     * @return the element which will be the parentGroup for anyReader elements
     * that appear in the block. This is null if there are no elements allowed
     * within the element.
     * @throws ReaderException
     */
    default Object onStart(ReaderContext context, Attributes attr) throws ReaderException
    {
      return null;
    }

    /**
     * Performs all actions required when the element is closed. This may create
     * a new object if it was not created in the start. A null value is ignored.
     *
     * @param context is the object created by this element.
     * @return the object produced or null if no object associated.
     * @throws ReaderException if the object requirements were not met.
     */
    default Object onEnd(ReaderContext context) throws ReaderException
    {
      return null;
    }

    /**
     * Called when the element is complete.
     *
     * @param context
     * @param parent
     * @param child
     * @throws ReaderException
     */
    @SuppressWarnings("unchecked")
    default void onCall(ReaderContext context, Object parent, Object child) throws ReaderException
    {
    }

    /**
     * Called while handling a document when text content is found.
     *
     * Used only when hasTextContent() is true.
     *
     * @param context
     * @param textContent
     * @return
     * @throws ReaderException
     */
    default Object onTextContent(ReaderContext context, String textContent) throws ReaderException
    {
      return null;
    }

//</editor-fold>
//<editor-fold desc="schema">
    /**
     * Convert the element handler into a schema.
     *
     * @param builder
     * @param type
     * @throws ReaderException
     */
    default void createSchemaElement(SchemaBuilder builder, DomBuilder type) throws ReaderException
    {
    }

    /**
     * Get the reader associated with the element handler.
     *
     * Used when building schema.
     *
     * @return the reader which will be used to create the schema, or null if no
     * schema type is required.
     */
    Reader getReader();

    /**
     * Get the localname associated with this element.
     *
     * This is used when building schema.
     *
     * @return the element name.
     */
    String getName();

//</editor-fold>
    /**
     * A key for this element in the lookup table.
     *
     * This is usually "name#url".
     *
     * @return the key
     */
    String getKey();

    /**
     * Options for the handler.
     *
     * see {@link Reader.Option}
     *
     * @return
     */
    EnumSet<Option> getOptions();

    /**
     * Get the class produced by this Handler.
     *
     * Used by handleReferences to get a reference with the correct type. This
     * will be getObjectType for a reader.
     *
     * @return
     */
    Class getObjectClass();

    /**
     * Get handlers for elements contained within.
     *
     * @param context
     * @return
     * @throws gov.llnl.utility.io.ReaderException
     */
    default ElementHandlerMap getHandlers(ReaderContext context) throws ReaderException
    {
      return null;
    }

//<editor-fold desc="option" defaultstate="collapsed">
    void addOption(Option option);

    boolean hasOption(Option option);
//</editor-fold>

    /**
     * Get the next handler.
     *
     * This is used when building schema and creating element handler maps.
     *
     * @return the nextHandler or null if this is the last handler in the group.
     */
    ElementHandler getNextHandler();

    void setNextHandler(ElementHandler handler);

    /**
     * Get the group that holds this element (sequence, choice, etc).
     *
     * Used when building schema.
     *
     * @return the parentGroup
     */
    ElementGroup getParentGroup();

    void setParentGroup(ElementGroup parentGroup);

  }

  /**
   * Option that apply the ElementHandlers.
   *
   * Some options are used to insert directives into the schema. Others are for
   * internally tracking the state of an ElementHandler.
   */
  public enum Option
  {
    //<editor-fold desc="occurance" defaultstate="collapsed">
    /**
     * Element can appear zero times.
     */
    OPTIONAL("minOccurs", "0"),
    /**
     * Element must appear at least one time.
     */
    REQUIRED("minOccurs", "1"),
    /**
     * Element can repeat as many times as desired. Usable with sequences.
     */
    UNBOUNDED("maxOccurs", "unbounded"),
    //</editor-fold>
    //<editor-fold desc="any" defaultstate="collapsed">
    /**
     * Specifies a reader will be marked as any in the xsd.
     *
     * This is automatically added for readers which were created with any or
     * anyReader. Can be applied to other readers that take wildcard such as
     * abstract. This flag is mostly internal.
     */
    ANY,
    /**
     * Specifies that any element not defined in this schema will be accepted.
     *
     * This argument is only valid on an any handler. Use this as an argument to
     * a handler with .options(Option.ANY_OTHER).
     */
    ANY_OTHER("namespace", "##other"),
    /**
     * Specifies that any element in this schema will be accepted.
     *
     * This argument is only valid on an any handler. Use this as an argument to
     * a handler with .options(Option.ANY_ALL).
     */
    ANY_ALL("namespace", "##any"),
    /**
     * Specifies that any element matched will not be validated.
     *
     * This argument is only valid on an any handler. Use this as an argument to
     * a handler with .options(Option.ANY_SKIP).
     */
    ANY_SKIP("processContents", "skip"),
    /**
     * Specifies that any element matched will must be validated.
     *
     * This argument is only valid on an any handler. Use this as an argument to
     * a handler with .options(Option.ANY_STRICT).
     */
    ANY_STRICT("processContents", "strict"),
    /**
     * Specifies that the schema applied to any element matched will be loosely
     * applied.
     *
     * This argument is only valid on an any handler. Use this as an argument to
     * a handler with .options(Option.ANY_LAX).
     */
    ANY_LAX("processContents", "lax"),
    //</editor-fold>
    //<editor-fold desc="referencing" defaultstate="collapsed">
    /**
     * Must be defined, cannot be a reference even if a reference is otherwise
     * allowed. Used mostly internally.
     */
    NO_REFERENCE,
    /**
     * Specifies that this element must be a reference and will produce an error
     * if the element contexts are defined.
     */
    MUST_REFERENCE,
    /**
     * Specifies that this element can be defined after the element is ended.
     *
     * This is used for elements that do not require forward referencing.
     * Actions specified will be called with the element is defined later in the
     * document. The context for the call will reflect then it was defined
     * rather than where the reference was used.
     */
    DEFERRABLE,
    /**
     * Indication that the "id" attribute should not be used for referencing.
     *
     * Applying when the schema has a different concept for the attribute "id".
     * Rarely used.
     */
    NO_ID,
    //</editor-fold>
    //<editor-fold desc="internal" defaultstate="collapsed">
    AUTO_ATTRIBUTES,
    NO_SCHEMA,
    /**
     * Declaration Option to require child elements. Without this child elements
     * may not have content even if an element is required. Referenceable
     * elements may not require content.
     */
    CONTENT_REQUIRED,
    //</editor-fold>
    //<editor-fold desc="internal" defaultstate="collapsed">
    /**
     * Internal flag used to indicate that handler should capture text and pass
     * it to Reader.contents when he end tag is reached.
     */
    CAPTURE_TEXT;

    public static boolean check(Option[] options, Option option)
    {
      if (options.length == 0)
        return false;
      for (Option o : options)
      {
        if (o == option)
          return true;
      }
      return false;
    }

    final String key, value;

    Option()
    {
      key = null;
      value = null;
    }

    Option(String key, String value)
    {
      this.key = key;
      this.value = value;
    }

    public String getKey()
    {
      return key;
    }

    public String getValue()
    {
      return value;
    }
//</editor-fold>
  }

//</editor-fold>
}
