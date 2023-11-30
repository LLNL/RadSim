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
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import gov.llnl.utility.xml.bind.Reader.ElementHandler;
import java.util.EnumSet;
import org.xml.sax.Attributes;

/**
 *
 * @author nelson85
 */
@Internal
class ElementHandlerMapImpl implements Reader.ElementHandlerMap
{
  private static final Comparator<ElementHandler> BY_KEYS
          = (ElementHandler o1, ElementHandler o2)
          -> o1.getKey().compareTo(o2.getKey());

  private static final Comparator KEY_FINDER
          = (Comparator<Object>) (Object o1, Object o2)
          -> ((ElementHandler) o1).getKey().compareTo((String) o2);

  final String namespaceURI;
  final ElementHandler first;
  final ElementHandler[] handlers;
  final boolean hasAny;

  private ElementHandlerMapImpl(String namespaceURI, ElementHandler first,
          ElementHandler[] handlers, boolean hasAny)
  {
    this.namespaceURI = namespaceURI;
    this.first = first;
    this.handlers = handlers;
    this.hasAny = hasAny;

    // Sanity check for missing namespace.
    if (!namespaceURI.startsWith("#"))
      throw new RuntimeException("namespace issue");
  }

  /**
   * Create a ElementHandlerMap given a namespace and handler list.
   *
   * @param namespaceURI
   * @param handlerList
   * @return
   */
  static ElementHandlerMapImpl newInstance(String namespaceURI,
          ReaderBuilderImpl.HandlerList handlerList)
  {
    ElementHandler rootHandler = handlerList.firstHandler;
    boolean hasAny = false;

    // Count the elements 
    int count = 0;
    ElementHandler iter = rootHandler;
    while (iter != null)
    {
      if (iter.hasOption(Reader.Option.ANY))
        hasAny = true;
      count++;
      iter = iter.getNextHandler();
    }

    // Pack into array
    ElementHandler[] handlers = new ElementHandler[count];
    int i = 0;
    iter = rootHandler;
    while (iter != null)
    {
      // Sanity check
      if (iter.getKey() == null)
        throw new NullPointerException("Null pointer getting key " + iter);
      int u1 = iter.getKey().indexOf('#', 1);
      int u2 = iter.getKey().indexOf('#', u1 + 1);
      if (u2 != -1)
        throw new RuntimeException("bad key " + iter.getKey());

      // Pack into array      
      handlers[i++] = iter;
      iter = iter.getNextHandler();
    }

    // sort array
    Arrays.sort(handlers, BY_KEYS);
    return new ElementHandlerMapImpl(namespaceURI, rootHandler, handlers, hasAny);
  }

  @Override
  public List<ElementHandler> toList()
  {
    return Arrays.asList(handlers);
  }

  @Override
  public ElementHandler[] searchHandlers(ElementHandler last,
          String namespaceURI,
          String localName,
          String qualifiedName,
          Attributes attr)
  {
    // Short cut if no any handlers in this map.
    if (!hasAny)
      return null;

    // Starting with the last one to handle the element
    ElementHandler iter = last;
    while (iter != null)
    {
      Reader search = iter.getReader();
      ElementHandler current = iter;
      iter = iter.getNextHandler();
      if (!current.hasOption(Reader.Option.ANY) || search == null)
        continue;
      Reader found = search.findReader(namespaceURI, localName, qualifiedName, attr);
      if (found == null)
        continue;
      return delegate(current, found);
    }

    // End of the list, start back at the top searching to the last
    //   FIXME there is a minor bug in this system when we have multiple 
    //   any contents type elements in a sequence.  We should not go back to 
    //   the top of the list if it is Order.Sequence, but we should if it is a 
    //   Order.Choice, Order.Options, or Order.All.  Unfortunately, we would
    //   have to get information on the type of group that we are in currently
    //   to address this.
    iter = this.first;
    while (iter != null && iter != last)
    {
      Reader search = iter.getReader();
      ElementHandler current = iter;
      iter = iter.getNextHandler();
      if (!current.hasOption(Reader.Option.ANY) || search == null)
        continue;
      Reader found = search.findReader(namespaceURI, localName, qualifiedName, attr);
      if (found == null)
        continue;
      return delegate(current, found);
    }

    return null;
  }

  @Override
  public ElementHandler get(String namespaceURI, String localName)
  {
    String handlerName;
    if (namespaceURI == null)
      handlerName = localName + this.namespaceURI;
    else
      handlerName = localName + "#" + namespaceURI;
    @SuppressWarnings("unchecked")
    int index = Arrays.binarySearch(handlers, handlerName, KEY_FINDER);
    if (index < 0)
      return null;
    return handlers[index];
  }

  public boolean isEmpty()
  {
    return handlers.length == 0;
  }

  @Override
  public void createSchemaType(SchemaBuilder builder, DomBuilder type)
          throws ReaderException
  {
    if (first == null)
      return;
    ElementHandlerMapBuildSchema.process(builder, type, first);
  }

  /**
   * Delegate for an any contents type element.
   *
   * @param origin
   * @param reader
   * @return
   */
  private ElementHandler[] delegate(final ElementHandler origin, Reader reader)
  {
    // We are going to borrow the options from the delegant, but as the 
    // new handler may update the options based on the reader, we will 
    // need to clone.
    //
    // This code makes the assumption that getOptions() never returns
    // null, but as the only way to get here is if Option.ANY is set
    // this seems reasonable.
    EnumSet<Reader.Option> options = origin.getOptions().clone();
    ReaderHandler next = new ReaderHandler("any", options, null, reader)
    {
      /**
       * Redirect the call to the original handlers method.
       *
       */
      @Override
      public void onCall(ReaderContext context, Object parent, Object child) throws ReaderException
      {
        origin.onCall(context, parent, child);
      }
    };

    // Return the origin and a proxy to the reader
    return new ElementHandler[]
    {
      origin, next
    };
  }

}
