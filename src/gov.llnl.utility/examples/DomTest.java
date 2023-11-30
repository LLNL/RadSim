
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Attr;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/*
 * Copyright 2016, Lawrence Livermore National Security, LLC. 
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
/**
 *
 * @author nelson85
 */
public class DomTest
{
  static public void main(String[] args) throws ParserConfigurationException
  {
    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    DocumentBuilder documentBuilder = dbf.newDocumentBuilder();
    DOMImplementation dom = documentBuilder.getDOMImplementation();
    Document document = dom.createDocument("urn:test", "test", null);
    Attr attr;
    Element root = document.getDocumentElement();
    root.setAttributeNodeNS(attr = document.createAttributeNS("urn:foo", "foo:a"));
//    root.
//    root.removeAttributeNode(attr);
//    DomUtilities.printXml(System.out, document, 2);
  }
}
