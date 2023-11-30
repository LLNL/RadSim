/*
 * Copyright 2016, Lawrence Livermore National Security, LLC. 
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.utility.xml.bind;

import gov.llnl.utility.Singletons;
import gov.llnl.utility.io.WriterException;
import java.net.URI;
import java.net.URL;
import org.xml.sax.EntityResolver;

/**
 * SchemaManager processes schema files to define reader and writers for
 * objects.
 * 
 * The creation of readers and writers is largely governed by the schema
 * file.  Our xsd files contain augmentations that indicate which java class
 * we will marshal to and from.
 *
 * @author nelson85
 */
public interface SchemaManager extends Singletons.Singleton
{
//<editor-fold desc="singleton">
  static final SchemaManager SELF
          = new gov.llnl.utility.xml.bind.SchemaManagerImpl();

  static SchemaManager getInstance()
  {
    return SELF;
  }
  
  public void alias(URI systemId, URL location);

//</editor-fold>
//<editor-fold desc="get readers">
  /** 
   * Determine the type of object that will be produced given an xml
   * element.
   * 
   * @param namespaceURI
   * @param localName
   * @return the Java class for the xml element or null if not defined.
   * @throws ClassNotFoundException if the class cannot be loaded.
   */
  Class<?> getObjectClass(String namespaceURI, String localName)
          throws ClassNotFoundException;

  /**
   * Locate the object reader by class.
   * 
   * @param <T>
   * @param cls
   * @return the reader or null if not available.
   */
  <T> ObjectReader<T> findObjectReader(Class<T> cls);
  
  <T> ObjectWriter<T> findObjectWriter(WriterContext context, Class<T> cls)
          throws WriterException;

//</editor-fold>
//<editor-fold desc="registry">
  /** Adds a new method for generating readers for a class.
   * 
   * This is not used often as most of our classes are well defined.
   * Likely should be deprecated and reworked.
   * 
   * @param factory 
   */ 
  void registerReaderFactory(ReaderFactory factory);

  /** Define a reader for a class.  
   * 
   * This is used when a class is external to our framework and requires
   * a reader.  This is a very uncommon case.  The schema has pretty much
   * replaced this usage as the schema can define the reader for elements.
   * 
   * @param cls
   * @param readerCls 
   */
  void registerReaderFor(Class cls, Class<? extends ObjectReader> readerCls);

  
  void registerWriterFactory(WriterFactory factory);

  void registerWriterFor(Class cls, Class<? extends ObjectWriter> writerCls);

  /** 
   * Access the entity resolved directly.
   * 
   * This is used when testing or validating a document.
   * 
   * @return 
   */
  EntityResolver getEntityResolver();

  public interface ReaderFactory
  {
    ObjectReader getReader(Class cls);
  }

  public interface WriterFactory
  {
    ObjectWriter getWriter(Class cls);
  }

//</editor-fold>
}
