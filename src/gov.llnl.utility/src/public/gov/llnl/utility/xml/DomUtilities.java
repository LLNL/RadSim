/*
 * Copyright 2016, Lawrence Livermore National Security, LLC.
 * All rights reserved
 *
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.utility.xml;

import gov.llnl.utility.io.WriterException;
import gov.llnl.utility.xml.bind.Marshaller;
import gov.llnl.utility.xml.bind.ObjectWriter;
import gov.llnl.utility.xml.bind.WriterContext;
import gov.llnl.utility.xml.bind.WriterContextImpl;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.TreeMap;
import javax.xml.XMLConstants;
import javax.xml.namespace.NamespaceContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.bootstrap.DOMImplementationRegistry;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSOutput;
import org.w3c.dom.ls.LSSerializer;
import org.xml.sax.SAXException;

/**
 * Utilities for processing DOM files.
 */
@SuppressWarnings("unchecked")
public class DomUtilities
{

  private static class DocumentNamespaceResolver implements NamespaceContext
  {

    final Document doc;

    DocumentNamespaceResolver(Document doc)
    {
      this.doc = doc;
    }

    @Override
    public String getNamespaceURI(String prefix)
    {
      String out;
      if (prefix.equals(XMLConstants.DEFAULT_NS_PREFIX))
        out = doc.lookupNamespaceURI(null);
      else
        out = doc.lookupNamespaceURI(prefix);
      return out;
    }

    @Override
    public String getPrefix(String prefix)
    {
      return doc.lookupPrefix(prefix);
    }

    @Override
    public Iterator getPrefixes(String string)
    {
      return null;
    }
  }

  /**
   * Searches the children of an DOM element to find the first element that has
   * a given XPath. This function has issues on Matlab when using Java 8. Matlab
   * replaces the XPath utilities with Xerces which is not compatible with Java
   * 8.
   *
   * @param origin
   * @param path
   * @return the element with the requested xpath or null if not found or the
   * xpath is invalid.
   */
  public static Element findFirst(Element origin, String path)
  {
    XPath xpath = XPathFactory.newInstance().newXPath();
    try
    {
      xpath.setNamespaceContext(new DocumentNamespaceResolver(origin.getOwnerDocument()));
      return (Element) xpath.evaluate(path, origin, XPathConstants.NODE);
    }
    catch (XPathExpressionException ex)
    {
      return null;
    }
  }

  /**
   * Find all elements with a given DOM element that match a specified XPath.
   *
   * @param origin
   * @param path
   * @return an iterable with all of the requested elements.
   */
  public static NodeListElementIterable findAll(Element origin, String path)
  {
    XPath xpath = XPathFactory.newInstance().newXPath();
    try
    {
      xpath.setNamespaceContext(new DocumentNamespaceResolver(origin.getOwnerDocument()));
      NodeList nl = (NodeList) xpath.evaluate(path, origin, XPathConstants.NODESET);
      return new NodeListElementIterable(nl);
    }
    catch (XPathExpressionException ex)
    {
      return null;
    }
  }

  public static void removeWhitespace(Document document)
  {
    try
    {
      XPathFactory xpathFactory = XPathFactory.newInstance();
      XPathExpression xpathExp = xpathFactory.newXPath().compile("//text()[normalize-space(.) = '']");
      for (Node emptyTextNode : new NodeListIterable((NodeList) xpathExp.evaluate(document, XPathConstants.NODESET)))
      {
        emptyTextNode.getParentNode().removeChild(emptyTextNode);
      }
    }
    catch (XPathExpressionException ex)
    {
      throw new RuntimeException(ex);
    }
  }

  /**
   * Print a document to a Writer with indentation. This function requires that
   * the unneeded white space had been trimmed, however this is difficult
   * without a Schema. Also it does not handle spacing in comments. Last there
   * is often a difficulty when dealing with windows and unix line endings. It
   * is not clear how to resolve these issues.
   *
   * @param out
   * @param document
   */
  public static void printXml(OutputStream out, Node document)
  {
    printXml(out, document, null);
  }
  
  public interface PropertyInterface
  {
    <T> T get(String key, Class<T> type, T defaultValue);
  }
  
