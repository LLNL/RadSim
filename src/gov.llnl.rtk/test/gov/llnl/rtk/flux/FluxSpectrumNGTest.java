/*
 * Copyright 2022, Lawrence Livermore National Security, LLC.
 * All rights reserved.
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.rtk.flux;

import gov.llnl.rtk.flux.FluxGroup;
import gov.llnl.rtk.flux.FluxEvaluator;
import gov.llnl.rtk.flux.FluxLine;
import gov.llnl.rtk.flux.FluxGroupBin;
import gov.llnl.rtk.flux.FluxSpectrum;
import gov.llnl.rtk.data.EnergyScale;
import gov.llnl.rtk.data.EnergyScaleFactory;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;
import static org.testng.Assert.*;
import org.testng.annotations.Test;

/**
 *
 * @author nelson85
 */
public class FluxSpectrumNGTest
{

  public FluxSpectrumNGTest()
  {
  }

  FluxSpectrum newInstance_1()
  {
    EnergyScale gammaEdges = EnergyScaleFactory.newScale(new double[]
    {
      1, 2, 3, 4, 6
    });
    double[] gammaCounts = new double[]
    {
      10, 8, 5, 3
    };
    return new FluxSpectrum(gammaEdges, gammaCounts, null, null);
  }
  
  FluxSpectrum newInstance_2()
  {
    EnergyScale gammaEdges = EnergyScaleFactory.newScale(new double[]
    {
      1, 2, 3, 4, 6
    });
    double[] gammaCounts = new double[]
    {
      10, 8, 5, 3
    };
    EnergyScale neutronEdges = EnergyScaleFactory.newScale(new double[]
    {
      1, 2, 3, 4, 6
    });
    double[] neutronCounts = new double[]
    {
      3, 5, 8, 10
    };
    return new FluxSpectrum(gammaEdges, gammaCounts, neutronEdges, neutronCounts);
  }

  @Test
  public void testGetGammaLines()
  {
    FluxSpectrum instance = newInstance_1();
    List<FluxLine> expResult = Collections.EMPTY_LIST;
    List<FluxLine> result = instance.getPhotonLines();
    assertEquals(result, expResult);
  }

  @Test
  public void testGetGammaGroups()
  {
    FluxSpectrum instance = newInstance_1();
    List result = instance.getPhotonGroups();
    assertEquals(result.size(), 4);
  }

  @Test
  public void testGetNeutronGroups()
  {
    FluxSpectrum instance = newInstance_1();
    List expResult = Collections.EMPTY_LIST;
    List result = instance.getNeutronGroups();
    assertEquals(result, expResult);
    
    instance = newInstance_2();
    result = instance.getNeutronGroups();
    assertEquals(result.size(), 4);
  }

  @Test
  public void testNewGammaEvaluator()
  {
    FluxSpectrum instance = newInstance_1();
    FluxEvaluator result = instance.newPhotonEvaluator();
    assertNotNull(result);
  }

  @Test
  public void testNewNeutronEvaluator()
  {
    FluxSpectrum instance = newInstance_1();
    FluxEvaluator result = instance.newNeutronEvaluator();
    assertNotNull(result);
  }

  @Test
  public void testGetGammaScale()
  {
    FluxSpectrum instance = newInstance_1();
    EnergyScale expResult = EnergyScaleFactory.newScale(new double[]
    {
      1, 2, 3, 4, 6
    });
    EnergyScale result = instance.getGammaScale();
    assertEquals(result, expResult);
  }

  @Test
  public void testGetGammaCounts()
  {
    FluxSpectrum instance = newInstance_1();
    double[] expResult = new double[]
    {
      10, 8, 5, 3
    };
    double[] result = instance.getGammaCounts();
    assertTrue(Arrays.equals(result, expResult));
  }

  @Test
  public void testGetNeutronScale()
  {
    FluxSpectrum instance = newInstance_2();
    EnergyScale expResult = EnergyScaleFactory.newScale(new double[]
    {
      1, 2, 3, 4, 6
    });
    EnergyScale result = instance.getNeutronScale();
    assertEquals(result, expResult);
  }

  @Test
  public void testGetNeutronCounts()
  {
    FluxSpectrum instance = newInstance_2();
    double[] expResult = new double[]
    {
      3, 5, 8, 10
    };
    double[] result = instance.getNeutronCounts();
    assertTrue(Arrays.equals(result, expResult));
  }
  
  @Test
  public void testToString()
  {
    FluxSpectrum instance = newInstance_1();
    String expResult = "FluxSpectrum()";
    String result = instance.toString();
    assertEquals(result, expResult);
  }

  @Test
  public void testEquals()
  {
    Object obj = newInstance_1();
    FluxSpectrum instance = newInstance_1();
    assertTrue(instance.equals(obj));
    assertFalse(instance.equals(false));
    assertFalse(instance.equals(new Object()));
    
    obj = newInstance_2();
    instance = newInstance_2();
    assertTrue(instance.equals(obj));
    assertFalse(instance.equals(false));
    assertFalse(instance.equals(new Object()));
  }

