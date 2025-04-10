/*
 * Copyright 2024, Lawrence Livermore National Security, LLC.
 * All rights reserved.
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.nist.physics.xray;

import gov.llnl.rtk.physics.Xray;
import gov.llnl.rtk.physics.XrayEdge;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * NIST version of xray edge data.
 *
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