  public static void printXml(OutputStream out, Node document, PropertyInterface pi)
  {
    if (pi == null)
      pi = new PropertyInterface(){
      @Override
      public <T> T get(String key, Class<T> type, T defaultValue)
      {
        return defaultValue;
      }
    };
    
    try
    {
      Transformer transformer = TransformerFactory.newInstance().newTransformer();
      StreamResult result = new StreamResult(out);
      transformer.setOutputProperty(OutputKeys.ENCODING, pi.get("encoding", String.class, "UTF-8"));
      transformer.setOutputProperty(OutputKeys.INDENT, pi.get("indent", String.class, "yes"));
      transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", pi.get("indent-amount", String.class, "2"));
//      transformer.setOutputProperty("{http://xml.apache.org/xslt}line-separator", "\n");
      DOMSource domSource = new DOMSource(document);
      transformer.transform(domSource, result);
      out.flush();
    }
    catch (TransformerException | IOException ex)
    {
      throw new RuntimeException(ex);
    }
  }

  static public String printXmlToString(Document doc)
  {
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    printXml(bos, doc, null);
    return new String(bos.toByteArray());
  }

  public static XMLPrinter newPrinter(OutputStream out)
  {
    return new XMLPrinter(out);
  }

  public static class XMLPrinter
  {
    LSOutput output;
    LSSerializer writer;
    private OutputStream out;

    XMLPrinter(OutputStream out)
    {
      try
      {
        DOMImplementationRegistry registry = DOMImplementationRegistry.newInstance();
        DOMImplementationLS impl = (DOMImplementationLS) registry.getDOMImplementation("LS");
        this.out = out;
        output = impl.createLSOutput();
        writer = impl.createLSSerializer();
        output.setByteStream(out);
        output.setEncoding("UTF-8");
        writer.setNewLine("\n");
        writer.getDomConfig().setParameter("format-pretty-print", true);
      }
      catch (ClassNotFoundException | InstantiationException | IllegalAccessException | ClassCastException ex)
      {
        throw new RuntimeException(ex);
      }
    }

    public XMLPrinter set(String key, Object value)
    {
      writer.getDomConfig().setParameter(key, value);
      return this;
    }

    public void write(Node node) throws IOException
    {
      writer.write(node, output);
      out.flush();
    }
  }

  /**
   * Adaptor class to iterate though a NodeList. Does not support remove.
   */
  public static class NodeListIterator implements Iterator<Node>
  {

    NodeList nl;
    int i = 0;

    private NodeListIterator(NodeList nl)
    {
      this.nl = nl;
    }

    @Override
    public boolean hasNext()
    {
      return i < nl.getLength();
    }

    @Override
    public Node next()
    {
      return nl.item(i++);
    }

    @Override
    public void remove()
    {
      throw new UnsupportedOperationException("Not supported yet.");
    }
  }

  /**
   * Adaptor class to treat a NodeList as an Iterable object.
   */
  public static class NodeListIterable implements Iterable<Node>
  {

    NodeList nl;

    public NodeListIterable(NodeList nl)
    {
      this.nl = nl;
    }

    @Override
    public Iterator<Node> iterator()
    {
      return new NodeListIterator(nl);
    }
  }

  /**
   * Adaptor class to iterate though a NodeList selecting only nodes of a given
   * type. Does not support remove.
   */
  public static class NodeListTypeIterator<T> implements Iterator<T>
  {

    NodeList nl;
    short type;
    int i = 0;

    private NodeListTypeIterator(NodeList nl, short type)
    {
      this.nl = nl;
      this.type = type;
      advance();
    }

    private void advance()
    {
      while (i < nl.getLength() && nl.item(i).getNodeType() != type)
      {
        i++;
      }
    }

    @Override
    public boolean hasNext()
    {
      return i < nl.getLength();
    }

    @Override
    public T next()
    {
      Node current = nl.item(i);
      i++;
      advance();
      return (T) current;
    }

    @Override
    public void remove()
    {
      throw new UnsupportedOperationException("Not supported yet.");
    }
  }

  public static class NodeListElementIterable implements Iterable<Element>
  {

    NodeList nl;

    public NodeListElementIterable(NodeList nl)
    {
      this.nl = nl;
    }

