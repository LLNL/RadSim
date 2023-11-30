/*
 * Copyright 2019, Lawrence Livermore National Security, LLC. 
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.rtk.test;

import gov.llnl.math.random.RandomGenerator;
import gov.llnl.rtk.data.DoubleSpectrum;
import gov.llnl.rtk.data.EnergyScaleFactory;
import gov.llnl.rtk.data.IntegerSpectrum;
import gov.llnl.rtk.model.GammaSensorModel;
import java.util.List;
import static org.testng.Assert.*;
import org.testng.annotations.Test;

/**
 *
 * @author nelson85
 */
public class SampleGeneratorNGTest
{
  
  public SampleGeneratorNGTest()
  {
  }

  @Test
  public void testNewBuilder() throws Exception
  {
    GammaSensorModel gdm = GammaSensorModel.withEnergyScale(EnergyScaleFactory.newLinearScale(0, 3000, 1000));
    SampleGenerator.Builder result = SampleGenerator.newBuilder(gdm);
    assertNotNull(result);
  }

  @Test
  public void testDrawInteger() throws Exception
  {
    SampleGenerator instance = new SampleGeneratorImpl();
    IntegerSpectrum expResult = null;
    IntegerSpectrum result = instance.drawInteger();
    assertEquals(result, expResult);
  }

  @Test
  public void testDrawDouble() throws Exception
  {
    SampleGenerator instance = new SampleGeneratorImpl();
    DoubleSpectrum expResult = null;
    DoubleSpectrum result = instance.drawDouble();
    assertEquals(result, expResult);
  }

  @Test
  public void testSetParameter()
  {
    String key = "";
    Object value = null;
    SampleGenerator instance = new SampleGeneratorImpl();
    instance.setParameter(key, value);
  }

  @Test
  public void testGetParameter_String()
  {
    String key = "";
    SampleGenerator instance = new SampleGeneratorImpl();
    Object expResult = null;
    Object result = instance.getParameter(key);
    assertEquals(result, expResult);
  }

  @Test
  public void testGetParameter_String_Class()
  {
    String key = "";
    Class cls = null;
    SampleGenerator instance = new SampleGeneratorImpl();
    Object expResult = null;
    Object result = instance.getParameter(key, cls);
    assertEquals(result, expResult);
  }

  @Test
  public void testGetActions()
  {
    SampleGenerator instance = new SampleGeneratorImpl();
    List expResult = null;
    List result = instance.getActions();
    assertEquals(result, expResult);
  }

  @Test
  public void testGetAccumulator()
  {
   SampleGenerator instance = new SampleGeneratorImpl();
    DoubleSpectrum expResult = null;
    DoubleSpectrum result = instance.getAccumulator();
    assertEquals(result, expResult);
  }

  @Test
  public void testGetRandomGenerator()
  {
    SampleGenerator instance = new SampleGeneratorImpl();
    RandomGenerator expResult = null;
    RandomGenerator result = instance.getRandomGenerator();
    assertEquals(result, expResult);
  }

  @Test
  public void testGetGammaDetectorModel()
  {
    SampleGenerator instance = new SampleGeneratorImpl();
    GammaSensorModel expResult = null;
    GammaSensorModel result = instance.getGammaDetectorModel();
    assertEquals(result, expResult);
  }

  @Test
  public void testByWeigthedSNR()
  {
    double target = 1.0;
    SampleGenerator.ScalingRule result = SampleGenerator.byWeigthedSNR(target);
    assertNotNull(result);
  }

  @Test
  public void testByTime()
  {
    SampleGenerator.ScalingRule result = SampleGenerator.byTime();
    assertNotNull(result);
  }

  public class SampleGeneratorImpl implements SampleGenerator
  {
    @Override
    public IntegerSpectrum drawInteger() throws SampleException
    {
      return null;
    }

    @Override
    public DoubleSpectrum drawDouble() throws SampleException
    {
      return null;
    }

    @Override
    public void setParameter(String key, Object value)
    {
    }

    @Override
    public Object getParameter(String key)
    {
      return null;
    }

    @Override
    public <Type> Type getParameter(String key, Class<Type> cls)
    {
      return null;
    }

    @Override
    public List<Action> getActions()
    {
      return null;
    }

    @Override
    public DoubleSpectrum getAccumulator()
    {
      return null;
    }

    @Override
    public RandomGenerator getRandomGenerator()
    {
      return null;
    }

    @Override
    public GammaSensorModel getGammaDetectorModel()
    {
      return null;
    }
  }
  
}
