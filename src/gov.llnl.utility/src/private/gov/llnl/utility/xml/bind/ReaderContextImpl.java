/*
 * Copyright 2016, Lawrence Livermore National Security, LLC.
 * All rights reserved
 *
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.utility.xml.bind;

import gov.llnl.utility.Copyable;
import gov.llnl.utility.Serializer;
import gov.llnl.utility.URIUtilities;
import gov.llnl.utility.annotation.Internal;
import gov.llnl.utility.io.PathLocation;
import gov.llnl.utility.io.ReaderException;
import gov.llnl.utility.xml.bind.Reader.ElementHandler;
import gov.llnl.utility.xml.bind.Reader.Option;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import java.io.NotSerializableException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author nelson85
 */
@Internal
public class ReaderContextImpl implements ReaderContext
{
  static ElementHandlerImpl DEFERRED = new ElementHandlerImpl("#deferred", null, null, null);

  // Information on the current document being loaded
  URI currentFile = null;
  URI currentPath = null;
  Locator locator;

  // State variables
  final Map<String, Object> documentReferences = new HashMap<>();
  ElementContextImpl currentContext = null;
  final DeferredMap deferred = new DeferredMap();
  ExceptionHandler exceptionHandler = null;
  boolean contextAccessed = false;
  DocumentReader documentReader;
  PropertyMap propertyHandler = null;
  ElementHandlerCache cache = new ElementHandlerCache();

  public ReaderContextImpl()
  {
  }

//<editor-fold desc="state" defaultstate="collapsed">
  @Override
  public Object getState()
  {
    contextAccessed = true;
    return this.currentContext.state;
  }

  @Override
  public void setState(Object state)
  {
    contextAccessed = true;
    this.currentContext.state = state;
  }

//</editor-fold>
//<editor-fold desc="location-handling" defaultstate="collapsed">
  @Override
  public PathLocation getLocation()
  {
    String element = getElementPath();
    return new PathLocation(currentFile, locator != null ? locator.getLineNumber() : -1, element);
  }

  @Override
  public String getElementPath()
  {
    StringBuilder sb = new StringBuilder();
    ElementContextImpl context = this.currentContext;
    while (context != null)
    {
      sb.insert(0, context.localName);
      sb.insert(0, '/');
      context = context.parentContext;
    }
    return sb.toString();
  }

  public void setFile(URI file)
  {
    if (file == null)
      throw new NullPointerException();
    this.currentFile = file;
    this.currentPath = URIUtilities.resolve(file, ".");
  }

  @Override
  public URI getFile()
  {
    return currentFile;
  }

  /**
   * Set the SAX locator for error reporting.
   */
  void setLocator(Locator locator)
  {
    this.locator = locator;
  }
//</editor-fold>
//<editor-fold desc="temporary-context" defaultstate="collapsed">

  /**
   * Push a temporary context for a deferred action
   *
   * @param parent
   * @param child
   */
  @Override
  public void pushTemporaryContext(Object parent, Object child)
  {
    ElementContextImpl context = new ElementContextImpl(this.currentContext, parent, null, "#deferred", null);
    context.targetObject = child;
    this.currentContext = context;
  }

  /**
   * Pop a temporary context for a deferred action.
   */
  @Override
  public void popTemporaryContext()
  {
    ElementContextImpl old = this.currentContext;
    this.currentContext = old.parentContext;
  }
//</editor-fold>
//<editor-fold desc="deferred" defaultstate="collapsed">

  @Override
  @SuppressWarnings("unchecked")
  public <T, T2> void addDeferred(T target, BiConsumer<T, T2> method, String refId, Class<T2> cls) throws ReaderException
  {
    if (refId == null)
      return;
    ReferenceHandler rh = new ReferenceHandler(null, null, cls, method);
    DeferredAction dh = new DeferredAction(this, rh, target, false);
    this.addDeferred(refId, dh);
  }

  void addDeferred(String refId, DeferredAction handler) throws ReaderException
  {
    Object f = documentReferences.get(refId);
    if (f != null)
    {
      // Executed immediately.
      handler.executeDeferred(f);
    }
    else
      deferred.add(refId, handler);
  }

