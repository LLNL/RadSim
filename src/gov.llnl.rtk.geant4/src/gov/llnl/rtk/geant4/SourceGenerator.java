/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gov.llnl.rtk.geant4;

import java.io.IOException;

import gov.llnl.rtk.flux.*;
import gov.llnl.rtk.physics.ConicalSection;
import gov.llnl.rtk.physics.CylindricalSection;
import gov.llnl.rtk.physics.Section;
import gov.llnl.rtk.physics.SphericalSection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author cheung27
 */
public class SourceGenerator {

  private int NUM_ENERGY_BINS = 4001;
  private double LOWER_ENERGY_BIN_keV = 0.0;
  private double UPPER_ENERGY_BIN_keV = 4000.0;
  private double[] energyBins;
  private List<Section> shieldingSections = new ArrayList<>();
  private int numSourceParticles = 100000;
  private String sourceParticle = "e-";
  private boolean isSphericalSource = false;
  private double sourceRadius = 0.0;

  public List<double[]> distribution;
  public GEANT4Environment environment;

  public SourceGenerator() {
    this.energyBins = new double[NUM_ENERGY_BINS];
    for (int i = 0; i < energyBins.length; i++) {
      energyBins[i] = LOWER_ENERGY_BIN_keV + i * (UPPER_ENERGY_BIN_keV - LOWER_ENERGY_BIN_keV) / (NUM_ENERGY_BINS - 1);
    }
  }

  public SourceGenerator(int numEnergyBins, double upperEnergy, int numSourceParticles, String sourceParticle) {
    this.NUM_ENERGY_BINS = numEnergyBins;
    this.UPPER_ENERGY_BIN_keV = upperEnergy;
    this.energyBins = new double[NUM_ENERGY_BINS];
    this.sourceParticle = sourceParticle;
    this.NUM_ENERGY_BINS = numSourceParticles;
    for (int i = 0; i < energyBins.length; i++) {
      energyBins[i] = LOWER_ENERGY_BIN_keV + i * (UPPER_ENERGY_BIN_keV - LOWER_ENERGY_BIN_keV) / (NUM_ENERGY_BINS - 1);
    }
  }

  public void setFlux(Flux flux) throws Exception {
    if (flux instanceof FluxBinned) {
      this.distribution = convertFluxToDistribution((FluxTrapezoid) flux);
    }
    this.distribution = convertFluxToDistribution((FluxTrapezoid) flux);
  }

  public static List<double[]> convertFluxToDistribution(FluxTrapezoid flux) {
    List<double[]> distribution = new ArrayList<>();
    distribution.add(new double[]{
      flux.getPhotonGroups().get(0).getEnergyLower(), flux.getPhotonGroups().get(0).getDensityLower(), 0.0});
    double cdf = 0.0;
    for (FluxGroupTrapezoid group : flux.getPhotonGroups()) {
      cdf += group.getIntegral(group.getEnergyLower(), group.getEnergyUpper());
      distribution.add(new double[]{group.getEnergyUpper(), group.getDensityUpper(), cdf});
    }
    return distribution;
  }

  // WIP
  private void addConicalSection(ConicalSection conicalSection) {
    shieldingSections.add(conicalSection);
  }
  // WIP
  private void addSphericalSection(SphericalSection sphericalSection) {
    shieldingSections.add(sphericalSection);
  }
  // WIP
  private void addCylindricalSection(CylindricalSection cylindricalSection) {
    shieldingSections.add(cylindricalSection);
  }

  public void setSourceParticle(String particleType) {
    this.sourceParticle = particleType;
  }

  public void setNumberOfParticle(int number) {
    this.numSourceParticles = number;
  }

  public void setSphericalSource(boolean isSpherical) {
    this.isSphericalSource = isSpherical;
  }

  public void setSourceRadius(double radius) {
    this.sourceRadius = radius;
  }

  public void setEnvironment() {
    this.environment = new GEANT4Environment(this.distribution, this.sourceParticle,
            this.numSourceParticles, this.isSphericalSource, this.sourceRadius, this.shieldingSections);
  }

  public void fetchEnvironment() {
    this.environment.setSourceParticle(this.sourceParticle);
    this.environment.setNumberOfParticle(this.numSourceParticles);
    this.environment.setDistribution(this.distribution);
    this.environment.writeDistributionToFile();
  }
}
