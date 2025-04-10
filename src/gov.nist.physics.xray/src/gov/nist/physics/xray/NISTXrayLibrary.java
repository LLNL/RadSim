/*
 * Copyright 2024, Lawrence Livermore National Security, LLC.
 * All rights reserved.
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.nist.physics.xray;

import gov.llnl.rtk.physics.Element;
import gov.llnl.rtk.physics.Elements;
import gov.llnl.rtk.physics.XrayData;
import gov.llnl.rtk.physics.XrayLibrary;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

/**
 *
 * @author nelson85
 */
public class NISTXrayLibrary implements XrayLibrary
{

  public HashMap<String, XrayDataImpl> bySymbol = new HashMap<>();
  public HashMap<Integer, XrayDataImpl> byNumber = new HashMap<>();

  static NISTXrayLibrary INSTANCE;

  public static NISTXrayLibrary getInstance()
  {
    if (INSTANCE == null)
    {
      INSTANCE = new NISTXrayLibrary();
      try (InputStream is = XrayParser.class.getClassLoader().getResourceAsStream("gov/nist/physics/xray/resources/ElamDB12.txt"))
      {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        XrayParser parser = new XrayParser();
        parser.parse(INSTANCE, reader);
      }
      catch (IOException ex)
      {
        throw new RuntimeException(ex);
      }
    }
    return INSTANCE;
  }

  void add(XrayDataImpl element)
  {
    bySymbol.put(element.name, element);
    byNumber.put(element.atomic_number, element);
  }

  @Override
  public XrayData get(Element element)
  {
    // not my problem if they request bad data.
    if (element == null)
    {
      return null;
    }

    // Get the data which is indexed by atomic number.
    return this.byNumber.get(element.getAtomicNumber());
  }

}
