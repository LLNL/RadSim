/*
 * Copyright 2022, Lawrence Livermore National Security, LLC.
 * All rights reserved.
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.rtk.flux;

import gov.llnl.rtk.flux.FluxGroup;
import gov.llnl.rtk.flux.FluxGroupBin;
import gov.llnl.rtk.flux.FluxItem;
import gov.llnl.rtk.flux.FluxLine;
import gov.llnl.rtk.flux.FluxLineStep;
import java.util.ArrayList;
import java.util.List;
import static org.testng.Assert.*;
import org.testng.annotations.Test;

/**
 *
 * @author nelson85
 */
public class FluxEvaluatorSortedNGTest
{

  public FluxEvaluatorSortedNGTest()
  {
  }

  FluxEvaluatorSorted newInstance()
  {
    List<FluxLineStep> lines = new ArrayList<>();
    List<FluxGroupBin> groups = new ArrayList<>();
    lines.add(new FluxLineStep(10, 100, 0));
    lines.add(new FluxLineStep(20, 200, 0));
    lines.add(new FluxLineStep(30, 300, 0));
    groups.add(new FluxGroupBin(0, 10, 6));
    groups.add(new FluxGroupBin(10, 15, 5));
    groups.add(new FluxGroupBin(15, 25, 4));
    groups.add(new FluxGroupBin(25, 35, 3));
    return new FluxEvaluatorSorted(lines, groups);
  }

  @Test
  public void testSeekLine()
  {
    FluxEvaluatorSorted instance = newInstance();
    instance.seekLine(0);
    assertEquals(((FluxLine) instance.lineIterator.next()).getEnergy(), 10.0, 0.0);
    instance.seekLine(10);
    assertEquals(((FluxLine) instance.lineIterator.next()).getEnergy(), 10.0, 0.0);
    instance.seekLine(11);
    assertEquals(((FluxLine) instance.lineIterator.next()).getEnergy(), 20.0, 0.0);
    instance.seekLine(19);
    assertEquals(((FluxLine) instance.lineIterator.next()).getEnergy(), 20.0, 0.0);
    instance.seekLine(20);
    assertEquals(((FluxLine) instance.lineIterator.next()).getEnergy(), 20.0, 0.0);
    instance.seekLine(21);
    assertEquals(((FluxLine) instance.lineIterator.next()).getEnergy(), 30.0, 0.0);
    instance.seekLine(29);
    assertEquals(((FluxLine) instance.lineIterator.next()).getEnergy(), 30.0, 0.0);
    instance.seekLine(30);
    assertEquals(((FluxLine) instance.lineIterator.next()).getEnergy(), 30.0, 0.0);
    instance.seekLine(31);
    assertFalse(instance.lineIterator.hasNext());
  }

  @Test
  public void testSeekGroup()
  {
    FluxEvaluatorSorted instance = newInstance();
    instance.seekGroup(-1);
    assertEquals(((FluxGroup) instance.groupIterator.next()).getEnergyLower(), 0.0, 0.0);
    instance.seekGroup(0);
    assertEquals(((FluxGroup) instance.groupIterator.next()).getEnergyLower(), 0.0, 0.0);
    instance.seekGroup(1);
    assertEquals(((FluxGroup) instance.groupIterator.next()).getEnergyLower(), 0.0, 0.0);
    instance.seekGroup(9);
    assertEquals(((FluxGroup) instance.groupIterator.next()).getEnergyLower(), 0.0, 0.0);

    instance.seekGroup(10);
    assertEquals(((FluxGroup) instance.groupIterator.next()).getEnergyLower(), 10.0, 0.0);
    instance.seekGroup(11);
    assertEquals(((FluxGroup) instance.groupIterator.next()).getEnergyLower(), 10.0, 0.0);
    instance.seekGroup(14);
    assertEquals(((FluxGroup) instance.groupIterator.next()).getEnergyLower(), 10.0, 0.0);

    instance.seekGroup(15);
    assertEquals(((FluxGroup) instance.groupIterator.next()).getEnergyLower(), 15.0, 0.0);
    instance.seekGroup(16);
    assertEquals(((FluxGroup) instance.groupIterator.next()).getEnergyLower(), 15.0, 0.0);
    instance.seekGroup(24);
    assertEquals(((FluxGroup) instance.groupIterator.next()).getEnergyLower(), 15.0, 0.0);

    instance.seekGroup(25);
    assertEquals(((FluxGroup) instance.groupIterator.next()).getEnergyLower(), 25.0, 0.0);
    instance.seekGroup(26);
    assertEquals(((FluxGroup) instance.groupIterator.next()).getEnergyLower(), 25.0, 0.0);
    instance.seekGroup(34);
    assertEquals(((FluxGroup) instance.groupIterator.next()).getEnergyLower(), 25.0, 0.0);
    instance.seekGroup(35);
    assertFalse(instance.groupIterator.hasNext());
    instance.seekGroup(36);
    assertFalse(instance.groupIterator.hasNext());
    instance.seekGroup(34);
    assertEquals(((FluxGroup) instance.groupIterator.next()).getEnergyLower(), 25.0, 0.0);
  }

  @Test
  public void testGetIntegral()
  {
    FluxEvaluatorSorted instance = newInstance();
    assertEquals(instance.getIntegral(0, 10, FluxItem.GROUPS), 6.0, 0.0);
    assertEquals(instance.getIntegral(0, 20, FluxItem.GROUPS), 13.0, 0.0);
  }

  @Test
  public void testGetLines()
  {
    FluxEvaluatorSorted instance = newInstance();
    assertEquals(instance.getLines(-10, 100).size(), 3);
    assertEquals(instance.getLines(10, 100).size(), 3);
    assertEquals(instance.getLines(11, 100).size(), 2);
  }

}
