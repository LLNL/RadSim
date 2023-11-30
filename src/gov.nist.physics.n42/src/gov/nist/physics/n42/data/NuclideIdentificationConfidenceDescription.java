/*
 * Copyright 2022, Lawrence Livermore National Security, LLC.
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.nist.physics.n42.data;

/**
 *
 * @author nelson85
 */
public class NuclideIdentificationConfidenceDescription implements NuclideIdentificationConfidence
{
  private String value;

  public NuclideIdentificationConfidenceDescription(String value)
  {
    this.value=value;
  }

  public String getValue()
  {
    return value;
  }

  public void setValue(String value)
  {
    this.value = value;
  }
}
