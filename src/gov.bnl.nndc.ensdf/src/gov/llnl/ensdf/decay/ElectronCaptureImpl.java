/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gov.llnl.ensdf.decay;

import gov.llnl.ensdf.EnsdfElectronCapture;
import gov.llnl.rtk.physics.ElectronCapture;
import gov.llnl.rtk.physics.Quantity;
import gov.llnl.rtk.physics.Transition;
import java.io.Serializable;

/**
 *
 * @author nelson85
 */
public class ElectronCaptureImpl implements ElectronCapture, Serializable
{

  public final EnsdfElectronCapture record;
  final Transition transition;
  final Quantity energy;
  final Quantity intensity;
  final Quantity logFT;

  public ElectronCaptureImpl(Transition transition, EnsdfElectronCapture record, double intensity)
  {
    this.transition = transition;
    this.record = record;
    this.intensity = Quantity.of(intensity, null);

    // FIXME how do we compute the energy from the Q value.
    // FIXME sometimes the beta energy is implied.
    if (record.E.isSpecified())
      this.energy = Quantity.of(record.E.toDouble(), "keV"); // FIXME VC: When uncertainty is ready, use another Quantity constructor
    else
      this.energy = Quantity.of(record.dataSet.parents.get(0).QP.toDouble() - record.level.E.toDouble(), "keV"); // FIXME VC: When uncertainty is ready, use another Quantity constructor 
              
    // FIXME what if it isn't specified
    if (record.LOGFT.isSpecified())
      this.logFT = Quantity.of(record.LOGFT.toDouble(), null);
    else
      this.logFT = Quantity.UNSPECIFIED;
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
