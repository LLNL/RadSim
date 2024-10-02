/*
 * Copyright 2018, Lawrence Livermore National Security, LLC.
 * All rights reserved
 *
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.utility.xml.bind;

import gov.llnl.utility.UtilityPackage;
import gov.llnl.utility.annotation.Internal;
import gov.llnl.utility.io.ReaderException;
import gov.llnl.utility.xml.DomBuilder;
import org.xml.sax.Attributes;

/**
 * Specialized reader to serve as a base class for reading wildcard "any" type
 * elements and abstract contents.
 *
 * This can be used as a list to collect any contents with readers that match a
 * specified type.
 *
 * @param <T>
 */
@Reader.Declaration(pkg = UtilityPackage.class,
        name = "any", order = Reader.Order.SEQUENCE,
        referenceable = true)
public class AnyReader<T> extends ObjectReader<T>
{
  final Class<T> cls;

  /**
   * Used to build the schema.
   */
  @Internal
  AnyReader()
  {
    this.cls = null;
  }

  protected AnyReader(Class cls)
  {
    this.cls = cls;
  }

  static public <T> AnyReader<T> of(Class<T> cls)
  {
    return new AnyReader<>(cls);
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
    builder.anyReader(this).callContext((c, o, v) -> c.setState(v));
    return builder.getHandlers();
  }

  @Override
  public Class getObjectClass()
  {
    return cls;
  }

  /**
   * Create a reader for a wild card element.
   *
   * @param namespaceURI
   * @param localName
   * @param qualifiedName
   * @param attr
   * @return
   */
  @Override
  public Reader findReader(
          String namespaceURI,
          String localName,
          String qualifiedName,
          Attributes attr)
  {
    try
    {
      // Check the manager to see if we can find a class.
      SchemaManager schemaMgr = SchemaManager.getInstance();
      Class<?> cls = schemaMgr.getObjectClass(namespaceURI, localName);
      if (cls == null)
        return null;
      ObjectReader ori = schemaMgr.findObjectReader(cls);
      if (ori == null)
        return null;

      // Sanity check.  Does the reader produce the requested object type?
      Class objClass = getObjectClass();
      if (!objClass.isAssignableFrom(ori.getObjectClass()))
        return null;
      return ori;
    }
    catch (ClassNotFoundException ex)
    {
      throw new RuntimeException(ex);
    }
  }

  /**
   * Specialize the schema element declaration to use xs:any if there is no
   * element name.
   *
   * @param builder
   * @param name
   * @param group
   * @param topLevel
   * @return
   * @throws ReaderException
   */
  @Override
  public DomBuilder createSchemaElement(SchemaBuilder builder, String name, DomBuilder group, boolean topLevel) throws ReaderException
  {
    if (name.isEmpty())
      return group.element("xs:any");
    return super.createSchemaElement(builder, name, group, topLevel);
  }

}
