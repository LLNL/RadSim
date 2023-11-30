/*
 * Copyright 2019, Lawrence Livermore National Security, LLC. 
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.rtk.physics;

import java.util.List;
import java.util.Map;

/**
 *
 * @author nelson85
 */
public interface XrayEdge
{
  String getName();
  double getFlorencenceYield();
  List<Xray> getXrays();
  Map<String, Double> getCosterKronig();
}
