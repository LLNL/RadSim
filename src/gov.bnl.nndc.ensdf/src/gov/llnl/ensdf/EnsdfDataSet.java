/*
 * Copyright 2022, Lawrence Livermore National Security, LLC.
 * All rights reserved.
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.ensdf;

import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;

/**
 *
 * @author nelson85
 */
public class EnsdfDataSet implements Serializable
{

  public EnsdfIdentification identification;
  public List<EnsdfHistory> history = new ArrayList<>();
  public EnsdfQValue QValue = null;
  public List<EnsdfParent> parents = new ArrayList<>();
  public List<EnsdfNormalization> normalizations = new ArrayList<>();
  public EnsdfProductionNormalization productionNormalization = null;
  public EnsdfUnassigned unassigned = new EnsdfUnassigned(this);
  public List<EnsdfLevel> levels = new ArrayList<>();

  @Override
  public String toString()
  {
    return String.format("EnsdfDataset(%s)", identification.DSID);
  }
  
  public List<EnsdfGamma> collectGammas()
  {
    ArrayList<EnsdfGamma> out = new ArrayList<>();
    out.addAll(unassigned.gamma);
    for (EnsdfLevel level : levels)
    {
      out.addAll(level.gamma);
    }
    return out;
  }
  
  public List<EnsdfParticle> collectParticles()
  {
    ArrayList<EnsdfParticle> out = new ArrayList<>();
    out.addAll(unassigned.particles);
    for (EnsdfLevel level : levels)
    {
      out.addAll(level.particles);
    }
    return out;
  }
  
  public List<EnsdfAlpha> collectAlphas()
  {
    ArrayList<EnsdfAlpha> out = new ArrayList<>();
    out.addAll(unassigned.alpha);
    for (EnsdfLevel level : levels)
    {
      out.addAll(level.alpha);
    }
    return out;
  }
  
  public List<EnsdfBeta> collectBetas()
  {
    ArrayList<EnsdfBeta> out = new ArrayList<>();
    out.addAll(unassigned.beta);
    for (EnsdfLevel level : levels)
    {
      out.addAll(level.beta);
    }
    return out;
  }
  
  public List<EnsdfElectronCapture> collectCaptures()
  {
    ArrayList<EnsdfElectronCapture> out = new ArrayList<>();
    out.addAll(unassigned.ec);
    for (EnsdfLevel level : levels)
    {
      out.addAll(level.ec);
    }
    return out;
  }
  
}
