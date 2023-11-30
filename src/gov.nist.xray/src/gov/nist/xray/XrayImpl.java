/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gov.nist.xray;

import gov.llnl.rtk.physics.Transition;
import gov.llnl.rtk.physics.Xray;
import gov.llnl.rtk.physics.Quantity;

/**
 *
 * @author nelson85
 */
public class XrayImpl implements Xray
{
   public String symbolIUPAC;
   public String symbolSiegbahn;
   public Quantity energy; // ev
   public Quantity intensity;

  @Override
  public String getName()
  {
    return this.symbolIUPAC;
  }

  @Override
  public Quantity getEnergy()
  {
    return Quantity.ScaleBy(this.energy, 1/1000.0);
  }

  @Override
  public Quantity getIntensity()
  {
    return this.intensity;
  }

  @Override
  public Transition getOrigin()
  {
    return null;
  }
}
