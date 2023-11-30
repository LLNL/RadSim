/*
 * Copyright 2016, Lawrence Livermore National Security, LLC. 
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.utility.xml.bind;

import gov.llnl.utility.ReflectionUtilities;
import gov.llnl.utility.io.DataFileWriter;
import gov.llnl.utility.io.WriterException;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Path;
import org.w3c.dom.Document;

/**
 * Document writers convert objects into xml documents using an ObjectWriter.DocumentWriter can be created from a PackageResource.
 *
 * @author nelson85
 * @param <Type>
 */
public interface DocumentWriter<Type> extends DataFileWriter<Type>
{
  /**
   * Create an implementation for a document writer.
   *
   * @param <Type>
   * @param writer
   * @return a new document writer.
   */
  @SuppressWarnings("unchecked")
  static <Type> DocumentWriter<Type> create(ObjectWriter<Type> writer)
  {
    return ReflectionUtilities
            .getConstructor("gov.llnl.utility.xml.bind.DocumentWriterImpl", DocumentWriter.class, ObjectWriter.class)
            .apply(writer);
  }

  static <Type> DocumentWriter<Type> create(Class<Type> cls) throws WriterException
  {
    return create(ObjectWriter.create(cls));
  }

  /**
   * Get the context for the writer. This can be used to define marshallers or
   * set properties for the writer.
   *
   * @return
   */
  WriterContext getContext();

  /**
   * Get the object writer used to serialize objects.
   *
   * @return
   */
  ObjectWriter getObjectWriter();

  /**
   *
   * @param path
   * @param object
   * @throws IOException
   * @throws WriterException
   */
  @Override
  void saveFile(Path path, Type object) throws IOException, WriterException;

  /**
   * Save an object to a stream.
   *
   * @param stream
   * @param object
   * @throws IOException
   * @throws WriterException
   */
  @Override
  void saveStream(OutputStream stream, Type object) throws IOException, WriterException;

  /**
   * Convert an object to a document.
   *
   * @param object
   * @return
   * @throws WriterException
   */
  Document toDocument(Type object) throws WriterException;

  /**
   * Convert an object to a string for debugging.
   *
   * @param object
   * @return
   * @throws WriterException
   */
  String toXML(Type object) throws WriterException;
  
  void setProperty(String key, Object value);
}
