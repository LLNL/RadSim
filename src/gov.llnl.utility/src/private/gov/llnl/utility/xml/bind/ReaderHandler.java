/* 
 * Copyright 2016, Lawrence Livermore National Security, LLC.
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.utility.xml.bind;

import gov.llnl.utility.annotation.Internal;
import gov.llnl.utility.io.ReaderException;
import gov.llnl.utility.xml.DomBuilder;
import gov.llnl.utility.xml.bind.Reader.ElementHandlerMap;
import gov.llnl.utility.xml.bind.Reader.Option;
import java.util.EnumSet;
import java.util.function.BiConsumer;
import org.xml.sax.Attributes;

/**
 *
 * @author nelson85
 * @param <T>
 * @param <T2>
 */
@Internal
public class ReaderHandler<T, T2> extends ElementHandlerImpl<T, T2>
{
  final Reader reader;
  final Reader.Declaration decl;

  @SuppressWarnings("unchecked")
  public ReaderHandler(String key, EnumSet<Option> flags, BiConsumer<T, T2> method, Reader reader)
  {
    super(key, flags, reader.getObjectClass(), method);
    this.reader = reader;
    this.decl = reader.getDeclaration();
    Reader.Contents contents = decl.contents();
    if (contents == Reader.Contents.TEXT || contents == Reader.Contents.MIXED)
      this.addOption(Option.CAPTURE_TEXT);
  }

  @Override
  @SuppressWarnings("unchecked")
  public Object onStart(ReaderContext context, Attributes attr) throws ReaderException
  {
    // Call the reader to create the initial object using the attributes
    Object obj = reader.start(context, attr);
  
    // If we have automatic attributes then apply them

    if (Option.check(decl.options(), Option.AUTO_ATTRIBUTES))
    {
      if (obj == null)
        throw new ReaderException("Auto attributes applied to null object");

      AttributeHandlers attributeHandlers = new AttributeHandlers(obj.getClass());
      attributeHandlers.applyAttributes(context, obj, attr);
    }
    return obj;
  }

  @Override
  public Object onEnd(ReaderContext context) throws ReaderException
  {
    // Finish the object
    return reader.end(context);
  }

  @Override
  public Object onTextContent(ReaderContext context, String textContent) throws ReaderException
  {
    return reader.contents(context, textContent);
  }

  @Override
  public ElementHandlerMap getHandlers(ReaderContext context) throws ReaderException
  {
    return reader.getHandlers(context);
  }

  @Override
  public Reader getReader()
  {
    return reader;
  }

  @Override
  public void createSchemaElement(SchemaBuilder builder, DomBuilder group) throws ReaderException
  {
    // Ask the reader to create its element schema
    group = reader.createSchemaElement(builder, getName(), group, false);

    // Add the options as requested
    SchemaBuilderUtilities.applyOptions(group, options);
  }

  /**
   * Get as String for debugging.
   *
   * @return a String describing this object.
   */
  @Override
  public String toString()
  {
    StringBuilder sb = new StringBuilder();
    sb.append("ReaderHandler(");
    sb.append(this.getKey());
    sb.append(")");
    return sb.toString();
  }

}
