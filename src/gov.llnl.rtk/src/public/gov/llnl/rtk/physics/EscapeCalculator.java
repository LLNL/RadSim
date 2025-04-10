/*
 * Copyright 2019, Lawrence Livermore National Security, LLC. 
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.rtk.physics;

import gov.llnl.math.DoubleArray;
import gov.llnl.rtk.physics.SphericalRayTrace.Segment;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author nelson85
 */
public class EscapeCalculator
{
  public int N = 30;
  public int M = 30;
  public int KD = 6;

  public static double LOW_ANGLE = 0.025 * Math.PI;

  KleinNishinaDistribution kn = new KleinNishinaDistribution();

  SphericalRayTrace rt = new SphericalRayTrace();
  PhotonCrossSectionLibrary library = null;
  public Units energyUnits = Units.get("keV");
  public HashMap<Layer, Workspace> map = new HashMap<>();

  /**
   * Set the library for material cross sections.
   *
   * @param library
   */
  public void setLibrary(PhotonCrossSectionLibrary library)
  {
    this.library = library;
  }

  /**
   * Set the units used for energy.
   *
   * @param units are a unit of energy such as J, keV, MeV.
   */
  public void setEnergyUnits(Units units)
  {
    units.require(PhysicalProperty.ENERGY);
    energyUnits = units;
  }

  /**
   * Compute the escape probability for a series of energies.
   *
   * This is the original (to be replaced).
   *
   * @param model is the spherical model to transport.
   * @param emitting is the layer which is emitting photons.
   * @param energy in specified energy unit.
   * @return the probability for a photon to escape for the volume. Multiply by
   * activity per volume to get number escaping.
   */
  public double[] escape(SourceModel model, Layer emitting, double... energy)
  {
    if (library == null)
      throw new IllegalStateException("Library must be set");

    // convert energies into a cross_section table
    computeCrossSections(model, energy);

    // Compute a mesh based on the skin depth of the material
    double sigma = 0;  // FIXME lookup in the material table
    double density = emitting.getMaterial().getDensity().as(PhysicalProperty.DENSITY);
    double depth = 1 / (sigma * density);
    double thickness = emitting.getThickness().get();
    double inner = emitting.getInner().get();
    double outer = emitting.getOuter().get();

    if (thickness > KD * depth)
      inner = outer - KD * depth;

    int n = energy.length;

    // Mutable quantities for the calculator
    QuantityImpl rq = new QuantityImpl(PhysicalProperty.LENGTH);

    double[] q0 = new double[n];
    double[] q1 = new double[n];
    double[] S = new double[n];
    double dt = Math.PI / M;

    // Best to work the inner as radial for integration
    for (int i0 = 0; i0 < M; i0++)
    {
      double tm = (i0 + 0.5) * dt;

      rq.value = inner;
      double dr = (outer - inner) / N;
      rt.trace(model, rq, tm);
      computeLogAttenuation(q0);

      for (int i1 = 0; i1 < N; i1++)
      {
        double rm = dr * (i1 + 0.5);

        // Compute the path to surface
        rq.value = dr * (i1 + 1);
        rt.trace(model, rq, tm);

        // Convert to escape probability
        computeLogAttenuation(q1);

        // Numerical integration in one axis
        double k = 2 * Math.PI * Math.sin(tm) * dt * dr * rm * rm;
        for (int i2 = 0; i2 < n; i2++)
          S[i2] += k * (Math.exp(q1[i2]) - Math.exp(q0[i2])) / (q1[i2] - q0[i2]);

        // Update for the next slide
        DoubleArray.assign(q0, q1);
      }
    }
    return S;
  }

  /**
   * Revised escape probability calculator.
   *
   * Multiply by the specific activity per unit volume to the get the photon
   * counts that escaped.
   *
   * @param model is the spherical model to transport.
   * @param emitting is the layer which is emitting photons.
   * @param energy in specified energy unit.
   * @return the probability for a photon to escape for the volume. Multiply by
   * activity per volume to get number escaping.
   */
  public double[] escape2(SourceModel model, Layer emitting, double... energy)
  {

    // convert energies into a cross_section table
    computeCrossSections(model, energy);

    // Get the layers from the model
    List<? extends Layer> layers = model.getLayers();

    // Check for bad inputs
    if (layers.isEmpty() || emitting == null)
    {
      double[] out = new double[energy.length];
      Arrays.fill(out, 1.0);
      return out;
    }

    // Define the limits of integration
    double outer0 = emitting.getOuter().get();
    double outer1 = layers.get(layers.size() - 1).getOuter().get();
    double angle0 = 0;
    double angle1 = Math.asin(outer0 / outer1);
    double dphi = (angle1 - angle0) / (N + 1);

    QuantityImpl Rq = new QuantityImpl(-outer1, PhysicalProperty.LENGTH, 0, true);
    int n = energy.length;

    // Set up attenuation to surface and emitted photons
    double[] P = new double[n];
    double[] S = new double[n];

    // Compute the scaling constant
    double k = 2 * Math.PI * outer1 * outer1 * dphi;

    // Integrate through the angles
    for (int i0 = 0; i0 < N; i0++)
    {
      double phi = (i0 + 0.5) * dphi;

      // Compute the path from the surface back into the object
      rt.trace(model, Rq, phi);

      // Set the chord importance and scaling factor 
      Arrays.fill(P, k * Math.sin(phi) * Math.cos(phi));

      // For each segment in the ray trace
      for (Segment seg : rt)
      {
        // Skip any invalid layer
        if (seg.layer == null)
          continue;

        // Attenuation constant times the path length
        double pathLength = seg.length.get();
        double[] alpha = map.get(seg.layer).attenuation;

        // For the emitting segment add to the source term
        if (seg.layer == emitting)
        {
          // Compute the source terms using the exact integral
          for (int i1 = 0; i1 < n; ++i1)
          {
            if (alpha[i1] > 0)
              S[i1] += -P[i1] * Math.expm1(-alpha[i1] * pathLength) / alpha[i1];
          }
        }

        // Add attenuation for the next segment
        for (int i1 = 0; i1 < n; ++i1)
          P[i1] *= Math.exp(-alpha[i1] * pathLength);
      }
    }

    return S;
  }

