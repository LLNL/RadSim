package gov.llnl.rtk.physics;

/*
 * Copyright 2019, Lawrence Livermore National Security, LLC. 
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
/**
 *
 * @author nelson85
 */
/**
 *
 * @author nelson85
 */
public class KleinNishinaDistribution implements ScatteringDistribution
{
  final static double RE2 = Constants.RADIUS_E.get() * Constants.RADIUS_E.get();
  final static double KCS = 2 * Math.PI * RE2 * Constants.MEC2.get() * Constants.MEC2.get();

  @Override
  public double getCrossSection(Quantity energyIncident, Quantity energyEmitted)
  {
    double ei = energyIncident.get();
    double k = Constants.MEC2.get();
    double ep = energyEmitted.get();
    // ep must be greater than ei/(1+e/k*(1-cos(pi)) and less than e
    if (ei < 0 || ep > ei || ep + 2 * ei * ep < k * ei)
      return 0;
    double cosT = -1 + k / ep - k / ei;
    double sinT2 = (1 - cosT * cosT);
    // Standard Klein-Nisha with proper normalizations 
    return KCS * (ei / ep + ep / ei - sinT2) / ei / ei;
  }

  /**
   * Compute the total cross section.
   *
   * @param energyIncident is the energy of the initial gamma.
   * @return is the cross section in SI units
   */
  public double getTotalCrossSection(Quantity energyIncident)
  {
    double ei = energyIncident.get();
    double k = Constants.MEC2.get();
    double x = ei / k;
    return Math.PI * RE2 * (2 * x * (2 + 8 * x + 9 * x * x + x * x * x) / (1 + 2 * x) / (1 + 2 * x)
            + (-2 - 2 * x + x * x) * Math.log(1 + 2 * x)) / x / x / x;
  }

  /**
   * Compute the cross section between two angles.
   *
   * This will be needed to compute low angle scatter probability.
   *
   * @param energyIncident is the energy of the initial gamma.
   * @param a0 is the minimum scatter angle.
   * @param a1 is the maximum scatter angle.
   * @return is the cross section in SI units
   */
  public double getAngularCrossSection(Quantity energyIncident, double a0, double a1)
  {
    double ei = energyIncident.get();
    double k = Constants.MEC2.get();
    double x = ei / k;
    double c0 = x * Math.cos(a0);
    double c1 = x * Math.cos(a1);
    double v0 = ((-2 - 6 * x - 5 * x * x + 2 * (1 + 2 * x) * c0) / 2 / sqr(1 + x - c0)
            + (-2 - 2 * x + x * x) * Math.log(1 + x - c0) - c0) / (x * x * x);
    double v1 = ((-2 - 6 * x - 5 * x * x + 2 * (1 + 2 * x) * c1) / 2 / sqr(1 + x - c1)
            + (-2 - 2 * x + x * x) * Math.log(1 + x - c1) - c1) / (x * x * x);
    return Math.PI * RE2 * (v1 - v0);
  }

  private static double sqr(double x)
  {
    return x * x;
  }

  /**
   * Get the energy of the scattered photon.
   *
   * @param energyIncident is the energy of the initial gamma.
   * @param theta is the scatter angle.
   * @return the energy of the scattered photon.
   */
  public Quantity getEmitted(Quantity energyIncident, double theta)
  {
    double ei = energyIncident.get();
    double k = Constants.MEC2.get();
    double x = ei / k;
    return Quantity.of(ei / (1 + x * (1 - Math.cos(theta))), "keV");
  }

}
