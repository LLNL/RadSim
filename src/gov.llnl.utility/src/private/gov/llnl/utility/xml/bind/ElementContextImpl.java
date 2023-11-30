/* 
 * Copyright 2016, Lawrence Livermore National Security, LLC.
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.utility.xml.bind;

import gov.llnl.utility.annotation.Internal;
import org.xml.sax.Attributes;
import gov.llnl.utility.xml.bind.Reader.ElementHandler;
import gov.llnl.utility.xml.bind.Reader.ElementHandlerMap;
import java.util.Objects;

/**
 *
 * @author nelson85
 */
@Internal
public class ElementContextImpl implements ReaderContext.ElementContext
{
  // Immutable
  final ElementContextImpl parentContext;
  final Object parentObject;
  final String namespaceURI;
  final String localName;
  final Attributes attributes;
  
  ElementContextImpl childContext;

  // State information
  ElementHandler currentHandler;
  ElementHandler previousHandler; // Last handler that was found, needed for sequences
  ElementHandlerMap handlerMap;

  // Objects
  Object targetObject;
  Object state;

  // Referencing
  String referenceName;
  boolean isReference = false;

  // Element contents
  StringBuilder textContent;


  public ElementContextImpl(ElementContextImpl parentContext, 
          Object parentObject, String namespace, String localName, Attributes attributes)
  {
    this.parentContext = parentContext;
    this.parentObject = parentObject;
    this.namespaceURI = namespace;
    this.localName = localName;
    this.attributes = attributes;
  }
  
  // Notes: previousHandler is important for the purposes of processing
  // #any type tags or abstract tags in xml documents.  Because order
  // maters we can only seach for #any tags that are forward of the 
  // last element we processed.  

//<editor-fold desc="XML" defaultstate="collapsed">
  @Override
  public String getNamespaceURI()
  {
    return namespaceURI;
  }

  @Override
  public String getLocalName()
  {
    return localName;
  }

  @Override
  public Attributes getAttributes()
  {
    return this.attributes;
  }

  /**
   * Receive a section of text in the document.
   *
   * @param chars
   * @param start
   * @param length
   */
  public void characters(char[] chars, int start, int length)
  {
    if (textContent == null)
      return;
    textContent.append(chars, start, length);
  }

//</editor-fold>
//<editor-fold desc="references" defaultstate="collapsed">
//  @Override
//  public Object getReference(String name)
//  {
//    ElementContextImpl hc = this;
//    while (hc != null)
//    {
//      if (hc.references != null)
//      {
//        Object obj = hc.references.get(name);
//        if (obj != null)
//          return obj;
//      }
//      hc = hc.parentContext;
//    }
//    return null;
//  }
//
//  @Override
//  public <T> T putReference(String name, T obj)
//  {
//    if (references == null)
//      references = new TreeMap<>();
//    references.put(name, obj);
//    return obj;
//  }
//</editor-fold>
//<editor-fold desc="contents" defaultstate="collapsed">

  @Override
  public Object getObject()
  {
    return this.targetObject;
  }
  
  @Override 
  public Object getState()
  {
    return this.state;
  }

  @Override
  public Object getParentObject()
  {
    return this.parentObject;
  }
//</editor-fold>

  @Override
  public boolean equals(Object o)
  {
    if (!(o instanceof ElementContextImpl))
      return false;
    ElementContextImpl o2 = (ElementContextImpl) o;
    return Objects.equals(o2.parentContext, this.parentContext)
            && Objects.equals(o2.currentHandler, this.currentHandler)
            && Objects.equals(o2.previousHandler, this.previousHandler)
            && Objects.equals(o2.handlerMap, this.handlerMap)
            && Objects.equals(o2.parentObject, this.parentObject)
            && Objects.equals(o2.targetObject, this.targetObject)
            && Objects.equals(o2.referenceName, this.referenceName)
            && o2.isReference == this.isReference
            && Objects.equals(o2.namespaceURI, this.namespaceURI)
            && Objects.equals(o2.localName, this.localName)
            && Objects.equals(o2.attributes, this.attributes)
            && Objects.equals(o2.textContent, this.textContent);
//            && Objects.equals(o2.references, this.references);
  }
}
