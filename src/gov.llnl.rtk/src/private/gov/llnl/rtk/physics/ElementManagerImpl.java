/*
 * Copyright 2016, Lawrence Livermore National Security, LLC.
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.rtk.physics;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 *
 * @author nelson85
 */
public class ElementManagerImpl implements Elements.ElementManager
{

  static Element[] ELEMENT_TABLE_BY_MASS;
  static Element[] ELEMENT_TABLE_BY_SYMBOL;

  static
  {
    load();
    ELEMENT_TABLE_BY_SYMBOL = ELEMENT_TABLE_BY_MASS.clone();
    Arrays.sort(ELEMENT_TABLE_BY_SYMBOL,
            (Element e1, Element e2) -> e1.getSymbol().compareTo(e2.getSymbol()));
  }

  @Override
  public Element getElement(int z)
  {
    return ELEMENT_TABLE_BY_MASS[z];
  }

  @Override
  public Element getElement(String name)
  {
    Pattern pattern = Pattern.compile("([A-z][A-z]*?)([0-9]*)(m[0-9]?)?");
    Matcher match = pattern.matcher(name);
    if (!match.matches())
      throw new RuntimeException("Unable to parse element " + name);
    int index = Arrays.binarySearch(ELEMENT_TABLE_BY_SYMBOL, match.group(1),
            (Object o1, Object o2) -> ((Element) o1).getSymbol().compareTo((String) o2));
    if (index < 0)
      return null;
    return ELEMENT_TABLE_BY_SYMBOL[index];
  }

  static public void main(String[] args)
  {
    System.out.println(Elements.get("Cs137").getSymbol());
    System.out.println(Elements.get("C").getSymbol());
    System.out.println(Elements.get("Ir").getSymbol());
    System.out.println(Elements.get("Tc99m").getSymbol());
    System.out.println(Elements.get("Eu152m2").getSymbol());
    System.out.println(Elements.getElement(1));
    System.out.println(Elements.get("He").getMolarMass());
  }

//<editor-fold desc="loader" defaultstate="collapsed">
  static private class ElementDatabaseHandler extends DefaultHandler
  {
    List<ElementImpl> results = new ArrayList<>();

    @Override
    public void startElement(String uri, String localName, String qName,
            Attributes attributes) throws SAXException
    {
      if ("element".equals(qName))
      {
        String symbol = attributes.getValue("symbol");
        String atomicNumber = attributes.getValue("atomicNumber");
        String molarMass = attributes.getValue("molarMass");

        if (symbol == null)
          return;

        ElementImpl element = new ElementImpl(
                symbol.trim(),
                Integer.parseInt(atomicNumber));

        if (molarMass != null)
        {
          double v = Double.parseDouble(molarMass.trim());
          element.setMolarMass(v);
        }

        results.add(element);
      }
    }

    @Override
    public void endElement(String uri, String localName,
            String qName) throws SAXException
    {
      // not used
    }

    @Override
    public void characters(char ch[], int start, int length) throws SAXException
    {
      // not used
    }

  }

  private static void load()
  {
    try (InputStream is = NuclideManagerImpl.class.getResourceAsStream("/gov/llnl/rtk/resources/elements.xml.gz"))
    {
      if (is == null)
      {
        throw new RuntimeException("Element library resource not found");
      }
      GZIPInputStream gis = new GZIPInputStream(is);
      SAXParserFactory factory = SAXParserFactory.newInstance();
      SAXParser saxParser = factory.newSAXParser();
      ElementDatabaseHandler handler = new ElementDatabaseHandler();
      saxParser.parse(gis, handler);
      ElementManagerImpl.ELEMENT_TABLE_BY_MASS = handler.results.toArray(new ElementImpl[0]);
    }
    catch (SAXException | IOException | ParserConfigurationException ex)
    {
      Logger.getLogger(NuclideManagerImpl.class.getName()).log(Level.SEVERE, null, ex);
    }
  }
//</editor-fold>
}
