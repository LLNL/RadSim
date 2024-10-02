/* 
 * Copyright 2016, Lawrence Livermore National Security, LLC.
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.utility.xml.bind;

import gov.llnl.utility.io.PathLocation;
import gov.llnl.utility.io.ReaderException;
import java.net.URI;
import java.net.URL;
import java.util.Map;
import java.util.function.BiConsumer;
import org.xml.sax.Attributes;

/**
 * State of the ObjectReader while processing.
 *
 * This holds attributes and all references.
 */
public interface ReaderContext
{
  /**
   * Get the document that is being read.
   *
   * @return the documentReader
   */
  DocumentReader getDocumentReader();

  /**
   * Get a temporary storage the the context.
   *
   * @return
   */
  default Object getState()
  {
    return null;
  }

  /**
   * Set the temporary storage for the context.
   *
   * @param state
   */
  default void setState(Object state)
  {
  }

  /**
   * Get an object reference associated with this context.
   *
   * @param <T>
   * @param name is the name of the element.
   * @param cls is the class for requested reference.
   * @return the references object or null if not found.
   */
  <T> T get(String name, Class<T> cls);

  /**
   * Get all references in the document.
   *
   * @return
   */
  Iterable<Map.Entry<String, Object>> getReferences();

  /**
   * Define a global object reference.
   *
   * To add a local reference use getCurrent().putReference().
   *
   * @param <Obj>
   * @param name is the name of the reference
   * @param object is the object to reference or null to remove the reference.
   * @return the object.
   * @throws ReaderException if the definition results in an exception in a
   * deferred action.
   */
  <Obj> Obj put(String name, Obj object) throws ReaderException;

  /**
   * Get the current element path for this context. Used primarily to print
   * exception debugging.
   *
   * @return a string describing the location in the document.
   */
  String getElementPath();

  /**
   * Fetch an external referenced object relative to this context.
   *
   * Uses the DocumentReader.SEARCH_PATHS property to locate any resources that
   * are not found in the same directory as the document being processed.
   *
   * @param extern is the URL name of the entity to locate.
   * @return the URL of the named object.
   * @throws ReaderException if the resource cannot be located or the external
   * resource is malformed.
   *
   */
  default URL getExternal(String extern) throws ReaderException
  {
    return getExternal(extern, false);
  }
  
  /**
   * Fetch an external referenced object relative to this context.
   *
   * Uses the DocumentReader.SEARCH_PATHS property to locate any resources that
   * are not found in the same directory as the document being processed.
   *
   * @param extern is the URL name of the entity to locate.
   * @param prioritizeSearchPaths prioritize the search paths over internal local path
   * @return the URL of the named object.
   * @throws ReaderException if the resource cannot be located or the external
   * resource is malformed.
   *
   */
  URL getExternal(String extern, boolean prioritizeSearchPaths) throws ReaderException;
  
  

  /**
   * Gets the current file being loaded.
   *
   * @return the URI for the file being processed.
   */
  URI getFile();

  /**
   * Returns the current document location. PathLocation includes the file, line
   * number, and element path.
   *
   * @return the path location in the current document.
   */
  PathLocation getLocation();

  /** 
   * Searches through parent context to find the one for the current reader.
   * 
   * @param cls is a reader class.
   * @return the context found or null if not found.
   */
  ElementContext getContext(Class cls);
  
  /**
   * Gets the context for parent xml element.
   * 
   * This object may not be constructed.
   *
   * @return the parent context handler.
   */
  ElementContext getParentContext();

  /**
   * Gets the context for the current xml element.
   *
   * @return the current context handler.
   */
  ElementContext getCurrentContext();

  /**
   * Gets the context in the last xml element.
   *
   * This includes the attributes and element name. This function is used to
   * view the attributes when processing a directive.  The context is only valid
   * during the "call" operation.
   *
   * @return the context for the last handler.
   */
  ElementContext getChildContext();

  /**
   * Set the context to execute a deferred method.
   *
   * This should be used in conjunction with popTemporaryContext. This is also
   * used in testing to set up the context for an emulated action.
   *
   * @param object
   * @param previous
   */
  void pushTemporaryContext(Object object, Object previous);

  /**
   * Restore the context.
   */
  void popTemporaryContext();

  public interface ExceptionHandler
  {
    void handle(ReaderContext context, Throwable ex) throws ReaderException;
  }

  /**
   * A method for handling all checked exceptions that are received while
   * processing a document.
   *
   * @param handler
   */
  void setErrorHandler(ExceptionHandler handler);

  /**
   * Call to the error handler.
   *
   * @param ex
   * @throws ReaderException
   */
  void handleException(Throwable ex) throws ReaderException;

  /**
   * Set an external source of properties.
   *
   * @param handler
   */
  void setPropertyHandler(PropertyMap handler);

  /**
   * Defers an action until a reference is defined.
   *
   * @param <T>
   * @param <T2>
   * @param target is the parent to receive the action.
   * @param method is the action to call.
   * @param refId is the name of the reference.  If null this action is ignored.
   * @param cls is the class of the argument.
   * @throws ReaderException
   */
  <T, T2> void addDeferred(T target, BiConsumer<T, T2> method, String refId, Class<T2> cls) throws ReaderException;

  /**
   * ElementContext holds the state of the parser at a particular place in the
 document.
   */
  public static interface ElementContext
  {
//<editor-fold desc="objects" defaultstate="collapsed">   
    /**
     * Get the object that is being constructed from the XML element.
     *
     * @return the target object or null if not available.
     */
    Object getObject();

    /** 
     * The state associated with this context.
     * 
     * @return the state for this object reader or null if state was not set.
     */    
    Object getState();

    /**
     * Get the object that will hold this XML element.
     *
     * @return the object or null if it does not yet exist.
     */
    Object getParentObject();

//</editor-fold>   
//<editor-fold desc="XML" defaultstate="collapsed">

    /**
     * Get namespace of the current element.
     *
     * @return the namespace or null if not available.
     */
    String getNamespaceURI();

    /**
     * Get the element name for the current element.
     *
     * @return the localname or null if not available.
     */
    String getLocalName();

    /**
     * Get the attributes for the element.
     *
     * @return attributes or null if not available.
     */
    default Attributes getAttributes()
    {
      return null;
    }
//</editor-fold>
  }
}
