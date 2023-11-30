/*
 * Copyright 2022, Lawrence Livermore National Security, LLC.
 * All rights reserved.
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.ensdf;

/**
 *
 * @author nelson85
 */
public enum EnsdfParticleType
{
  PROTON('P'),
  LEVEL('L'),
  ALPHA('A'),
  BETA('B'),
  NEUTRON('N'),
  ELECTRON_CAPTURE('E'),
  GAMMA('G'),
  XRAY('X'),
  AUGER('U');

  private char code;

  EnsdfParticleType(char code)
  {
    this.code = code;
  }

  public static EnsdfParticleType fromCode(char c)
  {
    if (c == 'A')
      return ALPHA;
    if (c == 'B')
      return BETA;
    if (c == 'G')
      return GAMMA;
    if (c == 'X')
        return XRAY;
    if (c == 'U')
        return AUGER;
    if (c == 'E')
      return ELECTRON_CAPTURE;
    if (c == 'P')
      return PROTON;
    if (c == 'N')
      return NEUTRON;
    throw new UnsupportedOperationException("Unknown code " + c);
  }

  char getCode()
  {
    return this.code;
  }
}