  /**
   * Solid ball analytic solution.
   *
   * This requires the model have no more than one layer.
   *
   * @param model is the spherical model with one layer.
   * @param energy in specified energy unit.
   * @return the probability for a photon to escape for the volume. Multiply by
   * activity per volume to get number escaping.
   */
  public double[] solid(SourceModel model, double... energy)
  {
    if (model.getLayers().size() != 1)
      throw new IllegalArgumentException("Must have only one layer");

    computeCrossSections(model, energy);
    Layer layer = model.getLayers().get(0);
    double R = layer.getThickness().get();
    double[] alpha = map.get(layer).attenuation;
    double[] out = new double[energy.length];
    for (int i = 0; i < out.length; ++i)
    {
      double kR = R * alpha[i];
      out[i] = Math.PI / 2 / cube(alpha[i])
              * (-1 + 2 * kR * kR + Math.exp(-2 * kR) * (1 + 2 * kR));
    }
    return out;
  }

  public double[] solidReference(SourceModel model, double... energy)
  {
    if (model.getLayers().size() != 1)
      throw new IllegalArgumentException("Must have only one layer");

    computeCrossSections(model, energy);
    Layer layer = model.getLayers().get(0);
    double R = layer.getThickness().get();
    double[] attenuation = map.get(layer).attenuation;
    double[] out = new double[energy.length];

    double dphi = Math.PI / 2 / (N + 1);
    double k = 2 * Math.PI * R * R * dphi;
    for (int j = 0; j < attenuation.length; ++j)
    {
      double S = 0;
      double alpha = attenuation[j];
      for (int i = 0; i < N; i++)
      {
        double phi = (i + 0.1) * dphi;
        S += Math.sin(phi) * Math.cos(phi) * (1 / alpha - Math.exp(-alpha * 2 * R * Math.cos(phi)));
      }
      out[j] = k * S;
    }
    return out;
  }

  private static double cube(double x)
  {
    return x * x * x;
  }

  /**
   * Compute the probability of escaping on the current path.
   *
   * @param out
   */
  void computeLogAttenuation(double[] out)
  {
    // Clear the old data
    Arrays.fill(out, 0);
    for (Segment seg : rt)
    {
      if (seg.layer == null)
        continue;

      double[] attenuation = map.get(seg.layer).attenuation;

      // Add up attenuation * path length
      double len = seg.length.get();
      for (int i = 0; i < out.length; ++i)
        out[i] += len * attenuation[i];
    }
  }

  /**
   * Get the total cross section by layer.
   *
   * (We will need the same table for low angle scattering)
   *
   * @param model
   * @param energy
   * @return
   */
  private void computeCrossSections(SourceModel model, double[] energy)
  {
    this.map.clear();

    var layers = model.getLayers();
    for (int i = 0; i < layers.size(); ++i)
    {
      Layer layer = layers.get(i);
      Workspace ws = new Workspace(layer, energy);
      map.put(ws.layer, ws);
    }
  }

  /**
   * LayerWorkspace
   */
  public class Workspace
  {
    public final Layer layer;
    public double density;
    public double[] attenuation;
    public double[] lowAngle;

    public Workspace(Layer layer, double[] energy)
    {
      this.layer = layer;
      this.density = layer.getMaterial().getDensity().get();
      this.attenuation = new double[energy.length];
      this.lowAngle = new double[energy.length];

      QuantityImpl e = new QuantityImpl(0, energyUnits, 0, true);

      PhotonCrossSections xcom = library.get(layer.getMaterial());
      xcom.setInputUnits(energyUnits);
      xcom.setOutputUnits(PhysicalProperty.CROSS_SECTION);
      PhotonCrossSectionsEvaluator eval = xcom.newEvaluator();
      for (int j = 0; j < energy.length; ++j)
      {
        e.assign(energy[j], energyUnits);
        eval.seek(energy[j]);
        this.attenuation[j] = this.density * eval.getTotal();

        // Compute the 
        this.lowAngle[j] = kn.getAngularCrossSection(e, 0, 0.015 * Math.PI);
      }
    }
  }

}
