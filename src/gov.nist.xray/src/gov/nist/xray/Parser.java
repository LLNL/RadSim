/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gov.nist.xray;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.stream.Stream;
import gov.llnl.rtk.physics.Quantity;

/**
 *
 * @author nelson85
 */
class Parser
{

  void parse(NISTLibrary lib, BufferedReader br) throws IOException
  {
    // Read all lines
    LinkedList<String> lines = new LinkedList<>();
    while (true)
    {
      String line = br.readLine();
      if (line == null)
        break;
      lines.add(line);
    }

    while (!lines.isEmpty())
    {
      String line = lines.removeFirst();

      if (line.startsWith("//"))
        continue;
      if (line.startsWith("Element"))
      {
        lib.add(parseElement(line, lines));
      }
    }
  }

  private XrayDataImpl parseElement(String line, LinkedList<String> lines)
  {
    String[] parts = line.split("  *");
    XrayDataImpl el = new XrayDataImpl();
    el.name = parts[1];
    el.atomic_number = Integer.parseInt(parts[2]);
    el.atomic_weigth = Double.parseDouble(parts[3]);
    el.density = Double.parseDouble(parts[4]);

    while (!lines.isEmpty() && !lines.getFirst().startsWith("EndElement"))
    {
      line = lines.removeFirst();
      if (line.startsWith("Edge"))
      {
        el.edges.add(parseEdge(line, lines));
        continue;
      }
      if (line.startsWith("Scatter"))
      {
        el.scatter = parseMatrix(line, lines);
        continue;
      }
      if (line.startsWith("Photo"))
      {
        el.photo = parseMatrix(line, lines);
        continue;
      }
      throw new RuntimeException("Unknown line " + line);
    }
    return el;
  }

  private XrayEdgeImpl parseEdge(String header, LinkedList<String> lines)
  {
    String[] parts = header.stripLeading().split("  *");
    XrayEdgeImpl edge = new XrayEdgeImpl();
    edge.name = parts[1];
    edge.energy = Double.parseDouble(parts[2]);
    edge.fluorescence_yield = Double.parseDouble(parts[3]);
    edge.ratio_jump = Double.parseDouble(parts[4]);

    while (!lines.isEmpty() && lines.getFirst().startsWith("  "))
    {
      header = lines.removeFirst();
      if (header.startsWith("  Lines"))
      {
        edge.lines.addAll(parseLines(lines));
        continue;
      }
      if (header.startsWith("  CK "))
      {
        String[] values = header.substring(6).split(" +");
        for (int i = 0; i < values.length; i += 2)
          edge.CK.put(values[i], Double.parseDouble(values[i + 1]));
        continue;
      }
      if (header.startsWith("  CKtotal "))
      {
        continue;
      }
      throw new RuntimeException("bad line " + header);
    }
    return edge;
  }

  private double[][] parseMatrix(String header, LinkedList<String> lines)
  {
    ArrayList<double[]> out = new ArrayList<>();
    while (!lines.isEmpty() && lines.getFirst().startsWith("    "))
    {
      String line = lines.removeFirst();
      String[] parts = line.stripLeading().split("  ");
      out.add(Stream.of(parts).mapToDouble(p -> Double.parseDouble(p)).toArray());
    }
    return out.toArray(double[][]::new);
  }

  private Collection<? extends XrayImpl> parseLines(LinkedList<String> lines)
  {
    ArrayList<XrayImpl> out = new ArrayList<>();
    while (!lines.isEmpty() && lines.getFirst().startsWith("    "))
    {
      String line = lines.removeFirst();
      String[] parts = line.stripLeading().split("  ");
      XrayImpl l = new XrayImpl();
      l.symbolIUPAC = parts[0];
      l.symbolSiegbahn = parts[1];
      l.energy = Quantity.of(Double.parseDouble(parts[2]), "eV");
      l.intensity = Quantity.of(Double.parseDouble(parts[3]), null);
      out.add(l);
    }
    return out;
  }

  public static void main(String[] args) throws IOException
  {
    try ( InputStream is = Parser.class.getClassLoader().getResourceAsStream("gov/llnl/xray/resources/ElamDB12.txt"))
    {
      BufferedReader reader = new BufferedReader(new InputStreamReader(is));
      Parser parser = new Parser();
      parser.parse(new NISTLibrary(), reader);
    }
  }
}
