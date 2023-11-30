/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gov.nist.xray;

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
public class NISTLibrary implements XrayLibrary
{

  public HashMap<String, XrayDataImpl> bySymbol = new HashMap<>();
  public HashMap<Integer, XrayDataImpl> byNumber = new HashMap<>();

  static NISTLibrary INSTANCE;

  public static NISTLibrary getInstance()
  {
    if (INSTANCE == null)
    {
      INSTANCE = new NISTLibrary();
      try ( InputStream is = Parser.class.getClassLoader().getResourceAsStream("gov/nist/xray/resources/ElamDB12.txt"))
      {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        Parser parser = new Parser();
        parser.parse(INSTANCE, reader);
      } catch (IOException ex)
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
    return this.byNumber.get(element.getAtomicNumber());
  }
  
  public static void main(String args[])
  {
    XrayData data = getInstance().get(Elements.get("Pb"));
    System.out.println(data.getEdges());
  }
}
