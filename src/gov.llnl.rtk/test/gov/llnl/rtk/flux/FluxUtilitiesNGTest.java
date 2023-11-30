/*
 * Copyright 2022, Lawrence Livermore National Security, LLC.
 * All rights reserved.
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.rtk.flux;

import gov.llnl.rtk.data.EnergyScale;
import gov.llnl.rtk.data.EnergyScaleFactory;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.IntStream;
import static org.testng.Assert.*;
import org.testng.annotations.Test;

/**
 *
 * @author nelson85
 */
public class FluxUtilitiesNGTest
{

  @Test
  public void testCtor()
  {
    assertNotNull(new FluxUtilities());
  }

  @Test
  public void testToBinned_FluxSpectrum_EnergyScale()
  {
    FluxSpectrum spectrum = (FluxSpectrum) TestSupport.loadResource("fluxSpectrum.bin", FluxEncoding.getInstance());
    EnergyScale scale = EnergyScaleFactory.newSqrtScale(20, 5000, 101);
//    FluxBinned expResult = null;
    FluxBinned result = FluxUtilities.toBinned(spectrum, scale);
    assertNotNull(result);
//    assertEquals(result, expResult);
  }

  @Test
  public void testExtract()
  {
    double[] edges = new double[]
    {
      1, 2, 3, 4, 5, 6, 7, 8, 9, 10
    };
    double[] counts = new double[]
    {
      1, 1, 1, 1, 1, 100, 0, 0, 0
    };
    FluxSpectrum spectrum = new FluxSpectrum(EnergyScaleFactory.newScale(edges), counts, null, null);
    double[][] expResult = new double[][]
    {
      new double[]
      {
        1, 1, 1, 1, 1, 0.5, 0, 0, 0
      },
      new double[]
      {
        0, 0, 0, 0, 0, 99.5, 0, 0, 0
      },
      new double[]
      {
        0, 0, 0, 0, 0, 1.0, 0, 0, 0
      }
    };
    double[][] result = FluxUtilities.extract(spectrum);
    assertTrue(Arrays.equals(result[0], expResult[0]));
    assertTrue(Arrays.equals(result[1], expResult[1]));
    assertTrue(Arrays.equals(result[2], expResult[2]));
  }

  @Test
  public void testToBinned_Flux_EnergyScale()
  {
    Flux flux = (FluxTrapezoid) TestSupport.loadResource("fluxTrapezoid.bin", FluxEncoding.getInstance());
    EnergyScale scale = EnergyScaleFactory.newSqrtScale(20, 5000, 101);
    FluxBinned result = FluxUtilities.toBinned(flux, scale);
    assertNotNull(result);
  }

  @Test
  public void testToSpectrum() throws IOException
  {
    Flux flux = TestSupport.loadResource("fluxTrapezoid.bin", FluxEncoding.getInstance());
    EnergyScale scale = EnergyScaleFactory.newScale(IntStream.range(0, 500).mapToDouble(p -> 6 * p).toArray());
    FluxSpectrum expResult = (FluxSpectrum) TestSupport.loadResource("results/toSpectrum.bin", FluxEncoding.getInstance());
    FluxSpectrum result = FluxUtilities.toSpectrum(flux, scale, FluxItem.ALL);
    for (int i = 0; i < result.photonCounts.length; ++i)
    {
      System.out.println(result.photonCounts[i] + " " + expResult.photonCounts[i]);
    }
//    Utility.saveFile(Paths.get("toSpectrum.bin"), FluxConverter.toBytes(result));
    assertEquals(result, expResult);
  }

  @Test
  public void testToTrapezoid() throws IOException
  {
    FluxBinned binned = (FluxBinned) TestSupport.loadResource("fluxBinned.bin", FluxEncoding.getInstance());
    System.out.println(binned.photonGroups.size());
    FluxTrapezoid expResult = (FluxTrapezoid) TestSupport.loadResource("results/toTrapezoid.bin", FluxEncoding.getInstance());
    FluxTrapezoid result = FluxUtilities.toTrapezoid(binned);
    for (int i = 0; i < result.photonLines.size(); ++i)
    {
      System.out.println(result.photonLines.get(i) + " " + expResult.photonLines.get(i));
    }
    System.out.println(expResult.photonGroups.size());
    System.out.println(result.photonGroups.size());

    for (int i = 0; i < expResult.photonGroups.size(); ++i)
    {
      System.out.println(result.photonGroups.get(i) + " " + expResult.photonGroups.get(i));
    }
//    Utility.saveFile(Paths.get("toTrapezoid.bin"), FluxConverter.toBytes(result));
    assertEquals(result, expResult);
  }

  @Test
  public void testInsertGroup()
  {
    List<FluxGroupBin> groups = new ArrayList<>();
    FluxUtilities.insertGroup(groups, new FluxGroupBin(0, 10, 1));
    FluxUtilities.insertGroup(groups, new FluxGroupBin(10, 20, 1));
    FluxUtilities.insertGroup(groups, new FluxGroupBin(30, 40, 1));
    FluxUtilities.insertGroup(groups, new FluxGroupBin(20, 30, 1));
    FluxUtilities.insertGroup(groups, null);
    Iterator<FluxGroupBin> iter = groups.iterator();
    assertEquals(iter.next().getEnergyLower(), 0.0, 0.0);
    assertEquals(iter.next().getEnergyLower(), 10.0, 0.0);
    assertEquals(iter.next().getEnergyLower(), 20.0, 0.0);
    assertEquals(iter.next().getEnergyLower(), 30.0, 0.0);
    assertFalse(iter.hasNext());

  }

  @Test
  public void testInsertLine()
  {
    List<FluxLine> lines = new ArrayList<>();
    FluxUtilities.insertLine(lines, new FluxLineStep(10, 10, 0));
    FluxUtilities.insertLine(lines, new FluxLineStep(5, 10, 0));
    FluxUtilities.insertLine(lines, new FluxLineStep(20, 10, 0));
    FluxUtilities.insertLine(lines, new FluxLineStep(7, 10, 0));
    Iterator<FluxLine> iter = lines.iterator();
    assertEquals(iter.next().getEnergy(), 5.0, 0.0);
    assertEquals(iter.next().getEnergy(), 7.0, 0.0);
    assertEquals(iter.next().getEnergy(), 10.0, 0.0);
    assertEquals(iter.next().getEnergy(), 20.0, 0.0);
    assertFalse(iter.hasNext());
  }

}