  @Test
  public void testGroupList()
  {
    FluxSpectrum instance = newInstance_1();
    List<FluxGroup> groups = instance.getPhotonGroups();
    assertEquals(groups.size(), 4);
    assertFalse(groups.isEmpty());

    // Test a bunch of stuff we do not support
    try
    {
      groups.contains(new Object());
      fail("not thrown");
    }
    catch (UnsupportedOperationException ex)
    {
    }

    try
    {
      groups.add((FluxGroup) null);
      fail("not thrown");
    }
    catch (UnsupportedOperationException ex)
    {
    }

    try
    {
      groups.remove((FluxGroup) null);
      fail("not thrown");
    }
    catch (UnsupportedOperationException ex)
    {
    }
    try
    {
      groups.remove(0);
      fail("not thrown");
    }
    catch (UnsupportedOperationException ex)
    {
    }

    try
    {
      groups.addAll(Arrays.asList(new FluxGroupBin(0, 1, 0)));
      fail("not thrown");
    }
    catch (UnsupportedOperationException ex)
    {
    }

    try
    {
      groups.addAll(0, Arrays.asList(new FluxGroupBin(0, 1, 0)));
      fail("not thrown");
    }
    catch (UnsupportedOperationException ex)
    {
    }

    try
    {
      groups.containsAll(Arrays.asList(new FluxGroupBin(0, 1, 0)));
      fail("not thrown");
    }
    catch (UnsupportedOperationException ex)
    {
    }

    try
    {
      groups.removeAll(Arrays.asList(new FluxGroupBin(0, 1, 0)));
      fail("not thrown");
    }
    catch (UnsupportedOperationException ex)
    {
    }

    try
    {
      groups.retainAll(Arrays.asList(new FluxGroupBin(0, 1, 0)));
      fail("not thrown");
    }
    catch (UnsupportedOperationException ex)
    {
    }

    try
    {
      groups.clear();
      fail("not thrown");
    }
    catch (UnsupportedOperationException ex)
    {
    }

    try
    {
      groups.set(0, null);
      fail("not thrown");
    }
    catch (UnsupportedOperationException ex)
    {
    }

    try
    {
      groups.indexOf(null);
      fail("not thrown");
    }
    catch (UnsupportedOperationException ex)
    {
    }

    try
    {
      groups.lastIndexOf(null);
      fail("not thrown");
    }
    catch (UnsupportedOperationException ex)
    {
    }

    try
    {
      groups.subList(0, 3);
      fail("not thrown");
    }
    catch (UnsupportedOperationException ex)
    {
    }

//
//    @Override
//    public FluxGroup get(int index)
//    {
//      return new FluxSpectrum.SpectrumGroup(scale, counts, index);
//    }
//
//    @Override
//    public List<FluxGroup> subList(int fromIndex, int toIndex)
//    {
//      // immutable
//      throw new UnsupportedOperationException();
//    }
//
    Object[] array = groups.toArray();
    assertEquals(array.length, 4);

    FluxGroup[] array2 = groups.toArray(new FluxGroup[0]);
    assertEquals(array2.length, 4);

    ListIterator<FluxGroup> liter = groups.listIterator();
    assertTrue(liter.hasNext());
    assertFalse(liter.hasPrevious());
    assertEquals(liter.nextIndex(), 0);
    assertEquals(liter.previousIndex(), -1);
    FluxGroup g = liter.next();
    assertEquals(g.getEnergyLower(), 1.0, 0.0);
    assertEquals(g.getEnergyUpper(), 2.0, 0.0);
    assertEquals(g.getCounts(), 10.0, 0.0);
    assertTrue(liter.hasPrevious());
    assertEquals(liter.nextIndex(), 1);

    assertEquals(g.getIntegral(-1, 10), 10.0, 0.0);
    assertEquals(g.getIntegral(-1, 1), 0.0, 0.0);
    assertEquals(g.getIntegral(2, 3), 0.0, 0.0);
    assertEquals(g.getIntegral(1, 1.5), 5.0, 0.0);

    liter = groups.listIterator(2);
    assertTrue(liter.hasNext());
    assertTrue(liter.hasPrevious());
    g = liter.previous();
    assertEquals(g.getEnergyLower(), 2.0, 0.0);
    assertEquals(g.getEnergyUpper(), 3.0, 0.0);
    assertEquals(g.getCounts(), 10.0, 8.0);
  }

  @Test
  public void testCreateGamma()
  {
    EnergyScale gammaScale = EnergyScaleFactory.newLinearScale(0, 4, 4);
    double[] gammaCounts = new double[]
    {
      1, 2, 3, 4
    };
    FluxSpectrum result = FluxSpectrum.createGamma(gammaScale, gammaCounts);
    assertNotNull(result);
  }

  @Test
  public void testCreateNeutron()
  {
    EnergyScale neutronScale = EnergyScaleFactory.newLinearScale(0, 4, 4);
    double[] neutronCounts = new double[]
    {
      1, 2, 3, 4
    };
    FluxSpectrum result = FluxSpectrum.createNeutron(neutronScale, neutronCounts);
    assertNotNull(result);
  }

}
