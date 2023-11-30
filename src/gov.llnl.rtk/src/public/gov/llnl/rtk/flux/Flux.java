/* 
 * Copyright 2022, Lawrence Livermore National Security, LLC.
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.rtk.flux;

import java.util.List;

/**
 * Flux represents the emissions from a source over some area.
 *
 * Typically we use total flux into 4 pi. But flux may also be defined emissions
 * per area.
 *
 * @author nelson85
 */
public interface Flux
{

  // FIXME figure out how to represent time and flux geometry.
  /**
   * Gamma lines defined for this flux.
   *
   * The lines should not be modified.
   *
   * @return lines for this flux or an empty list if not defined.
   */
  public List<? extends FluxLine> getPhotonLines();

  /**
   * Gamma groups for this flux.
   *
   * @return a list holding groups.
   */
  public List<? extends FluxGroup> getPhotonGroups();

  /**
   * Neutron groups for this flux.
   *
   * @return a list holding groups.
   */
  public List<? extends FluxGroup> getNeutronGroups();

  /**
   * Accelerated calculator for gamma integrals.
   *
   * @return
   */
  public FluxEvaluator newPhotonEvaluator();

  /**
   * Accelerated calculator for neutron integrals.
   *
   * @return
   */
  public FluxEvaluator newNeutronEvaluator();
}
