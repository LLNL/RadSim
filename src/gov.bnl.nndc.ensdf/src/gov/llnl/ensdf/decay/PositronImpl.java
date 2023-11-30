/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gov.llnl.ensdf.decay;

import gov.llnl.ensdf.EnsdfElectronCapture;
import gov.llnl.rtk.physics.Positron;
import gov.llnl.rtk.physics.Transition;
import gov.llnl.rtk.physics.Quantity;
import java.io.Serializable;

/**
 *
 * @author nelson85
 */
public class PositronImpl implements Positron, Serializable
{

  Transition transition;
  EnsdfElectronCapture record;
  private final Quantity intensity;
  private final Quantity energy;
  private final Quantity logFT;

  public PositronImpl(Transition transition, EnsdfElectronCapture record, double intensity)
  {
    this.transition = transition;
    this.record = record;
    this.intensity = Quantity.of(intensity, null);
    if (record.E.isSpecified())
      this.energy = Quantity.of(record.E.toDouble() - 1022, "keV"); // FIXME VC: When uncertainty is ready, use another Quantity constructor
    else
      this.energy = Quantity.of(record.dataSet.parents.get(0).QP.toDouble() - record.level.E.toDouble() - 1022.0, "keV"); // FIXME VC: When uncertainty is ready, use another Quantity constructor

    if (record.LOGFT.isSpecified())
      // FIXME what if it isn't specified
      this.logFT = Quantity.of(record.LOGFT.toDouble(), null);
    else
      this.logFT = Quantity.UNSPECIFIED;
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

  @Override
  public Quantity getLogFT()
  {
    return this.logFT;
  }

  @Override
  public Transition getOrigin()
  {
    return this.transition;
  }

  @Override
  public String getForbiddenness()
  {
    return record.UN;
  }

}
