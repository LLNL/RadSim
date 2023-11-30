/*
 * Copyright 2022, Lawrence Livermore National Security, LLC.
 * All rights reserved.
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.ensdf;

import java.io.Serializable;

/**
 *
 * @author nelson85
 */
public class EnsdfProductionNormalization extends EnsdfRecord implements Serializable
{
  //Multiplier for converting relative photon intensity (RI in the GAMMA record) 
  // to photons per 100 decays of the parent.  If left blank (NR DNR)(BR DBR) 
  // from N record will used for normalization
  public final EnsdfQuantity NRBR;
  // Multiplier for converting relative transition intensity (including 
  // conversion electrons) [TI in the GAMMA record] to transitions
  // per 100 decays of the parent. (Normally NTBR) If left blank (NT DNT)(BR DBR) 
  // from N record will be used for normalization.
  public final EnsdfQuantity NTBR;
  // Multiplier for converting relative beta and e
  // intensities (IB in the B- record; IB, IE, TI in the EC record) to 
  // intensities per 100 decays.
  // If left blank (NB DNB)(BR DBR) from N record will be used for normalization.
  public final EnsdfQuantity NBBR;
  
  public final EnsdfQuantity NP;
  
  // If blank, comment associated with the intensity
  // option will appear in the drawing in the Nuclear Data Sheets.
  // If letter `C' is given, the desired comment to appear in the
  // drawing should be given on the continuation record
  public final char COM;
  
  // Option Intensity displayed               Comment in drawing
  // == ===================================   ==========================
  // 1 TI or RI(1 + alpha)                     Relative I(gamma + ce)
  // 2 TI x NT or RI x NR x(1 + alpha)         I(gamma + ce) per 100 (mode) decays
  // 3 TI x NT x BR or RI x BR x NR(1 + alpha) I(gamma + ce) per 100 parent decays
  // 4 RI x NT x BR                            I(gamma) per 100 parent decays
  // 5 RI                                      Relative I(gamma)
  // 6 RI                                      Relative photon branching from each level
  // 7 RI                                      % photon branching from each level
  public final char OPT;

  public EnsdfProductionNormalization(EnsdfDataSet dataSet,
          EnsdfQuantity NRBR, EnsdfQuantity NTBR,
          EnsdfQuantity NBBR, EnsdfQuantity NP, char COM, char OPT)
  {
    super(dataSet, 'N');  // This is confusing because it is actually PN
    this.NRBR = NRBR;
    this.NTBR = NTBR;
    this.NBBR = NBBR;
    this.NP = NP;
    this.COM = COM;
    this.OPT = OPT;
  }

  @Override
  public String toString()
  {
    return String.format("ProductionNormalization: NRBR=%s NTBR=%s NBBR=%s NP=%s",
            NRBR.toString(),
            NTBR.toString(),
            NBBR.toString(),
            NP.toString());
  }
}