    @Override
    public Iterator<Element> iterator()
    {
      return new NodeListTypeIterator<>(nl, Node.ELEMENT_NODE);
    }
  }

  static public Element appendElement(Element parent, String name)
  {
    Document doc = parent.getOwnerDocument();
    Element elem = doc.createElement(name);
    parent.appendChild(elem);
    return elem;
  }

  static public Element appendElement(Element parent, String name, String value)
  {
    Document doc = parent.getOwnerDocument();
    Element elem = doc.createElement(name);
    elem.appendChild(doc.createTextNode(value));
    parent.appendChild(elem);
    return elem;
  }

  static public Element appendElement(Element parent, String name, int value)
  {
    return appendElement(parent, name, Integer.toString(value));
  }

  static public Element appendElement(Element parent, String name, double value)
  {
    return appendElement(parent, name, Double.toString(value));
  }

  /**
   * Convenience function for debugging XML file loading.
   *
   * @param e
   * @return a string the describes where the node is in the document.
   */
  static public String getXmlPath(Node e)
  {
    StringBuilder sb = new StringBuilder();
    Element e2 = null;
    while (e != null)
    {
      if (e.getNodeType() == Node.ELEMENT_NODE)
      {
        String id = ((Element) e).getAttribute("id");
        if (!id.isEmpty())
        {
          id = "[id=" + id + "]";
        }
        sb.insert(0, e.getNodeName() + id);
        if (e2 == null)
          e2 = (Element) e;
      }
      sb.insert(0, "/");
      e = e.getParentNode();
    }

    if (e2 != null && e2.hasAttribute("parser:line_number"))
    {
      String source = e2.getAttribute("parser:source");
      String lineNumber = e2.getAttribute("parser:line_number");
      sb.append(String.format(" in %s:%s", source, lineNumber));
    }

    return sb.toString();
  }

  static class LocationFilter extends org.xml.sax.helpers.XMLFilterImpl
  {

    org.xml.sax.Locator locator;
    String source;

    public LocationFilter(org.xml.sax.XMLReader xmlReader, String source)
    {
      super(xmlReader);
      this.source = source;
    }

    @Override
    public void setDocumentLocator(org.xml.sax.Locator locator)
    {
      super.setDocumentLocator(locator);
      this.locator = locator;
    }

    @Override
    public void startElement(String uri, String localName, String qName, org.xml.sax.Attributes attributes) throws SAXException
    {
      //org.xml.sax.helpers.AttributesImpl attrs = new org.xml.sax.helpers.AttributesImpl(attributes);
      org.xml.sax.ext.Attributes2Impl attrs = new org.xml.sax.ext.Attributes2Impl(attributes);
      attrs.addAttribute("urn:gov.llnl.utilities", "source", "parser:source", "CDATA", source);
      attrs.addAttribute("urn:gov.llnl.utilities", "line_number", "parser:line_number", "CDATA", Integer.toString(locator.getLineNumber()));
      super.startElement(uri, localName, qName, attrs);
//      System.out.println(" element " + uri + " " + localName + " " + qName);
    }

  }

  public static Document parseStreamToDOM(InputStream inputStream)
          throws IOException, ParserConfigurationException, SAXException
  {
    DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
    dbFactory.setValidating(false);
    dbFactory.setNamespaceAware(true);
    DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
    return dBuilder.parse(inputStream);
  }

