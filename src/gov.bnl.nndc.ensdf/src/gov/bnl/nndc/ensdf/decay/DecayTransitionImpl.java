/*
 * Copyright 2024, Lawrence Livermore National Security, LLC.
 * All rights reserved.
 *
 * Terms and conditions are given in "Notice" file.
 */
package gov.bnl.nndc.ensdf.decay;

import gov.bnl.nndc.ensdf.EnsdfDataSet;
import gov.llnl.rtk.physics.DecayTransition;
import gov.llnl.rtk.physics.Emission;
import gov.llnl.rtk.physics.EmissionCorrelation;
import gov.llnl.rtk.physics.Nuclide;
import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;

/**
 *
 * @author nelson85
 */
public class DecayTransitionImpl implements DecayTransition, Serializable
{   
  private static final long serialVersionUID = -1753569047943434652L;
  Nuclide parent;
  Nuclide child;
  double branchingRatio;
  public final ArrayList<Emission> emissions = new ArrayList<>();
  public final ArrayList<EmissionCorrelation> correlations = new ArrayList<>();
  final EnsdfDataSet dataSet;

  public DecayTransitionImpl(EnsdfDataSet dataSet, Nuclide parent, Nuclide child, double branchingRatio)
  {
    this.dataSet = dataSet;
    this.parent = parent;
    this.child = child;
    this.branchingRatio = branchingRatio;
    if (Double.isNaN(branchingRatio))
      throw new RuntimeException("Branching ratio failed");
  }

  public String toString()
  {
      return String.format("Decay transition (%s->%s), %.3e", parent.toString(), child.toString(), this.branchingRatio);
  }
  
  @Override
  public Nuclide getChild()
  {
    return this.child;
  }

  @Override
  public Nuclide getParent()
  {
    return this.parent;
  }

  @Override
  public double getBranchingRatio()
  {
    return this.branchingRatio;
  }

  @Override
  public List<Emission> getEmissions()
  {
    return emissions;
  }

  @Override
  public List<EmissionCorrelation> getCorrelations()
  {
    return this.correlations;
  }

  public EnsdfDataSet getDataSet()
  {
    return dataSet;
  }

}
