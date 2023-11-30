/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gov.llnl.ensdf.decay;

import gov.llnl.ensdf.EnsdfDataSet;
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
