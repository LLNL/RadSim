/*
 * Copyright 2016, Lawrence Livermore National Security, LLC.
 * All rights reserved
 *
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.rtk.physics;

import gov.llnl.rtk.physics.Nuclides.NuclideManager;
import gov.llnl.utility.annotation.Internal;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
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
 * This class is a singleton intended to insure that Nuclides with the same name
 * refer to the same object.
 *
 * @author nelson85
 */
@Internal
public class NuclideManagerImpl implements NuclideManager
{
  HashMap<String, Nuclide> byName = new HashMap<>();
  HashMap<Integer, Nuclide> byId = new HashMap<>();

  public NuclideManagerImpl()
  {
    load();
  }

  @Override
  public ArrayList<String> getNuclideNames()
  {
    return (ArrayList<String>) byName.keySet();
  }

  static final Pattern PATTERN1 = Pattern.compile("([0-9]+)-?([A-z])([A-z]?)([Mm]?[2-9]?)");
  static final Pattern PATTERN2 = Pattern.compile("([A-z])([A-z]?)-?([0-9]+)([Mm]?[2-9]?)");

  @Override
  public Nuclide getByName(String name)
  {
    if (name == null)
      return null;

    Nuclide nuclide = byName.get(name);
    if (nuclide != null)
      return nuclide;

    String desc;
    Matcher matcher = PATTERN1.matcher(name);
    if (matcher.matches())
    {
      desc = String.format("%s%s%s%s",
              matcher.group(2).toUpperCase(),
              matcher.group(3).toLowerCase(),
              matcher.group(1),
              matcher.group(4).toLowerCase());
      nuclide = byName.get(desc);
      if (nuclide != null)
        return nuclide;
      if (matcher.group(3).toLowerCase().equals("m"))
      {
        desc = String.format("%s%s%s%s",
                matcher.group(2).toUpperCase(),
                matcher.group(1),
                matcher.group(3).toLowerCase(),
                matcher.group(4).toLowerCase());
        nuclide = byName.get(desc);
        if (nuclide != null)
          return nuclide;
      }
    }

    matcher = PATTERN2.matcher(name);
    if (matcher.matches())
    {
      desc = String.format("%s%s%s%s",
              matcher.group(1).toUpperCase(),
              matcher.group(2).toLowerCase(),
              matcher.group(3),
              matcher.group(4).toLowerCase());
      nuclide = byName.get(desc);
      if (nuclide != null)
        return nuclide;
    }

    return nuclide;
  }

  @Override
  public Nuclide getById(int id)
  {
    return this.byId.get(id);
  }

  @Override
  public Nuclide natural(Element element)
  {
    if (element == null)
      return null;
    String name = element.getSymbol();
    NuclideImpl impl = new NuclideImpl(name);
    impl.setAtomicNumber(element.getAtomicNumber());
    byName.put(name, impl);
    return impl;
  }

  static private class NuclideDatabaseHandler extends DefaultHandler
  {
    NuclideManagerImpl manager;

    @Override
    public void startElement(String uri, String localName, String qName,
            Attributes attributes) throws SAXException
    {
      if ("nuclide".equals(qName))
      {
        String symbol = attributes.getValue("symbol");
        String atomicNumber = attributes.getValue("atomicNumber");
        String massNumber = attributes.getValue("massNumber");
        String isomerNumber = attributes.getValue("isomerNumber");
        String atomicMass = attributes.getValue("atomicMass");
        String halfLife = attributes.getValue("halfLife");

        if (symbol == null)
          return;

        NuclideImpl nuclide = new NuclideImpl(symbol.trim());
        if (atomicNumber != null)
        {
          int v = Integer.parseInt(atomicNumber.trim());
          nuclide.setAtomicNumber(v);
        }

        if (massNumber != null)
        {
          int v = Integer.parseInt(massNumber.trim());
          nuclide.setMassNumber(v);
          nuclide.setAtomicMass(v);
        }

        if (isomerNumber != null)
        {
          int v = Integer.parseInt(isomerNumber.trim());
          nuclide.setIsomerNumber(v);
        }
        if (atomicMass != null)
        {
          double v = Double.parseDouble(atomicMass.trim());
          nuclide.setAtomicMass(v);
        }

        if (halfLife != null)
        {
          double v;
          if ("INF".equals(halfLife))
            v = Double.POSITIVE_INFINITY;
          else
            v = Double.parseDouble(halfLife.trim());
          nuclide.setHalfLife(v);
        }

        nuclide.setId(nuclide.getAtomicNumber() * 10000 + nuclide.getMassNumber() * 10 + nuclide.getIsomerNumber());

        manager.byName.put(nuclide.getName(), nuclide);
        manager.byId.put(nuclide.id, nuclide);

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

  private void load()
  {
    try ( InputStream is = NuclideManagerImpl.class.getResourceAsStream("/gov/llnl/rtk/resources/nuclides.xml.gz"))
    {
      if (is == null)
      {
        throw new RuntimeException("Nuclide library resource not found");
      }
      GZIPInputStream gis = new GZIPInputStream(is);
      SAXParserFactory factory = SAXParserFactory.newInstance();
      SAXParser saxParser = factory.newSAXParser();
      NuclideDatabaseHandler handler = new NuclideDatabaseHandler();
      handler.manager = this;
      saxParser.parse(gis, handler);
    }
    catch (SAXException | IOException | ParserConfigurationException ex)
    {
      Logger.getLogger(NuclideManagerImpl.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  public static void main(String[] args)
  {
    Nuclide nuclide = Nuclides.get("Tc99m");
    System.out.println(nuclide);
    System.out.println("symbol: " + nuclide.getName());
    System.out.println("halflife: " + nuclide.getHalfLife());
  }
}
