/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gov.llnl.ensdf.decay;

import gov.llnl.rtk.physics.Emission;
import gov.llnl.rtk.physics.Quantity;
import gov.llnl.rtk.physics.Transition;
import gov.llnl.rtk.physics.Xray;
import java.io.Serializable;

/**
 *
 * @author nelson85
 */
public class XrayImpl implements Xray, Serializable
{

  private final DecayTransitionImpl origin;
  private final Quantity energy;
  private final Quantity intensity;
  private final String name;

  public XrayImpl(DecayTransitionImpl dt, Xray xray, double d)
  {
    this.origin = dt;
    this.energy = xray.getEnergy();
    this.intensity = Quantity.ScaleBy(xray.getIntensity(), d);
    this.name = xray.getName();
  }

  @Override
  public Transition getOrigin()
  {
    return this.origin;
  }

  @Override
  public String getName()
  {
    return this.name;
  }

  @Override
  public Quantity getEnergy()
  {
    return this.energy;
  }

  @Override
  public Quantity getIntensity()
  {
    return this.intensity;
  }
  
}