  public boolean hasDeferred()
  {
    return !deferred.map.isEmpty();
  }

  /**
   * Produce a list of all deferred objects that are pending.
   *
   * Used for error message when deferred elements are unresolved.
   *
   * @return a string with the name of all the deferred elements.
   */
  public String getDeferredElements()
  {
    StringBuilder sb = new StringBuilder();
    this.deferred.map.keySet().forEach(entry
            -> sb.append(entry).append(" "));
    return sb.toString();
  }

//</editor-fold>
//<editor-fold desc="external" defaultstate="collapsed">
  public URL getExternal(String file, boolean prioritizeSearchPaths) throws ReaderException
  {
    try
    {
      if (file == null || file.isEmpty())
        throw new ReaderException("Null external resource file specified.");

      if (!prioritizeSearchPaths)
      {
        URI f = URIUtilities.resolve(currentPath, file);

        // Verify that the result exists
        if (URIUtilities.exists(f))
          // Try to convert the uri to a url.
          return f.toURL();
      }

      // Search the path for file
      Path[] paths = (Path[]) this.getDocumentReader().getProperty(DocumentReader.SEARCH_PATHS);
      if (paths != null)
      {
        for (Path path : paths)
        {
          Path out = path.resolve(file).toAbsolutePath().normalize();
          if (Files.exists(out))
            return out.toUri().toURL();
        }
      }

      if (prioritizeSearchPaths)
      {
        URI f = URIUtilities.resolve(currentPath, file);

        // Verify that the result exists
        if (URIUtilities.exists(f))
          // Try to convert the uri to a url.
          return f.toURL();
      }

      throw new ReaderException("Unable to locate external reference " + file);
    }
    catch (MalformedURLException ex)
    {
      throw new ReaderException("Error locating external reference " + file, ex);
    }
  }
//</editor-fold>
//<editor-fold desc="object-referencing" defaultstate="collapsed">

  @Override
  public <Obj> Obj put(String name, Obj object) throws ReaderException
  {
    if (object == null)
    {
      documentReferences.remove(name);
      return null;
    }
    documentReferences.put(name, object);

    // Execute any deferred actions that were pending
    List<DeferredAction> actions = deferred.get(name);
    if (actions != null)
    {
      for (DeferredAction a : actions)
      {
        a.executeDeferred(object);
      }
      deferred.clear(name);
    }
    return object;
  }

  @Override
  @SuppressWarnings("unchecked")
  public <T> T get(String name, Class<T> kls)
  {
    Object o = documentReferences.get(name);
    if (o == null && propertyHandler != null)
      o = propertyHandler.get(name);
    if (o == null)
      return null;

    if (kls.isInstance(o))
      return (T) o;
    else
      throw new ClassCastException("Reference type mismatch " + o.getClass() + " is not a " + kls);
  }

  @Override
  public List<Map.Entry<String, Object>> getReferences()
  {
    return new ArrayList<>(documentReferences.entrySet());
  }

  @Override
  public void setPropertyHandler(PropertyMap handler)
  {
    this.propertyHandler = handler;
  }

//</editor-fold>
//<editor-fold desc="currentHandler-contexts">
  @Override
  public ElementContextImpl getContext(Class cls)
  {
    ElementContextImpl ctxt = currentContext;
    while (ctxt != null)
    {
      if (ctxt.currentHandler != null && ctxt.currentHandler.getReader() != null
              && ctxt.currentHandler.getReader().getClass() == cls)
        return ctxt;
      ctxt = ctxt.parentContext;
    }
    return null;
  }

  @Override
  public ElementContextImpl getParentContext()
  {
    return currentContext.parentContext;
  }

  @Override
  public ElementContextImpl getCurrentContext()
  {
    contextAccessed = true;
    return currentContext;
  }

  @Override
  public ElementContextImpl getChildContext()
  {
    return currentContext.childContext;
  }

