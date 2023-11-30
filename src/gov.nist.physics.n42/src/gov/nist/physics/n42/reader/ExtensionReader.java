/*
 * Copyright 2022, Lawrence Livermore National Security, LLC.
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.nist.physics.n42.reader;

import gov.llnl.utility.PackageResource;
import gov.llnl.utility.xml.bind.ReaderDeclarationImpl;
import gov.llnl.utility.io.ReaderException;
import gov.llnl.utility.xml.DomBuilder;
import gov.llnl.utility.xml.bind.AnyReader;
import gov.llnl.utility.xml.bind.Reader;
import gov.llnl.utility.xml.bind.ReaderContext;
import gov.llnl.utility.xml.bind.SchemaBuilder;
import gov.nist.physics.n42.N42Package;
import gov.nist.physics.n42.utility.IgnoreReader;
import java.util.HashMap;
import org.xml.sax.Attributes;

/**
 *
 * @author nelson85
 */
@Reader.Declaration(pkg = N42Package.class,
        name = "Extension")
public class ExtensionReader extends AnyReader
{
  private final String name;

  public ExtensionReader(String name)
  {
    super(Object.class);
    this.name = name;
  }

  @Override
  public Reader.Declaration getDeclaration()
  {
    return new ExtensionDeclaration();
  }

  @Override
  public Object start(ReaderContext context, Attributes attributes) throws ReaderException
  {
    return new HashMap<>();
  }

  @Override
  public ElementHandlerMap getHandlers(ReaderContext context) throws ReaderException
  {
    return null;
  }

  @Override
  public String getSchemaType()
  {
    return name;
  }

  @Override
  public DomBuilder createSchemaElement(SchemaBuilder builder, String name, DomBuilder group, boolean topLevel)
          throws ReaderException
  {
    String ns = SchemaBuilder.getXmlPrefix(this);

    // Otherwise reference the type
    DomBuilder out = group.element("xs:element");

    if (!topLevel)
    {
      out.attr("ref", ns + this.name);
    }
    else
    {
      out.attr("name", this.name);
      out.attr("abstract", "true");
      out.attr("nillable", "false");
    }
    return out;
  }

  @Override
  public void createSchemaType(SchemaBuilder builder)
          throws ReaderException
  {
    // These are abstract elements so they don't need types.
    // But they will require a element type.
    builder.addObjectReader(this);
  }

  @Override
  public Reader findReader(String namespaceURI, String localName, String qualifiedName, Attributes attr)
  {
    Reader out = super.findReader(namespaceURI, localName, qualifiedName, attr);
    if (out != null)
      return out;
    // Otherwise if this is a different namespace than the current we can just ignore it
    if (!this.getPackage().getNamespaceURI().equals(namespaceURI))
    {
      return new IgnoreReader();
    }
    return null;
  }

  private static Object notUsed()
  {
    throw new UnsupportedOperationException();
  }

  /**
   * Declaration for an Extension.
   *
   * This is a misuse of the Java annotation system because we need to generate
   * annotations on the fly.
   */
  @SuppressWarnings("AnnotationAsSuperInterface")
  class ExtensionDeclaration extends ReaderDeclarationImpl
  {
    @Override
    public Class<? extends PackageResource> pkg()
    {
      return N42Package.class;
    }

    @Override
    public String name()
    {
      return name;
    }

    @Override
    public Contents contents()
    {
      return Contents.MIXED;
    }

    @Override
    public boolean document()
    {
      return true;
    }

    @Override
    public boolean autoAttributes()
    {
      return (boolean) notUsed();
    }

    @Override
    public Class cls()
    {
      return Object.class;
    }

  }

}
