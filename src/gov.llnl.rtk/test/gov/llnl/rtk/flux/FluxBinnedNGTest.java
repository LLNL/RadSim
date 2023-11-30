/*
 * Copyright 2022, Lawrence Livermore National Security, LLC.
 * All rights reserved.
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.rtk.flux;

import java.util.List;
import static org.testng.Assert.*;
import org.testng.annotations.Test;

/**
 *
 * @author nelson85
 */
public class FluxBinnedNGTest
{

  public FluxBinnedNGTest()
  {
  }

  FluxBinned newInstance()
  {
    FluxBinned out = new FluxBinned();
    out.addPhotonLine(new FluxLineStep(662, 100, 0.2));
    out.addPhotonGroup(new FluxGroupBin(300, 400, 10));
    out.addPhotonGroup(new FluxGroupBin(400, 500, 14));
    out.addPhotonGroup(new FluxGroupBin(500, 600, 20));
    out.addPhotonGroup(new FluxGroupBin(600, 700, 15));
    return out;
  }

  @Test
  public void testGetGammaLines()
  {
    FluxBinned instance = newInstance();
    List result = instance.getPhotonLines();
    assertEquals(result.size(), 1);
  }

  @Test
  public void testGetGammaGroups()
  {
    FluxBinned instance = newInstance();
    List result = instance.getPhotonGroups();
    assertEquals(result.size(), 4);
  }

  @Test
  public void testGetNeutronGroups()
  {
    FluxBinned instance = newInstance();
    List result = instance.getNeutronGroups();
    assertEquals(result.size(), 0);
  }

  @Test
  public void testNewGammaEvaluator()
  {
    FluxBinned instance = newInstance();
    FluxEvaluator result = instance.newPhotonEvaluator();
    assertNotNull(result);
  }

  @Test
  public void testNewNeutronEvaluator()
  {
    FluxBinned instance = new FluxBinned();
    FluxEvaluator result = instance.newNeutronEvaluator();
    assertNotNull(result);
  }

  @Test
  public void testAddGammaLine()
  {
    FluxLine line = new FluxLineStep(100, 10, 0);
    FluxBinned instance = newInstance();
    instance.addPhotonLine(line);
    FluxEvaluator<FluxLineStep, FluxGroupBin> evaluator = instance.newPhotonEvaluator();
    assertEquals(evaluator.getLines(0, 1000).size(), 2);
    assertEquals(evaluator.getLines(0, 200).size(), 1);
  }

  @Test
  public void testAddGammaGroup()
  {
    FluxGroup group = new FluxGroupBin(40, 50, 70);
    FluxBinned instance = new FluxBinned();
    instance.addPhotonGroup(group);
    assertEquals(instance.newPhotonEvaluator().getIntegral(20, 60, FluxItem.ALL), 70.0, 0.0);
  }

  @Test
  public void testEquals()
  {
    Object obj = newInstance();
    FluxBinned instance = newInstance();
    assertTrue(instance.equals(obj));
    assertFalse(instance.equals(new Object()));
    instance.addPhotonGroup(new FluxGroupBin(700, 800, 15));
    assertFalse(instance.equals(obj));
    instance.addPhotonLine(new FluxLineStep(1022, 100, 0.2));
    assertFalse(instance.equals(obj));
  }

  @Test
  public void testAddNeutronGroup()
  {
    FluxGroup group = new FluxGroupBin(40, 50, 70);
    FluxBinned instance = new FluxBinned();
    instance.addNeutronGroup(group);
    assertEquals(instance.newNeutronEvaluator().getIntegral(20, 60, FluxItem.ALL), 70.0, 0.0);
  }

}