  /**
   * Start of element delegated from SaxHandler.
   *
   * @param namespaceURI
   * @param localName
   * @param qualifiedName
   * @param attr
   * @return
   * @throws ReaderException
   */
  @SuppressWarnings("unchecked")
  ElementContextImpl startElement(String namespaceURI, String localName,
          String qualifiedName, Attributes attr)
          throws ReaderException
  {
    Reader.ElementHandlerMap handlers = null;
    try
    {
      // Get the current handler context
      ElementContextImpl previous = currentContext;

      // If we do not have a context then how did we get here.
      if (previous == null)
        throw new ReaderException("internal error");

      // If this element does not support children.
      if (previous.handlerMap == null)
        throw new ReaderException(previous.localName + " does not support children");
      handlers = previous.handlerMap;

      // Get the currentHandler for this element
      if (namespaceURI.isEmpty())
        namespaceURI = previous.namespaceURI;

      // Find the handler for this element.
      ElementHandler handler = handlers.get(namespaceURI, localName);

      // If no handler was found it may be an any.
      if (handler == null)
      {
        // we need two pieces of information here.
        //  The element handler which matches the any pattern,
        //  and a handler for this element.
        // search handlers will have to be responsible for
        ElementHandler[] found = handlers.searchHandlers(previous.previousHandler, namespaceURI, localName, qualifiedName, attr);
        if (found != null)
        {
          previous.previousHandler = found[0];
          handler = found[1];
        }
      }
      else
        previous.previousHandler = handler;

      // Warn if we do not find a valid handler for this type.
      if (handler == null)
      {
        throw new ReaderException(localName + " is not valid child for " + previous.localName);
      }

      // Determine the target object for the method
      Object parentObject = previous.getObject();

      // Handler documentReferences
      ElementContextImpl context = this.handleReferences(handler, previous, parentObject, namespaceURI, localName, attr);
      currentContext = context;

      // If it is not a reference then create a new object
      if (context == null)
      {
        // Otherwise, proceed to set up the currentHandler context
        context = new ElementContextImpl(previous, parentObject, namespaceURI, localName, attr);
        context.targetObject = null;
        context.referenceName = null;
        context.currentHandler = handler;

        // Update the context for the current location.
        currentContext = context;

        // Call the currentHandler on start
        Object target = handler.onStart(this, attr);
        if (target != null)
          context.targetObject = target;
      }

      // Set up referencing
      handleId(handler, attr, context);

      // non references can have contents
      if (!context.isReference)
      {
        // Set up to capture text
        if (handler.hasOption(Option.CAPTURE_TEXT))
          context.textContent = new StringBuilder();

        // After we have called start create the currentHandler map.
        // We do this now to ensure the currentHandler has access
        // to getReference() if needed.
        context.handlerMap = this.cache.get(handler);
        if (context.handlerMap == null)
        {
          // Watch for reinterent handler maps.
          this.contextAccessed = false;
          context.handlerMap = handler.getHandlers(this);

          // If the context was accessed then we can't cache it.
          if (!contextAccessed)
            cache.put(handler, context.handlerMap);
        }
      }
      return context;
    }
    catch (ReaderException ex)
    {
      throw ex;
    }
    catch (RuntimeException ex)
    {
      if (handlers == null)
        throw new ReaderException(ex);
      throw new ReaderException(ex);
    }
  }

  /**
   * End of element delegated from SaxHandler.
   *
   * @return
   * @throws ReaderException
   */
  ElementContextImpl endElement() throws ReaderException
  {
    ElementContextImpl context = this.currentContext;
    ElementContextImpl parent = context.parentContext;
    ElementHandler handler = context.currentHandler;

    // If is was a reference then we don't have contexts we need to process.
    if (!context.isReference)
    {
      // Handle contents
      if (context.textContent != null)
      {
        String textContents = context.textContent.toString();
        if (context.textContent == null)
          throw new ReaderException("Text contents not found");
        Object child = handler.onTextContent(this, textContents);

        // Update the target if not null.
        if (child != null)
          context.targetObject = child;
      }

      // Finish the object and call post actions
      Object child = handler.onEnd(this);
      if (child != null)
        context.targetObject = child;
    }

    // Pop the stack so that we can handle the parent element
    this.currentContext = parent;

    // Execute post completion actions.
    //   For the duration of this call the child context is available.
    //   This handles the rare case when we want to use the attributes
    //   for the newly created child.  But this is really more about
    //   being lazy when defining a simple class with string contents
    //   and attributes.
    parent.childContext = context;
    handler.onCall(this, context.parentObject, context.targetObject);
    parent.childContext = null;  // prevent memory leak

    // Reference on element close
    //   This is a safety feature as we are not guaranteed to have complete
    //   the element until onEnd was complete.
    if (context.referenceName != null)
      this.put(context.referenceName, context.targetObject);

    return parent;
  }

