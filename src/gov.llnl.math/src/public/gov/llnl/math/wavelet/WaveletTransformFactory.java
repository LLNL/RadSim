/*
 * Copyright 2016, Lawrence Livermore National Security, LLC. 
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.math.wavelet;

import java.util.Set;
import java.util.TreeMap;

/**
 *
 * @author nelson85
 */
public class WaveletTransformFactory
{
  private WaveletFamily family = new WaveletFamilyDaubechies();
  private int length;

  public void setFamily(WaveletFamily family)
  {
    if (family == null)
      throw new NullPointerException("Family is null");
    this.family = family;
  }

  /**
   * Specify the wavelet family to produce a transform for.
   *
   * @param family is the family name.
   * @throws WaveletNotFoundException
   */
  public void setFamily(String family) throws WaveletNotFoundException
  {
    try
    {
      Class<WaveletFamily> familyClass = FAMILIES.get(family);
      if (familyClass == null)
        throw new WaveletNotFoundException("Wavelet family not found");
      this.family = familyClass.newInstance();
    }
    catch (InstantiationException | IllegalAccessException ex)
    {
      throw new WaveletNotFoundException("Error loading wavelet family", ex);
    }
  }

  public WaveletFamily getFamily()
  {
    return this.family;
  }

  /**
   * Get the list of available wavelet families.
   *
   * @return
   */
  public Set<String> getAvailableFamilies()
  {
    return FAMILIES.keySet();
  }

  /**
   * Set the length of the wavelet.
   *
   * @param length
   */
  public void setLength(int length)
  {
    this.length = length;
  }

  /**
   * Create a wavelet transform with the specified paramaters.
   *
   * @return a new wavelet transform.
   * @throws WaveletNotFoundException if the specified wavelet transform could
   * not be created.
   */
  public WaveletTransform create() throws WaveletNotFoundException
  {
    double[] filterCoef = family.get(length);
    return new WaveletTransformImpl(filterCoef);
  }

  /**
   * Creates a new transform directly based on a named wavelet.
   *
   * This is a short cut for creating a wavelet transform directly. This is
   * primarily intended to support loading of wavelets from a file in which the
   * fully specified name is given as a string.
   *
   * @param description
   * @return a new wavelet transform.
   * @throws WaveletNotFoundException if the wavelet description is not found.
   */
  static public WaveletTransform newTransform(String description) throws WaveletNotFoundException
  {
    WaveletTransformFactory wtf = new WaveletTransformFactory();
    wtf.setFamily(description.replaceAll("[0-9]", ""));
    wtf.setLength(Integer.parseInt(description.replaceAll("[^0-9]", "")));
    return wtf.create();
  }

//<editor-fold desc="internal" defaultstate="collapsed">
  static final TreeMap<String, Class> FAMILIES = new TreeMap<>();

  // Populate with names for available families
  
  {
    FAMILIES.put("daub", WaveletFamilyDaubechies.class);
  }
//</editor-fold>

}
