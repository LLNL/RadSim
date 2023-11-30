/*
 * Copyright 2022, Lawrence Livermore National Security, LLC.
 * All rights reserved.
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.rtk.flux;

import gov.llnl.rtk.flux.FluxEvaluator;
import gov.llnl.rtk.flux.FluxGroupTrapezoid;
import gov.llnl.rtk.flux.FluxLine;
import gov.llnl.rtk.flux.FluxLineStep;
import gov.llnl.rtk.flux.FluxTrapezoid;
import java.util.List;
import static org.testng.Assert.*;
import org.testng.annotations.Test;

/**
 *
 * @author nelson85
 */
public class FluxTrapezoidNGTest
{

  FluxTrapezoid newInstance()
  {
    FluxTrapezoid out = new FluxTrapezoid();
    out.addPhotonLine(new FluxLineStep(10, 10, 2));
    out.addPhotonLine(new FluxLineStep(20, 20, 1));
    out.addPhotonLine(new FluxLineStep(30, 10, 1));
    out.addPhotonGroup(new FluxGroupTrapezoid(0, 10, 0, 4));
    out.addPhotonGroup(new FluxGroupTrapezoid(10, 20, 2.0, 2.5));
    out.addPhotonGroup(new FluxGroupTrapezoid(20, 30, 1.5, 2));
    out.addPhotonGroup(new FluxGroupTrapezoid(30, 40, 1, 0));
    out.addNeutronGroup(new FluxGroupTrapezoid(0, 10, 0, 4));
    out.addNeutronGroup(new FluxGroupTrapezoid(10, 20, 2.0, 2.5));
    out.addNeutronGroup(new FluxGroupTrapezoid(20, 30, 1.5, 2));
    out.addNeutronGroup(new FluxGroupTrapezoid(30, 40, 1, 0));
    return out;
  }

  @Test
  public void testGetGammaLines()
  {
    FluxTrapezoid instance = newInstance();
    List<FluxLineStep> result = instance.getPhotonLines();
    double[] e = result.stream().mapToDouble(p -> p.getEnergy()).toArray();
    double[] e2 = new double[]
    {
      10, 20, 30
    };
    assertEquals(e, e2);
  }

  @Test
  public void testGetGammaGroups()
  {
    FluxTrapezoid instance = newInstance();
    List<FluxGroupTrapezoid> result = instance.getPhotonGroups();
    double[] e = result.stream().mapToDouble(p -> p.getEnergyLower()).toArray();
    double[] e2 = new double[]
    {
      0, 10, 20, 30
    };
    assertEquals(e, e2);
  }

  @Test
  public void testGetNeutronGroups()
  {
    FluxTrapezoid instance = newInstance();
    List<FluxGroupTrapezoid> result = instance.getNeutronGroups();
    double[] e = result.stream().mapToDouble(p -> p.getEnergyLower()).toArray();
    double[] e2 = new double[]
    {
      0, 10, 20, 30
    };
    assertEquals(e, e2);
  }

  @Test
  public void testNewGammaEvaluator()
  {
    FluxTrapezoid instance = newInstance();
    FluxEvaluator result = instance.newPhotonEvaluator();
    assertNotNull(result);
  }

  @Test
  public void testNewNeutronEvaluator()
  {
    FluxTrapezoid instance = newInstance();
    FluxEvaluator result = instance.newNeutronEvaluator();
    assertNotNull(result);
  }

  @Test
  public void testAddGammaLine()
  {
    FluxLine line = new FluxLineStep(40, 1, 2);
    FluxTrapezoid instance = newInstance();
    instance.addPhotonLine(line);
  }

  @Test
  public void testAddGammaGroup()
  {
    FluxGroupTrapezoid group = new FluxGroupTrapezoid(40, 50, 0, 0);
    FluxTrapezoid instance = newInstance();
    instance.addPhotonGroup(group);
  }
  
  @Test
  public void testAddNeutronGroup()
  {
    FluxGroupTrapezoid group = new FluxGroupTrapezoid(40, 50, 0, 0);
    FluxTrapezoid instance = newInstance();
    instance.addNeutronGroup(group);
  }

  @Test
  public void testEquals()
  {
    Object obj = newInstance();
    FluxTrapezoid instance = newInstance();
    assertTrue(instance.equals(obj));
    assertFalse(instance.equals(new Object()));
    instance.addPhotonGroup(new FluxGroupTrapezoid(40, 50, 1, 0));
    assertFalse(instance.equals(obj));
    instance.addPhotonLine(new FluxLineStep(40, 10, 1));
    assertFalse(instance.equals(obj));
    instance.addNeutronGroup(new FluxGroupTrapezoid(50, 60, 1, 0));
    assertFalse(instance.equals(obj));
  }

}
