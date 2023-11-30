/*
 * Copyright 2022, Lawrence Livermore National Security, LLC.
 * All rights reserved.
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.ensdf;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author nelson85
 */
public class EnsdfEmissions extends EnsdfExtendable
{

  final public List<EnsdfBeta> beta = new ArrayList<>();
  final public List<EnsdfAlpha> alpha = new ArrayList<>();
  final public List<EnsdfElectronCapture> ec = new ArrayList<>();
  final public List<EnsdfParticle> particles = new ArrayList<>();
  final public List<EnsdfGamma> gamma = new ArrayList<>();
  
  // Extensions
  public List<EnsdfXray> xray = new ArrayList<>();
  public List<EnsdfAuger> auger  = new ArrayList<>();

  EnsdfEmissions(EnsdfDataSet dataSet, char type)
  {
    super(dataSet, type);
  }
  
  public EnsdfEmissions() {
    
  }
}
