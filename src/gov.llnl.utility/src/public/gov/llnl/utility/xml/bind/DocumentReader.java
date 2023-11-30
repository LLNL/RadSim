/*
 * Copyright 2016, Lawrence Livermore National Security, LLC.
 * All rights reserved
 *
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.utility.xml.bind;

import gov.llnl.utility.Configurable;
import gov.llnl.utility.io.ReaderException;
import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.net.URL;
import java.nio.file.Path;
import java.util.Map;
import org.xml.sax.InputSource;

/**
 * Document readers are used to deserialize objects from xml.
 *
 * They can be used on files, urls, resources or streams. DocumentReaders are
 * created by the PackageResource which holds the schema.
 *
 * @author nelson85
 * @param <Component>
 */
public interface DocumentReader<Component> extends Configurable
{
  /**
   * Instruction to Document writer to compute the M5SUM for files read.
   *
   * Type is Boolean.
   */
  public static final String COMPUTE_MD5SUM = "http://utility.llnl.gov/DocumentReader#computeMD5Sum";

  /**
   * Result of completed M5SUM calculations.
   *
   * Type is Map&lt;String,String&gt;.
   */
  public static final String RESULT_MD5SUM = "http://utility.llnl.gov/DocumentReader#resultMD5Sum";

  /**
   * Specify a set of paths to search for external files.
   *
   * Type is Path[].
   */
  public static final String SEARCH_PATHS = "http://utility.llnl.gov/DocumentReader#Paths";

  public static final String SCHEMA_SOURCE = "http://java.sun.com/xml/jaxp/properties/schemaSource";

  public static final String XSLT_SOURCE = "http://utility.llnl.gov/DocumentReader#xslt";

  /**
   * Create an implementation for a document writer.
   *
   * @param <Type>
   * @param writer
   * @return a new document writer.
   */
  @SuppressWarnings("unchecked")
  static <Type> DocumentReader<Type> create(ObjectReader<Type> writer)
  {
    return new DocumentReaderImpl(writer);
  }

  @SuppressWarnings("unchecked")
  static <Type> DocumentReader<Type> create(Class<Type> cls) throws ReaderException
  {
    return new DocumentReaderImpl(cls);
  }

  /**
   * Remove the context. This is needed if a document reader is reused.
   */
  void clearContext();

  /**
   * Get the reader context. 
   * 
   * Reader context can be used to set properties on how
   * the data is to be interpreted. It can also be used to retrieve named
   * objects from the serialized object.
   *
   * @return the current context for reading.
   */
  ReaderContext getContext();

  default Component loadString(String str) throws ReaderException
  {
    InputStream stream = new ByteArrayInputStream(str.getBytes());
    return this.loadStream(stream);
  }
  
  /**
   * Load a file from a path.
   *
   * External resources requested in the file are treated as files relative to
   * the loaded file.
   *
   * @param file
   * @return
   * @throws ReaderException
   * @throws FileNotFoundException
   * @throws IOException
   */
  Component loadFile(Path file) throws ReaderException, FileNotFoundException, IOException;

  /**
   * Load from a resource. When loaded from a resource all external references
   * are treated as resources loads as well.
   *
   * @param resourceName
   * @return
   * @throws ReaderException
   * @throws IOException
   */
  Component loadResource(String resourceName) throws ReaderException, IOException;

  /**
   * Load from a stream. Note that when loading directly from a stream the file
   * must be self contained and thus cannot have external references.
   *
   * @param stream
   * @return
   * @throws ReaderException
   */
  Component loadStream(InputStream stream) throws ReaderException;

  /**
   * Load an object from a url. When loading from a url, all external references
   * are treated as url requests.
   *
   * @param url
   * @return
   * @throws IOException
   * @throws ReaderException
   */
  Component loadURL(URL url) throws IOException, ReaderException;

  /**
   * Load an object from a sax input source.
   *
   * All the other load methods are just front ends for this method.
   *
   * @param inputSource
   * @return
   * @throws ReaderException
   */
  Component loadSource(InputSource inputSource) throws ReaderException;

  /**
   * Set a handler for exceptions that occur during processing of the document.
   * This will allow the ability to continue to process the document in the
   * event of an error.
   *
   * @param exceptionHandler
   */
  void setErrorHandler(ReaderContext.ExceptionHandler exceptionHandler);

  void setPropertyHandler(PropertyMap handler);

  /**
   * Get the underlying object reader.
   *
   * @return the object reader for this document.
   */
  ObjectReader<Component> getObjectReader();

  /**
   * Get the properties set on this document reader.
   *
   * @return an unmodifiable collection of properties.
   */
  @Override
  Map<String, Object> getProperties();

  /** 
   * Creates a context for the document reader.Required for testing.
   * 
   * 
   * @return 
   * @throws gov.llnl.utility.io.ReaderException 
   */
  ReaderContext createContext() throws ReaderException;
  
  @Retention(RetentionPolicy.RUNTIME)
  public @interface Hooks
  {
    Class<? extends Hook> value();
  }
  
  public interface Hook
  {
    public void startDocument(DocumentReader dr);

    public void endDocument(DocumentReader dr);
  }
}