  // from http://stackoverflow.com/questions/2798376/is-there-a-way-to-parse-xml-via-sax-dom-with-line-numbers-available-per-node
  public static Document parseFileToDOM(Path file)
          throws ParserConfigurationException, SAXException, FileNotFoundException, IOException, TransformerConfigurationException, TransformerException
  {
    // Create a sax reader
    SAXParserFactory saxFactory = SAXParserFactory.newInstance();
    saxFactory.setNamespaceAware(true);
    saxFactory.setValidating(false);
    SAXParser parser = saxFactory.newSAXParser();
    LocationFilter locationFilter = new LocationFilter(parser.getXMLReader(), file.getFileName().toString());

    // Create an input source for the file.
    try ( InputStream inputStream = Files.newInputStream(file))
    {
      org.xml.sax.InputSource inputSource = new org.xml.sax.InputSource(inputStream);
      inputSource.setSystemId(file.getFileName().toString());
      SAXSource saxSource = new SAXSource(locationFilter, inputSource);
      TransformerFactory transformerFactory = TransformerFactory.newInstance();
      Transformer transformer = transformerFactory.newTransformer();

      // Create a destination for the transfrom
      DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
      dbf.setNamespaceAware(true);
      DocumentBuilder db = dbf.newDocumentBuilder();
      Document document = db.newDocument();
      DOMResult domResult = new DOMResult(document);

      // Transform the document adding attributes for line numbers
      transformer.transform(saxSource, domResult);

      // KLUDGE I don't understand why getLocalName is returning null
      //  https://bugs.openjdk.java.net/browse/JDK-4629655
//    NodeList nodeList = document.getElementsByTagName("*");
//    for (int i = 0; i < nodeList.getLength(); i++) {
//        Node node = nodeList.item(i);
//        if (node.getNodeType() == Node.ELEMENT_NODE) {
//            // do something with the current element
//            if (node.getNodeName()==null)
//            {
//              // WTF
//            }
//        }
//    }
//
      return (Document) domResult.getNode();
    }
  }

  static public float toFloat(Element e)
  {
    return Float.parseFloat(e.getTextContent().trim());
  }

  static public double toDouble(Element e)
  {
    return Double.parseDouble(e.getTextContent().trim());
  }

  static public int toInteger(Element e)
  {
    return Integer.parseInt(e.getTextContent().trim());
  }

  public static int toIntegerAttribute(Element element, String attribute, int defaultValue)
  {
    String value = element.getAttribute(attribute);
    if (value.isEmpty())
      return defaultValue;
    return Integer.parseInt(value);
  }

  static public boolean toBoolean(Element e)
  {
    return Boolean.parseBoolean(e.getTextContent().trim());
  }

  public static double toDoubleAttribute(Element element, String attribute, double defaultValue)
  {
    String value = element.getAttribute(attribute);
    if (value.isEmpty())
      return defaultValue;
    return Double.parseDouble(value);
  }

  static class DocumentNamespaceContext implements NamespaceContext
  {
    TreeMap<String, String> contextMap = new TreeMap<>();

    DocumentNamespaceContext(Document doc)
    {
      Element root = doc.getDocumentElement();
      NamedNodeMap attributes = root.getAttributes();
      for (int i = 0; i < attributes.getLength(); i++)
      {
        Node attr = attributes.item(i);
        if (attr.getNodeName().startsWith("xmlns:"))
        {
          contextMap.put(attr.getLocalName(), attr.getNodeValue());
        }
      }
    }

    @Override
    public String getNamespaceURI(String string)
    {
      return contextMap.get(string);
    }

    @Override
    public String getPrefix(String string)
    {
      return null;
    }

    @Override
    public Iterator getPrefixes(String string)
    {
      return null;
    }

  }

  public static NamespaceContext createNamespaceContext(Document doc)
  {
    return new DocumentNamespaceContext(doc);
  }

  public static void trimWhitespace(Node node)
  {
    NodeList children = node.getChildNodes();
    for (int i = 0; i < children.getLength(); ++i)
    {
      Node node2 = children.item(i);
      if (node2.getNodeType() == Node.TEXT_NODE)
        node2.setTextContent(node2.getTextContent().trim());
      else
        trimWhitespace(node2);
    }
  }

  public static class AttributeListIterator implements Iterator<Node>
  {
    NamedNodeMap al;
    int i = 0;

    private AttributeListIterator(NamedNodeMap al)
    {
      this.al = al;
    }

    @Override
    public boolean hasNext()
    {
      return i < al.getLength();
    }

    @Override
    public Node next()
    {
      Node out = al.item(i);
      ++i;
      return out;
    }

    @Override
    public void remove()
    {
      throw new UnsupportedOperationException("Not supported yet.");
    }
  }

  public static class AttributeListIterable implements Iterable<Node>
  {
    NamedNodeMap al;

    public AttributeListIterable(NamedNodeMap al)
    {
      this.al = al;
    }

    @Override
    public Iterator<Node> iterator()
    {
      return new AttributeListIterator(al);
    }

  }

}
