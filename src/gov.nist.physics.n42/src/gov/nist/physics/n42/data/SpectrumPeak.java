/*
 * Copyright 2022, Lawrence Livermore National Security, LLC.
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.nist.physics.n42.data;

import gov.llnl.utility.xml.bind.ReaderInfo;
import gov.llnl.utility.xml.bind.WriterInfo;
import gov.nist.physics.n42.reader.SpectrumPeakReader;
import gov.nist.physics.n42.writer.SpectrumPeakWriter;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

/**
 *
 * @author nelson85
 */
@ReaderInfo(SpectrumPeakReader.class)
@WriterInfo(SpectrumPeakWriter.class)
public class SpectrumPeak extends ComplexObject
{
  private Double energy;
  private Double netArea;
  private Double netAreaUncertainty;

  /**
   * @return the energy
   */
  public Double getEnergy()
  {
    return energy;
  }

  /**
   * @return the netArea
   */
  public Double getNetArea()
  {
    return netArea;
  }

  /**
   * @return the netAreaUncertainty
   */
  public Double getNetAreaUncertainty()
  {
    return netAreaUncertainty;
  }

  public void setEnergy(Double u)
  {
    this.energy = u;
  }

  public void setNetArea(Double u)
  {
    this.netArea = u;
  }

  public void setNetAreaUncertainty(Double u)
  {
    this.netAreaUncertainty = u;
  }
  
}
