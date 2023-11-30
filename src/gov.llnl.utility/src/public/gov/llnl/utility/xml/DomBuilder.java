/* 
 * Copyright 2016, Lawrence Livermore National Security, LLC.
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.utility.xml;

import gov.llnl.utility.PackageResource;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

/**
 * Simple builder to simplify creating DOM documents.
 */
public class DomBuilder
{
  Element element;

  /**
   * Create a DomBuilder pointed to an element.
   *
   * @param element
   */
  public DomBuilder(Element element)
  {
    this.element = element;
  }

  /**
   * Add the attribute to the current element.
   *
   * @param name
   * @param value
   * @return the DomBuilder for the current element.
   */
  public DomBuilder attr(String name, String value)
  {
    element.setAttribute(name, value);
    return this;
  }

  /**
   * Add an attribute with a namespace to the current element. The qualified
   * name must include the prefix in the form "ns:attr". If the namespace is
   * already used the namespace will be added to the element.
   *
   * @param namespaceURI is the namespace
   * @param qualifiedName is the qualified name
   * @param value is the attribute value
   */
  public DomBuilder attrNS(String namespaceURI, String qualifiedName, String value)
  {
    element.setAttributeNS(namespaceURI, qualifiedName, value);
     return this;
 }

  /**
   * Create a new element and return a DomBuilder for the new element. Elements
   * are appended to the list.
   *
   * @param pkg
   * @param name is the name of the element to create, must not be null.
   * @return a DomBuilder pointed to the new element.
   */
  public DomBuilder elementNS(PackageResource pkg, String name)
  {
    Element element2;
    if (pkg == null)
      element2 = element.getOwnerDocument().createElement(name);
    else
      element2 = element.getOwnerDocument()
              .createElementNS(pkg.getNamespaceURI(), pkg.getQualifiedName(name));
    element.appendChild(element2);
    return new DomBuilder(element2);
  }

  /**
   * Create a new element and return a DomBuilder for the new element. Elements
   * are appended to the list.
   *
   * @param name is the name of the element to create, must not be null.
   * @return a DomBuilder pointed to the new element.
   */
  public DomBuilder element(String name)
  {
    return element(name, false);
  }

  /**
   * Create a new element and return a DomBuilder for the new element. Elements
   * are either inserted at the head or the tail of the child list.
   *
   * @param name is the name of the element to create, must not be null.
   * @param first specifies to insert at the head if true.
   * @return a DomBuilder pointed to the new element.
   */
  public DomBuilder element(String name, boolean first)
  {
    Element element2 = element.getOwnerDocument().createElement(name);
    if (first)
      element.insertBefore(element2, element.getFirstChild());
    else
      element.appendChild(element2);
    return new DomBuilder(element2);
  }

  /**
   * Adds a comment to the tail of the child list
   *
   * @param string is the comment to add
   * @return the DomBuilder for the current element.
   */
  public DomBuilder comment(String string)
  {
    element.appendChild(element.getOwnerDocument().createComment(string));
    return this;
  }

  public DomBuilder text(String value)
  {

    Text text = element.getOwnerDocument().createTextNode(value);
    element.appendChild(text);
    return this;
  }

  public Element toElement()
  {
    return element;
  }
}
