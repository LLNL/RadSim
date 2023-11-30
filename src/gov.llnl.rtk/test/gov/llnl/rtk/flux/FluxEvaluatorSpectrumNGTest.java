/*
 * Copyright 2022, Lawrence Livermore National Security, LLC.
 * All rights reserved.
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.rtk.flux;


import gov.llnl.rtk.data.EnergyScaleFactory;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import static org.testng.Assert.*;
import org.testng.annotations.Test;

/**
 *
 * @author nelson85
 */
public class FluxEvaluatorSpectrumNGTest
{

  public FluxEvaluatorSpectrumNGTest()
  {
  }

  FluxEvaluatorSpectrum newInstance()
  {
    double[] e = new double[]
    {
      1, 2, 3, 4, 5
    };
    double[] c = new double[]
    {
      50, 25, 10, 2
    };
    return new FluxEvaluatorSpectrum(EnergyScaleFactory.newScale(e), c);
  }

//  @Test
//  public void testSeek()
//  {
//    FluxEvaluatorSpectrum instance = newInstance();
//    instance.seek(-1);
//    assertEquals(instance.index, 0);
//    instance.seek(20);
//    assertEquals(instance.index, 3);
//    instance.seek(0.9);
//    assertEquals(instance.index, 0);
//    instance.seek(1);
//    assertEquals(instance.index, 0);
//    instance.seek(1.1);
//    assertEquals(instance.index, 0);
//    instance.seek(3.9);
//    assertEquals(instance.index, 2);
//    instance.seek(4.0);
//    assertEquals(instance.index, 3);
//    instance.seek(4.1);
//    assertEquals(instance.index, 3);
//    instance.seek(4.9);
//    assertEquals(instance.index, 3);
//    instance.seek(5.0);
//    assertEquals(instance.index, 3);
//    instance.seek(5.1);
//    assertEquals(instance.index, 3);
//  }
  @Test
  public void testGetIntegral()
  {
    Set<FluxItem> method = FluxItem.ALL;
    FluxEvaluatorSpectrum instance = newInstance();
    assertEquals(instance.getIntegral(1, 2, method), 50.0, 0.0);
    assertEquals(instance.getIntegral(2, 3, method), 25.0, 0.0);
    assertEquals(instance.getIntegral(3, 4, method), 10.0, 0.0);
    assertEquals(instance.getIntegral(4, 5, method), 2.0, 0.0);
    assertEquals(instance.getIntegral(0, 2, method), 50.0, 0.0);
    assertEquals(instance.getIntegral(4, 7, method), 2.0, 0.0);
    assertEquals(instance.getIntegral(-10, 10, method), 87.0, 0.0);
  }

  @Test
  public void testGetLines()
  {
    FluxEvaluatorSpectrum instance = newInstance();
    List expResult = Collections.EMPTY_LIST;
    List result = instance.getLines(-10, 10);
    assertEquals(result, expResult);
  }

}
