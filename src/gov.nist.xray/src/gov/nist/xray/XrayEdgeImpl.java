/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gov.nist.xray;

import gov.llnl.rtk.physics.Xray;
import gov.llnl.rtk.physics.XrayEdge;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Collection of xray emissions associated with an edge ("K","L","M",...).
 * 
 * @author nelson85
 */
class XrayEdgeImpl implements XrayEdge
{
  public String name;
  public double energy, fluorescence_yield, ratio_jump;
  public List<XrayImpl> lines = new ArrayList<>();
  // CosterKronig
  Map<String, Double> CK = new HashMap<>();

  @Override
  public String getName()
  {
    return name;
  }

  @Override
  public double getFlorencenceYield()
  {
    return this.fluorescence_yield;
  }

  @Override
  public List<Xray> getXrays()
  {
    return Collections.unmodifiableList(lines);
  }

  @Override
  public Map<String, Double> getCosterKronig()
  {
    return this.CK;
  }
}
