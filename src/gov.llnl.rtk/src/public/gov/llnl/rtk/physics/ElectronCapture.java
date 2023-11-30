/* 
 * Copyright 2023, Lawrence Livermore National Security, LLC.
 * All rights reserved.
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.rtk.physics;

/**
 *
 * @author nelson85
 */
public interface ElectronCapture extends Emission
{

  /**
   * @return the energy
   */
  Quantity getEnergy();

  /**
   * Get the forbiddenness for this emission.
   * 
   * It is not clear if this applies only to the B+.
   * 
   * @return the forbiddenness
   */
  String getForbiddenness();

  /**
   * @return the intensity
   */
  Quantity getIntensity();

  /**
   * @return the logFT
   */
  Quantity getLogFT();

}
