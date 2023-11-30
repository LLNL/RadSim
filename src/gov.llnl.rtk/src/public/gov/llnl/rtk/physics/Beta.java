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
public interface Beta extends Emission
{

  /** 
   * End point energy for the beta.
   * 
   * @return 
   */
  Quantity getEnergy();

  Quantity getIntensity();

  /**
   * Get the Log FT.
   * 
   * @return the logFT or null if not specified.
   */  
  Quantity getLogFT();
 
  String getForbiddenness();

}
