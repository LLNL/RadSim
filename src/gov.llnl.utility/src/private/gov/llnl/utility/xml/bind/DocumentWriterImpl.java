/*
 * Copyright 2016, Lawrence Livermore National Security, LLC.
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.utility.xml.bind;

import gov.llnl.utility.PackageResource;
import gov.llnl.utility.annotation.Internal;
import gov.llnl.utility.io.WriterException;
import gov.llnl.utility.xml.DomUtilities;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.zip.GZIPOutputStream;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Document writers convert an object into an XML document with the package
 * schema.
 *
 * @author nelson85
 * @param <Type>
 */
@Internal
class DocumentWriterImpl<Type> implements DocumentWriter<Type>
{
  WriterContextImpl context;
  ObjectWriter<Type> writer;

  public DocumentWriterImpl(ObjectWriter<Type> writer) throws WriterException
  {
    this.writer = writer;
    this.context = new WriterContextImpl(this, writer);
  }

  @Override
  public WriterContext getContext()
  {
    return context;
  }

  @Override
  public void saveFile(Path path, Type object) throws IOException, WriterException
  {
    Document document = toDocument(object);
    try ( OutputStream out = Files.newOutputStream(path))
    {
      OutputStream out2 = out;
      if (path.getFileName().toString().endsWith(".gz"))
        out2 = new GZIPOutputStream(out);
      DomUtilities.printXml(out2, document, context::getProperty);
      out2.flush();
      out2.close();
    }
  }

  @Override
  public void saveStream(OutputStream stream, Type object) throws IOException, WriterException
  {
    Document document = toDocument(object);
    try ( OutputStream out = stream)
    {
      DomUtilities.printXml(out, document, context::getProperty);
    }
  }

//  public static Document createDocument(ObjectWriter writer)
//  {
//    try
//    {
//      DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
//      DocumentBuilder documentBuilder = dbf.newDocumentBuilder();
//      DOMImplementation dom = documentBuilder.getDOMImplementation();
//      String ns = "";
//      if (writer.getPackage() != null)
//        ns = writer.getPackage().getNamespaceURI();
//      return dom.createDocument(
//              ns,
//              writer.getElementName(), null);
//    }
//    catch (ParserConfigurationException ex)
//    {
//      throw new RuntimeException(ex);
//    }
//  }
//
  @Override
  public Document toDocument(Type object) throws WriterException
  {
    context.clearReferences();
    Document document = context.newDocument(writer);
    context.write(writer, WriterContextImpl.ROOT, object);
    
    // Add the xsi declaration
    Element documentElement = document.getDocumentElement();

    // FIXME the writer context needs to keep track of the all the 
    // schema used in the document so we have a complete list to include in
    // the schema location. There may be a mechanism in the DOM system that
    // we are not aware of to perform this task.
    StringBuilder sb = new StringBuilder();
    for (PackageResource schema : context.schema)
    {
      if (schema == null)
        continue;
      sb.append(schema.getNamespaceURI());
      sb.append(" ");
      sb.append(schema.getSchemaURI());
      sb.append("  ");
    }
    String schemaLocation = sb.toString();
    if (!schemaLocation.isEmpty())
    {
      documentElement.setAttributeNS(
              "http://www.w3.org/2001/XMLSchema-instance",
              "xsi:schemaLocation",
              sb.toString());
    }
    return document;
  }

  @Override
  public String toXML(Type object) throws WriterException
  {
    Document document = toDocument(object);
    Element element = (Element) document.getDocumentElement();
    try ( ByteArrayOutputStream out = new ByteArrayOutputStream())
    {
      DomUtilities.newPrinter(out)
              .set("xml-declaration", false)
              .write(element);
      return new String(out.toByteArray());
    }
    catch (IOException ex)
    {
      throw new WriterException(ex);
    }
  }

  @Override
  public ObjectWriter getObjectWriter()
  {
    return this.writer;
  }

  @Override
  public void setProperty(String key, Object value)
  {
    this.getContext().setProperty(key, value);
  }

}
