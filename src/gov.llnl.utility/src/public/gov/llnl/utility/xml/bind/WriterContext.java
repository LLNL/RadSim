/*
 * Copyright 2016, Lawrence Livermore National Security, LLC.
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.utility.xml.bind;

import gov.llnl.utility.io.WriterException;
import gov.llnl.utility.xml.DomBuilder;
import gov.llnl.utility.xml.bind.ObjectWriter.WriterAttributes;
import gov.llnl.utility.xml.bind.ObjectWriter.WriterBuilder;
import org.w3c.dom.Document;

/**
 * Internal class used for marshalling objects to XML.
 *
 * @author nelson85
 */
public interface WriterContext
{

  /**
   * Create a new document.Used by the testing framework.
   *
   *
   * @param writer
   * @return
   */
  Document newDocument(ObjectWriter writer);

  /**
   * Create a new builder for a writer.
   *
   * This is usually accessed using ObjectWriter.newBuilder() as the context is
   * a hidden object.
   *
   * @param writer
   * @return
   */
  WriterBuilder newBuilder(ObjectWriter writer);

  /**
   * Get the element to be written.
   *
   * This is used to manually insert XML contents.
   *
   * @return
   */
  DomBuilder getDomBuilder();

  /**
   * Write an object into the document.
   *
   * @param <Type>
   * @param writer
   * @param elementName
   * @param object
   * @return
   * @throws WriterException
   */
  <Type> DomBuilder write(
          ObjectWriter<Type> writer,
          String elementName,
          Type object)
          throws WriterException;

  /**
   * Marshal the object as contents.
   * 
   * Strings are added directly.  All others go through marshallers.
   * 
   * @param <Type>
   * @param object
   * @throws WriterException 
   */
  <Type> void addContents(Type object) throws WriterException;

  /**
   * Get the attributes to use for writing this object.
   *
   * Used primarily during testing.
   *
   * @return
   */
  WriterAttributes getAttributes();

//<editor-fold desc="properties" defaultstate="collapsed">
  void setProperty(String key, Object value)
          throws UnsupportedOperationException;

  Object getProperty(String key);

  <T> T getProperty(String key, Class<T> cls, T defaultValue);
//</editor-fold>
//<editor-fold desc="references" defaultstate="collapsed">

  /**
   * Get the reference to a previously written object.
   *
   * @param obj
   * @return
   */
  String getReference(Object obj);

  /**
   * Clear existing references.
   *
   * This is used to free memory after loading objects. This should not be
   * called while objects are being written.
   */
  void clearReferences();
//</editor-fold>
//<editor-fold desc="marshallers" defaultstate="collapsed">  

  /**
   * Get the marshaller for a class if defined.
   *
   * @param cls
   * @return the marshaller or null if not available.
   * @throws WriterException
   */
  Marshaller getMarshaller(Class cls) throws WriterException;

  /**
   * Get the list of properties that will be applied to marshallers.
   *
   * @return
   */
  MarshallerOptions getMarshallerOptions();

  void setMarshaller(Marshaller marshall);

  /**
   * Marshaller options are a key based type store.
   */
  public interface MarshallerOptions
  {
    /**
     *
     * @param <Type>
     * @param key
     * @param cls
     * @param defaultValue
     * @return
     */
    <Type> Type get(String key, Class<Type> cls, Type defaultValue) throws ClassCastException;
  }
//</editor-fold>
}
