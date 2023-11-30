/*
 * Copyright 2021, Lawrence Livermore National Security, LLC. 
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
import gov.llnl.utility.xml.DomUtilities;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

/**
 *
 * @author nelson85
 */
public class XPathTest
{
  static public void main(String[] args) throws IOException, ParserConfigurationException, SAXException
  {
    testXPath();
  }

  static public void testXPath() throws IOException, ParserConfigurationException, SAXException
  {
    System.out.println(javax.xml.xpath.XPathFactory.newInstance());
    String mydoc = "<doc> <element>      <test/> <foo/>  </element> </doc>";
    Document doc = DomUtilities.parseStreamToDOM(new ByteArrayInputStream(mydoc.getBytes()));
    Element e1 = DomUtilities.findFirst(doc.getDocumentElement(), "//element");
    Element e2 = DomUtilities.findFirst(e1, "//element/*");
    System.out.println(e2);
  }
}
