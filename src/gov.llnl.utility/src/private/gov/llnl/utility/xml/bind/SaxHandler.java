/* 
 * Copyright 2016, Lawrence Livermore National Security, LLC.
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.utility.xml.bind;

import gov.llnl.utility.annotation.Internal;
import gov.llnl.utility.io.ReaderException;
import java.util.function.Consumer;
import javax.xml.XMLConstants;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

/**
 *
 * @author nelson85
 */
@Internal
public class SaxHandler extends DefaultHandler
{
  private final ReaderContextImpl readerContext;
  private ElementContextImpl handlerContext;
  private Consumer<Attributes> consumer = null;
  private boolean validate = false;
  private Consumer handleLocator;

  public static class SAXExceptionProxy extends SAXException
  {
    public Exception exception;

    SAXExceptionProxy(Exception ex)
    {
      super(ex);
      exception = ex;
    }
  }

  public SaxHandler(ReaderContextImpl context)
  {
    if (context == null)
      throw new NullPointerException("readerContext fail");
    this.readerContext = context;
  }

  @Override
  public void setDocumentLocator(org.xml.sax.Locator locator)
  {
    super.setDocumentLocator(locator);
    readerContext.setLocator(locator);
  }

  @Override
  public void startPrefixMapping(String prefix, String uri) throws SAXException
  {
    boolean b = uri.equals(XMLConstants.W3C_XML_SCHEMA_INSTANCE_NS_URI);
    if (b)
    {
      consumer = (p) -> this.handleSchema(p);
    }
  }

  void handleSchema(Attributes attr)
  {
    String includedSchema = attr.getValue(XMLConstants.W3C_XML_SCHEMA_INSTANCE_NS_URI, "schemaLocation");
    if (includedSchema != null)
    {
      SchemaManagerImpl schemaMgr = (SchemaManagerImpl) SchemaManager.getInstance();
      schemaMgr.processSchemaLocation(includedSchema);
    }
  }

  @Override
  public void endPrefixMapping(String string) throws SAXException
  {
  }

  @Override
  public void startElement(String uri, String localName, String qualifiedName, Attributes attr)
          throws SAXException
  {
    try
    {
      if (consumer != null)
      {
        consumer.accept(attr);
        consumer = null;
      }
      this.handlerContext = readerContext.startElement(uri, localName, qualifiedName, attr);
    }
    catch (ReaderException | NullPointerException ex)
    {
      throw new SAXExceptionProxy(ex);
    }
  }

  @Override
  public void endElement(String uri, String localName, String qualifiedName)
          throws SAXException
  {
    try
    {
      this.handlerContext = readerContext.endElement();
    }
    catch (ReaderException ex)
    {
      throw new SAXExceptionProxy(ex);
    }
  }

  @Override
  public void characters(char[] chars, int start, int length) throws SAXException
  {
    this.handlerContext.characters(chars, start, length);
  }

  @Override
  public void ignorableWhitespace(char[] chars, int i, int i1) throws SAXException
  {
    // not used
  }

  @Override
  public void warning(SAXParseException ex) throws SAXException
  {
    throw ex;
  }

  @Override
  public void error(SAXParseException ex) throws SAXException
  {
    throw ex;
  }

  @Override
  public void fatalError(SAXParseException ex) throws SAXException
  {
    throw ex;
  }

  public boolean isValidate()
  {
    return validate;
  }

  public void setValidate(boolean validate)
  {
    this.validate = validate;
  }

}
