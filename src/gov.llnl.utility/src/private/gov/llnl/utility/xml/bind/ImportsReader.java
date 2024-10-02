/* 
 * Copyright 2016, Lawrence Livermore National Security, LLC.
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.utility.xml.bind;

import gov.llnl.utility.PackageResource;
import gov.llnl.utility.annotation.Internal;
import gov.llnl.utility.io.ReaderException;
import gov.llnl.utility.xml.DomBuilder;
import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.regex.Pattern;
import org.xml.sax.Attributes;

/**
 * Reads an object from an external source.
 *
 * This reader appears inside of the imports section.
 *
 * @author nelson85
 */
@Internal
final public class ImportsReader extends ObjectReader<Object>
{
  final ObjectReader objectReader;
  final DocumentReaderImpl documentReader;
//  ReaderContext localContext;

  public ImportsReader(ObjectReader reader)
  {
    this.objectReader = reader;
    this.documentReader = new DocumentReaderImpl(reader);
  }

  /**
   * This is a dynamically created reader and thus we create the declaration on
   * the fly.
   *
   * @return
   */
  @Override
  public Reader.Declaration getDeclaration()
  {
    return new ReaderDeclarationImpl()
    {
      @Override
      public Class<? extends PackageResource> pkg()
      {
        return objectReader.getDeclaration().pkg();
      }

      @Override
      public String name()
      {
        return objectReader.getDeclaration().name();
      }

      @Override
      public Order order()
      {
        return Order.FREE;
      }

      @Override
      public String schema()
      {
        return Reader.Declaration.NULL;
      }
    };
  }

  @Override
  @SuppressWarnings("unchecked")
  public Object start(ReaderContext context, Attributes attributes) throws ReaderException
  {
    URL url;
    String uri = attributes.getValue("extern");
    if (uri == null)
      throw new ReaderException("extern is required for import on " + this.getXmlName());

    String prioritizeSearchPaths = attributes.getValue("prioritizeSearchPaths");
    if (prioritizeSearchPaths != null)
    {
      url = context.getExternal(uri, Boolean.parseBoolean(prioritizeSearchPaths));
    }
    else
    {
      url = context.getExternal(uri);
    }
    try
    {
      synchronized (documentReader)
      {
        // Copy anyReader properties from the parent documentReader
        Map<String, Object> props = context.getDocumentReader().getProperties();
        // Load the file
        for (Map.Entry<String, Object> p : props.entrySet())
        {
          documentReader.setProperty(p.getKey(), p.getValue());
        }

        Object out = documentReader.loadURL(url);

        // Grab the context to extract references from
        context.setState(documentReader.getContext());

        this.documentReader.clearContext();
        return out;
      }
    }
    catch (IOException ex)
    {
      throw new ReaderException("Error loading resource " + url.toString(), ex);
    }
  }

  @Override
  @SuppressWarnings("unchecked")
  public Class<Object> getObjectClass()
  {
    return documentReader.getObjectReader().getObjectClass();
  }

  @Override
  public ElementHandlerMap getHandlers(ReaderContext context) throws ReaderException
  {
    ReaderBuilder<Object> builder = newBuilder();
    builder.reader(new ReferenceReader()).nop();
    return builder.getHandlers();
  }

  @Override
  public void createSchemaType(SchemaBuilder builder) throws ReaderException
  {
  }

  @Override
  public DomBuilder createSchemaElement(SchemaBuilder builder, String name, DomBuilder type, boolean options) throws ReaderException
  {
    return type;
  }

  /**
   * This element can appear inside of a extern to bring references into scope.
   */
  final class ReferenceReader extends ObjectReader<Object>
  {
    /**
     * This is a dynamically created reader and thus we create the declaration
     * on the fly.
     *
     * @return
     */
    @Override
    public Reader.Declaration getDeclaration()
    {
      return new ReaderDeclarationImpl()
      {
        @Override
        public Class<? extends PackageResource> pkg()
        {
          return objectReader.getDeclaration().pkg();
        }

        @Override
        public String name()
        {
          return "reference";
        }

        @Override
        public Order order()
        {
          return Order.ALL;
        }
      };
    }

    @Override
    public Object start(ReaderContext context, Attributes attributes) throws ReaderException
    {
      String prefix = attributes.getValue("prefix");
      if (prefix == null)
        prefix = "";
      else
        prefix += ".";

      // import a specific reference, may be renamed
      String from = attributes.getValue("import");
      ReaderContext localContext = (ReaderContext) context.getContext(ImportsReader.class).getState();
      if (from != null)
      {
        Object object = localContext.get(from, Object.class);
        if (object == null)
          throw new ReaderException("Unable to find imported reference " + from);
        String to = attributes.getValue("id");
        if (to == null)
          context.put(prefix + from, object);
        return object;
      }

      // Copy over all references that fit a particular pattern
      String filter = attributes.getValue("filter");
      if (filter != null)
      {
        if (attributes.getValue("id") != null)
          throw new ReaderException("id is not supported on filter imports");
        Pattern pattern = Pattern.compile(filter);
        for (Map.Entry<String, Object> entry : localContext.getReferences())
        {
          if (pattern.matcher(entry.getKey()).matches())
          {
            context.put(prefix + entry.getKey(), entry.getValue());
          }
        }
        return null;
      }
      throw new ReaderException("Incorrect reference");
    }

    @Override
    public Class<Object> getObjectClass()
    {
      return null;
    }

    @Override
    public ElementHandlerMap getHandlers(ReaderContext context) throws ReaderException
    {
      return null;
    }

    @Override
    public void createSchemaType(SchemaBuilder builder) throws ReaderException
    {
    }

    @Override
    public DomBuilder createSchemaElement(SchemaBuilder builder, String name, DomBuilder type, boolean options)
            throws ReaderException
    {
      return type;
    }
  }
}
