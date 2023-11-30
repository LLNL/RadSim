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
import gov.llnl.utility.io.ReaderException;
import gov.llnl.utility.xml.DomBuilder;
import gov.llnl.utility.xml.bind.Reader.Option;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.logging.Level;

/**
 * Standard methods for creating the schema.
 *
 * @author nelson85
 */
@Internal
public class SchemaBuilderUtilities
{
  public static <Component> DomBuilder createSchemaTypeDefaultString(Reader<Component> reader, SchemaBuilder builder) throws ReaderException
  {
    // Create a type definition
    DomBuilder type = builder.getRoot()
            .element("xs:complexType")
            .attr("name", reader.getSchemaType());
    Reader.Declaration decl = reader.getDeclaration();
    Reader.TextContents textContents = reader.getTextContents();
    DomBuilder contents = type.element("xs:simpleContent")
            .element("xs:extension");
    if (textContents != null)
      contents.attr("base", textContents.base());
    else
      contents.attr("base", "xs:string");

    if (decl.referenceable())
      contents.element("xs:attributeGroup").attr("ref", "util:object-attribs");

    if (decl.copyable())
      contents.element("xs:attribute")
              .attr("name", "copy_of").attr("type", "xs:string");

    AttributesUtilities.createSchemaType(contents, reader.getAttributesDecl(), reader.getAnyAttributeDecl());
    return type;
  }

  /**
   * Standard schema definition for XML Readers.
   *
   * @param <Component>
   * @param reader
   * @param builder
   * @return
   * @throws ReaderException
   */
  public static <Component> DomBuilder createSchemaTypeDefault(Reader<Component> reader, SchemaBuilder builder) throws ReaderException
  {
    UtilityPackage.LOGGER.log(Level.FINE, "Create schema type for {0}", reader.getSchemaType());

    // Create a type definition
    DomBuilder type = builder.getRoot()
            .element("xs:complexType")
            .attr("name", reader.getSchemaType());

    // Get the declaration
    Reader.Declaration decl = reader.getDeclaration();

    // Define the contents
    if (decl.contents() == Reader.Contents.TEXT)
    {
      Reader.TextContents textContents = reader.getTextContents();
      type = type.element("xs:simpleContent")
              .element("xs:extension");
      if (textContents != null)
        type.attr("base", textContents.base());
      else
        type.attr("base", "xs:string");
    }
    else
    {
      // Element or mixed contents.
      if (decl.contents() == Reader.Contents.MIXED)
      {
        type.attr("mixed", "true");
      }

      // Add in definitions for each element
      Reader.ElementHandlerMap handlers = reader.getHandlers(new SchemaReaderContext(reader));
      if (handlers != null)
      {
        handlers.createSchemaType(builder, type);
      }
    }

    // Automatically set up attributes that are attributed
    Class objectClass = reader.getObjectClass();
    if (objectClass != null && decl.autoAttributes())
    {
      // sort methods before tyring to write xsd.
      Method[] methods = objectClass.getDeclaredMethods();
      Arrays.sort(methods,
              (Method o1, Method o2) -> o1.getName().compareToIgnoreCase(o2.getName()));
      for (Method method : methods)
      {
        Reader.Attribute attr = method.getAnnotation(Reader.Attribute.class);
        if (attr == null)
          continue;
        AttributeHandlers.declareAttribute(type, attr, method);
      }
    }

    // Add the standard attribs
    if (decl.referenceable())
      type.element("xs:attributeGroup").attr("ref", "util:object-attribs");

    if (decl.copyable())
      type.element("xs:attribute").attr("name", "copy_of").attr("type", "xs:string");

    AttributesUtilities.createSchemaType(type, reader.getAttributesDecl(), reader.getAnyAttributeDecl());
    return type;
  }

  /**
   * Create a schema definition entry for a reader.
   *
   * @param <Component>
   * @param reader
   * @param builder
   * @param name
   * @param group
   * @param topLevel
   * @return
   * @throws ReaderException
   */
  public static <Component> DomBuilder createSchemaElementDefault(
          Reader<Component> reader,
          SchemaBuilder builder,
          String name,
          DomBuilder group,
          boolean topLevel)
          throws ReaderException
  {
    String ns = SchemaBuilder.getXmlPrefix(reader);

    // Otherwise reference the type
    DomBuilder out = group.element("xs:element")
            .attr("name", name);

    PackageResource resource = reader.getPackage();
    if (resource != null)
    {
      String prefix = resource.getSchemaPrefix();
      if (!builder.hasNamespace(resource))
        throw new ReaderException("Package does not include " + resource.getNamespaceURI());
      if (!prefix.equals(Schema.NONE))
        ns = resource.getSchemaPrefix() + ":";
      else
        ns = "";
    }
    String schematype = reader.getSchemaType();
    if (!schematype.isEmpty())
      out.attr("type", ns + schematype);

    // Element types at the top level include the Java class which uses it.
    if (topLevel)
    {
      String util = UtilityPackage.getInstance().getNamespaceURI();
      Class objectClass = reader.getObjectClass();
      if (objectClass != null)
        out.attrNS(util, "util:class", objectClass.getName());
    }

    return out;
  }

  public static <Obj> DomBuilder createSchemaElementSimple(Reader<Obj> reader, SchemaBuilder builder, String name, DomBuilder group, boolean topLevel)
  {
    DomBuilder out = group.element("xs:element").attr("name", name);

    // Text contents is required for this type.
    Reader.TextContents textContents = reader.getTextContents();
    if (textContents == null)
      throw new NullPointerException("text contents not set on " + reader.getClass());
    out.attr("type", textContents.base());

    // If this is a top level then add an entry so it will be 
    // avaliable by name.
    //
    // FIXME this is poor design because we are storing the object
    // type not the reader type.  One object may have many 
    // readers so the mapping is not  one-to-one, but this interface
    // forces each object ot have only one reader.
    if (topLevel)
    {
      String util = UtilityPackage.getInstance().getNamespaceURI();
      Class objectClass = reader.getObjectClass();
      if (objectClass != null)
        out.attrNS(util, "util:class", objectClass.getName());
    }

    return out;
  }

  /**
   * Add standard options to the schema.
   *
   * @param group
   * @param options
   */
  public static void applyOptions(DomBuilder group, EnumSet<Option> options)
  {
    // If no options are specified, we can skip this step.
    if (options == null)
      return;

    // Add each option as an attribute
    for (Option ops : options)
    {
      if (ops.getKey() != null)
        group.attr(ops.getKey(), ops.getValue());
    }
  }
}
