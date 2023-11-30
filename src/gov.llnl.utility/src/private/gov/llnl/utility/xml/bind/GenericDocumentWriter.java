/*
 * Copyright 2016, Lawrence Livermore National Security, LLC. 
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.utility.xml.bind;

import gov.llnl.utility.annotation.Internal;
import gov.llnl.utility.io.WriterException;
import gov.llnl.utility.xml.DomUtilities;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Used to support toXML in ObjectWriter.
 * 
 * @author nelson85
 */
@Internal
public class GenericDocumentWriter
{

  @SuppressWarnings("unchecked")
  public static <T> String dumpXML(ObjectWriter<T> objectWriter, T object) throws WriterException
  {
    ObjectWriter<Generic> genericWriter = new GenericWriter(objectWriter);
    DocumentWriter<Generic> genericDocumentWriter = DocumentWriter.create(genericWriter);
    Document document = genericDocumentWriter.toDocument(new Generic(object));
    Element element = (Element) document.getDocumentElement().getFirstChild();
    try (ByteArrayOutputStream out = new ByteArrayOutputStream())
    {
      DomUtilities.printXml(out, element, null);
      out.flush();
      return new String(out.toByteArray());
    }
    catch (IOException ex)
    {
      throw new WriterException(ex);
    }
  }

  @Internal
  static public class Generic<T>
  {
    T object;

    private Generic(T object)
    {
      this.object = object;
    }
  }

  @Internal
  static public class GenericWriter<T> extends ObjectWriter<Generic<T>>
  {
    private final ObjectWriter<T> writer;

    public GenericWriter(ObjectWriter<T> writer)
    {
      super(ObjectWriter.Options.NONE, "generic", writer.getPackage());
      this.writer = writer;
    }

    @Override
    public void attributes(ObjectWriter.WriterAttributes attributes, Generic<T> object) throws WriterException
    {
    }

    @Override
    public void contents(Generic<T> object) throws WriterException
    {
      WriterBuilder wb = newBuilder();
      wb.writer(writer).put(object.object);
    }

  }
}
