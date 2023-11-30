/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gov.nist.xray;

import gov.llnl.rtk.physics.Element;
import gov.llnl.rtk.physics.Elements;
import gov.llnl.rtk.physics.XrayData;
import gov.llnl.rtk.physics.XrayEdge;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author nelson85
 */
public class XrayDataImpl implements XrayData
{

  public String name;
  public int atomic_number;
  public double atomic_weigth, density;
  
  public List<XrayEdgeImpl> edges = new ArrayList<>();
  
  // photo_absorption_log_value photo_absorption_spline
  public double[][] photo; 
   // coherent_scatter_log_value coherent_scatter_spline incoherent_scatter_log_value incoherent_scatter_spline
  public double[][] scatter;
  
  @Override
  public Element getElement()
  {
    return Elements.getElement(atomic_number);
  }

  @Override
  public List<XrayEdge> getEdges()
  {
    return Collections.unmodifiableList(edges);
  }
  
  
}
