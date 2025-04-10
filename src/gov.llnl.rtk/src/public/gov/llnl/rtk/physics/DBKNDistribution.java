/*
 * Copyright 2022, Lawrence Livermore National Security, LLC.
 * All rights reserved.
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.rtk.physics;

import static gov.llnl.math.SpecialFunctions.erf;
import java.util.List;

/**
 * KleinNishina with a modification for Doppler broadening.
 *
 * This has been verified to work against GEANT. The ad hoc modification to
 * Klein Nishina equation based on each electron shell.  This modification
 * only captures the shape of the cross section but not the angular response.
 *
 * FIXME connect the X-ray data to the calculator directly rather than through
 * its own class.
 *
 * @author nelson85
 */
public class DBKNDistribution implements ScatteringDistribution
{
  final static double WIDTH = 0.017;
  final double[] fraction;
  final double[] width;

  public class ElectronShell
  {
    public int count;
    public Quantity energy;
  }

  public DBKNDistribution(List<ElectronShell> electrons)
  {
    int n = electrons.size();
    fraction = new double[n];
    width = new double[n];
    double c = electrons.stream().mapToInt(p -> p.count).sum();
    int i = 0;
    for (ElectronShell e : electrons)
    {
      fraction[i] = e.count / c;
      width[i] = WIDTH * Math.sqrt(e.energy.as("eV"));
      i++;
    }
  }

  @Override
  public double getCrossSection(Quantity energyIncident, Quantity energyEmitted)
  {
    double ei = energyIncident.get();
    double ep = energyEmitted.get();

    double em = ei / (1 + 2.0 * ei / Constants.MEC2.get());
    double w = Math.sqrt(ei);

    double sinT2 = 0;
    double u;
    if (ep < em)
    {
      u = em / ei;
    }
    else
    {
      double cosT = 1 + Constants.MEC2.get() * (1 / ei - 1 / ep);
      sinT2 = 1 - cosT * cosT;
      u = ep / ei;
    }

    double k = 0.5 * (u + 1 / u - sinT2);
    double s = 1;
    for (int i = 0; i < this.fraction.length; ++i)
    {
      s += fraction[i] * erf((ep - em) / w / width[i]);
    }
    return k * s;
  }

}
