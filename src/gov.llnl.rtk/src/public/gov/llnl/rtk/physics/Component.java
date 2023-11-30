/* 
 * Copyright (c) 2016, Lawrence Livermore National Security, LLC.
 * All rights reserved.
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.rtk.physics;

import gov.llnl.utility.xml.bind.ReaderInfo;
import gov.llnl.utility.xml.bind.WriterInfo;

/**
 * Component for each nuclide of the composition.
 */
@ReaderInfo(ComponentReader.class)
@WriterInfo(ComponentWriter.class)
public interface Component
{
  /**
   * Get the nuclide.
   *
   * @return
   */
  Nuclide getNuclide();

  /**
   * Get the activity of the source. Units will be defined by the nuclide. Units
   * should be SI.
   *
   * FIXME this is being used for both specific activity and activity. This
   * should duality should be removed.
   *
   * @return the activity of the source if known, or 0 if not available.
   */
  double getActivity();

  /**
   * Get the fraction of dose associated with the template that comes from this
   * nuclide.
   * 
   * This is used for activity estimates based on dose.  
   *
   * @return the dose fraction or 0 if not available.
   */
  double getDoseFraction();

  /** Get the mass fraction of a material for the component.  
   * 
   * This is used for non-radioactive components of a material.
   * 
   * @return 
   */ 
  double getMassFraction();


}