  /**
   * Check for an "id" if the item is referenceable.
   *
   * @param handler
   * @param attr
   * @param context
   */
  private void handleId(ElementHandler handler, Attributes attr, ElementContextImpl context)
  {
    if (!handler.hasOption(Option.NO_ID))
    {
      String id = attr.getValue("id");
      if (id != null)
        context.referenceName = id;
    }
  }

  @SuppressWarnings("unchecked")
  private ElementContextImpl handleReferences(ElementHandler handler,
          ElementContextImpl previous, Object parentObject,
          String namespace, String localName, Attributes attr)
          throws ReaderException
  {
    boolean copy = false;

    // Handlers that don't support id can't have documentReferences
    if (handler.hasOption(Option.NO_ID))
      return null;

    // Check for referenceName attribute
    String refId = attr.getValue("ref_id");
    String copyId = attr.getValue("copy_of");

    if (refId != null && copyId != null)
      throw new ReaderException("copy_of and ref_id are mutually exclusive.");

    // Check for requirements
    if (handler.hasOption(Option.MUST_REFERENCE) && refId == null)
      throw new ReaderException("element must be a reference");

    if (copyId != null)
    {
      refId = copyId;
      copy = true;
    }

    // If there is no referenceName attribute, then no need to continue
    if (refId == null)
      return null;

    if (handler.hasOption(Option.NO_REFERENCE))
      throw new ReaderException("element must not be a reference");

    Object targetObject = null;
    // handle the referenceName
    // deferred currentHandler will grab parentGroup from the context in onStart

    // If the referenceName must already be resolved, execute immediately
    if (!handler.hasOption(Option.DEFERRABLE))
    {
      targetObject = this.get(refId, handler.getObjectClass());
      if (targetObject == null)
        throw new ReaderException("Unable to find reference for " + refId);
      if (copy == true)
      {
        if (targetObject instanceof Copyable)
          targetObject = ((Copyable) targetObject).copyOf();
        else if (targetObject instanceof Serializable)
          try
        {
          targetObject = Serializer.copy((Serializable) targetObject);
        }
        catch (NotSerializableException ex)
        {
          throw new ReaderException("Unable to copy a portion of " + refId, ex);
        }
        else
          throw new ReaderException("Unable to copy " + refId);
      }
      // Handler must stay the same so that we execute the normal method at onEnd
    }
    else
    {
      if (copy == true)
        throw new ReaderException("Copies cannot be deferred for " + refId);

      if (parentObject == null)
        throw new ReaderException("Parent was not set when deferring action. "
                + localName + " " + refId);
      DeferredAction deferredHandler
              = new DeferredAction(this, handler, parentObject, copy);
      this.addDeferred(refId, deferredHandler);
      // Replace the handler with a dummy so that we skip normal method.
      handler = DEFERRED;
    }

    // Set up a new context
    ElementContextImpl context = new ElementContextImpl(previous, parentObject, namespace, localName, attr);
    context.currentHandler = handler;
    context.handlerMap = null;
    context.targetObject = targetObject;
    context.isReference = !copy;
    return context;
  }
//</editor-fold>

  @Override
  public void setErrorHandler(ExceptionHandler handler)
  {
    this.exceptionHandler = handler;
  }

  @Override
  public void handleException(Throwable ex) throws ReaderException
  {
    if (this.exceptionHandler == null)
      throw new ReaderException("Unhandled exception", ex);
    exceptionHandler.handle(this, ex);
  }

  @Override
  public DocumentReader getDocumentReader()
  {
    return documentReader;
  }

}
