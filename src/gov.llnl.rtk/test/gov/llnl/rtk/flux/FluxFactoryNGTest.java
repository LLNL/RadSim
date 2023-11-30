/*
 * Copyright 2022, Lawrence Livermore National Security, LLC.
 * All rights reserved.
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.rtk.flux;

import gov.llnl.rtk.flux.Flux;
import gov.llnl.rtk.flux.FluxItem;
import gov.llnl.rtk.flux.FluxFactory;
import static org.testng.Assert.*;
import org.testng.annotations.Test;

/**
 *
 * @author nelson85
 */
public class FluxFactoryNGTest
{

  public FluxFactoryNGTest()
  {
  }

  @Test
  public void testMonoenergetic()
  {
    double energy = 10.0;
    double intensity = 5.0;
    Flux result = FluxFactory.monoenergetic(energy, intensity);
    assertEquals(result.newPhotonEvaluator().getIntegral(0, 3000.0, FluxItem.ALL), 5.0, 0.0);
  }

  @Test
  public void testGroup()
  {
    double energy0 = 40.0;
    double energy1 = 50.0;
    double intensity = 5.0;
    Flux result = FluxFactory.group(energy0, energy1, intensity);
    assertEquals(result.newPhotonEvaluator().getIntegral(0, 3000.0, FluxItem.ALL), 5.0, 0.0);
  }

}
