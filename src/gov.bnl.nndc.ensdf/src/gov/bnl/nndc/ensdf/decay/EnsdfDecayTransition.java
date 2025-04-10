/*
 * Copyright 2024, Lawrence Livermore National Security, LLC.
 * All rights reserved.
 *
 * Terms and conditions are given in "Notice" file.
 */
package gov.bnl.nndc.ensdf.decay;

import gov.bnl.nndc.ensdf.EnsdfDataSet;
import gov.bnl.nndc.ensdf.EnsdfNormalization;
import gov.llnl.rtk.physics.DecayTransition;
import gov.llnl.rtk.physics.Emission;
import gov.llnl.rtk.physics.Nuclide;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import gov.llnl.rtk.physics.EmissionCorrelation;

/**
 *
 * @author nelson85
 */
public class EnsdfDecayTransition implements DecayTransition
{

  public final Nuclide child;
  public final Nuclide parent;
  final EnsdfDataSet record;
  final double brachingRatio;
  final List<Emission> emissions = new ArrayList<>();
  public double vacanciesK;
  public double vacanciesL;

  public EnsdfDecayTransition(EnsdfDataSet record)
  {
    this.child = record.identification.product;
    this.parent = record.parents.get(0).nuclide;
    this.record = record;
    EnsdfNormalization normalization = record.normalizations.get(0);
    this.brachingRatio = normalization.BR.toDouble();
    if (this.parent==null)
      throw new RuntimeException();
  }

  public EnsdfDataSet getDataSet()
  {
    return record;
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
    return this.brachingRatio;
  }

  @Override
  public List<Emission> getEmissions()
  {
    return this.emissions;
  }

  @Override
  public List<EmissionCorrelation> getCorrelations()
  {
    return Collections.EMPTY_LIST;
  }

}
