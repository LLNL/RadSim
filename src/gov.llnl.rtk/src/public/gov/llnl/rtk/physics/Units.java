/*
 * Copyright (c) 2016, Lawrence Livermore National Security, LLC.
 * All rights reserved.
 *
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.rtk.physics;

import gov.llnl.rtk.RtkPackage;
import gov.llnl.utility.io.ReaderException;
import gov.llnl.utility.io.WriterException;
import gov.llnl.utility.xml.bind.ObjectReader;
import gov.llnl.utility.xml.bind.ObjectWriter;
import gov.llnl.utility.xml.bind.Reader;
import gov.llnl.utility.xml.bind.ReaderContext;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.TreeMap;
import org.xml.sax.Attributes;

/**
 * Conversion for units.
 *
 * Whenever receiving an item from an program multiply by the incoming units to
 * convert to SI. When passing an SI unit to something requiring a different
 * unit divide by the corresponding unit.
 *
 * {@code
 * <pre>
 *
 *     double processToLengthInKm( double mass_in_grams)
 *     {
 *        double mass_si = mass_in_gram * Units.get("mass:g");
 *        ...
 *        // Return needs to be in km
 *        return Units.to(length_si, "length:km");
 *     }
 *
 * </pre> }
 *
 * author nelson85
 */
public class Units
{
  final static TreeMap<String, Double> UNIT_MAP;

  static
  {
    UNIT_MAP = new TreeMap<>();
    try ( InputStream is = RtkPackage.getInstance().getResource("units.csv"))
    {
      BufferedReader br = new BufferedReader(new InputStreamReader(is));

      while (true)
      {
        String buffer = br.readLine();
        if (buffer == null)
          break;
        if (buffer.startsWith("#"))
          continue; // skip comments
        if (buffer.isEmpty())
          continue;
        int i0 = buffer.indexOf(",");
        String unit = buffer.substring(0, i0).trim();
        double value = Double.parseDouble(buffer.substring(i0 + 1).trim());
        if (unit.contains(":"))
        {
          String[] parts = unit.split(":");
          UNIT_MAP.put(parts[1], value);
        }
        UNIT_MAP.put(unit, value);
      }
    }
    catch (IOException ex)
    {
      throw new RuntimeException(ex);
    }
  }

  static public double get(String unitName) throws UnitsException
  {
    Double unit = UNIT_MAP.get(unitName);
    if (unit == null)
      throw new UnitsException("Unknown unit " + unitName);
    return unit;
  }

  static public double to(double value, String unitName)
  {
    return value / get(unitName);
  }

  static public class UnitWriter extends ObjectWriter<Double>
  {
    String unitName;
    double factor;

    public UnitWriter(String unitName)
    {
      super(Options.NONE, "value", null);
      this.unitName = unitName;
      factor = Units.get(unitName);
    }

    @Override
    public void attributes(WriterAttributes attributes, Double object) throws WriterException
    {
      String unit = unitName;
      if (unitName.contains(":"))
      {
        String[] parts = unitName.split(":");
        unit = parts[1];
      }
      attributes.add("units", unit);
    }

    @Override
    public void contents(Double object) throws WriterException
    {
      addContents(object / factor);
    }
  }

  static public void main(String[] args)
  {
    System.out.println(Units.get("time:yr"));

  }

  @Reader.Declaration(pkg = RtkPackage.class,
          name = "value", 
          contents = Reader.Contents.TEXT,
          cls = Double.class)
  @Reader.Attribute(name = "units", type = String.class, required = true)
  public static class UnitReader extends ObjectReader<Double>
  {
    private String property;
    private double factor;

    public UnitReader()
    {
    }

    public UnitReader(PhysicalProperty unitType)
    {
      this.property = unitType.toString();
    }

    @Override
    public Double start(ReaderContext context, Attributes attributes) throws ReaderException
    {
      String unitString = attributes.getValue("units");
      if (property != null)
      {
        if (unitString.contains(":"))
        {
          if (!unitString.startsWith(property))
            throw new ReaderException("Expected unit type " + property + " got " + unitString);
        }
        else
        {
          unitString = property + ":" + unitString;
        }
      }
      this.factor = Units.get(unitString);
      return null;
    }

    @Override
    public Double contents(ReaderContext context, String textContents) throws ReaderException
    {
      return Double.parseDouble(textContents.trim()) * this.factor;
    }
  }
}
