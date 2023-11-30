/*
 * Copyright 2016, Lawrence Livermore National Security, LLC.
 * All rights reserved
 *
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.utility.xml.bind;

import gov.llnl.utility.ClassUtilities;
import gov.llnl.utility.PackageResource;
import gov.llnl.utility.io.ReaderException;
import gov.llnl.utility.xml.DomBuilder;
import org.xml.sax.Attributes;

/**
 * Base class to use for all Reader classes that require element contents.
 * <p>
 * Example:
 * <pre>
 * {code
 * Reader.Declaration(pkg=MySchema.class, name="foo", order=Reader.Order.ALL
 *    cls=Foo.class)
 * public class FooReader extends ObjectReader&lt;Foo&gt;
 * {
 *
 *   public Object start(Attributes attributes) throws ReaderException
 *   {
 *     return new Foo();
 *   }
 *
 *   public ElementHandlerMap getHandlers() throws ReaderException
 *   {
 *     ReaderBuilder&lt;Foo&gt; 
 *     builder=this.newBuilder();
 *     builder.element("bar").reader(new BarReader()).call(Foo::setBar);
 *     return builder.getHandlers();
 * }
 *
 * }</pre>
 *
 * @param <Component> is the class for objects produced by this Reader.
 */
public abstract class ObjectReader<Component>
        implements Reader<Component>
{
  /**
   * Create the object reader for this class.Searches in order: Primitives,
   * annotations, and contents map.
   *
   * @param <T>
   * @param cls
   * @return does not return null.
   * @throws ReaderException if the contents cannot be found.
   */
  public static <T> ObjectReader<T> create(Class<T> cls) throws ReaderException
  {
    return SchemaManager.getInstance().findObjectReader(cls);
  }

  /**
   * Event called at the start of an element. {@code start} will produce the
   * loaded object if it can be created. The created object will be used as the
   * parent for all handled methods. Elements that contain child elements should
   * always return an object on {@code start}.
   * <p>
   * Readers that use attributes should set the options {@code Option.ANY_ATTR}
   * or {@code Option.ATTR_DEF} in the constructor.
   *
   * @param attributes Holds the attributes to use in creating this object
   * @return the object produced by this element, or null if the object is to be
   * produced at a later stage
   * @throws ReaderException on anyReader failure in the start method. This includes
 incorrect or missing attributes.
   */
  @Override
  public Component start(ReaderContext context, Attributes attributes) throws ReaderException
  {
    return null;
  }

  /**
   * Utility to help with unpacking attributes.
   *
   * @param <T>
   * @param attr
   * @param name
   * @param cls
   * @param defaultValue
   * @return
   */
  @SuppressWarnings("unchecked")
  protected static <T> T getAttribute(Attributes attr, String name, Class<T> cls, T defaultValue)
  {
    String s = attr.getValue(name);
    if (s == null)
      return defaultValue;
    return (T) ClassUtilities.newValueOf(cls).valueOf(s);
  }

  @SuppressWarnings("unchecked")
  protected static <T> T getAttribute(Attributes attr, String name, Class<T> cls) throws ReaderException
  {
    String s = attr.getValue(name);
    if (s == null)
      throw new ReaderException("Required attribute " + name + " was not found.");
    return (T) ClassUtilities.newValueOf(cls).valueOf(s);
  }

  @Override
  public ElementHandlerMap getHandlers(ReaderContext context)
          throws ReaderException
  {
    return null;
  }

  /**
   * Event called at the end of the element to process the text contents.
   *
   * @param context
   * @param textContents is the text held by this element
   * @return the object to be produced for this element, will override object if
   * not null
   * @throws ReaderException
   */
  @Override
  public Component contents(ReaderContext context, String textContents) throws ReaderException
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
  @Override
  public Component end(ReaderContext context) throws ReaderException
  {
    return null;
  }

  @Override
  final public String getHandlerKey()
  {
    String name = getXmlName();
    if (name == null)
      return null;
    PackageResource resource = this.getPackage();
    if (resource != null)
      return name + "#" + resource.getNamespaceURI();
    return name + "#";
  }

  @Override
  public String getSchemaType()
  {
    // If the user specifies a typename we should use it.
    // This is in the case that the typename got moved or there is some conflict.
    Declaration decl = getDeclaration();
    if (!decl.typeName().equals(Declaration.NULL))
      return decl.typeName();

    // Otherwise use automatic naming
    String className = this.getClass().getName();
    String out = className.replaceAll("[A-z0-9]*\\.", "").replaceAll("[$]+", "-") + "-type";
    return out;
  }

  /**
   * Gets the current object being read.
   *
   * @param context
   * @return the current object or null if the object has not yet been
   * constructed.
   */
  @SuppressWarnings("unchecked")
  protected Component getObject(ReaderContext context)
  {
    if (context == null)
      return null;
    return (Component) context.getCurrentContext().getObject();
  }

  /**
   * Creates a builder to make a ElementHandlerMap.
   *
   * @return a new builder for this object.
   */
  @SuppressWarnings("unchecked")
  final public ReaderBuilder<Component> newBuilder()
  {
    return new ReaderBuilderImpl(this.getPackage(),
            this.getDeclaration());
  }

  @SuppressWarnings("unchecked")
  final public <T extends Component> ReaderBuilder<T> newBuilder(Class<T> aClass)
  {
    return new ReaderBuilderImpl(this.getPackage(),
            this.getDeclaration());
  }
//<editor-fold desc="sections" defaultstate="collapsed">

  /**
   * Defines a section with element contents. Sections allow a portion of the
   * readers setOptions to be contained from others. Sections can never be
   * references and do not create a new object.
   *
   * <pre>
   * {@code
   * class FooReader extends ObjectReader<Foo>
   * {
   *    ...
   *    public ElementHandlerMap getHandlers() throws ReaderException
   *    {
   *      ReaderBuilder<Foo> builder = this.newBuilder();
   *      builder.section(new BarSection());
   *      return builder.create();
   *    }
   *
   *    class BarSection extends Section
   *    {
   *      ...
   *     }
   *   }
   * }</pre>
   *
   */
  public abstract class Section extends SectionImpl<Component>
  {
    final Order order;
    final String name;

    public Section(Order order, String name)
    {
      this.order = order;
      this.name = name;
    }

    @Override
    public Reader.Declaration getDeclaration()
    {
      return new ReaderDeclarationImpl()
      {
        @Override
        public Class<? extends PackageResource> pkg()
        {
          return ObjectReader.this.getDeclaration().pkg();
        }

        @Override
        public String name()
        {
          return Section.this.name;
        }

        @Override
        public Order order()
        {
          return Section.this.order;
        }

        @Override
        public String schema()
        {
          return Reader.Declaration.NULL;
        }
      };
    }

    /**
     * {@inheritDoc}
     * <p>
     * {@code start} is optional for {@code Section}s.
     */
    @Override
    public Component start(ReaderContext context, Attributes attributes) throws ReaderException
    {
      return null;
    }

    /**
     * {@inheritDoc}
     * <p>
     * {@code Section}s may not have text contents.
     */
    @Override
    final public Component contents(ReaderContext context, String textContents)
            throws ReaderException
    {
      return null;
    }

    /**
     * {@inheritDoc}
     * <p>
     * {@code end} is optional for Section.
     */
    @Override
    public Component end(ReaderContext context) throws ReaderException
    {
      return null;
    }

    @SuppressWarnings("unchecked")
    protected Component getObject(ReaderContext context)
    {
      return (Component) context.getCurrentContext().getObject();
    }

    @Override
    final public Class getObjectClass()
    {
      return ObjectReader.this.getObjectClass();
    }

    @SuppressWarnings("unchecked")
    final public ReaderBuilder<Component> newBuilder()
    {
      return new ReaderBuilderImpl(this.getPackage(),
              this.getDeclaration());
    }

    @SuppressWarnings("unchecked")
    final public <T extends Component> ReaderBuilder<T> newBuilder(Class<T> cls)
    {
      return new ReaderBuilderImpl(this.getPackage(),
              this.getDeclaration());
    }

  }

  /**
   * Defines a section with only text contents.
   *
   * @see ObjectReader.Section
   */
  public abstract class StringSection extends SectionImpl<Component>
  {

    @SuppressWarnings("unchecked")
    protected Component getObject(ReaderContext context)
    {
      return (Component) context.getCurrentContext().getObject();
    }

    /**
     * {@inheritDoc}
     * <p>
     * start() is optional for classed derived from StringSection.
     * <p>
     */
    @Override
    public Component start(ReaderContext context, Attributes attributes) throws ReaderException
    {
      return null;
    }

    /**
     * {@inheritDoc}
     * <p>
     * All classes derived from StringSection must implement contents().
     */
    @Override
    public abstract Component contents(ReaderContext context, String textContents)
            throws ReaderException;

    /**
     * {@inheritDoc}
     * <p>
     * {@code end} is optional for StringSection.
     */
    @Override
    public Component end(ReaderContext context)
    {
      return null;
    }

    /**
     * {@inheritDoc}
     * <p>
     * StringSection is for objects that do not contain child elements. All
     * classes derived from StringSection may not implement getHandlers().
     */
    @Override
    final public ElementHandlerMap getHandlers(ReaderContext context)
    {
      return null;
    }

    @Override
    final public Class getObjectClass()
    {
      return ObjectReader.this.getObjectClass();
    }
  }
//</editor-fold>
//<editor-fold desc="imports">

  /**
   * Use this to import resources into the document so that they can be
   * referenced later. No schema is applied to the contents of this section.
   *
   * <pre>
   * {@code
   * public ElementHandlerMap getHandlers() throws ReaderException
   * {
   *   ReaderBuilder builder = this.newBuilder();
   *   builder.section(new Imports()).optional();
   *   ..
   * }
   * }
   * </pre>
   */
  public class Imports extends Section
  {
    public Imports()
    {
      super(Order.FREE, "imports");
    }

    @Override
    @SuppressWarnings("unchecked")
    public ElementHandlerMap getHandlers(ReaderContext context) throws ReaderException
    {
      ReaderBuilder builder = newBuilder();
      // Special reader for handling extern contexts.
      builder.reader(new ImportsAny()).nop().define().options(Option.ANY);
      return builder.getHandlers();
    }

    @Override
    public void createSchemaType(SchemaBuilder builder) throws ReaderException
    {
      // Create a type definition
      DomBuilder type = builder.getRoot()
              .element("xs:complexType")
              .attr("name", this.getSchemaType());
      
      // As many elements as we need.
      DomBuilder group = type.element("xs:choice")
              .attr("minOccurs", "0")
              .attr("maxOccurs", "unbounded");
      
      // Override the schema entirely so that we don't have conflicts with 
      // existing element definitions.
      group.element("xs:any").attr("processContents", "skip");
    }

  }

  /**
   * Use this to define resources into the document so that they can be
   * referenced later.
   *
   * <pre>
   * {@code
   * public ElementHandlerMap getHandlers() throws ReaderException
   * {
   *   ReaderBuilder builder = this.newBuilder();
   *   builder.section(new Defines()).optional();
   *   ..
   * }
   * }
   * </pre>
   */
  public class Defines extends Section
  {
    public Defines()
    {
      super(Order.FREE, "defines");
    }

    @Override
    @SuppressWarnings("unchecked")
    public ElementHandlerMap getHandlers(ReaderContext context) throws ReaderException
    {
      ReaderBuilder builder = this.newBuilder();
      builder.any(Object.class).nop().optional();
      return builder.getHandlers();
    }
  }
//</editor-fold>
}
